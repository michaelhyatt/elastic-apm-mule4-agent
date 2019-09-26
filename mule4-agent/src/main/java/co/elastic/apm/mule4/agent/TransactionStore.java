package co.elastic.apm.mule4.agent;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import co.elastic.apm.api.Transaction;

public class TransactionStore {

	private Map<String, Transaction> txMap = new ConcurrentHashMap<String, Transaction>();

	public boolean isTransactionPresent(String transactionId) {
		return txMap.containsKey(transactionId);
	}

	public void storeTransaction(String transactionId, Transaction transaction) {
		// TODO Auto-generated method stub
		txMap.put(transactionId, transaction);
	}

	public Transaction retrieveTransaction(String transactionId) {
		// TODO Auto-generated method stub
		return txMap.remove(transactionId);
	}

	public Optional<Transaction> getTransaction(String transactionId) {
		// TODO Auto-generated method stub
		Transaction transaction = txMap.get(transactionId);
		return transaction == null? Optional.empty(): Optional.of(transaction);
	}

}
