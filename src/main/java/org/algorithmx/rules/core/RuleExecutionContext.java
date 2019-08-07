package org.algorithmx.rules.core;

import org.algorithmx.rules.bind.BindingMatchingStrategy;
import org.algorithmx.rules.bind.BindingMatchingStrategyType;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.spring.util.Assert;

import java.util.Arrays;
import java.util.LinkedHashMap;

public class RuleExecutionContext {

    private final Bindings bindings;
    private final LinkedHashMap<String, RuleSet> rules = new LinkedHashMap<>();
    private final BindingMatchingStrategy matchingStrategy;

    private ParameterResolver parameterResolver = ParameterResolver.create();
    private BindableMethodExecutor bindableMethodExecutor = BindableMethodExecutor.create();

    public static RuleExecutionContext create(Bindings bindings) {
        return new RuleExecutionContext(bindings, BindingMatchingStrategyType.getDefault().getStrategy());
    }

    public static RuleExecutionContext create(Bindings bindings, BindingMatchingStrategy matchingStrategy) {
        return new RuleExecutionContext(bindings, matchingStrategy);
    }

    private RuleExecutionContext(Bindings bindings, BindingMatchingStrategy matchingStrategy) {
        super();
        Assert.notNull(bindings, "bindings cannot be null");
        Assert.notNull(matchingStrategy, "matchingStrategy cannot be null");
        this.bindings = bindings;
        this.matchingStrategy = matchingStrategy;
    }

    public Bindings bindings() {
        return bindings;
    }

    public BindingMatchingStrategy matchingStrategy() {
        return matchingStrategy;
    }

    public BindableMethodExecutor bindableMethodExecutor() {
        return bindableMethodExecutor;
    }

    public ParameterResolver parameterResolver() {
        return parameterResolver;
    }

    public BindableMethodExecutor methodExecutor() {
        return bindableMethodExecutor;
    }

    public RuleSet ruleSet(String name) {
        return rules.get(name);
    }

    public void setRules(RuleSet...rules) {
        Assert.notNull(rules, "rules cannot be null");
        Arrays.stream(rules).forEach(r -> this.rules.put(r.getName(), r));
    }

    public void setBindableMethodExecutor(BindableMethodExecutor bindableMethodExecutor) {
        Assert.notNull(bindableMethodExecutor, "bindableMethodExecutor cannot be null");
        this.bindableMethodExecutor = bindableMethodExecutor;
    }
}
