package org.algorithmx.rules.event;

import org.algorithmx.rules.lib.spring.util.Assert;

import java.util.Date;

public class ExecutionEvent<T> implements Comparable<ExecutionEvent> {

    private final EventType eventType;
    private final T data;
    private final Date time;

    public ExecutionEvent(EventType eventType, T data) {
        this(eventType, data, new Date());
    }

    public ExecutionEvent(EventType eventType, T data, Date time) {
        super();
        Assert.notNull(eventType, "eventType cannot be null.");
        Assert.notNull(time, "time cannot be null.");
        this.eventType = eventType;
        this.data = data;
        this.time = time;
    }

    @Override
    public int compareTo(ExecutionEvent o) {
        return time.compareTo(o.time);
    }

    public boolean isError() {
        return data instanceof Exception;
    }

    public EventType getEventType() {
        return eventType;
    }

    public T getData() {
        return data;
    }

    public Date getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "RuleEvent{" +
                "eventType=" + eventType +
                ", data=" + data +
                ", time=" + time +
                '}';
    }
}
