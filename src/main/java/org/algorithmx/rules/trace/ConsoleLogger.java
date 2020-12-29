package org.algorithmx.rules.trace;

import org.algorithmx.rules.event.ExecutionEvent;

public class ConsoleLogger extends ExecutionCollector {

    public ConsoleLogger() {
        super(true);
    }

    public ConsoleLogger(boolean detailed) {
        super(detailed);
    }

    @Override
    public void collect(ExecutionEvent event, String executionText) {
        System.out.println(executionText);
    }
}
