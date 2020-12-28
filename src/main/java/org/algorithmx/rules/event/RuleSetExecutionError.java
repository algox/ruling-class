package org.algorithmx.rules.event;

import org.algorithmx.rules.core.ruleset.RuleSet;
import org.algorithmx.rules.lib.spring.util.Assert;

public class RuleSetExecutionError {

    private final RuleSet rules;
    private final Exception error;

    public RuleSetExecutionError(RuleSet rules, Exception error) {
        super();
        Assert.notNull(rules, "rules cannot be null.");
        this.rules = rules;
        this.error = error;
    }

    public RuleSet getRules() {
        return rules;
    }

    public Exception getError() {
        return error;
    }

    @Override
    public String toString() {
        return "RuleSetExecutionError{" +
                "rules=" + rules +
                ", error=" + error +
                '}';
    }
}
