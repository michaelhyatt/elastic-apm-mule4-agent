package co.elastic.apm.mule4.agent;

import java.util.Optional;

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

		transactionStore.storeTransaction(getTransactionId(notification),
				populateTransactionDetails(transaction, notification));
	}

	private static Transaction populateTransactionDetails(Transaction transaction,
			PipelineMessageNotification notification) {

		ApmTransaction transaction2 = new ApmTransaction(transaction);

		transaction2.setStartTimestamp(getEventTimestamp(notification));

		String flowName = getFlowName(notification);
		transaction2.setName(flowName);
		transaction2.setFlowName(flowName);

		transaction2.setType(TRANSACTION_TYPE);

		return transaction2;
	}

	private static String getFlowName(PipelineMessageNotification notification) {
		return notification.getResourceIdentifier();
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

		if (!isEndOfTopFlow(transactionStore, notification))
			return;

		Transaction transaction = transactionStore.retrieveTransaction(getTransactionId(notification));

		populateFinalTransactionDetails(transaction, notification);

		transaction.end(getEventTimestamp(notification));
	}

	private static boolean isEndOfTopFlow(TransactionStore transactionStore, PipelineMessageNotification notification) {
		Optional<Transaction> optional = transactionStore.getTransaction(getTransactionId(notification));

		if (!optional.isPresent())
			return false;

		ApmTransaction transaction = (ApmTransaction) optional.get();

		if (transaction.getFlowName().equals(getFlowName(notification)))
			return true;

		return false;
	}

	private static long getEventTimestamp(PipelineMessageNotification notification) {
		return notification.getTimestamp() * 1_000;
	}

	private static void populateFinalTransactionDetails(Transaction transaction,
			PipelineMessageNotification notification) {
		// Noop
	}

}
