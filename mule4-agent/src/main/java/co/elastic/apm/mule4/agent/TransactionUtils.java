package co.elastic.apm.mule4.agent;

import java.util.Map;

import org.mule.runtime.api.component.location.ComponentLocation;
import org.mule.runtime.api.interception.InterceptionEvent;
import org.mule.runtime.api.interception.ProcessorParameterValue;

public class TransactionUtils {

	public static boolean isFirstEvent(TransactionStore transactionStore, ComponentLocation location, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	public static void startTransaction(TransactionStore transactionStore, ComponentLocation location, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent event) {
		// TODO Auto-generated method stub

	}

	public static void endTransaction(TransactionStore transactionStore, ComponentLocation location, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent event) {
		// TODO Auto-generated method stub

	}
}
