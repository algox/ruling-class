/*
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 1999-2021, Algorithmx Inc.
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

package org.algorithmx.rulii.core.rule;

import org.algorithmx.rulii.core.UnrulyException;
import org.algorithmx.rulii.core.action.Action;
import org.algorithmx.rulii.core.condition.Condition;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.event.EventType;
import org.algorithmx.rulii.event.ExecutionEvent;
import org.algorithmx.rulii.event.RuleExecution;
import org.algorithmx.rulii.lib.spring.util.Assert;

import java.util.Collections;
import java.util.List;

/**
 * Default Rule Implementation (implements Identifiable).
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class RulingClass<T> implements Rule<T> {

    private final RuleDefinition ruleDefinition;
    private final Condition preCondition;
    private final Condition condition;
    private final List<Action> actions;
    private final Action otherwiseAction;
    private final T target;

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
    RulingClass(RuleDefinition ruleDefinition, T target, Condition preCondition, Condition condition,
                       List<Action> thenActions, Action otherwiseAction) {
        super();
        Assert.notNull(ruleDefinition, "ruleDefinition cannot be null");
        this.ruleDefinition = ruleDefinition;
        this.target = target;
        this.preCondition = preCondition;
        this.condition = condition;
        // Then actions (optional)
        this.actions = thenActions != null ? Collections.unmodifiableList(thenActions) : Collections.emptyList();
        // Otherwise action (Optional)
        this.otherwiseAction = otherwiseAction;
    }

    @Override
    public RuleResult run(RuleContext context) throws UnrulyException {
        Assert.notNull(context, "context cannot be null");

        if (!context.isActive()) throw new UnrulyException("RuleContext is not Active. Perhaps it was stopped earlier ? "
                + "Create a new RuleContext and try again.");

        // Rule Start Event
        context.getEventProcessor().fireListeners(createEvent(EventType.RULE_START, null));

        Boolean result = false;

        try {
            // Check the Pre-Condition
            boolean preConditionCheck = processCondition(context, getPreCondition(), EventType.RULE_PRE_CONDITION_START,
                    EventType.RULE_PRE_CONDITION_END);
            // We did not pass the Pre-Condition
            if (!preConditionCheck) return new RuleResult(getName(), RuleExecutionStatus.SKIPPED);

            result = processCondition(context, getCondition(), EventType.RULE_CONDITION_START, EventType.RULE_CONDITION_END);

            // The Condition passed
            if (result) {
                // Execute associated Actions.
                for (Action action : getActions()) {
                    processAction(context, action, EventType.RULE_ACTION_START, EventType.RULE_ACTION_END);
                    // Looks like stopExecution was called on the RuleContext
                    if (!context.isActive()) break;
                }
            } else {
                // Execute otherwise Action.
                processAction(context, getOtherwiseAction(), EventType.RULE_OTHERWISE_ACTION_START,
                        EventType.RULE_OTHERWISE_ACTION_END);
            }
        } finally {
            // Rule End Event
            context.getEventProcessor().fireListeners(createEvent(EventType.RULE_END, result));
        }

        return new RuleResult(getName(), result ? RuleExecutionStatus.PASS : RuleExecutionStatus.FAIL);
    }

    protected boolean processCondition(RuleContext context, Condition condition, EventType startEventType, EventType endEventType) {

        // Check Condition exists
        if (condition == null) return true;

        // Fire the event
        context.getEventProcessor().fireListeners(createEvent(startEventType, condition));

        try {
            // Check the condition
            return condition.isTrue(context);
        } catch (Exception e) {
            throw new RuleExecutionException("Unexpected error occurred while trying to execution Condition ["
                    + startEventType.getDescription() + "] on Rule [" + getName() + "].", e, this.getTarget(), startEventType);
        } finally {
            // Fire the end event
            context.getEventProcessor().fireListeners(createEvent(endEventType, condition));
        }
    }

    protected void processAction(RuleContext context, Action action, EventType startEventType, EventType endEventType) {

        // Check if Action exists
        if (action == null) return;

        // Fire the start event
        context.getEventProcessor().fireListeners(createEvent(startEventType, action));

        try {
            action.run(context);
        } catch (Exception e) {
            throw new RuleExecutionException("Unexpected error occurred while trying to execution Action ["
                    + startEventType.getDescription() + "] on Rule [" + getName() + "].", e, this.getTarget(), startEventType);
        } finally {
            // Fire the end event
            context.getEventProcessor().fireListeners(createEvent(endEventType, action));
        }
    }

    protected ExecutionEvent<RuleExecution> createEvent(EventType eventType, Object executingElement) {
        RuleExecution ruleExecution = new RuleExecution( this, executingElement);
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
    public T getTarget() {
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
    public String toString() {
        String result = getTarget() != null ? getTarget().toString() : null;
        return result != null ? result : "Rule{" +
                "ruleDefinition=" + ruleDefinition +
                '}';
    }
}
