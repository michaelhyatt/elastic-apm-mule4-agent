package co.elastic.apm.mule4.agent;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.mule.runtime.api.notification.PipelineMessageNotification;
import org.mule.runtime.api.notification.PipelineMessageNotificationListener;

public class ApmPipelineNotificationListener
		implements PipelineMessageNotificationListener<PipelineMessageNotification> {

	private ApmHandler apmHandler;

	private Logger logger = LogManager.getLogger(ApmPipelineNotificationListener.class);

	public ApmPipelineNotificationListener(ApmHandler apmHandler) {
		this.apmHandler = apmHandler;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onNotification(PipelineMessageNotification notification) {
		logger.debug("===> Received " + notification.getClass().getName() + ":" + notification.getActionName());

		switch (notification.getAction().getActionId()) {
		case PipelineMessageNotification.PROCESS_START:
			apmHandler.handleFlowStartEvent(notification);
			break;

		case PipelineMessageNotification.PROCESS_END:
		case PipelineMessageNotification.PROCESS_COMPLETE:
			apmHandler.handleFlowEndEvent(notification);
			break;
		}
	}

}
