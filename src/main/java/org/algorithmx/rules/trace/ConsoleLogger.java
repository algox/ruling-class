package org.algorithmx.rules.trace;

import org.algorithmx.rules.event.ExecutionEvent;

public class ConsoleLogger extends ExecutionCollector {

    public ConsoleLogger() {
        super();
    }

    public ConsoleLogger(boolean detailed) {
        super();
        setDetailed(detailed);
    }

    @Override
    public void collect(ExecutionEvent event, String executionText) {
        System.out.println(executionText);
    }
}
