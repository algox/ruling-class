package org.algorithmx.rules.event;

public interface ExecutionListener {

    <T> void onEvent(ExecutionEvent<T> event);
}
