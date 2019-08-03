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
		
		if (isFirstEvent(location, parameters, event))
			startTransaction(location, parameters, event);
		else
			startSpan(location, parameters, event);
		
	}

	private void startSpan(ComponentLocation location, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent event) {
		// TODO Auto-generated method stub
		
	}

	private void startTransaction(ComponentLocation location, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent event) {
		// TODO Auto-generated method stub
		
	}

	private boolean isFirstEvent(ComponentLocation location, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	public void handleEndEvent(ComponentLocation location, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent event) {
		logger.trace("Handling end event");
		
		endSpan(location, parameters, event);
	}

	private void endSpan(ComponentLocation location, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void handleExceptionEvent(ComponentLocation location, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent event) {
		logger.trace("Handling exception event");
		
		captureException(location,parameters,event);
	}

	private void captureException(ComponentLocation location, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void handleSourceBeforeCallback(ComponentLocation location, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent event) {
		logger.trace("Handling source before callback event");
		
		endTransaction(location, parameters, event);
	}

	private void endTransaction(ComponentLocation location, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent event) {
		// TODO Auto-generated method stub
		
	}

}
