package co.elastic.apm.mule4.agent.span;

import java.util.Map;
import java.util.Optional;

import org.mule.runtime.api.component.location.ComponentLocation;
import org.mule.runtime.api.event.Event;
import org.mule.runtime.api.interception.InterceptionEvent;
import org.mule.runtime.api.interception.ProcessorParameterValue;
import org.mule.runtime.api.metadata.TypedValue;

import co.elastic.apm.api.Span;
import co.elastic.apm.mule4.agent.transaction.ApmTransaction;
import co.elastic.apm.mule4.agent.transaction.TransactionUtils;

/*
 * Creation and ending of APM Spans.
 */
public class SpanUtils {

	private static final String SUBTYPE = "mule-step";
	private static final String DOC_NAME = "doc:name";
	private static final String UNNAMED = "...";

	public static final String ELASTIC_APM_SPANS = "elastic-apm-spans";

	/*
	 * Start a span
	 */
	public static Span startSpan(ComponentLocation location, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent event) {

		// retrieve current transaction from event flowVar
		Optional<ApmTransaction> transactionOpt = SpanUtils.getTransaction(event);

		ApmSpan parentSpan;
		ApmSpanStack apmSpanStack;
		if (transactionOpt.isPresent()) {

			// If there is a transaction there already, check if the SpanStack is present
			// If it is, return the latest span as parent
			TypedValue<?> variable = event.getVariables().get(ELASTIC_APM_SPANS);

			if (variable != null) {
				// Found span stack
				apmSpanStack = (ApmSpanStack) variable.getValue();
				parentSpan = apmSpanStack.peek();
			} else {
				// If transaction exists and there is no corresponding spanstack, raise an
				// exception
				throw new RuntimeException("Found transaction with no corresponding SpanStack! Fix it!");
			}
		} else {
			// or, start a new one and store it in the flowVar, if doesn't exist
			ApmTransaction transaction = TransactionUtils.startTransaction(event);

			// TODO: populate more transaction details
			String flowName = getFlowName(location);
			transaction.setFlowName(flowName);
			transaction.setName(flowName);

			setTransactionAsFlowvar(event, transaction);

			// Create SpanStack as well with the new transaction as a first element
			parentSpan = new ApmSpan(transaction);
			apmSpanStack = new ApmSpanStack(parentSpan);
		}

		Span span = parentSpan.startSpan(getSpanType(location), getSubType(location), getAction(location));

		setSpanDetails(span, location, parameters, event);

		apmSpanStack.push(new ApmSpan(span));

		event.addVariable(ELASTIC_APM_SPANS, apmSpanStack);

		return span;
	}

	/*
	 * Populate Span details at creation time
	 */
	private static void setSpanDetails(Span span, ComponentLocation location,
			Map<String, ProcessorParameterValue> parameters, InterceptionEvent event) {
		// TODO Capture event properties
		// TODO Capture flow variables

		span.setName(getStepName(parameters));

	}

	/*
	 * Get Step name
	 */
	public static String getStepName(Map<String, ProcessorParameterValue> parameters) {
		ProcessorParameterValue nameParam = parameters.get(DOC_NAME);

		if (nameParam == null)
			return UNNAMED;

		return nameParam.providedValue();
	}

	/*
	 * Get Span action
	 */
	private static String getAction(ComponentLocation location) {
		// Action = Span type
		return getSpanType(location);
	}

	/*
	 * Get Span subtype
	 */
	private static String getSubType(ComponentLocation location) {

		// return const value
		return SUBTYPE;
	}

	/*
	 * Get Span type
	 */
	private static String getSpanType(ComponentLocation location) {

		// Get flow step type (e.g. "logger", "flow-ref", etc).
		return location.getComponentIdentifier().getIdentifier().getName();
	}

	/*
	 * End Span
	 */
	public static void endSpan(Span span, ComponentLocation location, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent event) {

		setFinalDetails(span, location, parameters, event);

		// TODO Check how to get the timestamps from the message
		span.end();

		TypedValue<?> variable = event.getVariables().get(ELASTIC_APM_SPANS);

		if (variable != null) {
			ApmSpanStack apmSpanStack = (ApmSpanStack) variable.getValue();
			apmSpanStack.pop();
		}
	}

	private static void setFinalDetails(Span span, ComponentLocation location,
			Map<String, ProcessorParameterValue> parameters, InterceptionEvent event) {
		// Noop currently
		// TODO: Populate output properties
		// TODO: Populate changed flowVars
		// TODO: Populate response body (if it is necessary).

	}

	/*
	 * Get the flow name
	 */
	public static String getFlowName(ComponentLocation location) {
		return location.getLocation().split("/")[0];
	}

	public static Optional<ApmTransaction> getTransaction(Event event) {

		Map<String, TypedValue<?>> variables = event.getVariables();
		TypedValue<?> typedValue = variables.get(TransactionUtils.ELASTIC_APM_TRANSACTION);

		if (typedValue == null) {
			return Optional.empty();
		}

		return Optional.ofNullable((ApmTransaction) typedValue.getValue());
	}

	public static void setTransactionAsFlowvar(InterceptionEvent event, ApmTransaction transaction) {
		event.addVariable(TransactionUtils.ELASTIC_APM_TRANSACTION, transaction);
	}

}
