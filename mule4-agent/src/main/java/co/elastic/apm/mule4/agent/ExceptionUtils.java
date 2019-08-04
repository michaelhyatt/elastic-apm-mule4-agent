package co.elastic.apm.mule4.agent;

import java.util.Map;

import org.mule.runtime.api.component.location.ComponentLocation;
import org.mule.runtime.api.interception.InterceptionEvent;
import org.mule.runtime.api.interception.ProcessorParameterValue;

public class ExceptionUtils {
	public static void captureException(ComponentLocation location, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent event) {
		// TODO Auto-generated method stub
		
	}
}
