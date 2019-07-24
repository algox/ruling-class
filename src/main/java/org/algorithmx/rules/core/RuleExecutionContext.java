package org.algorithmx.rules.core;

import org.algorithmx.rules.bind.BindingMatchingStrategy;
import org.algorithmx.rules.bind.BindingMatchingStrategyType;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.spring.util.Assert;

public class RuleExecutionContext {

    private final Bindings bindings;
    private final BindingMatchingStrategy matchingStrategy;

    private BindableMethodExecutor bindableMethodExecutor = BindableMethodExecutor.create();

    public static RuleExecutionContext create(Bindings bindings, BindingMatchingStrategy matchingStrategy) {
        return new RuleExecutionContext(bindings, matchingStrategy);
    }

    public static RuleExecutionContext create(Bindings bindings) {
        return new RuleExecutionContext(bindings, BindingMatchingStrategyType.getDefault().getStrategy());
    }

    private RuleExecutionContext(Bindings bindings, BindingMatchingStrategy matchingStrategy) {
        super();
        Assert.notNull(bindings, "bindings cannot be null");
        Assert.notNull(matchingStrategy, "matchingStrategy cannot be null");
        this.bindings = bindings;
        this.matchingStrategy = matchingStrategy;
    }

    public Bindings getBindings() {
        return bindings;
    }

    public BindingMatchingStrategy getMatchingStrategy() {
        return matchingStrategy;
    }

    public BindableMethodExecutor getBindableMethodExecutor() {
        return bindableMethodExecutor;
    }

    public void setBindableMethodExecutor(BindableMethodExecutor bindableMethodExecutor) {
        Assert.notNull(bindableMethodExecutor, "bindableMethodExecutor cannot be null");
        this.bindableMethodExecutor = bindableMethodExecutor;
    }
}
