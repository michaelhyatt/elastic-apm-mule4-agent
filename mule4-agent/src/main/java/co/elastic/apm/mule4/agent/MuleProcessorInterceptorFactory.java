package co.elastic.apm.mule4.agent;

import javax.inject.Inject;

import org.mule.runtime.api.artifact.Registry;
import org.mule.runtime.api.interception.ProcessorInterceptor;
import org.mule.runtime.api.interception.ProcessorInterceptorFactory;

/*
 * This is ProcessorInterceptorFactory API to intercept flow step execution.
 */ 
public class MuleProcessorInterceptorFactory implements ProcessorInterceptorFactory {

	@Inject
	private Registry registry;

	@Override
	public ProcessorInterceptor get() {
		ApmHandler apmHandler = registry.lookupByType(ApmHandler.class).get();
		return new MuleProcessorInterceptor(apmHandler);
	}

}
