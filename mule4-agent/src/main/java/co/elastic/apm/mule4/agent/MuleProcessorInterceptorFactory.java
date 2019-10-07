package co.elastic.apm.mule4.agent;

import javax.inject.Inject;

import org.mule.runtime.api.artifact.Registry;
import org.mule.runtime.api.interception.ProcessorInterceptor;
import org.mule.runtime.api.interception.ProcessorInterceptorFactory;
import org.mule.runtime.api.notification.PipelineMessageNotification;
import org.mule.runtime.api.notification.PipelineMessageNotificationListener;
import org.mule.runtime.core.api.context.notification.ServerNotificationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * This is ProcessorInterceptorFactory API to intercept flow step execution.
 */ 
public class MuleProcessorInterceptorFactory implements ProcessorInterceptorFactory {

	@Inject
	private Registry registry;

	private Logger logger = LoggerFactory.getLogger(MuleProcessorInterceptorFactory.class);

	/*
	 * Creation of flow interceptors is done with a byproduct of only-once instantiation of flow listeners.
	 */
	@Override
	public ProcessorInterceptor get() {
		ApmHandler apmHandler = registry.lookupByType(ApmHandler.class).get();

		setupPipelineListener(apmHandler);

		return new MuleProcessorInterceptor(apmHandler);
	}

	// Setup a singleton flow event listener once.
	// TODO: find a away to do it in the tracer.xml
	private void setupPipelineListener(ApmHandler apmHandler) {
		ServerNotificationManager notificationManager = registry.lookupByType(ServerNotificationManager.class).get();

		// Register only once, check if hasn't been registered before.
		synchronized (notificationManager) {
			if (notificationManager.getListeners().stream()
					.anyMatch(x -> x.getListener().getClass() == ApmPipelineNotificationListener.class))
				return;
		}

		ApmPipelineNotificationListener apmPipelineNotificationListener = new ApmPipelineNotificationListener(
				apmHandler);

		// Enable notifications
		notificationManager.setNotificationDynamic(true);
		notificationManager.addInterfaceToType(PipelineMessageNotificationListener.class,
				PipelineMessageNotification.class);

		notificationManager.addListener(apmPipelineNotificationListener);

		logger.debug("Created Elastic APM event lifecycle listeners");
	}

}
