package co.elastic.apm.mule4.agent.span;

import co.elastic.apm.api.HeaderInjector;
import co.elastic.apm.api.Scope;
import co.elastic.apm.api.Span;

@SuppressWarnings("deprecation")
public class ApmSpan {

	private Span span;

	public ApmSpan(Span span) {
		this.span = span;
	}

	public Span setName(String name) {
		return span.setName(name);
	}

	public Span setType(String type) {
		return span.setType(type);
	}

	public Span addTag(String key, String value) {
		return span.addTag(key, value);
	}

	public Span addLabel(String key, String value) {
		return span.addLabel(key, value);
	}

	public Span addLabel(String key, Number value) {
		return span.addLabel(key, value);
	}

	public Span addLabel(String key, boolean value) {
		return span.addLabel(key, value);
	}

	public Span setStartTimestamp(long epochMicros) {
		return span.setStartTimestamp(epochMicros);
	}

	public Span createSpan() {
		return span.createSpan();
	}

	public Span startSpan(String type, String subtype, String action) {
		return span.startSpan(type, subtype, action);
	}

	public Span startSpan() {
		return span.startSpan();
	}

	public void end() {
		span.end();
	}

	public void end(long epochMicros) {
		span.end(epochMicros);
	}

	public void captureException(Throwable throwable) {
		span.captureException(throwable);
	}

	public String getId() {
		return span.getId();
	}

	public String getTraceId() {
		return span.getTraceId();
	}

	public Scope activate() {
		return span.activate();
	}

	public boolean isSampled() {
		return span.isSampled();
	}

	public void injectTraceHeaders(HeaderInjector headerInjector) {
		span.injectTraceHeaders(headerInjector);
	}

}
