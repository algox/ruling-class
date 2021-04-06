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

package org.algorithmx.rulii.validation.registry;

import org.algorithmx.rulii.core.Runnable;
import org.algorithmx.rulii.core.rule.Rule;
import org.algorithmx.rulii.core.ruleset.RuleSet;
import org.algorithmx.rulii.lib.spring.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultRuleRegistry implements RuleRegistry {

    private final Map<String, Runnable> registry = new ConcurrentHashMap<>();

    public DefaultRuleRegistry() {
        super();
    }

    @Override
    public boolean isNameInUse(String name) {
        Assert.notNull(name, "name cannot be null.");
        return registry.containsKey(name);
    }

    @Override
    public Rule[] getRules() {
        List<Rule> result = new ArrayList<>();

        registry.values()
                .stream()
                .filter(r -> r instanceof Rule)
                .forEach(r -> result.add((Rule) r));

        return result.toArray(new Rule[result.size()]);
    }

    @Override
    public RuleSet[] getRuleSets() {
        List<RuleSet> result = new ArrayList<>();

        registry.values()
                .stream()
                .filter(r -> r instanceof RuleSet)
                .forEach(r -> result.add((RuleSet) r));

        return result.toArray(new RuleSet[result.size()]);
    }


    @Override
    public Runnable get(String name) {
        Assert.notNull(name, "name cannot be null.");
        return registry.get(name);
    }

    @Override
    public int getCount() {
        return registry.size();
    }

    @Override
    public void register(Rule rule) {
        Assert.notNull(rule, "rule cannot be null.");

        if (isNameInUse(rule.getName())) {
            throw new AlreadyRegisteredException(rule.getName(), rule);
        }

        registry.putIfAbsent(rule.getName(), rule);
    }

    @Override
    public void register(RuleSet rules) {
        Assert.notNull(rules, "rules cannot be null.");

        if (isNameInUse(rules.getName())) {
            throw new AlreadyRegisteredException(rules.getName(), rules);
        }

        registry.putIfAbsent(rules.getName(), rules);
    }
}

