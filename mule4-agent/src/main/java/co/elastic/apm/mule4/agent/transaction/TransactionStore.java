package co.elastic.apm.mule4.agent.transaction;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import co.elastic.apm.api.Transaction;

/*
 * Store for transactions inflight.
 */
public class TransactionStore {

	// Possible concurrent access to the same transaction.
	private Map<String, Transaction> txMap = new ConcurrentHashMap<String, Transaction>();

	public boolean isTransactionPresent(String transactionId) {
		return txMap.containsKey(transactionId);
	}

	public void storeTransaction(String transactionId, Transaction transaction) {
		txMap.put(transactionId, transaction);
	}

	/*
	 * Retrieve and remove the transaction from the store.
	 */
	public Transaction retrieveTransaction(String transactionId) {
		return txMap.remove(transactionId);
	}

	/*
	 * Get the transaction without removing it from the store.
	 */
	public Optional<Transaction> getTransaction(String transactionId) {
		return Optional.ofNullable(txMap.get(transactionId));
	}

}
