package co.elastic.apm.mule4.agent.exception;

import java.util.Map;

import org.mule.runtime.api.component.location.ComponentLocation;
import org.mule.runtime.api.interception.InterceptionEvent;
import org.mule.runtime.api.interception.ProcessorParameterValue;

import co.elastic.apm.api.Span;
import co.elastic.apm.mule4.agent.span.SpanUtils;
import co.elastic.apm.mule4.agent.transaction.ApmTransaction;

/* 
 * Handling of Exceptions thrown by flow steps.
 */
public class ExceptionUtils {
	private static final String ERROR_FLOW = "ERROR_FLOW";
	private static final String ERROR_STEP = "ERROR_STEP";

	public static void captureException(Span span, ComponentLocation location,
			Map<String, ProcessorParameterValue> parameters, InterceptionEvent event, Throwable ex) {

		// End the current span normally.
		// TODO: update to end with timestamp retrieved from external arguments.
		span.end();

		ApmTransaction transaction = SpanUtils.getTransaction(event).get();

		// Double-ensure there is no Exception info already attached to the transaction.
		if (transaction.getLabel(ERROR_STEP).isPresent() || transaction.getLabel(ERROR_FLOW).isPresent())
			return;

		// Capture the Exception details and store the transaction back.
		transaction.captureException(ex.getCause());
		transaction.addLabel(ERROR_STEP, SpanUtils.getStepName(parameters));
		transaction.addLabel(ERROR_FLOW, SpanUtils.getFlowName(location));

	}

}
