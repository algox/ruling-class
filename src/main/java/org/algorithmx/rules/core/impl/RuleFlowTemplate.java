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

import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.RuleContext;
import org.algorithmx.rules.core.RuleFlow;
import org.algorithmx.rules.spring.util.Assert;

/**
 * Work in progress.
 *
 * @param <T> Result Type
 * @author Max Arulananthan
 * @since 1.0
 */
public abstract class RuleFlowTemplate<T> implements RuleFlow<T> {

    private final RuleContext ctx;

    public RuleFlowTemplate(RuleContext ctx) {
        super();
        Assert.notNull(ctx, "ctx cannot be null.");
        this.ctx = ctx;
    }

    @Override
    public final Bindings bindings() {
        return ctx.bindings();
    }

    /*@Override
    public final boolean rule(Rule rule) {
        Assert.notNull(rule, "rule cannot be null.");
        return rule.isPass(ctx);
    }

    @Override
    public final boolean and(Rule[] rules) {
        Assert.notNull(rules, "rules cannot be null.");
        CompositeRule rule = RuleFactory.and(rules);
        return rule(rule);
    }

    @Override
    public final boolean or(Rule[] rules) {
        Assert.notNull(rules, "rules cannot be null.");
        CompositeRule rule = RuleFactory.or(rules);
        return rule(rule);
    }

    @Override
    public final boolean none(Rule[] rules) {
        Assert.notNull(rules, "rules cannot be null.");
        CompositeRule rule = RuleFactory.none(rules);
        return rule(rule);
    }

    @Override
    public final boolean and(RuleSet ruleSet) {
        return and(ruleSet.getRules());
    }

    @Override
    public final boolean or(RuleSet ruleSet) {
        return or(ruleSet.getRules());
    }

    @Override
    public final boolean none(RuleSet ruleSet) {
        return none(ruleSet.getRules());
    }*/
}
