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

package org.algorithmx.rulii.core.registry;

import org.algorithmx.rulii.core.Runnable;
import org.algorithmx.rulii.core.rule.Rule;
import org.algorithmx.rulii.core.ruleset.RuleSet;
import org.algorithmx.rulii.lib.spring.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class DefaultRuleRegistry implements RuleRegistry {

    private final Map<String, Runnable> registry = new HashMap<>();

    public DefaultRuleRegistry() {
        super();
    }

    @Override
    public boolean isNameInUse(String name) {
        Assert.notNull(name, "name cannot be null.");
        return registry.containsKey(name);
    }

    @Override
    public int getRuleCount() {
        return getRules().length;
    }

    @Override
    public int getRuleSetCount() {
        return getRuleSets().length;
    }

    @Override
    public Rule[] getRules() {
        return getRules(false);
    }

    @Override
    public Rule[] getRules(boolean ordered) {
        List<Rule> result = new ArrayList<>();

        for (Runnable runnable : registry.values()) {
            if (runnable instanceof Rule) result.add((Rule) runnable);
        }

        // Sort the Rules (if req)
        if (ordered) Collections.sort(result);
        return result.toArray(new Rule[result.size()]);
    }

    @Override
    public Rule[] getRules(Predicate<Rule> filter, boolean ordered) {
        return Arrays.stream(getRules(ordered)).filter(filter).toArray(size -> new Rule[size]);
    }

    @Override
    public RuleSet[] getRuleSets() {
        return getRuleSets(false);
    }

    @Override
    public RuleSet[] getRuleSets(boolean ordered) {
        List<RuleSet> result = new ArrayList<>();

        for (Runnable runnable : registry.values()) {
            if (runnable instanceof RuleSet) result.add((RuleSet) runnable);
        }

        // Sort the Rules (if req)
        if (ordered) Collections.sort(result);
        return result.toArray(new RuleSet[result.size()]);
    }

    @Override
    public Runnable get(String name) {
        Assert.notNull(name, "name cannot be null.");
        return registry.get(name);
    }

    @Override
    public <T> Rule<T> getRule(String name) {
        Assert.notNull(name, "name cannot be null.");
        return (Rule<T>) registry.get(name);
    }

    @Override
    public RuleSet getRuleSet(String name) {
        Assert.notNull(name, "name cannot be null.");
        return (RuleSet) registry.get(name);
    }

    @Override
    public void register(Rule rule) {
        Assert.notNull(rule, "rule cannot be null.");

        if (isNameInUse(rule.getName())) {
            throw new AlreadyRegisteredException(rule.getName(), rule);
        }

        registry.put(rule.getName(), rule);
    }

    @Override
    public void register(RuleSet rules) {
        Assert.notNull(rules, "rules cannot be null.");

        if (isNameInUse(rules.getName())) {
            throw new AlreadyRegisteredException(rules.getName(), rules);
        }

        registry.put(rules.getName(), rules);
    }
}
