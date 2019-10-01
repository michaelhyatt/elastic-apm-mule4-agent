package co.elastic.apm.mule4.agent.span;

import java.util.Map;

import org.mule.runtime.api.component.location.ComponentLocation;
import org.mule.runtime.api.interception.InterceptionEvent;
import org.mule.runtime.api.interception.ProcessorParameterValue;

import co.elastic.apm.api.Span;
import co.elastic.apm.api.Transaction;
import co.elastic.apm.mule4.agent.transaction.TransactionStore;

public class SpanUtils {
	private static final String SUBTYPE = "mule-step";
	private static final String DOC_NAME = "doc:name";
	private static final String UNNAMED = "...";

	public static Span startSpan(TransactionStore transactionStore, ComponentLocation location,
			Map<String, ProcessorParameterValue> parameters, InterceptionEvent event) {
		String transactionId = getTransactionId(event);

		Transaction transaction = transactionStore.getTransaction(transactionId)
				.orElseThrow(() -> new RuntimeException("Could not find transaction " + transactionId));

		Span span = transaction.startSpan(getSpanType(location), getSubType(location), getAction(location));

		setSpanDetails(span, location, parameters, event);

		return span;
	}

	private static void setSpanDetails(Span span, ComponentLocation location,
			Map<String, ProcessorParameterValue> parameters, InterceptionEvent event) {
		// TODO Auto-generated method stub

		span.setName(getStepName(parameters));

	}

	public static String getStepName(Map<String, ProcessorParameterValue> parameters) {
		ProcessorParameterValue nameParam = parameters.get(DOC_NAME);
		
		if (nameParam == null)
			return UNNAMED;
		
		return nameParam.providedValue();
	}

	private static String getAction(ComponentLocation location) {
		// TODO Auto-generated method stub
		return getSpanType(location);
	}

	private static String getSubType(ComponentLocation location) {
		// TODO Auto-generated method stub
		return SUBTYPE;
	}

	private static String getSpanType(ComponentLocation location) {
		// TODO Auto-generated method stub
		return location.getComponentIdentifier().getIdentifier().getName();
	}

	private static String getTransactionId(InterceptionEvent event) {
		return event.getCorrelationId();
	}

	public static void endSpan(Span span, ComponentLocation location, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent event) {

		setFinalDetails(span, location, parameters, event);

		// TODO Check how to get the timestamps from the message
		span.end();

	}

	private static void setFinalDetails(Span span, ComponentLocation location,
			Map<String, ProcessorParameterValue> parameters, InterceptionEvent event) {
		// Noop

	}

	public static String getFlowName(ComponentLocation location) {
		return location.getLocation().split("/")[0];
	}

}
