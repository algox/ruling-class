/**
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 1999-2019, Live Software & Consultants Inc (rules@algorithmx.org)
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
package org.algorithmx.rules.core.ruleset;

import org.algorithmx.rules.core.action.Action;
import org.algorithmx.rules.core.condition.Condition;
import org.algorithmx.rules.core.rule.Rule;
import org.algorithmx.rules.core.rule.RuleContext;
import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.lib.spring.util.Assert;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Default implementation of the RuleSet.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class DefaultRuleSet implements RuleSet {

    private final String name;
    private final String description;
    private final RuleSet.ORDER order;
    private final Rule[] rules;

    private final Condition preCondition;
    private final Action preAction;
    private final Action postAction;
    private final Condition stopCondition;

    public DefaultRuleSet(String name, String description, ORDER order, Rule[] rules,
                          Condition preCondition, Action preAction, Action postAction,
                          Condition stopCondition) {
        super();
        Assert.notNull(name, "name cannot be null");
        Assert.notNull(description, "description cannot be null");
        Assert.isTrue(rules != null && rules.length > 0, "RuleSet must have at least one Rule.");
        this.name = name;
        this.description = description;
        this.order = order;
        this.rules = rules;
        this.preCondition = preCondition;
        this.preAction = preAction;
        this.postAction = postAction;
        this.stopCondition = stopCondition;
    }

    @Override
    public void run(RuleContext ctx) throws UnrulyException {

        // Check to make sure we are still running
        if (!ctx.getState().isRunning()) {
            throw new UnrulyException("Error trying to run RuleSet [" + getName()
                    + "]. Invalid execution state [" + ctx.getState() + "]. RuleContext is not a running state. " +
                    "Try running withe a new RuleContext.");
        }

        // Run the PreCondition if there is one.
        if (getPreCondition() != null && !getPreCondition().isPass(ctx)) {
            return;
        }

        // Run the PreAction if there is one.
        if (getPreAction() != null) {
            getPreAction().run(ctx);
        }

        for (Rule rule : getRules()) {
            rule.run(ctx);
            // Check to see if we need to stop?
            if (getStopCondition() != null && getStopCondition().isPass(ctx)) break;
        }

        // Run the PostAction if there is one.
        if (getPostAction() != null) {
            getPostAction().run(ctx);
        }

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public ORDER getOrder() {
        return order;
    }

    @Override
    public Condition getPreCondition() {
        return preCondition;
    }

    @Override
    public Action getPreAction() {
        return preAction;
    }

    @Override
    public Action getPostAction() {
        return postAction;
    }

    @Override
    public Condition getStopCondition() {
        return stopCondition;
    }

    @Override
    public int size() {
        return rules.length;
    }

    @Override
    public Rule[] getRules() {
        return rules;
    }

    @Override
    public Iterator<Rule> iterator() {
        return Arrays.stream(rules).iterator();
    }
}
