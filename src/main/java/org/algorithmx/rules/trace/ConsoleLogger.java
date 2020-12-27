package org.algorithmx.rules.trace;

import org.algorithmx.rules.event.ExecutionEvent;

public class ConsoleLogger extends ExecutionCollector {

    public ConsoleLogger() {
        super();
    }

    @Override
    public void collect(ExecutionEvent event, String executionText) {
        System.out.println(executionText);
    }
}
