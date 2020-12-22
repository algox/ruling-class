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

import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.core.action.Action;
import org.algorithmx.rules.core.condition.Condition;
import org.algorithmx.rules.core.rule.Rule;
import org.algorithmx.rules.core.rule.RuleContext;
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
    private final Rule[] rules;

    private final Condition preCondition;
    private final Action preAction;
    private final Action postAction;
    private final Condition stopCondition;

    private final Condition errorHandler;

    public DefaultRuleSet(String name, String description,
                          Condition preCondition, Action preAction, Action postAction,
                          Condition stopCondition, Condition errorHandler,
                          Rule...rules) {
        super();
        Assert.notNull(name, "name cannot be null");
        Assert.notNull(description, "description cannot be null");
        Assert.isTrue(rules != null && rules.length > 0, "RuleSet must have at least one Rule.");
        this.name = name;
        this.description = description;
        this.rules = rules;
        this.preCondition = preCondition;
        this.preAction = preAction;
        this.postAction = postAction;
        this.stopCondition = stopCondition;
        this.errorHandler = errorHandler;
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

        try {
            // Create a new Scope for the RuleSet to use
            createRuleSetScope(ctx);

            // Run the PreAction if there is one.
            if (getPreAction() != null) {
                getPreAction().run(ctx);
            }

            for (Rule rule : getRules()) {

                try {
                    // Run the rule
                    rule.run(ctx);
                } catch (Exception e) {
                    boolean proceed = processError(ctx, e);

                    // Do not continue; Throw the exception
                    if (!proceed) throw e;
                }

                // Check to see if we need to stop?
                if (getStopCondition() != null && getStopCondition().isPass(ctx)) break;
            }
        } finally {
            try {
                // Run the PostAction if there is one.
                if (getPostAction() != null) {
                    getPostAction().run(ctx);
                }
            } finally {
                ctx.getBindings().removeScope();
            }
        }

    }

    protected void createRuleSetScope(RuleContext ctx) {
        ctx.getBindings().addScope();
    }

    protected boolean processError(RuleContext ctx, Exception e) {
        if (errorHandler == null) return false;

        try {
            Bindings errorScope = ctx.getBindings().addScope();
            errorScope.bind("ex", e);
            return errorHandler.isPass(ctx);
        } finally {
            ctx.getBindings().removeScope();
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
    public Condition getErrorHandler() {
        return errorHandler;
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

    @Override
    public String toString() {
        return "DefaultRuleSet{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", rules=" + Arrays.toString(rules) +
                ", preCondition=" + preCondition +
                ", postAction=" + postAction +
                ", stopCondition=" + stopCondition +
                '}';
    }
}
