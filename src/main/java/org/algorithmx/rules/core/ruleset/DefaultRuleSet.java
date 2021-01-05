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
import org.algorithmx.rules.core.context.RuleContext;
import org.algorithmx.rules.core.rule.Rule;
import org.algorithmx.rules.core.rule.RuleDefinition;
import org.algorithmx.rules.core.rule.RuleExecutionStatus;
import org.algorithmx.rules.core.rule.RuleResult;
import org.algorithmx.rules.event.EventType;
import org.algorithmx.rules.event.ExecutionEvent;
import org.algorithmx.rules.event.RuleSetExecution;
import org.algorithmx.rules.event.RuleSetExecutionError;
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
    private final Rule[] rules;

    private final Condition preCondition;
    private final Action preAction;
    private final Action postAction;
    private final Condition stopCondition;
    private final Condition errorCondition;

    public DefaultRuleSet(RuleSetDefinition ruleSetDefinition,
                          Condition preCondition, Action preAction, Action postAction,
                          Condition stopCondition, Condition errorCondition,
                          Rule...rules) {
        super();
        Assert.notNull(ruleSetDefinition, "ruleSetDefinition cannot be null");
        Assert.isTrue(rules != null && rules.length > 0, "RuleSet must have at least one Rule.");
        this.ruleSetDefinition = ruleSetDefinition;
        this.rules = rules;
        this.preCondition = preCondition;
        this.preAction = preAction;
        this.postAction = postAction;
        this.stopCondition = stopCondition;
        this.errorCondition = errorCondition;
    }

    @Override
    public RuleResultSet run(RuleContext context) throws UnrulyException {
        Assert.notNull(context, "context cannot be null");

        // RuleSet Start Event
        context.getEventProcessor().fireListeners(createEvent(EventType.RULE_SET_START, null));

        RuleResultSet result = new RuleResultSet();

        // Run the PreCondition if there is one.
        boolean preConditionCheck = processCondition(context, getPreCondition(), EventType.RULE_SET_PRE_CONDITION_START,
                EventType.RULE_SET_PRE_CONDITION_END);
        result.setPreConditionCheck(preConditionCheck);

        // RuleSet did not pass the precondition; Do not execute the rules.
        if (!preConditionCheck) return result;

        try {
            // Create a new Scope for the RuleSet to use
            createRuleSetScope(context, result);
            // Run the PreAction if there is one.
            processAction(context, getPreAction(), EventType.RULE_SET_PRE_ACTION_START, EventType.RULE_SET_PRE_ACTION_END);

            int index = 0;
            // Execute the rules in order; STOP if the stopCondition is met.
            for (Rule rule : getRules()) {
                try {
                    // Run the rule
                    RuleResult ruleResult = rule.run(context);
                    result.add(ruleResult);
                } catch (Exception e) {
                    context.getEventProcessor().fireListeners(new ExecutionEvent<>(EventType.RULE_SET_ERROR,
                            new RuleSetExecutionError(this, e)));

                    boolean proceed = processError(context, rule.getRuleDefinition(), index, e);

                    if (!proceed) {
                        throw new RuleSetExecutionException("Unexpected error occurred trying to execute Rule ["
                                + rule.getName() + "] at Index [" + index + "/" + size()
                                + "] on RuleSet [" + getName() + "]",
                                e, this, EventType.RULE_SET_ERROR_CONDITION_START);
                    }

                    result.add(new RuleResult(rule.getName(), RuleExecutionStatus.ERROR));
                } finally {
                    index++;
                }

                // Check to see if we need to stop the execution?
                if (getStopCondition() != null && processCondition(context, getStopCondition(),
                        EventType.RULE_SET_STOP_CONDITION_START, EventType.RULE_SET_STOP_CONDITION_END)) {
                    break;
                }
            }
        } finally {

            try {
                // Run the PostAction if there is one.
                processAction(context, getPostAction(), EventType.RULE_SET_POST_ACTION_START, EventType.RULE_SET_POST_ACTION_END);
            } finally {
                removeRuleSetScope(context);
            }

            // RuleSet Start Event
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
            throw new RuleSetExecutionException("Unexpected error occurred while trying to execution Condition ["
                    + startEventType.getDescription() + "] on RuleSet [" + getName() + "].", e, this, startEventType);
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
            throw new RuleSetExecutionException("Unexpected error occurred while trying to execution Action ["
                    + startEventType.getDescription() + "] on RuleSet [" + getName() + "].", e, this, startEventType);
        } finally {
            // Fire the end event
            context.getEventProcessor().fireListeners(createEvent(endEventType, action));
        }
    }

    protected ExecutionEvent<RuleSetExecution> createEvent(EventType eventType, Object executingElement) {
        RuleSetExecution ruleExecution = new RuleSetExecution( this, executingElement);
        return new ExecutionEvent<>(eventType, ruleExecution);
    }

    protected void createRuleSetScope(RuleContext context, RuleResultSet ruleResultSet) {
        context.getBindings().addScope();
        context.getBindings().bind("ruleResultSet", RuleResultSet.class, ruleResultSet);
    }

    protected void removeRuleSetScope(RuleContext context) {
        context.getBindings().removeScope();
    }

    protected boolean processError(RuleContext context, RuleDefinition ruleDefinition, int index, Exception ex) throws UnrulyException{

        if (getErrorCondition() == null) return false;

        boolean proceed;

        try {
            setupErrorScope(context, ex, "ex");
            proceed = processCondition(context, getErrorCondition(), EventType.RULE_SET_ERROR_CONDITION_START,
                    EventType.RULE_SET_ERROR_CONDITION_END);
        } finally {
            removeErrorScope(context);
        }

        return proceed;
    }

    protected void setupErrorScope(RuleContext context, Exception ex, String bindingName) {
        Bindings errorScope = context.getBindings().addScope();
        errorScope.bind(bindingName, ex);
    }

    protected void removeErrorScope(RuleContext context) {
        context.getBindings().removeScope();
    }

    public Rule get(int index) {
        return rules[index];
    }

    public Rule get(String ruleName) {
        Rule result = null;

        for (Rule rule : rules) {
            if (rule.getName().equals(ruleName)) {
                result = rule;
                break;
            }
        }

        return result;
    }

    @Override
    public RuleSetDefinition getRuleSetDefinition() {
        return ruleSetDefinition;
    }

    @Override
    public String getName() {
        return ruleSetDefinition.getName();
    }

    @Override
    public String getDescription() {
        return ruleSetDefinition.getDescription();
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
                ", rules=" + Arrays.toString(rules) +
                ", preCondition=" + preCondition +
                ", postAction=" + postAction +
                ", stopCondition=" + stopCondition +
                '}';
    }
}
