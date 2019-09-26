package co.elastic.apm.mule4.agent;

import java.util.Map;

import org.mule.runtime.api.component.location.ComponentLocation;
import org.mule.runtime.api.interception.InterceptionEvent;
import org.mule.runtime.api.interception.ProcessorParameterValue;

import co.elastic.apm.api.Span;
import co.elastic.apm.api.Transaction;

public class SpanUtils {
	public static Span startSpan(TransactionStore transactionStore, ComponentLocation location,
			Map<String, ProcessorParameterValue> parameters, InterceptionEvent event) {
		String transactionId = getTransactionId(event);

		Transaction transaction = transactionStore.getTransaction(transactionId)
				.orElseThrow(() -> new RuntimeException("Could not find transaction " + transactionId));

		Span span = transaction.startSpan(getType(location), getSubType(location), getAction(location));

		setSpanDetails(span, location, parameters, event);

		span.setStartTimestamp(getEventTimestamp(event));

		return span;
	}

	private static long getEventTimestamp(InterceptionEvent event) {
		return event.getContext().getReceivedTime().toEpochMilli() * 1_000;
	}

	private static void setSpanDetails(Span span, ComponentLocation location,
			Map<String, ProcessorParameterValue> parameters, InterceptionEvent event) {
		// TODO Auto-generated method stub
		
		span.setName(location.getComponentIdentifier().getIdentifier().getName());

	}

	private static String getAction(ComponentLocation location) {
		// TODO Auto-generated method stub
		return "step";
	}

	private static String getSubType(ComponentLocation location) {
		// TODO Auto-generated method stub
		return "subtype";
	}

	private static String getType(ComponentLocation location) {
		// TODO Auto-generated method stub
		return location.getComponentIdentifier().getType().name();
	}

	private static String getTransactionId(InterceptionEvent event) {
		return event.getCorrelationId();
	}

	public static void endSpan(Span span, ComponentLocation location, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent event) {

		setFinalDetails(span, location, parameters, event);
		
		span.end(getEventTimestamp(event));
		
	}

	private static void setFinalDetails(Span span, ComponentLocation location,
			Map<String, ProcessorParameterValue> parameters, InterceptionEvent event) {
		// Noop
		
	}

}
