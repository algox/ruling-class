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
import org.algorithmx.rulii.core.rule.RuleResultExtractor;
import org.algorithmx.rulii.lib.spring.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class RuleSetResult implements Iterable<RuleResult>, RuleResultExtractor {

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

    public void addAll(RuleSetResult ruleSetResult) {
        Assert.notNull(ruleSetResult, "ruleSetResult cannot be null.");
        this.results.addAll(Arrays.asList(ruleSetResult.extract()));
    }

    public void addAll(RuleResult...ruleResults) {
        Assert.notNull(ruleResults, "ruleResults cannot be null.");
        this.results.addAll(Arrays.asList(ruleResults));
    }

    public Bindings getBindings() {
        return bindings;
    }

    public RuleResult getLastResult() {
        int size = size();
        return size > 0 ? results.get(size - 1) : null;
    }

    public RuleResult[] getRuleResult(String ruleName) {
        Assert.notNull(ruleName, "ruleName cannot be null.");
        return getRuleResults(r -> ruleName.equals(r.getRuleName()));
    }

    public RuleResult[] getRuleResult(String ruleName, String parentName) {
        Assert.notNull(ruleName, "ruleName cannot be null.");
        Assert.notNull(parentName, "parentName cannot be null.");
        return getRuleResults(r -> ruleName.equals(r.getRuleName()) && parentName.equals(r.getParentName()));
    }

    public RuleResult[] getPassed() {
        return getRuleResults(RuleExecutionStatus.PASS);
    }

    public RuleResult[] getPassedOrSkipped() {
        return getRuleResults(RuleExecutionStatus.PASS, RuleExecutionStatus.SKIPPED);
    }

    public RuleResult[] getFailed() {
        return getRuleResults(RuleExecutionStatus.FAIL);
    }

    public RuleResult[] getFailedOrSkipped() {
        return getRuleResults(RuleExecutionStatus.FAIL, RuleExecutionStatus.SKIPPED);
    }

    public RuleResult[] getSkipped() {
        return getRuleResults(RuleExecutionStatus.SKIPPED);
    }

    public RuleResult get(int index) {
        return results.get(index);
    }

    public boolean isAllPass() {
        return isTrue(r -> r.getStatus().isPass());
    }

    public boolean isAllPassOrSkip() {
        return isTrue(r -> r.getStatus().isPass() || r.getStatus().isSkipped());
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

    public boolean isAllFail() {
        return isTrue(r -> r.getStatus().isFail());
    }

    public boolean isAnyFail() {
        return isTrue(r -> !r.getStatus().isFail());
    }

    public boolean isAllFailOrSkip() {
        return isTrue(r -> r.getStatus().isFail() || r.getStatus().isSkipped());
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

    public RuleResult[] getRuleResults(RuleExecutionStatus...statuses) {
        Set<RuleExecutionStatus> values = statuses != null ? new HashSet<>(Arrays.asList(statuses)) : new HashSet<>();
        return getRuleResults(r -> values.contains(r));
    }

    public RuleResult[] getRuleResults(Predicate<RuleResult> predicate) {
        List<RuleResult> result = new ArrayList<>();

        for (RuleResult ruleResult : results) {
            if (predicate.test(ruleResult)) {
                result.add(ruleResult);
            }
        }

        return result.toArray(new RuleResult[result.size()]);
    }

    @Override
    public Iterator<RuleResult> iterator() {
        return results.iterator();
    }

    @Override
    public RuleResult[] extract() {
        return results.toArray(new RuleResult[results.size()]);
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
