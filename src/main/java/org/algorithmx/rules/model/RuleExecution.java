package org.algorithmx.rules.model;

import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.spring.util.Assert;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public final class RuleExecution implements Comparable<RuleExecution> {

    private final WeakReference<RuleDefinition> ruleDefinition;
    private final Map<String, String> args = new LinkedHashMap<>();
    private final Date time = new Date();
    private Boolean result;
    private Exception error;

    public RuleExecution(RuleDefinition ruleDefinition) {
        super();
        Assert.notNull(ruleDefinition, "ruleDefinition cannot be null.");
        this.ruleDefinition = new WeakReference<>(ruleDefinition);
    }

    public RuleDefinition getRuleDefinition() {
        return ruleDefinition.get();
    }

    public void add(Binding<Object>...bindings) {
        if (bindings == null || bindings.length == 0) return;
        Arrays.stream(bindings).forEach(this::add);
    }

    public void add(Binding<Object> binding) {
        if (binding == null) return;
        args.put(binding.getName(), binding.getValue() != null ? binding.getValue().toString() : null);
    }

    public Map<String, String> getArgs() {
        return args;
    }

    public Boolean getResult() {
        return result;
    }

    public Exception getError() {
        return error;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public void setError(Exception error) {
        this.error = error;
    }

    public Date getTime() {
        return time;
    }

    public boolean isError() {
        return error != null;
    }

    @Override
    public int compareTo(RuleExecution execution) {
        return getTime().compareTo(execution.getTime());
    }

    @Override
    public String toString() {
        return "RuleExecution{" +
                "ruleDefinition=" + (ruleDefinition.get() != null ? ruleDefinition.get().getName() : "n/a") +
                ", args=" + args +
                ", result=" + result +
                ", error=" + error +
                ", time=" + time +
                '}';
    }
}
