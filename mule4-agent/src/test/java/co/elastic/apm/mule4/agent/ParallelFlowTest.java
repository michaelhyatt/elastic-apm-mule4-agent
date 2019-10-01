package co.elastic.apm.mule4.agent;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import co.elastic.apm.mule4.agent.config.BaseAbstractApmMuleTestCase;

public class ParallelFlowTest extends BaseAbstractApmMuleTestCase {

	@Test
	public void testSimpleFlow() throws Exception {
		flowRunner("ParallelFlowTest").run();
		
		Thread.sleep(1000);
		
		assertEquals("ParallelFlowTest", getTransaction().getNameAsString());
		assertEquals(9, getSpans().size());
		assertEquals("Logger1", getSpans().get(0).getNameAsString());
		assertEquals("Logger21", getSpans().get(1).getNameAsString());
		assertEquals("Logger22", getSpans().get(2).getNameAsString());
		assertEquals("Logger31", getSpans().get(3).getNameAsString());
		assertEquals("Logger23", getSpans().get(4).getNameAsString());
		assertEquals("Logger32", getSpans().get(5).getNameAsString());
		assertEquals("Logger41", getSpans().get(6).getNameAsString());
		assertEquals("Scatter-Gather", getSpans().get(7).getNameAsString());
		assertEquals("Logger5", getSpans().get(8).getNameAsString());
	}

	@Override
	protected String getConfigFile() {
		return "ParallelFlowTest.xml";
	}
	
}
