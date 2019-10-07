package co.elastic.apm.mule4.agent;

import java.util.Map;

import org.mule.runtime.api.component.location.ComponentLocation;
import org.mule.runtime.api.interception.InterceptionEvent;
import org.mule.runtime.api.interception.ProcessorParameterValue;
import org.mule.runtime.api.notification.PipelineMessageNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.elastic.apm.api.Span;
import co.elastic.apm.mule4.agent.exception.ExceptionUtils;
import co.elastic.apm.mule4.agent.span.SpanUtils;
import co.elastic.apm.mule4.agent.transaction.TransactionStore;
import co.elastic.apm.mule4.agent.transaction.TransactionUtils;

/*
 * Adapter called by flow event handlers and flow process step interceptors
 */
public class ApmHandler {

	private Logger logger = LoggerFactory.getLogger(ApmHandler.class);

	// Store for all active transactions in flight.
	private TransactionStore transactionStore = new TransactionStore();

	// What to invoke when Mule process step starts.
	public Span handleProcessorStartEvent(ComponentLocation location, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent event) {
		logger.trace("Handling start event");

		return SpanUtils.startSpan(transactionStore, location, parameters, event);
	}

	// What to invoke when Mule process step ends.
	public void handleProcessorEndEvent(Span span, ComponentLocation location,
			Map<String, ProcessorParameterValue> parameters, InterceptionEvent event) {
		logger.trace("Handling end event");

		SpanUtils.endSpan(span, location, parameters, event);
	}

	// What to invoke when an Exception thrown in the Mule flow.
	public void handleExceptionEvent(Span span, ComponentLocation location,
			Map<String, ProcessorParameterValue> parameters, InterceptionEvent event, Throwable ex) {
		logger.trace("Handling exception event");

		ExceptionUtils.captureException(span, transactionStore, location, parameters, event, ex);
	}

	// What to invoke when Mule flow starts execution.
	public void handleFlowStartEvent(PipelineMessageNotification notification) {
		logger.trace("Handling flow start event");

		if (TransactionUtils.isFirstEvent(transactionStore, notification))
			TransactionUtils.startTransaction(transactionStore, notification);

	}

	// What to invoke when Mule flow completes execution.
	public void handleFlowEndEvent(PipelineMessageNotification notification) {
		logger.trace("Handling flow end event");

		TransactionUtils.endTransaction(transactionStore, notification);
	}

}
