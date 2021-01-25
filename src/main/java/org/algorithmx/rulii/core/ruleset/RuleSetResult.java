/*
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 1999-2021, Algorithmx Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.algorithmx.rulii.core.ruleset;

import org.algorithmx.rulii.bind.Bindings;
import org.algorithmx.rulii.core.rule.RuleExecutionStatus;
import org.algorithmx.rulii.core.rule.RuleResult;
import org.algorithmx.rulii.lib.spring.util.Assert;

import java.util.ArrayList;
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

    public RuleResult getPreviousResult() {
        int size = size();
        return size > 0 ? results.get(size - 1) : null;
    }

    public RuleResult getRuleResult(String ruleName) {
        return getRuleResult(ruleName, 0);
    }

    public RuleResult getRuleResult(String ruleName, int startIndex) {
        Assert.notNull(ruleName, "ruleName cannot be null.");
        int size = size();
        Assert.isTrue(startIndex < size, "Invalid startIndex. Size [" + size
                + " Given start Index [" + startIndex + "]");


        RuleResult result = null;

        for (int i = startIndex; i < size; i++) {
            if (ruleName.equals(results.get(i).getRuleName())) {
                result = results.get(i);
                break;
            }
        }

        return result;
    }

    public String[] getPassedRuleNames() {
        return getRuleNames(RuleExecutionStatus.PASS);
    }

    public String[] getPassedOrSkippedRuleNames() {
        return getRuleNames(RuleExecutionStatus.PASS, RuleExecutionStatus.SKIPPED);
    }

    public String[] getFailedRuleNames() {
        return getRuleNames(RuleExecutionStatus.FAIL);
    }

    public String[] getFailedOrSkippedRuleNames() {
        return getRuleNames(RuleExecutionStatus.FAIL, RuleExecutionStatus.SKIPPED);
    }

    public String[] getSkippedRuleNames() {
        return getRuleNames(RuleExecutionStatus.SKIPPED);
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

    public boolean isTrue(RuleExecutionStatus...statuses) {
        Set<RuleExecutionStatus> values = statuses != null ? new HashSet<>(Arrays.asList(statuses)) : new HashSet<>();
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

    public int getCount(RuleExecutionStatus...statuses) {
        Set<RuleExecutionStatus> values = statuses != null ? new HashSet<>(Arrays.asList(statuses)) : new HashSet<>();
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

    public String[] getRuleNames(RuleExecutionStatus...statuses) {
        Set<RuleExecutionStatus> values = statuses != null ? new HashSet<>(Arrays.asList(statuses)) : new HashSet<>();
        return getRuleNames(r -> values.contains(r));
    }

    public String[] getRuleNames(Predicate<RuleResult> predicate) {
        List<String> result = new ArrayList<>();

        for (RuleResult ruleResult : results) {
            if (predicate.test(ruleResult)) {
                result.add(ruleResult.getRuleName());
            }
        }

        return result.toArray(new String[result.size()]);
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
