package co.elastic.apm.mule4.agent;

import javax.inject.Inject;

import org.mule.runtime.api.artifact.Registry;
import org.mule.runtime.api.interception.SourceInterceptor;
import org.mule.runtime.api.interception.SourceInterceptorFactory;

public class MuleSourceInterceptorFactory implements SourceInterceptorFactory {

	@Inject
	private Registry registry;

	@Override
	public SourceInterceptor get() {
		return new MuleSourceInterceptor(registry.lookupByType(ApmHandler.class).get());
	}

}
