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

package org.algorithmx.rulii.core.ruleset;

import org.algorithmx.rulii.bind.Bindings;
import org.algorithmx.rulii.core.Identifiable;
import org.algorithmx.rulii.core.Runnable;
import org.algorithmx.rulii.core.UnrulyException;
import org.algorithmx.rulii.core.condition.Condition;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.core.rule.RuleResult;
import org.algorithmx.rulii.core.rule.RuleResultExtractor;
import org.algorithmx.rulii.event.EventType;
import org.algorithmx.rulii.event.ExecutionEvent;
import org.algorithmx.rulii.event.RuleSetExecution;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.util.RuleUtils;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Default implementation of the RuleSet.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class RulingFamily implements RuleSet {

    private final RuleSetDefinition ruleSetDefinition;
    private final Runnable[] ruleSetItems;
    private final Condition preCondition;
    private final Condition stopCondition;

    public RulingFamily(RuleSetDefinition ruleSetDefinition,
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

        if (!context.isActive()) throw new UnrulyException("RuleContext is not Active. Perhaps it was stopped earlier ? "
                + "Create a new RuleContext and try again.");

        // RuleSet Start Event
        context.getEventProcessor().fireListeners(createEvent(EventType.RULE_SET_START, null));

        RuleSetResult result = new RuleSetResult(context.getBindings());

        // Run the PreCondition if there is one.
        boolean preConditionCheck = processCondition(context, getPreCondition(), EventType.RULE_SET_PRE_CONDITION_START,
                EventType.RULE_SET_PRE_CONDITION_END);
        result.setPreConditionCheck(preConditionCheck);

        // RuleSet did not pass the precondition; Do not execute the rules.
        if (!preConditionCheck) return result;

        // Create a new Scope for the RuleSet to use
        Bindings ruleSetScope = createRuleSetScope(context, result);
        try {
            int index = 0;

            // Execute the rules/actions in order; STOP if the stopCondition is met.
            for (Runnable runnable : getRuleSetItems()) {
                try {
                    // Run the rule/action
                    Object executionResult = runnable.run(context);

                    if (executionResult instanceof RuleResult) {
                        ((RuleResult) executionResult).setParentName(this.getName());
                    }

                    // Add the results if avail
                    if (runnable instanceof RuleResultExtractor) {
                        RuleResult[] results = ((RuleResultExtractor) executionResult).extract();
                        if (results != null) result.addAll();
                    }

                    index++;
                } catch (Exception e) {
                    throw new RuleSetExecutionException("Unexpected error occurred trying to execute "
                            + runnable.getClass().getSimpleName()
                            + "[" + (runnable instanceof Identifiable
                                    ? ((Identifiable) runnable).getName()
                                    : runnable.toString())
                                + "] at Index [" + index + "/" + size() + "] on RuleSet [" + getName() + "]", e, this);
                }

                // Looks like stopExecution was called on the RuleContext
                if (!context.isActive()) {
                    break;
                }

                // Check to see if we need to stop the execution?
                if (getStopCondition() != null && processCondition(context, getStopCondition(),
                        EventType.RULE_SET_STOP_CONDITION_START, EventType.RULE_SET_STOP_CONDITION_END)) {
                    break;
                }
            }
        } finally {
            removeRuleSetScope(context, ruleSetScope);
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

    protected Bindings createRuleSetScope(RuleContext context, RuleSetResult ruleResultSet) {
        Bindings result = context.getBindings().addScope(getName() + "-scope");
        context.getBindings().bind("ruleSetResult", RuleSetResult.class, ruleResultSet);
        return result;
    }

    protected void removeRuleSetScope(RuleContext context, Bindings target) {
        context.getBindings().removeScope(target);
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

    @Override
    public Runnable[] getRuleSetItems() {
        return ruleSetItems;
    }

    protected String prettyPrint() {
        StringBuilder result = new StringBuilder();

        result.append("RuleSet : " + getName());
        result.append(System.lineSeparator());
        result.append(RuleUtils.TAB);
        result.append("PreCondition : " + (getPreCondition() != null ? "Y" : "N"));
        result.append(System.lineSeparator());
        result.append(RuleUtils.TAB);
        result.append("StopCondition : " + (getStopCondition() != null ? "Y" : "N"));
        result.append(System.lineSeparator());
        result.append(RuleUtils.TAB);
        result.append("Items");
        result.append(System.lineSeparator());
        for (Runnable runnable : getRuleSetItems()) {
            result.append(RuleUtils.TAB);
            result.append(RuleUtils.TAB);
            result.append(runnable);
            result.append(System.lineSeparator());
        }
        return result.toString();
    }

    @Override
    public String toString() {
        return prettyPrint();
    }
}
