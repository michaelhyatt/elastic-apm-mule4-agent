package co.elastic.apm.mule4.agent;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.stream.Collectors;

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

		assertArrayEquals(Arrays.asList("Logger21", "Logger22", "Logger23", "Logger31", "Logger32").toArray(),
				getSpans().subList(1, 6).stream().map(x -> x.getNameAsString()).sorted().collect(Collectors.toList())
						.toArray());

		assertEquals("Scatter-Gather", getSpans().get(7).getNameAsString());
		assertEquals("Logger5", getSpans().get(8).getNameAsString());
	}

	@Override
	protected String getConfigFile() {
		return "ParallelFlowTest.xml";
	}

}
