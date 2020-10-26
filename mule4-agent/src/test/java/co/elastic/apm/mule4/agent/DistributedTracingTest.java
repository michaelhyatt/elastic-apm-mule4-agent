package co.elastic.apm.mule4.agent;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

import co.elastic.apm.mule4.agent.config.BaseAbstractApmMuleTestCase;

public class DistributedTracingTest extends BaseAbstractApmMuleTestCase {

	@Test
	public void testSimpleFlow() throws Exception {
		
		HttpGet getRequest = new HttpGet("http://localhost:8998/");
		getRequest.addHeader("elastic-apm-traceparent", "00-0af7651916cd43dd8448eb211c80319c-b9c7c989f97918e1-01");

		HttpClient httpClient = HttpClientBuilder.create().build();

		httpClient.execute(getRequest);

		Thread.sleep(1000);

	}

	@Override
	protected String getConfigFile() {
		return "DistributedTracingTest.xml";
	}

}
