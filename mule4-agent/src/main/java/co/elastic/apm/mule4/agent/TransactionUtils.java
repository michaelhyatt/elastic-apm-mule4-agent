package co.elastic.apm.mule4.agent;

import org.mule.runtime.api.notification.PipelineMessageNotification;

import co.elastic.apm.api.ElasticApm;
import co.elastic.apm.api.Transaction;

public class TransactionUtils {

	private static final String TRANSACTION_TYPE = "mule";

	public static boolean isFirstEvent(TransactionStore transactionStore, PipelineMessageNotification notification) {
		return !transactionStore.isTransactionPresent(getTransactionId(notification));
	}

	public static void startTransaction(TransactionStore transactionStore, PipelineMessageNotification notification) {
		Transaction transaction;

		if (hasRemoteParent(notification))
			transaction = ElasticApm.startTransactionWithRemoteParent(x -> getHeaderExtractor(x, notification));
		else {
			transaction = ElasticApm.startTransaction();
			transaction.ensureParentId();
		}

		populateTransactionDetails(transaction, notification);

		transactionStore.storeTransaction(getTransactionId(notification), transaction);
	}

	private static void populateTransactionDetails(Transaction transaction, PipelineMessageNotification notification) {

		transaction.setStartTimestamp(getEventTimestamp(notification));

		transaction.setName(getFlowName(notification));

		transaction.setType(TRANSACTION_TYPE);
	}

	private static String getFlowName(PipelineMessageNotification notification) {
		return notification.getEventName();
	}

	private static String getTransactionId(PipelineMessageNotification notification) {
		return notification.getEvent().getCorrelationId();
	}

	private static String getHeaderExtractor(String x, PipelineMessageNotification notification) {
		// TODO Auto-generated method stub
		return null;
	}

	private static boolean hasRemoteParent(PipelineMessageNotification notification) {
		// TODO Auto-generated method stub
		return false;
	}

	public static void endTransaction(TransactionStore transactionStore, PipelineMessageNotification notification) {
		Transaction transaction = transactionStore.retrieveTransaction(getTransactionId(notification));

		populateFinalTransactionDetails(transaction, notification);

		transaction.end(getEventTimestamp(notification));
	}

	private static long getEventTimestamp(PipelineMessageNotification notification) {
		return notification.getTimestamp() * 1_000;
	}

	private static void populateFinalTransactionDetails(Transaction transaction,
			PipelineMessageNotification notification) {
		// TODO Auto-generated method stub
	}

}
