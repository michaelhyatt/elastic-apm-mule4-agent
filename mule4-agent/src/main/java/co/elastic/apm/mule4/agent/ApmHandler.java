package co.elastic.apm.mule4.agent;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.mule.runtime.api.component.location.ComponentLocation;
import org.mule.runtime.api.interception.InterceptionEvent;
import org.mule.runtime.api.interception.ProcessorParameterValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApmHandler {

	private static final String EXECUTOR_THREAD_COUNT = "executor.thread.count";
	private Logger logger = LoggerFactory.getLogger(ApmHandler.class);
	private ExecutorService executor;

	private TransactionStore transactionStore = new TransactionStore();

	public ApmHandler() {

		executor = Executors.newFixedThreadPool(
				Integer.parseInt(System.getProperty(ApmStarter.ELASTIC_APM + EXECUTOR_THREAD_COUNT, "2")));
	}

	public void handleStartEvent(ComponentLocation location, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent event) {
		logger.trace("Handling start event");

		executor.submit(() -> {
			if (TransactionUtils.isFirstEvent(transactionStore, location, parameters, event))
				TransactionUtils.startTransaction(transactionStore, location, parameters, event);
			else
				SpanUtils.startSpan(transactionStore, location, parameters, event);
		});
	}

	public void handleEndEvent(ComponentLocation location, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent event) {
		logger.trace("Handling end event");

		executor.submit(() -> SpanUtils.endSpan(transactionStore, location, parameters, event));
	}

	public void handleExceptionEvent(ComponentLocation location, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent event) {
		logger.trace("Handling exception event");

		executor.submit(() -> ExceptionUtils.captureException(transactionStore, location, parameters, event));
	}

	public void handleSourceBeforeCallback(ComponentLocation location, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent event) {
		logger.trace("Handling source before callback event");

		executor.submit(() -> TransactionUtils.endTransaction(transactionStore, location, parameters, event));
	}

}
