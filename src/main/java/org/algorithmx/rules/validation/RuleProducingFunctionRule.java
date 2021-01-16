package org.algorithmx.rules.validation;

import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.core.context.RuleContext;
import org.algorithmx.rules.core.function.Function;
import org.algorithmx.rules.core.rule.Rule;
import org.algorithmx.rules.core.rule.RuleBuilder;
import org.algorithmx.rules.core.rule.RuleDefinition;
import org.algorithmx.rules.core.rule.RuleExecutionException;
import org.algorithmx.rules.core.rule.RuleResult;
import org.algorithmx.rules.core.rule.RulingClass;
import org.algorithmx.rules.lib.spring.util.Assert;
import org.algorithmx.rules.util.LambdaUtils;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

@Deprecated
public class RuleProducingFunctionRule<T> extends RulingClass {

    private final Function<T> supplier;

    public RuleProducingFunctionRule(Function<T> supplier) {
        super(load(supplier));
        Assert.notNull(supplier, "supplier cannot be null.");
        this.supplier = supplier;
    }

    @Override
    public RuleResult run(RuleContext context) throws UnrulyException {
        Object target;
        boolean eventsEnabled = context.getEventProcessor().isEventsEnabled();

        try {
            context.getEventProcessor().setEventsEnabled(false);
            target = supplier.apply(context);
        } catch (Exception e) {
            throw new RuleExecutionException("Unable to create Rule [" + getRuleDefinition().getName()
                    + "] using the given supplier.", e, this);
        } finally {
            context.getEventProcessor().setEventsEnabled(eventsEnabled);
        }

        Rule rule = RuleBuilder.build(target);
        return rule.run(context);
    }
    @Override
    public String toString() {
        return getName();
    }

    protected static <T> RuleDefinition load(Function<T> supplier) {
        Assert.notNull(supplier, "supplier cannot be null.");

        try {
            SerializedLambda lambda = supplier.getTarget() != null
                    ? LambdaUtils.getSafeSerializedLambda(supplier.getTarget())
                    : null;
            Class<?> ruleClass = lambda != null
                    ? getRuleClass(lambda)
                    : getRuleClass(supplier);
            RuleDefinition ruleDefinition = RuleBuilder.withTarget(ruleClass.equals(Object.class)
                    ? RuleProducingFunctionRule.class
                    : ruleClass).buildRuleDefinition();
            return ruleDefinition;
        } catch (Exception e) {
            return new RuleDefinition(RuleProducingFunctionRule.class, "RuleProducingFunctionRule", "Rule Wrapper");
        }
    }

    protected static Class<?> getRuleClass(SerializedLambda lambda) {
        Class<?> c = LambdaUtils.getImplementationClass(lambda);
        Method m = LambdaUtils.getImplementationMethod(lambda, c);
        return m.getReturnType();
    }

    protected static <T> Class<?> getRuleClass(Function<T> supplier) throws NoSuchMethodException {
        Method m = supplier.getClass().getDeclaredMethod("apply", Object[].class);
        return m.getReturnType();
    }
}
