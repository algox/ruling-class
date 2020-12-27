package org.algorithmx.rules.trace;

import org.algorithmx.rules.bind.match.ParameterMatch;
import org.algorithmx.rules.core.model.MethodDefinition;
import org.algorithmx.rules.event.ExecutionEvent;
import org.algorithmx.rules.event.RuleExecution;
import org.algorithmx.rules.event.RuleSetExecution;
import org.algorithmx.rules.util.RuleUtils;

public class ExecutionCollector implements ExecutionTracer {

    private boolean detailed = false;
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
        collect(event, createLog("PreCondition : " + event.getData().getResult(),
                event.getData().getMethodDefinition(), event.getData().getParameterMatches(),
                event.getData().getValues(), tabCount + 1));
    }

    protected String createLog(String description, MethodDefinition methodDefinition,
                                ParameterMatch[] matches, Object[] values, int tabCount) {
        StringBuilder result = new StringBuilder();
        result.append(RuleUtils.getTabs(tabCount));
        result.append(description);

        if (isDetailed()) {
            result.append(System.lineSeparator());
            result.append(RuleUtils.getMethodDescription(methodDefinition, matches, values, RuleUtils.getTabs(tabCount)));
        }

        return result.toString();
    }

    @Override
    public void onRuleCondition(ExecutionEvent<RuleExecution<Boolean>> event) {
        collect(event, createLog("Condition : " + event.getData().getResult(),
                event.getData().getMethodDefinition(), event.getData().getParameterMatches(),
                event.getData().getValues(), tabCount + 1));
    }

    @Override
    public void onRuleAction(ExecutionEvent<RuleExecution> event) {
        collect(event, createLog("Action : Executed",
                event.getData().getMethodDefinition(), event.getData().getParameterMatches(),
                event.getData().getValues(), tabCount + 1));
    }

    @Override
    public void onRuleOtherwiseAction(ExecutionEvent<RuleExecution> event) {
        collect(event, createLog("Otherwise Action : Executed",
                event.getData().getMethodDefinition(), event.getData().getParameterMatches(),
                event.getData().getValues(), tabCount + 1));
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

        result.append(System.lineSeparator());
        tabCount++;
        collect(event, result.toString());
    }

    @Override
    public void onRuleSetPreCondition(ExecutionEvent<RuleSetExecution<Boolean>> event) {
        collect(event, createLog("PreCondition : " + event.getData().getResult(),
                event.getData().getMethodDefinition(), event.getData().getParameterMatches(),
                event.getData().getValues(), tabCount));
    }

    @Override
    public void onRuleSetPreAction(ExecutionEvent<RuleSetExecution> event) {
        collect(event, createLog("PreAction : Executed",
                event.getData().getMethodDefinition(), event.getData().getParameterMatches(),
                event.getData().getValues(), tabCount));
    }

    @Override
    public void onRuleSetError(ExecutionEvent<RuleSetExecution<Exception>> event) {
        StringBuilder result = new StringBuilder();
        result.append("Error : " +  event.getData().getResult().getMessage());
    }

    @Override
    public void onRuleSetErrorCondition(ExecutionEvent<RuleSetExecution<Boolean>> event) {
        collect(event, createLog("ErrorCondition : " + event.getData().getResult(),
                event.getData().getMethodDefinition(), event.getData().getParameterMatches(),
                event.getData().getValues(), tabCount));
    }

    @Override
    public void onRuleSetStopCondition(ExecutionEvent<RuleSetExecution<Boolean>> event) {
        collect(event, createLog("StopCondition : " + event.getData().getResult(),
                event.getData().getMethodDefinition(), event.getData().getParameterMatches(),
                event.getData().getValues(), tabCount));
    }

    @Override
    public void onRuleSetPostAction(ExecutionEvent<RuleSetExecution> event) {
        collect(event, createLog("PostAction : Executed" + event.getData().getResult(),
                event.getData().getMethodDefinition(), event.getData().getParameterMatches(),
                event.getData().getValues(), tabCount));
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

    public void setDetailed(boolean detailed) {
        this.detailed = detailed;
    }
}
