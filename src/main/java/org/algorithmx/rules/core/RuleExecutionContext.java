package org.algorithmx.rules.core;

import org.algorithmx.rules.bind.BindingMatchingStrategy;
import org.algorithmx.rules.bind.BindingMatchingStrategyType;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.spring.util.Assert;

public class RuleExecutionContext {

    private Bindings bindings = Bindings.create();
    private BindingMatchingStrategy matchingStrategy = BindingMatchingStrategyType.getDefault().getStrategy();

    private ParameterResolver parameterResolver = ParameterResolver.create();
    private BindableMethodExecutor methodExecutor =  BindableMethodExecutor.create();
    private ObjectFactory objectFactory = ObjectFactory.create();

    private BindableMethodExecutor bindableMethodExecutor = BindableMethodExecutor.create();

    public static RuleExecutionContext create() {
        return new RuleExecutionContext();
    }

    public static RuleExecutionContext create(Bindings bindings, BindingMatchingStrategy matchingStrategy) {
        return new RuleExecutionContext(bindings, matchingStrategy);
    }

    public static RuleExecutionContext create(Bindings bindings) {
        return new RuleExecutionContext(bindings, BindingMatchingStrategyType.getDefault().getStrategy());
    }

    private RuleExecutionContext() {
        super();
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
        return methodExecutor;
    }

    public ObjectFactory objectFactory() {
        return objectFactory;
    }

    public void setBindableMethodExecutor(BindableMethodExecutor bindableMethodExecutor) {
        Assert.notNull(bindableMethodExecutor, "bindableMethodExecutor cannot be null");
        this.bindableMethodExecutor = bindableMethodExecutor;
    }
}
