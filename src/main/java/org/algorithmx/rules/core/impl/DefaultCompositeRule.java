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

import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.core.CompositeRule;
import org.algorithmx.rules.core.Rule;
import org.algorithmx.rules.core.RuleExecutionContext;
import org.algorithmx.rules.model.RuleDefinition;
import org.algorithmx.rules.spring.util.Assert;

import java.util.function.BiPredicate;

@Deprecated
public class DefaultCompositeRule extends RuleTemplate implements CompositeRule {

    private final Rule[] rules;
    private final RuleDefinition ruleDefinition;
    private final BiPredicate<Rule[], RuleExecutionContext> test;

    public DefaultCompositeRule(Rule[] rules, BiPredicate<Rule[], RuleExecutionContext> test) {
        super();
        Assert.notNull(rules, "rules cannot be null");
        Assert.notNull(test, "test cannot be null");
        Assert.isTrue(rules.length > 0, " there must at least one rule");
        this.rules = rules;
        this.test = test;
        this.ruleDefinition = new RuleDefinition(getClass(), "CompositeRule", null, null);
    }

    @Override
    public boolean isPass(Object...args) throws UnrulyException {
        return true;
    }

    /*@Override
    public boolean test(RuleExecutionContext ruleExecutionContext) {
        return false;
    }

    @Override
    public boolean isPass(RuleExecutionContext ctx) throws UnrulyException {
        return test.test(rules, ctx);
    }*/

    @Override
    public final Rule[] getRules() {
        return rules;
    }

    @Override
    public Object getTarget() {
        return this;
    }

    @Override
    public RuleDefinition getRuleDefinition() {
        return ruleDefinition;
    }
}
