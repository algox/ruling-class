package org.algorithmx.rules.core.ruleset;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class RuleResultSet {

    private List<RuleResult> results = new LinkedList<>();

    public RuleResultSet() {
        super();
    }

    public void add(RuleResult result) {
        results.add(result);
    }

    public boolean isAllPass() {
        Set<RuleExecutionStatus> candidates = new HashSet<>();
        candidates.add(RuleExecutionStatus.PASS);
        candidates.add(RuleExecutionStatus.SKIPPED);
        return filter(candidates);
    }

    private boolean filter(Set<RuleExecutionStatus> candidates) {
        boolean result = true;

        for (RuleResult ruleResult : results) {
            if (!candidates.contains(ruleResult.getStatus())) {
                result = false;
                break;
            }
        }

        return result;
    }
}
