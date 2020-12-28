package org.algorithmx.rules.trace;

import org.algorithmx.rules.core.action.Action;
import org.algorithmx.rules.core.condition.Condition;
import org.algorithmx.rules.core.rule.Rule;
import org.algorithmx.rules.event.ActionExecution;
import org.algorithmx.rules.event.EventType;
import org.algorithmx.rules.event.ExecutionEvent;
import org.algorithmx.rules.event.ExecutionListener;
import org.algorithmx.rules.event.FunctionExecution;
import org.algorithmx.rules.event.RuleExecution;
import org.algorithmx.rules.event.RuleSetExecution;
import org.algorithmx.rules.event.RuleSetExecutionError;
import org.algorithmx.rules.util.reflect.ReflectionUtils;

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

            // No Trace required
            //if (!ruleStack.isEmpty() && !ruleStack.peek().getRuleDefinition().isTrace()) {
            //   return;
            //}

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
    public void onCondition(ExecutionEvent<FunctionExecution<Boolean>> event) {}

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

    @EventMarker(eventType = EventType.RULE_SET_PRE_ACTION_START)
    public void onRuleSetPreActionStart(ExecutionEvent<RuleSetExecution<Action>> event) {}

    @EventMarker(eventType = EventType.RULE_SET_PRE_ACTION_END)
    public void onRuleSetPreActionEnd(ExecutionEvent<RuleSetExecution<Action>> event) {}

    @EventMarker(eventType = EventType.RULE_SET_ERROR)
    public void onRuleSetError(ExecutionEvent<RuleSetExecutionError> event) {}

    @EventMarker(eventType = EventType.RULE_SET_ERROR_CONDITION_START)
    public void onRuleSetErrorConditionStart(ExecutionEvent<RuleSetExecution<Condition>> event) {}

    @EventMarker(eventType = EventType.RULE_SET_ERROR_CONDITION_END)
    public void onRuleSetErrorConditionEnd(ExecutionEvent<RuleSetExecution<Condition>> event) {}

    @EventMarker(eventType = EventType.RULE_SET_STOP_CONDITION_START)
    public void onRuleSetStopConditionStart(ExecutionEvent<RuleSetExecution<Condition>> event) {}

    @EventMarker(eventType = EventType.RULE_SET_STOP_CONDITION_END)
    public void onRuleSetStopConditionEnd(ExecutionEvent<RuleSetExecution<Condition>> event) {}

    @EventMarker(eventType = EventType.RULE_SET_POST_ACTION_START)
    public void onRuleSetPostActionStart(ExecutionEvent<RuleSetExecution<Action>> event) {}

    @EventMarker(eventType = EventType.RULE_SET_POST_ACTION_END)
    public void onRuleSetPostActionEnd(ExecutionEvent<RuleSetExecution<Action>> event) {}
}
