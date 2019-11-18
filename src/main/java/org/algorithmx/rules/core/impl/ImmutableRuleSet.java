/**
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 2019, algorithmx.org (dev@algorithmx.org)
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
package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.core.Action;
import org.algorithmx.rules.core.Condition;
import org.algorithmx.rules.core.Rule;
import org.algorithmx.rules.core.RuleContext;
import org.algorithmx.rules.core.RuleSet;
import org.algorithmx.rules.error.UnrulyException;
import org.algorithmx.rules.spring.util.Assert;

import java.util.Iterator;

/**
 * Immutable version of a RuleSet. This class is used during the execution of a RuleSet so that its state cannot
 * be modified concurrently (during execution).
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class ImmutableRuleSet implements RuleSet {

    private final RuleSet target;

    public ImmutableRuleSet(RuleSet target) {
        super();
        Assert.notNull(target, "target cannot be null.");
        this.target = target;
    }

    /**
     * Returns the targeted RuleSet.
     *
     * @return real target RuleSet.
     */
    public RuleSet getTarget() {
        return target;
    }

    @Override
    public String getName() {
        return target.getName();
    }

    @Override
    public String getDescription() {
        return target.getDescription();
    }

    @Override
    public void preCondition(Condition condition) {
        throw new UnsupportedOperationException("RuleSet [" + getName() + "] is Immutable.");
    }

    @Override
    public void preAction(Action action) {
        throw new UnsupportedOperationException("RuleSet [" + getName() + "] is Immutable.");
    }

    @Override
    public void postAction(Action action) {
        throw new UnsupportedOperationException("RuleSet [" + getName() + "] is Immutable.");
    }

    @Override
    public void stopWhen(Condition condition) {
        throw new UnsupportedOperationException("RuleSet [" + getName() + "] is Immutable.");
    }

    @Override
    public Rule getRule(String ruleName) {
        return target.getRule(ruleName);
    }

    @Override
    public Rule getRule(Class<?> ruleClass) {
        return target.getRule(ruleClass);
    }

    @Override
    public int size() {
        return target.size();
    }

    @Override
    public Rule[] getRules() {
        return target.getRules();
    }

    @Override
    public RuleSet add(Rule rule) {
        throw new UnsupportedOperationException("RuleSet [" + getName() + "] is Immutable.");
    }

    @Override
    public RuleSet add(Class<?> ruleClass) {
        throw new UnsupportedOperationException("RuleSet [" + getName() + "] is Immutable.");
    }

    @Override
    public RuleSet add(String name, Rule rule) {
        throw new UnsupportedOperationException("RuleSet [" + getName() + "] is Immutable.");
    }

    @Override
    public RuleSet remove(String... ruleNames) {
        throw new UnsupportedOperationException("RuleSet [" + getName() + "] is Immutable.");
    }

    @Override
    public RuleSet remove(Class<?>... ruleClasses) {
        throw new UnsupportedOperationException("RuleSet [" + getName() + "] is Immutable.");
    }

    @Override
    public void run(RuleContext ctx) throws UnrulyException {
        target.run(ctx);
    }

    @Override
    public Iterator<Rule> iterator() {
        return target.iterator();
    }
}
