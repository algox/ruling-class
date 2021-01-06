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

import org.algorithmx.rules.core.Identifiable;
import org.algorithmx.rules.core.Runnable;
import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.core.condition.Condition;
import org.algorithmx.rules.core.context.RuleContext;
import org.algorithmx.rules.core.rule.Rule;
import org.algorithmx.rules.core.rule.RuleResult;
import org.algorithmx.rules.event.EventType;
import org.algorithmx.rules.event.ExecutionEvent;
import org.algorithmx.rules.event.RuleSetExecution;
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

    private final RuleSetDefinition ruleSetDefinition;
    private final Runnable[] ruleSetItems;

    private final Condition preCondition;
    private final Condition stopCondition;

    public DefaultRuleSet(RuleSetDefinition ruleSetDefinition,
                          Condition preCondition, Condition stopCondition,
                          Runnable...ruleSetItems) {
        super();
        Assert.notNull(ruleSetDefinition, "ruleSetDefinition cannot be null");
        this.ruleSetDefinition = ruleSetDefinition;
        this.ruleSetItems = ruleSetItems != null ? ruleSetItems : new Runnable[0];
        this.preCondition = preCondition;
        this.stopCondition = stopCondition;
        Assert.notNullArray(ruleSetItems, "ruleSetItems");
    }

    @Override
    public RuleSetResult run(RuleContext context) throws UnrulyException {
        Assert.notNull(context, "context cannot be null");

        // RuleSet Start Event
        context.getEventProcessor().fireListeners(createEvent(EventType.RULE_SET_START, null));

        RuleSetResult result = new RuleSetResult(context.getBindings());

        // Run the PreCondition if there is one.
        boolean preConditionCheck = processCondition(context, getPreCondition(), EventType.RULE_SET_PRE_CONDITION_START,
                EventType.RULE_SET_PRE_CONDITION_END);
        result.setPreConditionCheck(preConditionCheck);

        // RuleSet did not pass the precondition; Do not execute the rules.
        if (!preConditionCheck) return result;

        try {
            // Create a new Scope for the RuleSet to use
            createRuleSetScope(context, result);

            int index = 0;

            // Execute the rules/actions in order; STOP if the stopCondition is met.
            for (Runnable runnable : getRuleSetItems()) {
                try {
                    // Run the rule/action
                    Object executionResult = runnable.run(context);
                    if (runnable instanceof Rule) result.add((RuleResult) executionResult);
                    index++;
                } catch (Exception e) {
                    throw new RuleSetExecutionException("Unexpected error occurred trying to execute "
                            + runnable.getClass().getSimpleName()
                            + "[" + (runnable instanceof Identifiable
                                    ? ((Identifiable) runnable).getName()
                                    : runnable.toString())
                                + "] at Index [" + index + "/" + size() + "] on RuleSet [" + getName() + "]", e, this);
                }

                // Check to see if we need to stop the execution?
                if (getStopCondition() != null && processCondition(context, getStopCondition(),
                        EventType.RULE_SET_STOP_CONDITION_START, EventType.RULE_SET_STOP_CONDITION_END)) {
                    break;
                }
            }
        } finally {
            removeRuleSetScope(context);
            // RuleSet End Event
            context.getEventProcessor().fireListeners(createEvent(EventType.RULE_SET_END, null));
        }

        return result;
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
            throw new RuleSetExecutionException("Unexpected error occurred while trying to execution Pre Condition ["
                    + startEventType.getDescription() + "] on RuleSet [" + getName() + "].", e, this);
        } finally {
            // Fire the end event
            context.getEventProcessor().fireListeners(createEvent(endEventType, condition));
        }
    }

    protected ExecutionEvent<RuleSetExecution> createEvent(EventType eventType, Object executingElement) {
        RuleSetExecution ruleExecution = new RuleSetExecution( this, executingElement);
        return new ExecutionEvent<>(eventType, ruleExecution);
    }

    protected void createRuleSetScope(RuleContext context, RuleSetResult ruleResultSet) {
        context.getBindings().addScope();
        context.getBindings().bind("ruleSetResult", RuleSetResult.class, ruleResultSet);
    }

    protected void removeRuleSetScope(RuleContext context) {
        context.getBindings().removeScope();
    }

    @Override
    public RuleSetDefinition getRuleSetDefinition() {
        return ruleSetDefinition;
    }

    @Override
    public <T extends Runnable> T get(int index, Class<T> type) {
        return (T) getRuleSetItems()[index];
    }

    @Override
    public <T extends Runnable> T get(String name, Class<T> type) {
        Assert.notNull(name, "name cannot be null.");
        Runnable result = null;

        for (Runnable item : getRuleSetItems()) {
            if (item instanceof Identifiable && name.equals(((Identifiable) item).getName())) {
                result = item;
                break;
            }
        }

        return (T) result;
    }

    @Override
    public String getName() {
        return getRuleSetDefinition().getName();
    }

    @Override
    public String getDescription() {
        return getRuleSetDefinition().getDescription();
    }

    @Override
    public Condition getPreCondition() {
        return preCondition;
    }

    @Override
    public Condition getStopCondition() {
        return stopCondition;
    }

    @Override
    public int size() {
        return getRuleSetItems().length;
    }

    @Override
    public Iterator<Runnable> iterator() {
        return Arrays.stream(getRuleSetItems()).iterator();
    }

    private Runnable[] getRuleSetItems() {
        return ruleSetItems;
    }

    @Override
    public String toString() {
        return "DefaultRuleSet{" +
                ", ruleSetItems=" + Arrays.toString(getRuleSetItems()) +
                ", preCondition=" + getPreCondition() +
                ", stopCondition=" + getStopCondition() +
                '}';
    }
}
