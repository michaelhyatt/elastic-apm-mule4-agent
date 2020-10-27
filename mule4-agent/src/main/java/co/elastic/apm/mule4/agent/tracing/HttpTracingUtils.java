package co.elastic.apm.mule4.agent.tracing;

import org.mule.extension.http.api.HttpRequestAttributes;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.api.notification.PipelineMessageNotification;
import org.mule.runtime.api.util.MultiMap;

public class HttpTracingUtils {

	public static final String ELASTIC_APM_TRACEPARENT = "elastic-apm-traceparent";

	public static boolean isHttpEvent(PipelineMessageNotification notification) {
		return extractAttributes(notification).getDataType().getType() == HttpRequestAttributes.class;
	}

	public static boolean hasRemoteParent(PipelineMessageNotification notification) {

		if (!isHttpEvent(notification))
			return false;

		return getHttpAttributes(notification).containsKey(ELASTIC_APM_TRACEPARENT);
	}

	public static String getTracingHeaderValue(String x, PipelineMessageNotification notification) {
		return getHttpAttributes(notification).get(ELASTIC_APM_TRACEPARENT);
	}

	private static MultiMap<String, String> getHttpAttributes(PipelineMessageNotification notification) {
		
		HttpRequestAttributes attributes = (HttpRequestAttributes) extractAttributes(notification).getValue();
		
		return attributes.getHeaders();
	}

	private static TypedValue<Object> extractAttributes(PipelineMessageNotification notification) {
		return notification.getEvent().getMessage().getAttributes();
	}

}
