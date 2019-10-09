package co.elastic.apm.mule4.agent;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.notMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.core.api.event.CoreEvent;

import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import co.elastic.apm.mule4.agent.config.BaseAbstractApmMuleTestCase;

public class HttpRequesterPropagationTest extends BaseAbstractApmMuleTestCase {

	@Rule
	public WireMockRule wireMockService = new WireMockRule(options().port(8889).notifier(new ConsoleNotifier(true)));

	@Test
	public void testSimpleFlow() throws Exception {

		wireMockService.stubFor(get(urlEqualTo("/"))
				.withHeader("myotherheader", equalTo("value"))
				.withHeader("myheader", notMatching("^$"))
				.withHeader("elastic-apm-traceparent", matching("^00-(.*)-01$"))
				.willReturn(aResponse().withStatus(200)));

		CoreEvent result = flowRunner("TestMyHeaderFlow").run();

		Thread.sleep(1000);
		
		Map<String, TypedValue<?>> variables = result.getVariables();
		
		assertEquals("value", variables.get("this-is-just-a-var").getValue());
		assertEquals(32, variables.get("elastic-apm-trace-id").getValue().toString().length());
		
		assertEquals("TestMyHeaderFlow", getTransaction().getNameAsString());
		assertEquals(3, getSpans().size());
		assertEquals("Set Variable", getSpans().get(0).getNameAsString());
		assertEquals("Request", getSpans().get(1).getNameAsString());
		assertEquals("Logger", getSpans().get(2).getNameAsString());

	}

	@Override
	protected String getConfigFile() {
		return "HttpRequesterPropagationTest.xml";
	}

}
