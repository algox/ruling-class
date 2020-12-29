package org.algorithmx.rules.validation;

import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.core.function.Function;
import org.algorithmx.rules.core.rule.ClassBasedRuleBuilder;
import org.algorithmx.rules.core.rule.RuleBuilder;
import org.algorithmx.rules.core.rule.RuleContext;
import org.algorithmx.rules.core.rule.RuleDefinition;
import org.algorithmx.rules.core.rule.RuleExecutionException;
import org.algorithmx.rules.core.rule.RuleResult;
import org.algorithmx.rules.core.rule.RulingClass;
import org.algorithmx.rules.lib.spring.util.Assert;
import org.algorithmx.rules.util.LambdaUtils;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

public class RuleProducingFunctionRule<T> extends RulingClass {

    private final Function<T> supplier;

    public RuleProducingFunctionRule(Function<T> supplier) {
        super(load(supplier));
        Assert.notNull(supplier, "supplier cannot be null.");
        this.supplier = supplier;
    }

    @Override
    public RuleResult run(RuleContext ctx) throws UnrulyException {
        Object target;
        boolean eventsEnabled = ctx.isEventsEnabled();

        try {
            ctx.setEventsEnabled(false);
            target = supplier.apply(ctx);
        } catch (Exception e) {
            throw new RuleExecutionException("Unable to create Rule [" + getRuleDefinition().getName()
                    + "] using the given supplier.", e, this);
        } finally {
            ctx.setEventsEnabled(eventsEnabled);
        }

        org.algorithmx.rules.core.rule.Rule rule = RuleBuilder.create(target);
        return rule.run(ctx);
    }
    @Override
    public String toString() {
        return getName();
    }

    private static <T> RuleDefinition load(Function<T> supplier) {
        Assert.notNull(supplier, "supplier cannot be null.");

        try {
            SerializedLambda lambda = supplier.getTarget() != null
                    ? LambdaUtils.getSafeSerializedLambda(supplier.getTarget())
                    : null;
            Class<?> ruleClass = lambda != null
                    ? getRuleClass(lambda)
                    : getRuleClass(supplier);
            return new RuleDefinition(ruleClass, ClassBasedRuleBuilder.getRuleName(ruleClass),
                    ClassBasedRuleBuilder.getRuleDescription(ruleClass));
        } catch (Exception e) {
            return new RuleDefinition(RuleProducingFunctionRule.class, "RuleProducingFunctionRule", "Rule Wrapper");
        }
    }

    private static Class<?> getRuleClass(SerializedLambda lambda) {
        Class<?> c = LambdaUtils.getImplementationClass(lambda);
        Method m = LambdaUtils.getImplementationMethod(lambda, c);
        return m.getReturnType();
    }

    private static <T> Class<?> getRuleClass(Function<T> supplier) throws NoSuchMethodException {
        Method m = supplier.getClass().getDeclaredMethod("apply", Object[].class);
        return m.getReturnType();
    }
}
