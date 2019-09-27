package co.elastic.apm.mule4.agent;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.mule.runtime.api.component.location.ComponentLocation;
import org.mule.runtime.api.interception.InterceptionAction;
import org.mule.runtime.api.interception.InterceptionEvent;
import org.mule.runtime.api.interception.ProcessorInterceptor;
import org.mule.runtime.api.interception.ProcessorParameterValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.elastic.apm.api.Span;

public class MuleProcessorInterceptor implements ProcessorInterceptor {

	private Logger logger = LoggerFactory.getLogger(MuleProcessorInterceptor.class);
	private ApmHandler apmHandler;

	public MuleProcessorInterceptor(ApmHandler apmHandler) {
		this.apmHandler = apmHandler;
	}

	@Override
	public CompletableFuture<InterceptionEvent> around(ComponentLocation location,
			Map<String, ProcessorParameterValue> parameters, InterceptionEvent event, InterceptionAction action) {

		logger.debug("===> Before step {}", location.getLocation());

		Span span = apmHandler.handleProcessorStartEvent(location, parameters, event);

		return action.proceed().exceptionally(ex -> {

			logger.debug("===> Exception step {}", location.getLocation());
			

			apmHandler.handleExceptionEvent(span, location, parameters, event, ex);

			throw new RuntimeException(ex);

		}).thenApplyAsync(finalEvent -> {

			logger.debug("===> After step {}", location.getLocation());

			apmHandler.handleProcessorEndEvent(span, location, parameters, finalEvent);

			return finalEvent;
		});

	}

}
