package org.algorithmx.rules.event;

import org.algorithmx.rules.lib.spring.util.Assert;

import java.util.ArrayList;
import java.util.List;

public class DefaultEventProcessor implements EventProcessor {

    private final List<ExecutionListener> listeners = new ArrayList<>();
    private boolean eventsEnabled = true;

    public DefaultEventProcessor() {
        super();
    }

    public boolean isEventsEnabled() {
        return eventsEnabled;
    }

    public void setEventsEnabled(boolean eventsEnabled) {
        this.eventsEnabled = eventsEnabled;
    }

    public synchronized void addEventListener(ExecutionListener listener) {
        this.listeners.add(listener);
    }

    public synchronized void removeEventListener(ExecutionListener listener) {
        this.listeners.remove(listener);
    }

    public synchronized <T> void fireListeners(ExecutionEvent<T> event) {
        Assert.notNull(event, "event cannot be null.");
        // Events turned off
        if (!isEventsEnabled()) return;
        // Fire all the listeners
        for (ExecutionListener listener : listeners) {
            listener.onEvent(event);
        }
    }

}
