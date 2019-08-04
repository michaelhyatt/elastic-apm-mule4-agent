package co.elastic.apm.mule4.agent;

import java.util.Map;

import org.mule.runtime.api.component.location.ComponentLocation;
import org.mule.runtime.api.interception.InterceptionEvent;
import org.mule.runtime.api.interception.ProcessorParameterValue;

public class SpanUtils {
	public static void startSpan(TransactionStore transactionStore, ComponentLocation location, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	public static void endSpan(TransactionStore transactionStore, ComponentLocation location, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent event) {
		// TODO Auto-generated method stub
		
	}
	
}
