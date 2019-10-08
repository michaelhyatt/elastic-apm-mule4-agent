package co.elastic.apm.mule4.agent;

import static org.junit.Assert.assertEquals;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

import co.elastic.apm.mule4.agent.config.BaseAbstractApmMuleTestCase;

public class DistributedTracingHttpListenerTest extends BaseAbstractApmMuleTestCase {

	@Test
	public void testSimpleFlow() throws Exception {

		HttpGet getRequest = new HttpGet("http://127.0.0.1:8889/");
		getRequest.addHeader("elastic-apm-traceparent", "00-0af7651916cd43dd8448eb211c80319c-b9c7c989f97918e1-01");

		HttpClient httpClient = HttpClientBuilder.create().build();

		httpClient.execute(getRequest);

		Thread.sleep(1000);

		assertEquals("Test", getTransaction().getNameAsString());
		assertEquals(2, getSpans().size());
		assertEquals("Logger", getSpans().get(0).getNameAsString());
		assertEquals("Set Payload", getSpans().get(1).getNameAsString());

	}

	@Override
	protected String getConfigFile() {
		return "DistributedTracingHTTPHeaderListenerTest.xml";
	}

}
