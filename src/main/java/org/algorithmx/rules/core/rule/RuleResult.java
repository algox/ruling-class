package org.algorithmx.rules.core.rule;

import org.algorithmx.rules.lib.spring.util.Assert;

public class RuleResult {

    private final String ruleName;
    private final RuleExecutionStatus status;

    public RuleResult(String ruleName, RuleExecutionStatus status) {
        super();
        Assert.notNull(ruleName, "ruleName cannot be null.");
        Assert.notNull(status, "status cannot be null.");
        this.ruleName = ruleName;
        this.status = status;
    }

    public String getRuleName() {
        return ruleName;
    }

    public RuleExecutionStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "RuleResult{" +
                "ruleName='" + ruleName + '\'' +
                ", status=" + status +
                '}';
    }
}
