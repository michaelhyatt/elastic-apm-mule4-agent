package co.elastic.apm.mule4.agent;

import javax.inject.Inject;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.mule.runtime.api.artifact.Registry;
import org.mule.runtime.api.interception.ProcessorInterceptor;
import org.mule.runtime.api.interception.ProcessorInterceptorFactory;
import org.mule.runtime.api.notification.PipelineMessageNotification;
import org.mule.runtime.api.notification.PipelineMessageNotificationListener;
import org.mule.runtime.core.api.context.notification.ServerNotificationManager;

public class MuleProcessorInterceptorFactory implements ProcessorInterceptorFactory {

	@Inject
	private Registry registry;

	private Logger logger = LogManager.getLogger(MuleProcessorInterceptorFactory.class);

	@Override
	public ProcessorInterceptor get() {
		ApmHandler apmHandler = registry.lookupByType(ApmHandler.class).get();

		setupPipelineListener(apmHandler);

		return new MuleProcessorInterceptor(apmHandler);
	}

	private void setupPipelineListener(ApmHandler apmHandler) {
		ServerNotificationManager notificationManager = registry.lookupByType(ServerNotificationManager.class).get();

		// Register only once
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
