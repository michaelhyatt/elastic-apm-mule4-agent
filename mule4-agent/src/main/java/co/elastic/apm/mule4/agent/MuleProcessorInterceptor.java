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

/*
 * Flow step interceptor created by @MuleProcessorInterceptorFactory for each step in the Mule flow.
 */
public class MuleProcessorInterceptor implements ProcessorInterceptor {

	private Logger logger = LoggerFactory.getLogger(MuleProcessorInterceptor.class);
	private ApmHandler apmHandler;

	public MuleProcessorInterceptor(ApmHandler apmHandler) {
		this.apmHandler = apmHandler;
	}

	/*
	 * Wrapper around every step in the Mule flow.
	 * 
	 * @see org.mule.runtime.api.interception.ProcessorInterceptor#around(org.mule.
	 * runtime.api.component.location.ComponentLocation, java.util.Map,
	 * org.mule.runtime.api.interception.InterceptionEvent,
	 * org.mule.runtime.api.interception.InterceptionAction)
	 */
	@Override
	public CompletableFuture<InterceptionEvent> around(ComponentLocation location,
			Map<String, ProcessorParameterValue> parameters, InterceptionEvent event, InterceptionAction action) {

		// Before flow step is executing.

		logger.debug("===> Before step {}", location.getLocation());
		
		Span span = apmHandler.handleProcessorStartEvent(location, parameters, event);

		// CompletableFuture handling exception and completion of the step.
		return action.proceed().exceptionally(ex -> {

			// Flow step threw an Exception
			logger.debug("===> Exception step {}", location.getLocation());

			apmHandler.handleExceptionEvent(span, location, parameters, event, ex);

			throw new RuntimeException(ex);

		}).thenApply(finalEvent -> {

			// Flow step completed successfully.
			logger.debug("===> After step {}", location.getLocation());

			apmHandler.handleProcessorEndEvent(span, location, parameters, finalEvent);

			return finalEvent;
		});

	}

}
