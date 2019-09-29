package co.elastic.apm.mule4.agent;

import java.util.Map;
import java.util.Optional;

import org.mule.runtime.api.component.location.ComponentLocation;
import org.mule.runtime.api.interception.InterceptionEvent;
import org.mule.runtime.api.interception.ProcessorParameterValue;

import co.elastic.apm.api.Span;
import co.elastic.apm.api.Transaction;
import co.elastic.apm.mule4.agent.span.SpanUtils;
import co.elastic.apm.mule4.agent.transaction.TransactionStore;

public class ExceptionUtils {
	private static final String ERROR_FLOW = "ERROR_FLOW";
	private static final String ERROR_STEP = "ERROR_STEP";

	public static void captureException(Span span, TransactionStore transactionStore, ComponentLocation location,
			Map<String, ProcessorParameterValue> parameters, InterceptionEvent event, Throwable ex) {

		span.end();
		
		String transactionId = getTransactionId(event);
		
		Optional<Transaction> transaction = transactionStore.getTransaction(transactionId);

		if (!transaction.isPresent())
			return;
		
		Transaction transaction2 = transactionStore.retrieveTransaction(transactionId);
		transaction2.captureException(ex.getCause());
		transaction2.addLabel(ERROR_STEP, SpanUtils.getStepName(parameters));
		transaction2.addLabel(ERROR_FLOW, SpanUtils.getFlowName(location));
		transaction2.end();

	}

	private static String getTransactionId(InterceptionEvent event) {
		return event.getCorrelationId();
	}
}
