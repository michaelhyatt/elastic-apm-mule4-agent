package co.elastic.apm.mule4.agent;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.mule.runtime.api.component.location.ComponentLocation;
import org.mule.runtime.api.interception.InterceptionAction;
import org.mule.runtime.api.interception.InterceptionEvent;
import org.mule.runtime.api.interception.ProcessorInterceptor;
import org.mule.runtime.api.interception.ProcessorParameterValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MuleProcessorInterceptor implements ProcessorInterceptor {

	private Logger logger = LoggerFactory.getLogger(MuleProcessorInterceptor.class);
	private ApmHandler apmHandler;
	
	public MuleProcessorInterceptor(ApmHandler apmHandler) {
		this.apmHandler = apmHandler;
	}

	@Override
	public CompletableFuture<InterceptionEvent> around(ComponentLocation location,
			Map<String, ProcessorParameterValue> parameters, InterceptionEvent event, InterceptionAction action) {
		
		logger.debug("===> Before step " + location.getLocation());
		
		apmHandler.handleProcessorStartEvent(location, parameters, event);
		
		CompletableFuture<InterceptionEvent> result = action.proceed();
		
		if (result.isCompletedExceptionally())
			apmHandler.handleExceptionEvent(location, parameters, event);
		
		apmHandler.handleProcessorEndEvent(location, parameters, event);
		
		logger.debug("===> After step " + location.getLocation());
		
		return result;
		
	}
	
	

}
