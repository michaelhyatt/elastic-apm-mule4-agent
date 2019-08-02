package co.elastic.apm.mule4.agent;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.elastic.apm.attach.ElasticApmAttacher;

public class ApmStarter {
	
	private static final String ELASTIC_APM = "elastic.apm.";

	private Logger logger = LoggerFactory.getLogger(ApmStarter.class);
	
	public ApmStarter() {
		
		logger.debug("Initialising Elastic Java APM agent");

		logger.debug("Elastic APM system properties set:");

		// Ensure not to autoinstrument
		System.setProperty("elastic.apm.instrument", "false");
		System.setProperty("elastic.apm.log_level", "TRACE");
		System.setProperty("elastic.apm.disable_instrumentations",
				"annotations, apache-httpclient, asynchttpclient, concurrent, dispatcher-servlet, elasticsearch-restclient, executor, http-client, incubating, jax-rs, jax-ws, jdbc, jms, jsf, okhttp, opentracing, public-api, render, scheduled, servlet-api, servlet-api-async, servlet-input-stream, servlet-service-name, spring-mvc, spring-resttemplate, spring-service-name, urlconnection");

		Supplier<Stream<Entry<Object, Object>>> apmPropertiesFilter = () -> System.getProperties().entrySet().stream()
				.filter(x -> x.getKey().toString().startsWith(ELASTIC_APM));

		apmPropertiesFilter.get().map(x -> " " + x.getKey() + "=" + x.getValue()).forEach(logger::debug);

		logger.debug("Attaching ElasticApmAttacher");

		Map<String, String> configMap = apmPropertiesFilter.get().collect(
				Collectors.toMap(k -> k.getKey().toString().replace(ELASTIC_APM, ""), v -> v.getValue().toString()));

		ElasticApmAttacher.attach(configMap);
	}
	
}
