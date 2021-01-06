package org.algorithmx.rules.core.ruleset;

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.PreCondition;
import org.algorithmx.rules.annotation.Rules;
import org.algorithmx.rules.annotation.StopCondition;
import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.core.condition.Condition;
import org.algorithmx.rules.core.condition.ConditionBuilder;
import org.algorithmx.rules.lib.spring.util.Assert;
import org.algorithmx.rules.util.reflect.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ClassBasedRuleSetBuilder extends RuleSetBuilder {

    private final Class<?> ruleSetClass;
    private Object target;

    private ClassBasedRuleSetBuilder(Class<?> ruleSetClass, Object target) {
        super();
        Assert.notNull(ruleSetClass, "ruleSetClass cannot be null.");
        Assert.notNull(target, "target cannot be null.");
        this.ruleSetClass = ruleSetClass;
        this.target = target;
        load(ruleSetClass, target);
    }

    public static ClassBasedRuleSetBuilder with(Class<?> ruleSetClass, Object target) {
        return new ClassBasedRuleSetBuilder(ruleSetClass, target);
    }

    public static <T> String getRuleSetName(Class<T> ruleSetClass) {
        // Try and locate the RuleSet annotation on the class
        org.algorithmx.rules.annotation.RuleSet rule = ruleSetClass.getAnnotation(org.algorithmx.rules.annotation.RuleSet.class);

        String ruleName = rule == null ? ruleSetClass.getSimpleName() :
                org.algorithmx.rules.annotation.RuleSet.NOT_APPLICABLE.equals(rule.name())
                        ? ruleSetClass.getSimpleName()
                        : rule.name();

        return ruleName;
    }

    public static <T> String getRuleSetDescription(Class<T> ruleSetClass) {
        Description descriptionAnnotation = ruleSetClass.getAnnotation(Description.class);
        return descriptionAnnotation != null ? descriptionAnnotation.value() : null;
    }

    /**
     * Loads the given Rule class. The Rule class must be annotated with @Rule and must define a single "given" method
     * which returns a boolean. The when method can take a arbitrary number of arguments.
     *
     * @param ruleSetClass desired RuleSet class.
     * @param target ruleSet implementation.
     */
    protected void load(Class<?> ruleSetClass, Object target) {
        Assert.notNull(ruleSetClass, "ruleSetClass cannot be null.");
        Assert.notNull(target, "target cannot be null.");

        name(getRuleSetName(ruleSetClass));
        description(getRuleSetDescription(ruleSetClass));

        // Load all rules
        loadRules(ruleSetClass, target);
        loadPreCondition(target);
        loadStopCondition(target);
    }

    protected void loadPreCondition(Object target) {
        Condition[] preConditions = ConditionBuilder.build(target, PreCondition.class, 1);
        // Load Pre-Condition
        if (preConditions.length == 1) {
            preCondition(preConditions[0]);
        }
    }

    protected void loadStopCondition(Object target) {
        Condition[] stopConditions = ConditionBuilder.build(target, StopCondition.class, 1);
        // Load Stop-Condition
        if (stopConditions.length == 1) {
            stopWhen(stopConditions[0]);
        }
    }

    protected void loadRules(Class<?> clazz, Object target) {
        Assert.notNull(clazz, "clazz cannot be null.");
        Method[] candidates = ReflectionUtils.getMethodsWithAnnotation(clazz, Rules.class);

        if (candidates.length > 1) {
            // Too many matches
            throw new UnrulyException(clazz.getSimpleName() + " class has too many @Rules methods. "
                    + "There can be at most 1 "
                    + " methods (Annotated with @Rules"
                    + "). Currently there are [" + candidates.length
                    + "] candidates [" + Arrays.toString(candidates) + "]");
        }

        for (Method candidate : candidates) {
            if (!(candidate.getParameterCount() == 1 && candidate.getReturnType().equals(void.class)
                    && RuleSetBuilder.class.equals(candidate.getParameterTypes()[0]))) {
                throw new UnrulyException("@Rules method must take 1 arg (RuleSetBuilder) and return void. " +
                        "For Example : void load(RuleContext context)"
                        + clazz.getSimpleName() + " method [" + candidate + "]");
            }

        }

        if (candidates.length == 1) {

            try {
                // Build the Rules/Actions
                candidates[0].invoke(target, this);
            } catch (Exception e) {
                throw new UnrulyException("Unexpected error loading Rules from RuleSet", e);
            }
        }
    }

    public Object getTarget() {
        return target;
    }
}
