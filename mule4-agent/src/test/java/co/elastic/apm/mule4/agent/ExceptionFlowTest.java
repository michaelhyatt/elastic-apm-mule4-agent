package co.elastic.apm.mule4.agent;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import co.elastic.apm.mule4.agent.config.BaseAbstractApmMuleTestCase;

public class ExceptionFlowTest extends BaseAbstractApmMuleTestCase {

	@Test
	public void testSimpleFlow() throws Exception {
		flowRunner("ErrorFlowTest").withSourceCorrelationId("123").runExpectingException();
		
		Thread.sleep(1000);
		
		assertEquals("ErrorFlowTest", getTransaction().getNameAsString());
//		assertEquals(6, getSpans().size());
//		assertEquals("logger", getSpans().get(0).getNameAsString());
//		assertEquals("logger", getSpans().get(1).getNameAsString());
//		assertEquals("flow-ref", getSpans().get(2).getNameAsString());
//		assertEquals("logger", getSpans().get(3).getNameAsString());
//		assertEquals("flow-ref", getSpans().get(4).getNameAsString());
//		assertEquals("logger", getSpans().get(5).getNameAsString());
	}

	@Override
	protected String getConfigFile() {
		return "ErrorFlowTest.xml";
	}
	
}
