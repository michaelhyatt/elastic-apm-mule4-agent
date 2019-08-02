package co.elastic.apm.mule4.agent;

import java.util.Map;

import org.mule.runtime.api.component.location.ComponentLocation;
import org.mule.runtime.api.interception.InterceptionEvent;
import org.mule.runtime.api.interception.ProcessorParameterValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApmHandler {

	private Logger logger = LoggerFactory.getLogger(ApmHandler.class);

	public void handleStartEvent(ComponentLocation location, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent event) {
		logger.trace("Handling start event");
		
	}

	public void handleEndEvent(ComponentLocation location, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent event) {
		logger.trace("Handling end event");
		
	}

	public void handleExceptionEvent(ComponentLocation location, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent event) {
		logger.trace("Handling exception event");
		
	}

	public void handleSourceBeforeCallback(ComponentLocation location, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent event) {
		logger.trace("Handling source before callback event");
		
	}

}
