package co.elastic.apm.mule4.agent;

import java.util.Map;
import java.util.Optional;

import org.mule.runtime.api.component.location.ComponentLocation;
import org.mule.runtime.api.interception.InterceptionEvent;
import org.mule.runtime.api.interception.ProcessorParameterValue;

import co.elastic.apm.api.Span;
import co.elastic.apm.api.Transaction;
import co.elastic.apm.mule4.agent.transaction.TransactionStore;

public class ExceptionUtils {
	public static void captureException(Span span, TransactionStore transactionStore, ComponentLocation location,
			Map<String, ProcessorParameterValue> parameters, InterceptionEvent event, Throwable ex) {

		// Capture exception, finish span and tranaction
		span.captureException(ex);

		span.end();

		Optional<Transaction> transaction = transactionStore.getTransaction(getTransactionId(event));

		if (!transaction.isPresent())
			return;

		transaction.get().end();

	}

	private static String getTransactionId(InterceptionEvent event) {
		return event.getCorrelationId();
	}
}
