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

package org.algorithmx.rulii.trace;

import org.algorithmx.rulii.core.action.Action;
import org.algorithmx.rulii.core.condition.Condition;
import org.algorithmx.rulii.core.rule.Rule;
import org.algorithmx.rulii.event.ActionExecution;
import org.algorithmx.rulii.event.ConditionExecution;
import org.algorithmx.rulii.event.EventType;
import org.algorithmx.rulii.event.ExecutionEvent;
import org.algorithmx.rulii.event.ExecutionListener;
import org.algorithmx.rulii.event.FunctionExecution;
import org.algorithmx.rulii.event.RuleExecution;
import org.algorithmx.rulii.event.RuleSetExecution;
import org.algorithmx.rulii.util.reflect.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public abstract class ExecutionTracer implements ExecutionListener {

    private final Map<EventType, Method> eventHandlers = new HashMap<>();
    private final Stack<Rule> ruleStack = new Stack<>();

    protected ExecutionTracer() {
        super();
        init();
    }

    private void init() {
        Method[] methods = ReflectionUtils.getMethodsWithAnnotation(ExecutionTracer.class, EventMarker.class);

        if (methods == null) return;

        for (Method method : methods) {
            if (method.getParameterCount() != 1) continue;
            if (!ExecutionEvent.class.isAssignableFrom(method.getParameterTypes()[0])) continue;
            EventMarker marker = method.getAnnotation(EventMarker.class);
            eventHandlers.put(marker.eventType(), method);
        }
    }

    @Override
    public <T> void onEvent(ExecutionEvent<T> event) {

        if (event == null) return;

        try {
            if (event.getEventType() == EventType.RULE_START) {
                ruleStack.push(((RuleExecution) event.getData()).getRule());
            } else if (event.getEventType() == EventType.RULE_END) {
                ruleStack.pop();
            }

            Method handler = eventHandlers.get(event.getEventType());
            if (handler == null) return;
            handler.invoke(this, event);
        } catch (Exception e) {
            // TODO : Log error
        } finally {
            postEvent(event);
        }
    }

    public <T> void postEvent(ExecutionEvent<T> event) {}

    @EventMarker(eventType = EventType.ON_FUNCTION)
    public void onFunction(ExecutionEvent<FunctionExecution> event) {}

    @EventMarker(eventType = EventType.ON_CONDITION)
    public void onCondition(ExecutionEvent<ConditionExecution> event) {}

    @EventMarker(eventType = EventType.ON_ACTION)
    public void onAction(ExecutionEvent<ActionExecution> event) {}

    @EventMarker(eventType = EventType.RULE_START)
    public void onRuleStart(ExecutionEvent<RuleExecution> event) {}

    @EventMarker(eventType = EventType.RULE_END)
    public void onRuleEnd(ExecutionEvent<RuleExecution> event) {}

    @EventMarker(eventType = EventType.RULE_PRE_CONDITION_START)
    public void onRulePreConditionStart(ExecutionEvent<RuleExecution<Condition>> event) {}

    @EventMarker(eventType = EventType.RULE_PRE_CONDITION_END)
    public void onRulePreConditionEnd(ExecutionEvent<RuleExecution<Condition>> event) {}

    @EventMarker(eventType = EventType.RULE_CONDITION_START)
    public void onRuleConditionStart(ExecutionEvent<RuleExecution<Condition>> event) {}

    @EventMarker(eventType = EventType.RULE_CONDITION_END)
    public void onRuleConditionEnd(ExecutionEvent<RuleExecution<Condition>> event) {}

    @EventMarker(eventType = EventType.RULE_ACTION_START)
    public void onRuleActionStart(ExecutionEvent<RuleExecution<Action>> event) {}

    @EventMarker(eventType = EventType.RULE_ACTION_END)
    public void onRuleActionEnd(ExecutionEvent<RuleExecution<Action>> event) {}

    @EventMarker(eventType = EventType.RULE_OTHERWISE_ACTION_START)
    public void onRuleOtherwiseActionStart(ExecutionEvent<RuleExecution<Action>> event) {}

    @EventMarker(eventType = EventType.RULE_OTHERWISE_ACTION_END)
    public void onRuleOtherwiseActionEnd(ExecutionEvent<RuleExecution<Action>> event) {}

    @EventMarker(eventType = EventType.RULE_SET_START)
    public void onRuleSetStart(ExecutionEvent<RuleSetExecution> event) {}

    @EventMarker(eventType = EventType.RULE_SET_END)
    public void onRuleSetEnd(ExecutionEvent<RuleSetExecution> event) {}

    @EventMarker(eventType = EventType.RULE_SET_PRE_CONDITION_START)
    public void onRuleSetPreConditionStart(ExecutionEvent<RuleSetExecution<Condition>> event) {}

    @EventMarker(eventType = EventType.RULE_SET_PRE_CONDITION_END)
    public void onRuleSetPreConditionEnd(ExecutionEvent<RuleSetExecution<Condition>> event) {}

    @EventMarker(eventType = EventType.RULE_SET_STOP_CONDITION_START)
    public void onRuleSetStopConditionStart(ExecutionEvent<RuleSetExecution<Condition>> event) {}

    @EventMarker(eventType = EventType.RULE_SET_STOP_CONDITION_END)
    public void onRuleSetStopConditionEnd(ExecutionEvent<RuleSetExecution<Condition>> event) {}

}
