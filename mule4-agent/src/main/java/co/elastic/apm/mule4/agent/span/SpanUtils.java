package co.elastic.apm.mule4.agent.span;

import java.util.Map;

import org.mule.runtime.api.component.location.ComponentLocation;
import org.mule.runtime.api.interception.InterceptionEvent;
import org.mule.runtime.api.interception.ProcessorParameterValue;

import co.elastic.apm.api.Span;
import co.elastic.apm.api.Transaction;
import co.elastic.apm.mule4.agent.transaction.TransactionStore;

/*
 * Creation and ending of APM Spans.
 */
public class SpanUtils {

//	private static final String REQUEST_BUILDER_PARAM = "requestBuilder";
//	private static final String HTTP_REQUEST_ACTIVITY_NAMESPACE = "http";
//	private static final String HTTP_REQUEST_ACTIVITY_NAME = "request";

	private static final String SUBTYPE = "mule-step";
	private static final String DOC_NAME = "doc:name";
	private static final String UNNAMED = "...";

	/*
	 * Start a span
	 */
	public static Span startSpan(TransactionStore transactionStore, ComponentLocation location,
			Map<String, ProcessorParameterValue> parameters, InterceptionEvent event) {
		String transactionId = getTransactionId(event);

		// Span can only be started if there is an existing transaction created by flow
		// listener.
		Transaction transaction = transactionStore.getTransaction(transactionId)
				.orElseThrow(() -> new RuntimeException("Could not find transaction " + transactionId));

		Span span = transaction.startSpan(getSpanType(location), getSubType(location), getAction(location));

		populateTraceIdFlowVariable(span, event, transaction);

		propagateTracingContext(span, location, parameters, event);

		setSpanDetails(span, location, parameters, event);

		return span;
	}

	/*
	 * Propagate transaction context to external components
	 */
	private static void propagateTracingContext(Span span, ComponentLocation location,
			Map<String, ProcessorParameterValue> parameters, InterceptionEvent event) {

//		ComponentIdentifier identifier = location.getComponentIdentifier().getIdentifier();
//		String component = identifier.getName();
//		String type = identifier.getNamespace();

		// TODO Add support for more protocols and activities
		// Create HTTP header for http activities
//		if (HTTP_REQUEST_ACTIVITY_NAME.equals(component) && HTTP_REQUEST_ACTIVITY_NAMESPACE.equals(type))
//			span.injectTraceHeaders((name, value) -> addTraceHttpHeader(name, value, parameters));

	}

//	private static void addTraceHttpHeader(String name, String value, Map<String, ProcessorParameterValue> parameters) {
//		ProcessorParameterValue processorParameterValue = parameters.get(REQUEST_BUILDER_PARAM);
//		HttpRequesterRequestBuilder httpRequesterRequestBuilder = (HttpRequesterRequestBuilder) processorParameterValue
//				.resolveValue();
//		MultiMap<String, String> headers = httpRequesterRequestBuilder.getHeaders();
//		MultiMap<String, String> newHeaders = new MultiMap<String, String>(headers);
//		headers = newHeaders;
//		headers.put(name, value);
//		httpRequesterRequestBuilder.setHeaders(headers);
//	}

	/*
	 * Populate flow variable with trace id to be used to manually propagate trace
	 * context for client flow steps where trace context propagation is not
	 * happening automatically.
	 */
	private static void populateTraceIdFlowVariable(Span span, InterceptionEvent event, Transaction transaction) {
		// Create trace-id flowVar
		span.injectTraceHeaders((name, value) -> {
			if (!event.getVariables().containsKey(name))
				event.addVariable(name, value);
		});
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
	 * Get transactionId that is used to correlate Spans and Transactions. Comes
	 * from correlationId in the Mule event.
	 */
	private static String getTransactionId(InterceptionEvent event) {
		return event.getCorrelationId();
	}

	/*
	 * End Span
	 */
	public static void endSpan(Span span, ComponentLocation location, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent event) {

		setFinalDetails(span, location, parameters, event);

		// TODO Check how to get the timestamps from the message
		span.end();

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

}
