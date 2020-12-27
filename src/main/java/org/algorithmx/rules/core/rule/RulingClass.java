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
package org.algorithmx.rules.core.rule;

import org.algorithmx.rules.bind.match.ParameterMatch;
import org.algorithmx.rules.core.Identifiable;
import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.core.action.Action;
import org.algorithmx.rules.core.condition.Condition;
import org.algorithmx.rules.core.model.MethodDefinition;
import org.algorithmx.rules.event.EventType;
import org.algorithmx.rules.event.ExecutionEvent;
import org.algorithmx.rules.event.RuleExecution;
import org.algorithmx.rules.lib.spring.util.Assert;
import org.algorithmx.rules.util.RuleUtils;

import java.util.Collections;
import java.util.List;

/**
 * Default Rule Implementation (implements Identifiable).
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class RulingClass implements Rule {

    private final RuleDefinition ruleDefinition;
    private final Object target;
    private final Condition preCondition;
    private final Condition condition;
    private final List<Action> actions;
    private final Action otherwiseAction;

    /**
     * Rule defined with all the given properties.
     *
     * @param ruleDefinition meta information.
     * @param target target Rule class.
     * @param preCondition pre-condition.
     * @param condition given condition.
     * @param thenActions all the Then actions.
     * @param otherwiseAction the Otherwise action (optional);
     */
    public RulingClass(RuleDefinition ruleDefinition, Object target, Condition preCondition, Condition condition,
                       List<Action> thenActions, Action otherwiseAction) {
        super();
        Assert.notNull(ruleDefinition, "ruleDefinition cannot be null");
        Assert.notNull(condition, "condition cannot be null");
        this.ruleDefinition = ruleDefinition;
        this.target = target;
        this.preCondition = preCondition;
        this.condition = condition;

        // Then actions (optional)
        if (thenActions != null) {
            Collections.sort(thenActions);
            this.actions = Collections.unmodifiableList(thenActions);
        } else {
            this.actions = Collections.emptyList();
        }

        // Otherwise action (Optional)
        this.otherwiseAction = otherwiseAction;
    }

    @Override
    public RuleResult run(RuleContext ctx) throws UnrulyException {
        Assert.notNull(ctx, "ctx cannot be null");

        // Rule Start Event
        ctx.fireListeners(createEvent(EventType.RULE_START, null, null, null, null));

        Boolean result = false;

        try {
            // Check the Pre-Condition
            boolean preConditionCheck = processCondition(ctx, getPreCondition(), EventType.RULE_PRE_CONDITION,
                    "Unexpected error occurred trying to execute Pre-Condition on Rule.");
            // We did not pass the Pre-Condition
            if (!preConditionCheck) return new RuleResult(getName(), RuleExecutionStatus.SKIPPED);

            result = processCondition(ctx, getCondition(), EventType.RULE_CONDITION,
                    "Unexpected error occurred trying to execute Given Condition on Rule.");

            // The Condition passed
            if (result) {
                // Execute associated Actions.
                for (Action action : getActions()) {
                    processAction(ctx, action, EventType.RULE_ACTION,
                            "Unexpected error occurred trying to execute Action on Rule.");
                }
            } else {
                // Execute otherwise Action.
                processAction(ctx, getOtherwiseAction(), EventType.RULE_OTHERWISE_ACTION,
                        "Unexpected error occurred trying to execute Otherwise Action on Rule.");
            }
        } finally {
            // Rule End Event
            ctx.fireListeners(createEvent(EventType.RULE_END, result, null, null, null));
        }

        return new RuleResult(getName(), result ? RuleExecutionStatus.PASS : RuleExecutionStatus.FAIL);
    }

    protected boolean processCondition(RuleContext ctx, Condition condition, EventType eventType, String errorMessage) {
        // Check Condition if there is one
        if (condition == null) return true;

        ParameterMatch[] matches = null;
        Object[] values = null;
        ExecutionEvent<RuleExecution> event = null;

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
                    + RuleUtils.getRuleDescription(getRuleDefinition(), condition.getMethodDefinition(), RuleUtils.TAB)
                    + RuleUtils.getMethodDescription(condition.getMethodDefinition(), matches, values, RuleUtils.TAB), cause);

        } finally {
            if (event != null) ctx.fireListeners(event);
        }
    }

    protected void processAction(RuleContext ctx, Action action, EventType eventType, String errorMessage) {
        if (action == null) return;

        ParameterMatch[] matches = null;
        Object[] values = null;
        ExecutionEvent<RuleExecution> event = null;

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
                    + RuleUtils.getRuleDescription(getRuleDefinition(), getOtherwiseAction().getMethodDefinition(), RuleUtils.TAB)
                    + RuleUtils.getMethodDescription(getOtherwiseAction().getMethodDefinition(), matches, values, RuleUtils.TAB), cause);
        } finally {
            if (event != null) ctx.fireListeners(event);
        }
    }

    protected ExecutionEvent<RuleExecution> createEvent(EventType eventType, Object result, MethodDefinition methodDefinition,
                                                        ParameterMatch[] parameterMatches, Object[] values) {
        RuleExecution ruleExecution = new RuleExecution(result, this, methodDefinition, parameterMatches, values);
        return new ExecutionEvent<>(eventType, ruleExecution);
    }

    @Override
    public Condition getPreCondition() {
        return preCondition;
    }

    @Override
    public Condition getCondition() {
        return condition;
    }

    @Override
    public RuleDefinition getRuleDefinition() {
        return ruleDefinition;
    }

    @Override
    public String getName() {
        if (getTarget() != null && getTarget() instanceof Identifiable) return ((Identifiable) getTarget()).getName();
        return ruleDefinition.getName();
    }

    @Override
    public String getDescription() {
        return ruleDefinition.getDescription();
    }

    @Override
    public Object getTarget() {
        return target;
    }

    @Override
    public Action[] getActions() {
        return actions.toArray(new Action[actions.size()]);
    }

    @Override
    public Action getOtherwiseAction() {
        return otherwiseAction;
    }

    @Override
    public final boolean isIdentifiable() {
        return true;
    }

    @Override
    public String toString() {
        return "RulingClass{" +
                "ruleDefinition=" + ruleDefinition +
                '}';
    }
}
