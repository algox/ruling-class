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
import org.algorithmx.rules.bind.match.ParameterMatch;
import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.core.action.Action;
import org.algorithmx.rules.core.condition.Condition;
import org.algorithmx.rules.core.rule.Rule;
import org.algorithmx.rules.core.rule.RuleContext;
import org.algorithmx.rules.core.rule.RuleDefinition;
import org.algorithmx.rules.core.rule.RuleExecutionStatus;
import org.algorithmx.rules.core.rule.RuleResult;
import org.algorithmx.rules.event.EventType;
import org.algorithmx.rules.event.ExecutionEvent;
import org.algorithmx.rules.event.RuleSetExecution;
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
    public RuleResultSet run(RuleContext ctx) throws UnrulyException {
        RuleResultSet result = new RuleResultSet();

        // Run the PreCondition if there is one.
        boolean preConditionCheck = processPreCondition(ctx);
        result.setPreConditionCheck(preConditionCheck);

        // RuleSet did not pass the precondition; Do not execute the rules.
        if (!preConditionCheck) return result;

        try {
            // Create a new Scope for the RuleSet to use
            createRuleSetScope(ctx, result);
            // Run the PreAction if there is one.
            processPreAction(ctx);

            int index = 0;
            // Execute the rules in order; STOP if the stopCondition is met.
            for (Rule rule : getRules()) {
                try {
                    // Run the rule
                    RuleResult ruleResult = rule.run(ctx);
                    result.add(ruleResult);
                } catch (Exception e) {
                    processError(ctx, rule.getRuleDefinition(), index, e);
                    result.add(new RuleResult(rule.getRuleDefinition().getName(), RuleExecutionStatus.ERROR));
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

        return result;
    }

    protected boolean processPreCondition(RuleContext ctx) {
        if (getPreCondition() == null) return true;

        ParameterMatch[] matches = null;
        Object[] values = null;
        ExecutionEvent<RuleSetExecution> event = null;

        try {
            matches = ctx.match(getPreCondition().getMethodDefinition());
            values = ctx.resolve(matches, getPreCondition().getMethodDefinition());
            // Run the PreCondition if there is one.
            boolean result = getPreCondition().isPass(values);
            event = createEvent(EventType.RULE_SET_PRE_CONDITION, result, matches, values);
            return result;
        } catch (Exception e) {
            Throwable cause = e instanceof UnrulyException && e.getCause() != null ? e.getCause() : e;
            event = createEvent(EventType.RULE_SET_PRE_CONDITION, e, matches, values);
            throw new UnrulyException("Unexpected error occurred trying to execute Pre-Condition on RuleSet."
                    + System.lineSeparator()
                    + RuleUtils.getRuleSetDescription(this, RuleUtils.TAB)
                    + RuleUtils.getMethodDescription(getPreCondition().getMethodDefinition(), matches, values, RuleUtils.TAB), cause);
        } finally {
            if (event != null) ctx.fireListeners(event);
        }
    }

    protected void processPreAction(RuleContext ctx) {
        if (getPreAction() == null) return;

        ParameterMatch[] matches = null;
        Object[] values = null;
        ExecutionEvent<RuleSetExecution> event = null;

        try {
            matches = ctx.match(getPreAction().getMethodDefinition());
            values = ctx.resolve(matches, getPreAction().getMethodDefinition());
            // Run the PreAction if there is one.
            getPreAction().run(values);
            event = createEvent(EventType.RULE_SET_PRE_ACTION, null, matches, values);
        } catch (Exception e) {
            Throwable cause = e instanceof UnrulyException && e.getCause() != null ? e.getCause() : e;
            event = createEvent(EventType.RULE_SET_PRE_ACTION, e, matches, values);
            throw new UnrulyException("Unexpected error occurred trying to execute Pre-Action on RuleSet."
                    + System.lineSeparator()
                    + RuleUtils.getRuleSetDescription(this, RuleUtils.TAB)
                    + RuleUtils.getMethodDescription(getPreAction().getMethodDefinition(), matches, values, RuleUtils.TAB), cause);
        } finally {
            if (event != null) ctx.fireListeners(event);
        }
    }

    protected boolean processStopCondition(RuleContext ctx) {
        if (getStopCondition() == null) return false;

        ParameterMatch[] matches = null;
        Object[] values = null;
        ExecutionEvent<RuleSetExecution> event = null;
        boolean result;

        try {
            matches = ctx.match(getStopCondition().getMethodDefinition());
            values = ctx.resolve(matches, getStopCondition().getMethodDefinition());
            // Check to see if we need to stop?
            result = getStopCondition().isPass(values);
            event = createEvent(EventType.RULE_SET_STOP_CONDITION, result, matches, values);
            return result;
        } catch (Exception e) {
            Throwable cause = e instanceof UnrulyException && e.getCause() != null ? e.getCause() : e;
            event = createEvent(EventType.RULE_SET_STOP_CONDITION, e, matches, values);
            throw new UnrulyException("Unexpected error occurred trying to execute StopCondition on RuleSet."
                    + System.lineSeparator()
                    + RuleUtils.getRuleSetDescription(this, RuleUtils.TAB)
                    + RuleUtils.getMethodDescription(getStopCondition().getMethodDefinition(), matches, values, RuleUtils.TAB), cause);
        } finally {
            if (event != null) ctx.fireListeners(event);
        }
    }

    protected void processPostAction(RuleContext ctx) {
        if (getPostAction() == null) return;

        ParameterMatch[] matches = null;
        Object[] values = null;
        ExecutionEvent<RuleSetExecution> event = null;

        try {
            matches = ctx.match(getPostAction().getMethodDefinition());
            values = ctx.resolve(matches, getPostAction().getMethodDefinition());
            // Run the PostAction if there is one.
            getPostAction().run(values);
            event = createEvent(EventType.RULE_SET_POST_ACTION, null, matches, values);
        } catch (Exception e) {
            Throwable cause = e instanceof UnrulyException && e.getCause() != null ? e.getCause() : e;
            event = createEvent(EventType.RULE_SET_POST_ACTION, e, matches, values);
            throw new UnrulyException("Unexpected error occurred trying to execute Post-Action on RuleSet."
                    + System.lineSeparator()
                    + RuleUtils.getRuleSetDescription(this, RuleUtils.TAB)
                    + RuleUtils.getMethodDescription(getPostAction().getMethodDefinition(), matches, values, RuleUtils.TAB), cause);
        } finally {
            if (event != null) ctx.fireListeners(event);
        }
    }

    protected void createRuleSetScope(RuleContext ctx, RuleResultSet ruleResultSet) {
        ctx.getBindings().addScope();
        ctx.getBindings().bind("ruleResultSet", RuleResultSet.class, ruleResultSet);
    }

    protected void removeRuleSetScope(RuleContext ctx) {
        ctx.getBindings().removeScope();
    }

    protected void processError(RuleContext ctx, RuleDefinition ruleDefinition, int index, Exception ex) throws UnrulyException{
        if (errorHandler == null) return;

        ParameterMatch[] matches = null;
        Object[] values = null;
        ExecutionEvent<RuleSetExecution> event = null;
        boolean proceed;

        try {
            Bindings errorScope = ctx.getBindings().addScope();
            errorScope.bind("ex", ex);
            matches = ctx.match(getErrorHandler().getMethodDefinition());
            values = ctx.resolve(matches, getErrorHandler().getMethodDefinition());
            proceed = getErrorHandler().isPass(values);
        } catch (Exception e) {
            Throwable cause = e instanceof UnrulyException && e.getCause() != null ? e.getCause() : e;
            event = createEvent(EventType.RULE_SET_ERROR, e, matches, values);
            throw new UnrulyException("Unexpected error occurred trying to execute errorHandler on RuleSet."
                    + System.lineSeparator()
                    + RuleUtils.getRuleSetDescription(this, RuleUtils.TAB)
                    + RuleUtils.getMethodDescription(getErrorHandler().getMethodDefinition(), matches, values, RuleUtils.TAB), cause);
        } finally {
            ctx.getBindings().removeScope();
            if (event != null) ctx.fireListeners(event);
        }

        // Do not continue; Throw the exception
        if (!proceed) {
            throw new UnrulyException("Unexpected error occurred trying to execute Rule ["
                    + "] Name [" + ruleDefinition.getName() + "] at Index [" + index + "] on RuleSet."
                    + System.lineSeparator()
                    + RuleUtils.getRuleSetDescription(this, RuleUtils.TAB), ex);
        }
    }

    protected ExecutionEvent<RuleSetExecution> createEvent(EventType eventType, Object result,
                                                        ParameterMatch[] parameterMatches, Object[] values) {
        RuleSetExecution ruleSetExecution = new RuleSetExecution(result, this, parameterMatches, values);
        return new ExecutionEvent<>(eventType, ruleSetExecution);
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
