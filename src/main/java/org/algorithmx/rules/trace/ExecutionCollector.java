package org.algorithmx.rules.trace;

import org.algorithmx.rules.event.ExecutionEvent;
import org.algorithmx.rules.event.RuleExecution;
import org.algorithmx.rules.event.RuleSetExecution;
import org.algorithmx.rules.util.RuleUtils;

public class ExecutionCollector implements ExecutionTracer {

    private boolean detailed;
    private int tabCount = 0;

    public ExecutionCollector() {
        super();
    }

    public void collect(ExecutionEvent event, String executionText) {}

    @Override
    public void onRuleStart(ExecutionEvent<RuleExecution> event) {
        StringBuilder result = new StringBuilder();
        result.append(RuleUtils.getTabs(tabCount));
        result.append("Rule : " + event.getData().getRule().getTarget());
        tabCount++;

        collect(event, result.toString());
    }

    @Override
    public void onRulePreCondition(ExecutionEvent<RuleExecution<Boolean>> event) {
        StringBuilder result = new StringBuilder();
        result.append(RuleUtils.getTabs(tabCount));
        result.append("PreCondition : " + event.getData().getResult());
        collect(event, result.toString());
    }

    @Override
    public void onRuleCondition(ExecutionEvent<RuleExecution<Boolean>> event) {
        StringBuilder result = new StringBuilder();
        result.append(RuleUtils.getTabs(tabCount));
        result.append("Condition Met ?  : " + event.getData().getResult());
        collect(event, result.toString());
    }

    @Override
    public void onRuleAction(ExecutionEvent<RuleExecution> event) {
        StringBuilder result = new StringBuilder();
        result.append(RuleUtils.getTabs(tabCount));
        result.append("Action   : Executed" );
        collect(event, result.toString());
    }

    @Override
    public void onRuleOtherwiseAction(ExecutionEvent<RuleExecution> event) {
        StringBuilder result = new StringBuilder();
        result.append(RuleUtils.getTabs(tabCount));
        result.append("Otherwise Action : Executed" );
        collect(event, result.toString());
    }

    @Override
    public void onRuleEnd(ExecutionEvent<RuleExecution> event) {
        tabCount--;
    }

    @Override
    public void onRuleSetStart(ExecutionEvent<RuleSetExecution> event) {
        StringBuilder result = new StringBuilder();
        result.append(RuleUtils.getTabs(tabCount));
        result.append("RuleSet      : " + event.getData().getRules().getName());

        if (event.getData().getRules().getDescription() != null) {
            result.append(System.lineSeparator());
            result.append("Description  : " + event.getData().getRules().getDescription());
        }

        tabCount++;
        collect(event, result.toString());
    }

    @Override
    public void onRuleSetPreCondition(ExecutionEvent<RuleSetExecution<Boolean>> event) {
        StringBuilder result = new StringBuilder();
        result.append(RuleUtils.getTabs(tabCount));
        result.append("PreCondition : " + event.getData().getResult());
        collect(event, result.toString());
    }

    @Override
    public void onRuleSetPreAction(ExecutionEvent<RuleSetExecution> event) {
        StringBuilder result = new StringBuilder();
        result.append(RuleUtils.getTabs(tabCount));
        result.append("PreAction : Executed");
        collect(event, result.toString());
    }

    @Override
    public void onRuleSetError(ExecutionEvent<RuleSetExecution<Exception>> event) {
        StringBuilder result = new StringBuilder();
        result.append(RuleUtils.getTabs(tabCount));
        result.append("Error : " + event.getData().getResult().getMessage());
        collect(event, result.toString());
    }

    @Override
    public void onRuleSetErrorCondition(ExecutionEvent<RuleSetExecution<Boolean>> event) {
        StringBuilder result = new StringBuilder();
        result.append(RuleUtils.getTabs(tabCount));
        result.append("ErrorCondition : " + event.getData().getResult());
        collect(event, result.toString());
    }

    @Override
    public void onRuleSetStopCondition(ExecutionEvent<RuleSetExecution<Boolean>> event) {
        StringBuilder result = new StringBuilder();
        result.append(RuleUtils.getTabs(tabCount));
        result.append("StopCondition : " + event.getData().getResult());
        collect(event, result.toString());
    }

    @Override
    public void onRuleSetPostAction(ExecutionEvent<RuleSetExecution> event) {
        StringBuilder result = new StringBuilder();
        result.append(RuleUtils.getTabs(tabCount));
        result.append("PostAction : Executed");
        collect(event, result.toString());
    }

    @Override
    public void onRuleSetEnd(ExecutionEvent<RuleSetExecution> event) {
        tabCount--;
    }

    public void reset() {
        tabCount = 0;
    }

    public boolean isDetailed() {
        return detailed;
    }
}
