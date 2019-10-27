package co.elastic.apm.mule4.agent.span;

import java.util.Stack;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ApmSpanStack {
	
	private Stack<ApmSpan> stack = new Stack<ApmSpan>();
	
	public ApmSpanStack(ApmSpan parentSpan) {
		synchronized (stack) {
			stack.push(parentSpan);
		}
	}

	synchronized public ApmSpan peek() {
		return stack.peek();
	}

	synchronized public void push(ApmSpan item) {
		stack.push(item);
	}

	synchronized public ApmSpan pop() {
		return stack.pop();
	}
	
	public String getElasticApmTraceparent() throws InterruptedException, ExecutionException {
		
		CompletableFuture<String> future = new CompletableFuture<>();
		
		ApmSpan span = stack.peek();
		
		span.injectTraceHeaders((name, value) -> {
			future.complete(value);
		});
		
		return future.get();
	}

}
