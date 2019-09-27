package co.elastic.apm.mule4.agent;

import java.util.Map;

import org.mule.runtime.api.component.location.ComponentLocation;
import org.mule.runtime.api.interception.InterceptionEvent;
import org.mule.runtime.api.interception.ProcessorParameterValue;

import co.elastic.apm.api.Span;
import co.elastic.apm.mule4.agent.transaction.TransactionStore;

public class ExceptionUtils {
	public static void captureException(Span span, TransactionStore transactionStore, ComponentLocation location,
			Map<String, ProcessorParameterValue> parameters, InterceptionEvent event, Throwable ex) {
		// TODO Auto-generated method stub

	}
}
