package co.elastic.apm.mule4.agent;

import org.mule.runtime.api.notification.PipelineMessageNotification;
import org.mule.runtime.api.notification.PipelineMessageNotificationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Listener for Mule notifications on flow start, end and completion.
 */
public class ApmPipelineNotificationListener
		implements PipelineMessageNotificationListener<PipelineMessageNotification> {

	private ApmHandler apmHandler;

	private Logger logger = LoggerFactory.getLogger(ApmPipelineNotificationListener.class);

	public ApmPipelineNotificationListener(ApmHandler apmHandler) {
		this.apmHandler = apmHandler;
	}

	@Override
	public void onNotification(PipelineMessageNotification notification) {
		logger.debug("===> Received " + notification.getClass().getName() + ":" + notification.getActionName());

		// Event listener
		String identifier = notification.getAction().getIdentifier();

//		if (String.valueOf(PipelineMessageNotification.PROCESS_START).equals(identifier))
//			apmHandler.handleFlowStartEvent(notification);

		// On exception this event doesn't fire, only on successful flow completion.
		// else if (identifier ==
		// String.valueOf(PipelineMessageNotification.PROCESS_END)

//		else 
			if (String.valueOf(PipelineMessageNotification.PROCESS_COMPLETE).equals(identifier))
			apmHandler.handleFlowEndEvent(notification);

	}

}
