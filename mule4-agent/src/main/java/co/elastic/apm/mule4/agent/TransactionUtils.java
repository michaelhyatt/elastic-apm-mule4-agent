package co.elastic.apm.mule4.agent;

import java.util.Map;

import org.mule.runtime.api.component.location.ComponentLocation;
import org.mule.runtime.api.interception.InterceptionEvent;
import org.mule.runtime.api.interception.ProcessorParameterValue;

import co.elastic.apm.api.ElasticApm;
import co.elastic.apm.api.Transaction;

public class TransactionUtils {

	public static boolean isFirstEvent(TransactionStore transactionStore, ComponentLocation location,
			Map<String, ProcessorParameterValue> parameters, InterceptionEvent event) {
		String transactionId = getTransactionId(event);
		return transactionStore.isTransactionPresent(transactionId);
	}

	public static void startTransaction(TransactionStore transactionStore, ComponentLocation location,
			Map<String, ProcessorParameterValue> parameters, InterceptionEvent event) {

		Transaction transaction;
		if (hasParentTraceContext(event, parameters))
			transaction = ElasticApm.startTransactionWithRemoteParent(key -> extractHeader(key, parameters, event));
		else {
			transaction = ElasticApm.startTransaction();
		}

		transaction.ensureParentId();

		populateTransactionDetails(transaction, location, parameters, event);

		transactionStore.storeTransaction(getTransactionId(event), transaction);
	}

	public static void endTransaction(TransactionStore transactionStore, ComponentLocation location,
			Map<String, ProcessorParameterValue> parameters, InterceptionEvent event) {

		Transaction transaction = transactionStore.retrieveTransaction(getTransactionId(event));

		updateSettings(transaction, location, parameters, event);

		transaction.end(getTimestamp(event));
	}

	protected static String getTransactionId(InterceptionEvent event) {
		return event.getCorrelationId();
	}

	private static String extractHeader(String key, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent event) {
		// TODO Auto-generated method stub
		return null;
	}

	private static void populateTransactionDetails(Transaction transaction, ComponentLocation location,
			Map<String, ProcessorParameterValue> parameters, InterceptionEvent event) {
		// TODO Auto-generated method stub
		
		transaction.setStartTimestamp(getTimestamp(event));

	}

	private static boolean hasParentTraceContext(InterceptionEvent event,
			Map<String, ProcessorParameterValue> parameters) {
		// TODO Auto-generated method stub
		return false;
	}

	private static long getTimestamp(InterceptionEvent event) {
		// TODO Auto-generated method stub
		return 0;
	}

	private static void updateSettings(Transaction transaction, ComponentLocation location,
			Map<String, ProcessorParameterValue> parameters, InterceptionEvent event) {
		// TODO Auto-generated method stub

	}
}
