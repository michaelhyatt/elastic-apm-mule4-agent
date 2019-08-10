package co.elastic.apm.mule4.agent;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SimpleFlowTest extends BaseAbstractApmMuleTestCase {

	@Test
	public void testSimpleFlow() throws Exception {
		flowRunner("dep-testFlow").run();
		
		Thread.sleep(1000);
		
		assertEquals("simple_testFlow", getTransaction().getNameAsString());
//		assertEquals(1, getSpans().size());
//		assertEquals("Logger", getSpans().get(0).getNameAsString());
	}

	@Override
	protected String getConfigFile() {
		return "SimpleFlowTest.xml";
	}
	
}
