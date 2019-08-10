package co.elastic.apm.mule4.agent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import co.elastic.apm.api.Transaction;

public class TransactionStore {

	private Map<String, Transaction> txMap = new HashMap<String, Transaction>();

	public boolean isTransactionPresent(String transactionId) {
		// TODO Auto-generated method stub
		return txMap.containsKey(transactionId);
	}

	public void storeTransaction(String transactionId, Transaction transaction) {
		// TODO Auto-generated method stub
		txMap.put(transactionId, transaction);
	}

	public Optional<Transaction> retrieveTransaction(String transactionId) {
		// TODO Auto-generated method stub
		Transaction tx = txMap.remove(transactionId);
		return tx == null? Optional.empty() : Optional.of(tx);
	}

}
