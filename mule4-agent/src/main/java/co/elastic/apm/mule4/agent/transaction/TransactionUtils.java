package co.elastic.apm.mule4.agent.transaction;

import java.lang.reflect.Field;
import java.util.Optional;

import org.mule.extension.http.api.HttpRequestAttributes;
import org.mule.runtime.api.event.Event;
import org.mule.runtime.api.metadata.DataType;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.api.notification.PipelineMessageNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.elastic.apm.api.ElasticApm;
import co.elastic.apm.api.Transaction;

/*
 * Handling of Transaction starts and ends
 */
public class TransactionUtils {

	private static Logger logger = LoggerFactory.getLogger(TransactionUtils.class);

	public static final String ELASTIC_APM_TRACEPARENT = "elastic-apm-traceparent";
	public static final String ELASTIC_APM_TRANSACTION = "elastic-apm-transaction";

	private static String getFlowName(PipelineMessageNotification notification) {
		return notification.getResourceIdentifier();
	}

	/*
	 * End transaction.
	 */
	public static void endTransaction(TransactionStore transactionStore, PipelineMessageNotification notification) {

		// We only create and end transactions related to the top level flow. All the
		// rest of the flows invoked through flow-ref are not represented as
		// transactions and ignored. Only the corresponding flow-ref step is represented
		// as Span.
		if (!isEndOfTopFlow(notification))
			return;

		Transaction transaction = retrieveTransaction(notification);

		populateFinalTransactionDetails(transaction, notification);

		transaction.end();
	}

	private static boolean isEndOfTopFlow(PipelineMessageNotification notification) {

		ApmTransaction value = retrieveTransaction(notification);
		String flowName = value.getFlowName();
		String notificationFlowname = getFlowName(notification);
		boolean result = notificationFlowname.equals(flowName);
		logger.debug("Comparing flow names {} == {} returning {}", notificationFlowname, flowName, result);

		return result;
	}

	protected static ApmTransaction retrieveTransaction(PipelineMessageNotification notification) {
		Event event = notification.getEvent();

		if (event == null) {
			Exception myException = notification.getException();

			// Hack hack hack to get to the event hidden in Exception super class.
			Field f;
			try {
				f = myException.getClass().getSuperclass().getDeclaredField("event");
				f.setAccessible(true);
				event = (Event) f.get(myException);
			} catch (NoSuchFieldException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		TypedValue<?> variable = event.getVariables().get(ELASTIC_APM_TRANSACTION);

		if (variable == null)
			throw new RuntimeException("Can't find variable with APM transaction");

		ApmTransaction value = (ApmTransaction) variable.getValue();
		return value;
	}

	private static void populateFinalTransactionDetails(Transaction transaction,
			PipelineMessageNotification notification) {
		// Noop
		// TODO: Populate the output properties
		// TODO: Populate final flowVars
		// TODO: Populate the transaction status
	}

	public static ApmTransaction startTransaction(Event event) {

		if (hasRemoteParent(event))
			return new ApmTransaction(ElasticApm.startTransactionWithRemoteParent(x -> headerExtractor(x, event)));
		else
			return new ApmTransaction(ElasticApm.startTransaction());

	}

	private static Optional<String> getHttpTraceHeaderValue(String name, TypedValue<Object> attributes) {
		HttpRequestAttributes httpRequestAttributes = (HttpRequestAttributes) attributes.getValue();
		return Optional.ofNullable(httpRequestAttributes.getHeaders().get(name));
	}

	private static boolean hasRemoteParent(Event event) {

		TypedValue<Object> attributes = event.getMessage().getAttributes();
		DataType type = attributes.getDataType();

		// Handle HTTP context propagation
		if (type.getType() == HttpRequestAttributes.class)
			return getHttpTraceHeaderValue(ELASTIC_APM_TRACEPARENT, attributes).isPresent();

		return false;
	}

	private static String headerExtractor(String x, Event event) {

		TypedValue<Object> attributes = event.getMessage().getAttributes();
		DataType type = attributes.getDataType();

		// Handle HTTP context propagation
		if (type.getType() == HttpRequestAttributes.class)
			return getHttpTraceHeaderValue(x, attributes).get();

		return null;
	}

}
