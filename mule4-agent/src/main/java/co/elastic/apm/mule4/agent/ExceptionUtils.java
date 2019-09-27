package co.elastic.apm.mule4.agent;

import java.util.Map;

import org.mule.runtime.api.component.location.ComponentLocation;
import org.mule.runtime.api.interception.InterceptionEvent;
import org.mule.runtime.api.interception.ProcessorParameterValue;

import co.elastic.apm.api.Span;

public class ExceptionUtils {
	public static void captureException(Span span, TransactionStore transactionStore, ComponentLocation location, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent finalEvent) {
		// TODO Auto-generated method stub
		
	}
}
