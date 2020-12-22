package org.algorithmx.rules.core.ruleset;

public enum RuleExecutionStatus {

    PASS, FAIL, SKIPPED, ERROR;

    boolean isPass() {
        return this == PASS;
    }

    boolean isFail() {
        return this == FAIL;
    }

    boolean isSkipped() {
        return this == SKIPPED;
    }

    boolean isError() {
        return this == ERROR;
    }
}
