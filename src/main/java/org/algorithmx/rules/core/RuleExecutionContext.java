package org.algorithmx.rules.core;

import org.algorithmx.rules.bind.BindingMatchingStrategy;
import org.algorithmx.rules.bind.BindingMatchingStrategyType;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.spring.util.Assert;

public class RuleExecutionContext {

    private final Bindings bindings;
    private final BindingMatchingStrategy matchingStrategy;

    private ParameterResolver parameterResolver = ParameterResolver.defaultParameterResolver();
    private BindableMethodExecutor methodExecutor = BindableMethodExecutor.defaultBindableMethodExecutor();

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

    public ParameterResolver parameterResolver() {
        return parameterResolver;
    }

    public BindableMethodExecutor methodExecutor() {
        return methodExecutor;
    }
}
