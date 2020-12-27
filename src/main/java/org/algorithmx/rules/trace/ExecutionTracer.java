package org.algorithmx.rules.trace;

import org.algorithmx.rules.event.EventType;
import org.algorithmx.rules.event.ExecutionEvent;
import org.algorithmx.rules.event.ExecutionListener;
import org.algorithmx.rules.event.RuleExecution;
import org.algorithmx.rules.event.RuleSetExecution;

public interface ExecutionTracer extends ExecutionListener {

    @Override
    default <T> void onEvent(ExecutionEvent<T> event) {
        if (event == null) return;

        if (event.getData() instanceof RuleExecution
                && !((RuleExecution) event.getData()).getRule().getRuleDefinition().isTrace()) {
            return;
        }

        try {
            if (EventType.RULE_START == event.getEventType()) {
                onRuleStart((ExecutionEvent<RuleExecution>) event);
            } else if (EventType.RULE_PRE_CONDITION == event.getEventType()) {
                onRulePreCondition((ExecutionEvent<RuleExecution<Boolean>>) event);
            } else if (EventType.RULE_CONDITION == event.getEventType()) {
                onRuleCondition((ExecutionEvent<RuleExecution<Boolean>>) event);
            } else if (EventType.RULE_ACTION == event.getEventType()) {
                onRuleAction((ExecutionEvent<RuleExecution>) event);
            } else if (EventType.RULE_OTHERWISE_ACTION == event.getEventType()) {
                onRuleOtherwiseAction((ExecutionEvent<RuleExecution>) event);
            } else if (EventType.RULE_END == event.getEventType()) {
                onRuleEnd((ExecutionEvent<RuleExecution>) event);
            } else if (EventType.RULE_SET_START == event.getEventType()) {
                onRuleSetStart((ExecutionEvent<RuleSetExecution>) event);
            } else if (EventType.RULE_SET_PRE_CONDITION == event.getEventType()) {
                onRuleSetPreCondition((ExecutionEvent<RuleSetExecution<Boolean>>) event);
            } else if (EventType.RULE_SET_PRE_ACTION == event.getEventType()) {
                onRuleSetPreAction((ExecutionEvent<RuleSetExecution>) event);
            } else if (EventType.RULE_SET_ERROR == event.getEventType()) {
                onRuleSetError((ExecutionEvent<RuleSetExecution<Exception>>) event);
            } else if (EventType.RULE_SET_ERROR_CONDITION == event.getEventType()) {
                onRuleSetErrorCondition((ExecutionEvent<RuleSetExecution<Boolean>>) event);
            } else if (EventType.RULE_SET_STOP_CONDITION == event.getEventType()) {
                onRuleSetStopCondition((ExecutionEvent<RuleSetExecution<Boolean>>) event);
            } else if (EventType.RULE_SET_POST_ACTION == event.getEventType()) {
                onRuleSetPostAction((ExecutionEvent<RuleSetExecution>) event);
            } else if (EventType.RULE_SET_END == event.getEventType()) {
                onRuleSetEnd((ExecutionEvent<RuleSetExecution>) event);
            }
        } finally {
            postEvent(event);
        }
    }

    default <T> void postEvent(ExecutionEvent<T> event) {

    }

    default void onRuleStart(ExecutionEvent<RuleExecution> event) {

    }

    default void onRulePreCondition(ExecutionEvent<RuleExecution<Boolean>> event) {

    }

    default void onRuleCondition(ExecutionEvent<RuleExecution<Boolean>> event) {

    }

    default void onRuleAction(ExecutionEvent<RuleExecution> event) {

    }

    default void onRuleOtherwiseAction(ExecutionEvent<RuleExecution> event) {

    }

    default void onRuleEnd(ExecutionEvent<RuleExecution> event) {

    }

    default void onRuleSetStart(ExecutionEvent<RuleSetExecution> event) {

    }

    default void onRuleSetPreCondition(ExecutionEvent<RuleSetExecution<Boolean>> event) {

    }

    default void onRuleSetPreAction(ExecutionEvent<RuleSetExecution> event) {

    }

    default void onRuleSetError(ExecutionEvent<RuleSetExecution<Exception>> event) {

    }

    default void onRuleSetErrorCondition(ExecutionEvent<RuleSetExecution<Boolean>> event) {

    }

    default void onRuleSetStopCondition(ExecutionEvent<RuleSetExecution<Boolean>> event) {

    }

    default void onRuleSetPostAction(ExecutionEvent<RuleSetExecution> event) {

    }

    default void onRuleSetEnd(ExecutionEvent<RuleSetExecution> event) {

    }
}
