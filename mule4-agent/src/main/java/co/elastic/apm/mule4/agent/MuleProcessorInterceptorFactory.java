package co.elastic.apm.mule4.agent;

import javax.inject.Inject;

import org.mule.runtime.api.artifact.Registry;
import org.mule.runtime.api.interception.ProcessorInterceptor;
import org.mule.runtime.api.interception.ProcessorInterceptorFactory;

public class MuleProcessorInterceptorFactory implements ProcessorInterceptorFactory {
	
	@Inject
	private Registry registry;

	@Override
	public ProcessorInterceptor get() {
		return new MuleProcessorInterceptor(registry.lookupByType(ApmHandler.class).get());
	}

}
