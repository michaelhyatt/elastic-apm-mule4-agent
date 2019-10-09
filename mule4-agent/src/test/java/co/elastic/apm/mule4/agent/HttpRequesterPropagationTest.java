package co.elastic.apm.mule4.agent;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.notMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import co.elastic.apm.mule4.agent.config.BaseAbstractApmMuleTestCase;

public class HttpRequesterPropagationTest extends BaseAbstractApmMuleTestCase {

	@Rule
	public WireMockRule wireMockService = new WireMockRule(options().port(8889));

	@Test
	public void testSimpleFlow() throws Exception {

		wireMockService.stubFor(get(urlEqualTo("/")).withHeader("myotherheader", equalTo("value"))
				.withHeader("myheader", notMatching("")));

		flowRunner("TestMyHeaderFlow").run();

		Thread.sleep(1000);

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
