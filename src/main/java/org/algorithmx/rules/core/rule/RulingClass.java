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
public class RulingClass implements Rule, Identifiable {

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

        // Check the Pre-Condition
        boolean preConditionCheck = processPreCondition(ctx);
        // We did not pass the Pre-Condition
        if (!preConditionCheck) return new RuleResult(getName(), RuleExecutionStatus.SKIPPED);

        boolean result = processGivenCondition(ctx);

        // The Condition passed
        if (result) {
            // Execute associated Actions.
            processActions(ctx);
        } else {
            // Execute otherwise Action.
            processOtherwiseAction(ctx);
        }

        return new RuleResult(getName(), result ? RuleExecutionStatus.PASS : RuleExecutionStatus.FAIL);
    }

    protected boolean processPreCondition(RuleContext ctx) {
        // Check Pre-Condition if there is one
        if (getPreCondition() == null) return true;

        ParameterMatch[] matches = null;
        Object[] values = null;
        ExecutionEvent<RuleExecution> event = null;

        try {
            matches = ctx.match(getPreCondition().getMethodDefinition());
            values = ctx.resolve(matches, getPreCondition().getMethodDefinition());
            boolean result = getPreCondition().isPass(values);
            event = createEvent(EventType.RULE_PRE_CONDITION, result, matches, values);
            return result;
        } catch (Exception e) {
            Throwable cause = e instanceof UnrulyException && e.getCause() != null ? e.getCause() : e;
            event = createEvent(EventType.RULE_PRE_CONDITION, e, matches, values);
            throw new UnrulyException("Unexpected error occurred trying to execute Pre-Condition on Rule."
                    + System.lineSeparator()
                    + RuleUtils.getRuleDescription(getRuleDefinition(), getPreCondition().getMethodDefinition(), RuleUtils.TAB)
                    + RuleUtils.getMethodDescription(getPreCondition().getMethodDefinition(), matches, values, RuleUtils.TAB), cause);

        } finally {
            if (event != null) ctx.fireListeners(event);
        }
    }

    protected boolean processGivenCondition(RuleContext ctx) {
        if (getCondition() == null) return true;

        ParameterMatch[] matches = null;
        Object[] values = null;
        ExecutionEvent<RuleExecution> event = null;

        // Check the Given Condition
        try {
            matches = ctx.match(getCondition().getMethodDefinition());
            values = ctx.resolve(matches, getCondition().getMethodDefinition());
            boolean result = getCondition().isPass(values);
            event = createEvent(EventType.RULE_GIVEN_CONDITION, result, matches, values);
            return result;
        } catch (Exception e) {
            Throwable cause = e instanceof UnrulyException && e.getCause() != null ? e.getCause() : e;
            event = createEvent(EventType.RULE_GIVEN_CONDITION, e, matches, values);
            throw new UnrulyException("Unexpected error occurred trying to execute Given Condition on Rule."
                    + System.lineSeparator()
                    + RuleUtils.getRuleDescription(getRuleDefinition(), getCondition().getMethodDefinition(), RuleUtils.TAB)
                    + RuleUtils.getMethodDescription(getCondition().getMethodDefinition(), matches, values, RuleUtils.TAB), cause);
        } finally {
            if (event != null) ctx.fireListeners(event);
        }
    }

    protected void processActions(RuleContext ctx) {
        if (getActions() == null) return;

        ParameterMatch[] matches = null;
        Object[] values = null;
        ExecutionEvent<RuleExecution> event = null;

        // Execute associated Actions.
        for (Action action : getActions()) {
            try {
                matches = ctx.match(action.getMethodDefinition());
                values = ctx.resolve(matches, action.getMethodDefinition());
                action.run(values);
                event = createEvent(EventType.RULE_ACTION, null, matches, values);
            } catch (Exception e) {
                Throwable cause = e instanceof UnrulyException && e.getCause() != null ? e.getCause() : e;
                event = createEvent(EventType.RULE_GIVEN_CONDITION, e, matches, values);
                throw new UnrulyException("Unexpected error occurred trying to execute Action on Rule."
                        + System.lineSeparator()
                        + RuleUtils.getRuleDescription(getRuleDefinition(), action.getMethodDefinition(), RuleUtils.TAB)
                        + RuleUtils.getMethodDescription(action.getMethodDefinition(), matches, values, RuleUtils.TAB), cause);
            } finally {
                if (event != null) ctx.fireListeners(event);
            }
        }
    }

    protected void processOtherwiseAction(RuleContext ctx) {
        if (getOtherwiseAction() == null) return;

        ParameterMatch[] matches = null;
        Object[] values = null;
        ExecutionEvent<RuleExecution> event = null;

        try {
            matches = ctx.match(getOtherwiseAction().getMethodDefinition());
            values = ctx.resolve(matches, getOtherwiseAction().getMethodDefinition());
            getOtherwiseAction().run(values);
            event = createEvent(EventType.RULE_OTHERWISE_ACTION, null, matches, values);
        } catch (Exception e) {
            Throwable cause = e instanceof UnrulyException && e.getCause() != null ? e.getCause() : e;
            event = createEvent(EventType.RULE_OTHERWISE_ACTION, e, matches, values);
            throw new UnrulyException("Unexpected error occurred trying to execute Otherwise Action on Rule."
                    + System.lineSeparator()
                    + RuleUtils.getRuleDescription(getRuleDefinition(), getOtherwiseAction().getMethodDefinition(), RuleUtils.TAB)
                    + RuleUtils.getMethodDescription(getOtherwiseAction().getMethodDefinition(), matches, values, RuleUtils.TAB), cause);
        } finally {
            if (event != null) ctx.fireListeners(event);
        }
    }

    protected ExecutionEvent<RuleExecution> createEvent(EventType eventType, Object result,
                                                        ParameterMatch[] parameterMatches, Object[] values) {
        RuleExecution ruleExecution = new RuleExecution(result, this, parameterMatches, values);
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
