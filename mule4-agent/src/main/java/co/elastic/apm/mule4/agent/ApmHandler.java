package co.elastic.apm.mule4.agent;

import java.util.Map;

import org.mule.runtime.api.component.location.ComponentLocation;
import org.mule.runtime.api.interception.InterceptionEvent;
import org.mule.runtime.api.interception.ProcessorParameterValue;
import org.mule.runtime.api.notification.PipelineMessageNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApmHandler {

	private Logger logger = LoggerFactory.getLogger(ApmHandler.class);

	private TransactionStore transactionStore = new TransactionStore();

	public void handleProcessorStartEvent(ComponentLocation location, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent event) {
		logger.trace("Handling start event");

		SpanUtils.startSpan(transactionStore, location, parameters, event);
	}

	public void handleProcessorEndEvent(ComponentLocation location, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent event) {
		logger.trace("Handling end event");

		SpanUtils.endSpan(transactionStore, location, parameters, event);
	}

	public void handleExceptionEvent(ComponentLocation location, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent event) {
		logger.trace("Handling exception event");

		ExceptionUtils.captureException(transactionStore, location, parameters, event);
	}

	public void handleFlowStartEvent(ComponentLocation location, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent event) {

	}

	public void handleFlowStartEvent(PipelineMessageNotification notification) {
		logger.trace("Handling flow start event");

		if (TransactionUtils.isFirstEvent(transactionStore, notification))
			TransactionUtils.startTransaction(transactionStore, notification);

	}

	public void handleFlowEndEvent(PipelineMessageNotification notification) {
		logger.trace("Handling flow end event");

		TransactionUtils.endTransaction(transactionStore, notification);
	}

}
