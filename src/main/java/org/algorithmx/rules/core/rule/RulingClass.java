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

import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.core.action.Action;
import org.algorithmx.rules.core.condition.Condition;
import org.algorithmx.rules.event.EventType;
import org.algorithmx.rules.event.ExecutionEvent;
import org.algorithmx.rules.event.RuleExecution;
import org.algorithmx.rules.lib.spring.util.Assert;

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
        ctx.fireListeners(createEvent(EventType.RULE_START, null));

        Boolean result = false;

        try {
            // Check the Pre-Condition
            boolean preConditionCheck = processCondition(ctx, getPreCondition(), EventType.RULE_PRE_CONDITION_START,
                    EventType.RULE_PRE_CONDITION_END);
            // We did not pass the Pre-Condition
            if (!preConditionCheck) return new RuleResult(getName(), RuleExecutionStatus.SKIPPED);

            result = processCondition(ctx, getCondition(), EventType.RULE_CONDITION_START, EventType.RULE_CONDITION_END);

            // The Condition passed
            if (result) {
                // Execute associated Actions.
                for (Action action : getActions()) {
                    processAction(ctx, action, EventType.RULE_ACTION_START, EventType.RULE_ACTION_END);
                }
            } else {
                // Execute otherwise Action.
                processAction(ctx, getOtherwiseAction(), EventType.RULE_OTHERWISE_ACTION_START,
                        EventType.RULE_OTHERWISE_ACTION_END);
            }
        } finally {
            // Rule End Event
            ctx.fireListeners(createEvent(EventType.RULE_END, result));
        }

        return new RuleResult(getName(), result ? RuleExecutionStatus.PASS : RuleExecutionStatus.FAIL);
    }

    protected boolean processCondition(RuleContext ctx, Condition condition, EventType startEventType, EventType endEventType) {

        // Check Condition exists
        if (condition == null) return true;

        // Fire the event
        ctx.fireListeners(createEvent(startEventType, condition));

        try {
            // Check the condition
            return condition.isPass(ctx);
        } finally {
            // Fire the end event
            ctx.fireListeners(createEvent(endEventType, condition));
        }
    }

    protected void processAction(RuleContext ctx, Action action, EventType startEventType, EventType endEventType) {

        // Check if Action exists
        if (action == null) return;

        // Fire the start event
        ctx.fireListeners(createEvent(startEventType, action));

        try {
            action.run(ctx);
        } finally {
            // Fire the end event
            ctx.fireListeners(createEvent(endEventType, action));
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
    public String toString() {
        String result = getTarget() != null ? getTarget().toString() : null;
        return result != null ? result : "RulingClass{" +
                "ruleDefinition=" + ruleDefinition +
                '}';
    }
}
