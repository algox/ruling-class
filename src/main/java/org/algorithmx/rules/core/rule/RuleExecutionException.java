package org.algorithmx.rules.core.rule;

import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.event.EventType;
import org.algorithmx.rules.lib.spring.util.Assert;

public class RuleExecutionException extends UnrulyException {

    private final Object ruleTarget;
    private final EventType event;

    public RuleExecutionException(String message, Throwable cause, Object ruleTarget) {
        this(message, cause, ruleTarget, null);
    }

    public RuleExecutionException(String message, Throwable cause, Object ruleTarget, EventType event) {
        super(message, cause);
        Assert.notNull(ruleTarget, "ruleTarget cannot be null.");
        this.ruleTarget = ruleTarget;
        this.event = event;
    }

    public Object getRuleTarget() {
        return ruleTarget;
    }

    public EventType getEvent() {
        return event;
    }
}