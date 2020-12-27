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
import org.algorithmx.rules.core.model.MethodDefinition;
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
    private final Condition errorCondition;

    public DefaultRuleSet(String name, String description,
                          Condition preCondition, Action preAction, Action postAction,
                          Condition stopCondition, Condition errorCondition,
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
        this.errorCondition = errorCondition;
    }

    @Override
    public RuleResultSet run(RuleContext ctx) throws UnrulyException {
        Assert.notNull(ctx, "ctx cannot be null");

        // RuleSet Start Event
        ctx.fireListeners(createEvent(EventType.RULE_SET_START, null, null,null, null));

        RuleResultSet result = new RuleResultSet();

        // Run the PreCondition if there is one.
        boolean preConditionCheck = processCondition(ctx, getPreCondition(), EventType.RULE_SET_PRE_CONDITION,
                "Unexpected error occurred trying to execute Pre-Condition on RuleSet.");
        result.setPreConditionCheck(preConditionCheck);

        // RuleSet did not pass the precondition; Do not execute the rules.
        if (!preConditionCheck) return result;

        try {
            // Create a new Scope for the RuleSet to use
            createRuleSetScope(ctx, result);
            // Run the PreAction if there is one.
            processAction(ctx, getPreAction(), EventType.RULE_SET_PRE_ACTION,
                    "Unexpected error occurred trying to execute Pre-Action on RuleSet.");

            int index = 0;
            // Execute the rules in order; STOP if the stopCondition is met.
            for (Rule rule : getRules()) {
                try {
                    // Run the rule
                    RuleResult ruleResult = rule.run(ctx);
                    result.add(ruleResult);
                } catch (Exception e) {
                    ctx.fireListeners(createEvent(EventType.RULE_SET_ERROR, e, null, null, null));
                    boolean proceed = processError(ctx, rule.getRuleDefinition(), index, e);

                    if (!proceed) {
                        throw new UnrulyException("Unexpected error occurred trying to execute Rule"
                                +" Name [" + rule.getRuleDefinition().getName() + "] at Index [" + index + "] on RuleSet."
                                + System.lineSeparator()
                                + RuleUtils.getRuleSetDescription(this, RuleUtils.TAB), e);
                    }
                    result.add(new RuleResult(rule.getName(), RuleExecutionStatus.ERROR));
                } finally {
                    index++;
                }

                // Check to see if we need to stop the execution?
                if (getStopCondition() != null && processCondition(ctx, getStopCondition(), EventType.RULE_SET_STOP_CONDITION,
                        "Unexpected error occurred trying to execute StopCondition on RuleSet.")) {
                    break;
                }
            }
        } finally {

            try {
                // Run the PostAction if there is one.
                processAction(ctx, getPostAction(), EventType.RULE_SET_POST_ACTION,
                        "Unexpected error occurred trying to execute Post-Action on RuleSet.");
            } finally {
                removeRuleSetScope(ctx);
            }

            // RuleSet Start Event
            ctx.fireListeners(createEvent(EventType.RULE_SET_END, result, null, null, null));
        }

        return result;
    }

    protected boolean processCondition(RuleContext ctx, Condition condition, EventType eventType, String errorMessage) {
        // Check Condition if there is one
        if (condition == null) return true;

        ParameterMatch[] matches = null;
        Object[] values = null;
        ExecutionEvent<RuleSetExecution> event = null;

        try {
            matches = ctx.match(condition.getMethodDefinition());
            values = ctx.resolve(matches, condition.getMethodDefinition());
            boolean result = condition.isPass(values);
            event = createEvent(eventType, result, condition.getMethodDefinition(), matches, values);
            return result;
        } catch (Exception e) {
            Throwable cause = e instanceof UnrulyException && e.getCause() != null ? e.getCause() : e;
            event = createEvent(eventType, e, condition.getMethodDefinition(), matches, values);
            throw new UnrulyException(errorMessage
                    + System.lineSeparator()
                    + RuleUtils.getRuleSetDescription(this, RuleUtils.TAB)
                    + RuleUtils.getMethodDescription(condition.getMethodDefinition(), matches, values, RuleUtils.TAB), cause);

        } finally {
            if (event != null) ctx.fireListeners(event);
        }
    }

    protected void processAction(RuleContext ctx, Action action, EventType eventType, String errorMessage) {
        if (action == null) return;

        ParameterMatch[] matches = null;
        Object[] values = null;
        ExecutionEvent<RuleSetExecution> event = null;

        try {
            matches = ctx.match(action.getMethodDefinition());
            values = ctx.resolve(matches, action.getMethodDefinition());
            action.run(values);
            event = createEvent(eventType, null, action.getMethodDefinition(), matches, values);
        } catch (Exception e) {
            Throwable cause = e instanceof UnrulyException && e.getCause() != null ? e.getCause() : e;
            event = createEvent(eventType, e, action.getMethodDefinition(), matches, values);
            throw new UnrulyException(errorMessage
                    + System.lineSeparator()
                    + RuleUtils.getRuleSetDescription(this, RuleUtils.TAB)
                    + RuleUtils.getMethodDescription(action.getMethodDefinition(), matches, values, RuleUtils.TAB), cause);
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

    protected boolean processError(RuleContext ctx, RuleDefinition ruleDefinition, int index, Exception ex) throws UnrulyException{
        if (errorCondition == null) return false;

        boolean proceed;

        try {
            setupErrorScope(ctx, ex, "ex");
            proceed = processCondition(ctx, getErrorCondition(), EventType.RULE_SET_ERROR_CONDITION,
                    "Unexpected error occurred trying to execute errorCondition on RuleSet.");
        } finally {
            removeErrorScope(ctx);
        }

        return proceed;
    }

    protected void setupErrorScope(RuleContext ctx, Exception ex, String bindingName) {
        Bindings errorScope = ctx.getBindings().addScope();
        errorScope.bind(bindingName, ex);
    }

    protected void removeErrorScope(RuleContext ctx) {
        ctx.getBindings().removeScope();
    }

    protected ExecutionEvent<RuleSetExecution> createEvent(EventType eventType, Object result, MethodDefinition methodDefinition,
                                                        ParameterMatch[] parameterMatches, Object[] values) {
        RuleSetExecution ruleSetExecution = new RuleSetExecution(result, this, methodDefinition, parameterMatches, values);
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
    public Condition getErrorCondition() {
        return errorCondition;
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
