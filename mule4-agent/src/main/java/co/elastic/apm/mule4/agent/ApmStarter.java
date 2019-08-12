package co.elastic.apm.mule4.agent;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import co.elastic.apm.attach.ElasticApmAttacher;

@Component
public class ApmStarter {

	protected static final String ELASTIC_APM = "elastic.apm.";

	private Logger logger = LoggerFactory.getLogger(ApmStarter.class);

	public ApmStarter() {

		logger.debug("Initialising Elastic Java APM agent");

		logger.debug("Elastic APM system properties set:");

		// Ensure not to autoinstrument
		System.setProperty("elastic.apm.instrument", "false");

		Supplier<Stream<Entry<Object, Object>>> apmPropertiesFilter = () -> System.getProperties().entrySet().stream()
				.filter(x -> x.getKey().toString().startsWith(ELASTIC_APM));

		apmPropertiesFilter.get().map(x -> " " + x.getKey() + "=" + x.getValue()).forEach(logger::debug);

		logger.debug("Attaching ElasticApmAttacher");

		Map<String, String> configMap = apmPropertiesFilter.get().collect(
				Collectors.toMap(k -> k.getKey().toString().replace(ELASTIC_APM, ""), v -> v.getValue().toString()));

		ElasticApmAttacher.attach(configMap);

	}

}
