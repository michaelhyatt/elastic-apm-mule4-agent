package co.elastic.apm.mule4.agent.exception;

import java.util.Map;
import java.util.Optional;

import org.mule.runtime.api.component.location.ComponentLocation;
import org.mule.runtime.api.interception.InterceptionEvent;
import org.mule.runtime.api.interception.ProcessorParameterValue;

import co.elastic.apm.api.Span;
import co.elastic.apm.api.Transaction;
import co.elastic.apm.mule4.agent.span.SpanUtils;
import co.elastic.apm.mule4.agent.transaction.ApmTransaction;
import co.elastic.apm.mule4.agent.transaction.TransactionStore;

/* 
 * Handling of Exceptions thrown by flow steps.
 */
public class ExceptionUtils {
	private static final String ERROR_FLOW = "ERROR_FLOW";
	private static final String ERROR_STEP = "ERROR_STEP";

	public static void captureException(Span span, TransactionStore transactionStore, ComponentLocation location,
			Map<String, ProcessorParameterValue> parameters, InterceptionEvent event, Throwable ex) {

		// End the current span normally.
		// TODO: update to end with timestamp retrieved from external arguments.
		span.end();

		String transactionId = getTransactionId(event);

		Optional<Transaction> transaction = Optional.empty();

		// Ensure the transaction is not being updated from multiple threads.
		synchronized (transaction) {

			// Ensure we are only attaching the Exception to transaction once, since
			// rethrowing it causes this methid to be invoked multiple times.
			transaction = transactionStore.getTransaction(transactionId);

			if (!transaction.isPresent())
				return;

			ApmTransaction transaction2 = (ApmTransaction) transaction.get();

			// Double-ensure there is no Exception info already attached to the transaction.
			if (transaction2.getLabel(ERROR_STEP).isPresent() || transaction2.getLabel(ERROR_FLOW).isPresent())
				return;

			// Capture the Exception details and store the transaction back.
			transaction2.captureException(ex.getCause());
			transaction2.addLabel(ERROR_STEP, SpanUtils.getStepName(parameters));
			transaction2.addLabel(ERROR_FLOW, SpanUtils.getFlowName(location));
			transactionStore.storeTransaction(transactionId, transaction2);
		}

	}

	private static String getTransactionId(InterceptionEvent event) {
		return event.getCorrelationId();
	}
}
