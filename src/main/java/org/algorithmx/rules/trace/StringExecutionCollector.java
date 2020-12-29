package org.algorithmx.rules.trace;

import org.algorithmx.rules.event.ExecutionEvent;

public class StringExecutionCollector extends ExecutionCollector {

    private final StringBuilder builder = new StringBuilder();

    public StringExecutionCollector() {
        super();
    }

    public StringExecutionCollector(boolean detailed) {
        super(detailed);
    }

    @Override
    public void collect(ExecutionEvent event, String executionText) {
        builder.append(executionText);
        builder.append(System.lineSeparator());
    }

    public String getOutput() {
        return builder.toString();
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}
