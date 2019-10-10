package co.elastic.apm.mule4.agent.span;

import java.util.Map;
import java.util.Optional;

import org.mule.runtime.api.event.Event;
import org.mule.runtime.api.interception.InterceptionEvent;
import org.mule.runtime.api.metadata.TypedValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.elastic.apm.mule4.agent.transaction.ApmTransaction;
import co.elastic.apm.mule4.agent.transaction.TransactionUtils;

public class ApmEventUtils {

	private static Logger logger = LoggerFactory.getLogger(ApmEventUtils.class);

	public static Optional<ApmTransaction> getTransaction(Event event) {

		Map<String, TypedValue<?>> variables = event.getVariables();
		TypedValue<?> typedValue = variables.get(TransactionUtils.ELASTIC_APM_TRANSACTION);
		
		if (typedValue == null) {
			logger.debug("getTransaction: Transaction **NOT** found in event.");
			return Optional.empty();
		}
			
		logger.debug("getTransaction: Transaction found in event.");
		
		return Optional.ofNullable((ApmTransaction) typedValue.getValue());
	}

	public static void setTransaction(InterceptionEvent event, ApmTransaction transaction) {
		event.addVariable(TransactionUtils.ELASTIC_APM_TRANSACTION, transaction);
	}

	public static ApmTransaction startTransaction(InterceptionEvent event) {
		return TransactionUtils.startTransaction(event);
	}
}
