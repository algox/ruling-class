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
import org.algorithmx.rules.core.rule.RuleDefinition;
import org.algorithmx.rules.lib.spring.util.Assert;
import org.algorithmx.rules.util.RuleUtils;

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

        try {
            // Run the PreCondition if there is one.
            processPreCondition(ctx);
            // Create a new Scope for the RuleSet to use
            createRuleSetScope(ctx);
            // Run the PreAction if there is one.
            processPreAction(ctx);

            int index = 0;
            // Execute the rules in order; STOP if the stopCondition is met.
            for (Rule rule : getRules()) {

                try {
                    // Run the rule
                    rule.run(ctx);
                } catch (Exception e) {
                    processError(ctx, rule.getRuleDefinition(), index, e);
                } finally {
                    index++;
                }

                // Check to see if we need to stop the execution?
                if (processStopCondition(ctx)) break;
            }
        } finally {
            try {
                // Run the PostAction if there is one.
                processPostAction(ctx);
            } finally {
                removeRuleSetScope(ctx);
            }
        }

    }

    protected void processPreCondition(RuleContext ctx) {
        try {
            // Run the PreCondition if there is one.
            if (getPreCondition() != null && !getPreCondition().isPass(ctx)) {
                return;
            }
        } catch (Exception e) {
            throw new UnrulyException("Unexpected error occurred trying to execute Pre-Condition on RuleSet."
                    + System.lineSeparator()
                    + RuleUtils.getRuleSetDescription(this, RuleUtils.TAB), e);
        }
    }

    protected void processPreAction(RuleContext ctx) {
        try {
            // Run the PreAction if there is one.
            if (getPreAction() != null) {
                getPreAction().run(ctx);
            }
        } catch (Exception e) {
            throw new UnrulyException("Unexpected error occurred trying to execute Pre-Action on RuleSet."
                    + System.lineSeparator()
                    + RuleUtils.getRuleSetDescription(this, RuleUtils.TAB), e);
        }
    }

    protected boolean processStopCondition(RuleContext ctx) {
        boolean result;

        try {
            // Check to see if we need to stop?
            result = getStopCondition() != null
                    ? getStopCondition().isPass(ctx)
                    : false;
        } catch (Exception e) {
            throw new UnrulyException("Unexpected error occurred trying to execute StopCondition on RuleSet."
                    + System.lineSeparator()
                    + RuleUtils.getRuleSetDescription(this, RuleUtils.TAB), e);
        }

        return result;
    }

    protected void processPostAction(RuleContext ctx) {
        try {
            // Run the PostAction if there is one.
            if (getPostAction() != null) {
                getPostAction().run(ctx);
            }
        } catch (Exception e) {
            throw new UnrulyException("Unexpected error occurred trying to execute Post-Action on RuleSet."
                    + System.lineSeparator()
                    + RuleUtils.getRuleSetDescription(this, RuleUtils.TAB), e);
        }
    }

    protected void createRuleSetScope(RuleContext ctx) {
        ctx.getBindings().addScope();
    }

    protected void removeRuleSetScope(RuleContext ctx) {
        ctx.getBindings().removeScope();
    }

    protected void processError(RuleContext ctx, RuleDefinition ruleDefinition, int index, Exception ex) throws UnrulyException{
        if (errorHandler == null) return;

        boolean proceed = false;

        try {
            Bindings errorScope = ctx.getBindings().addScope();
            errorScope.bind("ex", ex);
            proceed = errorHandler.isPass(ctx);

            // Do not continue; Throw the exception
            if (!proceed) {
                throw new UnrulyException("Unexpected error occurred trying to execute Rule ["
                        + "] Name [" + ruleDefinition.getName() + "] at Index [" + index + "] on RuleSet."
                        + System.lineSeparator()
                        + RuleUtils.getRuleSetDescription(this, RuleUtils.TAB), ex);
            }

        } catch (Exception e) {
            throw new UnrulyException("Unexpected error occurred trying to execute errorHandler on RuleSet."
                    + System.lineSeparator()
                    + RuleUtils.getRuleSetDescription(this, RuleUtils.TAB), e);
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
