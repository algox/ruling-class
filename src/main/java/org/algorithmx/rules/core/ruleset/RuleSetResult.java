package org.algorithmx.rules.core.ruleset;

import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.rule.RuleExecutionStatus;
import org.algorithmx.rules.core.rule.RuleResult;
import org.algorithmx.rules.lib.spring.util.Assert;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class RuleSetResult implements Iterable<RuleResult> {

    private final List<RuleResult> results = new LinkedList<>();
    private final Bindings bindings;
    private boolean preConditionCheck = false;

    public RuleSetResult(Bindings bindings) {
        super();
        Assert.notNull(bindings, "bindings cannot be null.");
        this.bindings = bindings;
    }

    public void add(RuleResult result) {
        Assert.notNull(result, "result cannot be null.");
        results.add(result);
    }

    public Bindings getBindings() {
        return bindings;
    }

    public RuleResult get(int index) {
        return results.get(index);
    }

    public boolean isAllPass() {
        return isTrue(r -> r.getStatus().isPass());
    }

    public int getPassCount() {
        return getCount(RuleExecutionStatus.PASS);
    }

    public boolean isAllPassOrSkip() {
        return isTrue(r -> r.getStatus().isPass() || r.getStatus().isSkipped());
    }

    public int getPassOrSkipCount() {
        return getCount(RuleExecutionStatus.PASS, RuleExecutionStatus.SKIPPED);
    }

    public boolean isAnyPass() {
        return isTrue(r -> !r.getStatus().isPass());
    }

    public boolean isAnySkip() {
        return isTrue(r -> !r.getStatus().isSkipped());
    }

    public boolean isAllSkip() {
        return isTrue(r -> r.getStatus().isSkipped());
    }

    public int getSkipCount() {
        return getCount(RuleExecutionStatus.SKIPPED);
    }

    public boolean isAllFail() {
        return isTrue(r -> r.getStatus().isFail());
    }

    public boolean isAnyFail() {
        return isTrue(r -> !r.getStatus().isFail());
    }

    public int getFailCount() {
        return getCount(RuleExecutionStatus.FAIL);
    }

    public boolean isAllFailOrSkip() {
        return isTrue(r -> r.getStatus().isFail() || r.getStatus().isSkipped());
    }

    public int getFailOrSkipCount() {
        return getCount(RuleExecutionStatus.FAIL, RuleExecutionStatus.SKIPPED);
    }

    public boolean isAnyError() {
        return isTrue(r -> !r.getStatus().isError());
    }

    public boolean isAllError() {
        return isTrue(r -> r.getStatus().isError());
    }

    public int getErrorCount() {
        return getCount(RuleExecutionStatus.ERROR);
    }

    public boolean isTrue(RuleExecutionStatus ... statuses) {
        Set<RuleExecutionStatus> values = new HashSet<>(Arrays.asList(statuses));
        return isTrue(r -> values.contains(r));
    }

    public boolean isTrue(Predicate<RuleResult> predicate) {
        boolean result = true;

        for (RuleResult ruleResult : results) {
            if (!predicate.test(ruleResult)) {
                result = false;
                break;
            }
        }

        return result;
    }

    public int getCount(RuleExecutionStatus ... statuses) {
        Set<RuleExecutionStatus> values = new HashSet<>(Arrays.asList(statuses));
        return getCount(r -> values.contains(r));
    }

    public int getCount(Predicate<RuleResult> predicate) {
        int result = 0;

        for (RuleResult ruleResult : results) {
            if (predicate.test(ruleResult)) {
                result++;
            }
        }

        return result;
    }

    @Override
    public Iterator<RuleResult> iterator() {
        return results.iterator();
    }

    public int size() {
        return results.size();
    }

    public boolean isPreConditionCheck() {
        return preConditionCheck;
    }

    void setPreConditionCheck(boolean preConditionCheck) {
        this.preConditionCheck = preConditionCheck;
    }

    @Override
    public String toString() {
        return "RuleResultSet{" +
                "results=" + results +
                ", preConditionCheck=" + preConditionCheck +
                '}';
    }
}
