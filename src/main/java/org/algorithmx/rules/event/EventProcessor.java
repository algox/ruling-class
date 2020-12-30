package org.algorithmx.rules.event;

public interface EventProcessor {

    static EventProcessor create() {
        return new DefaultEventProcessor();
    }

    boolean isEventsEnabled();

    void setEventsEnabled(boolean eventsEnabled);

    void addEventListener(ExecutionListener listener);

    void removeEventListener(ExecutionListener listener);

    <T> void fireListeners(ExecutionEvent<T> event);
}
