package co.elastic.apm.mule4.agent;

import java.util.Map;

import org.mule.runtime.api.component.location.ComponentLocation;
import org.mule.runtime.api.interception.InterceptionEvent;
import org.mule.runtime.api.interception.ProcessorParameterValue;
import org.mule.runtime.api.interception.SourceInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MuleSourceInterceptor implements SourceInterceptor {

	private Logger logger = LoggerFactory.getLogger(MuleSourceInterceptor.class);
	private ApmHandler apmHandler;

	public MuleSourceInterceptor(ApmHandler apmHandler) {
		this.apmHandler = apmHandler;
	}

	@Override
	public void beforeCallback(ComponentLocation location, Map<String, ProcessorParameterValue> parameters,
			InterceptionEvent event) {

		logger.debug("beforeCallback ending flow" + location.getLocation());
		
		apmHandler.handleSourceBeforeCallback(location, parameters, event);
		
	}

}
