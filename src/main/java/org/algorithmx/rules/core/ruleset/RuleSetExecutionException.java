package org.algorithmx.rules.core.ruleset;

import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.event.EventType;
import org.algorithmx.rules.lib.spring.util.Assert;

public class RuleSetExecutionException extends UnrulyException {

    private final RuleSet rules;
    private final EventType event;

    public RuleSetExecutionException(String message, Throwable cause, RuleSet rules, EventType event) {
        super(message, cause);
        Assert.notNull(rules, "rules cannot be null.");
        this.rules = rules;
        this.event = event;
    }

    public RuleSet getRule() {
        return rules;
    }

    public EventType getEvent() {
        return event;
    }
}
