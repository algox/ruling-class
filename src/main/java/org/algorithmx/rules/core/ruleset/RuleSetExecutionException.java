package org.algorithmx.rules.core.ruleset;

import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.lib.spring.util.Assert;

public class RuleSetExecutionException extends UnrulyException {

    private final RuleSet rules;

    public RuleSetExecutionException(String message, Throwable cause, RuleSet rules) {
        super(message, cause);
        Assert.notNull(rules, "rules cannot be null.");
        this.rules = rules;
    }

    public RuleSet getRule() {
        return rules;
    }
}
