package org.algorithmx.rules.core.rule;

public enum RuleExecutionStatus {

    PASS, FAIL, SKIPPED, ERROR;

    public boolean isPass() {
        return this == PASS;
    }

    public boolean isFail() {
        return this == FAIL;
    }

    public boolean isSkipped() {
        return this == SKIPPED;
    }

    public boolean isError() {
        return this == ERROR;
    }
}
