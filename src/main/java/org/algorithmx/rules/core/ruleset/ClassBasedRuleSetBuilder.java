package org.algorithmx.rules.core.ruleset;

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.ErrorCondition;
import org.algorithmx.rules.annotation.PostAction;
import org.algorithmx.rules.annotation.PreAction;
import org.algorithmx.rules.annotation.PreCondition;
import org.algorithmx.rules.annotation.Rules;
import org.algorithmx.rules.annotation.StopCondition;
import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.core.action.Action;
import org.algorithmx.rules.core.action.ActionBuilder;
import org.algorithmx.rules.core.condition.Condition;
import org.algorithmx.rules.core.condition.ConditionBuilder;
import org.algorithmx.rules.core.rule.Rule;
import org.algorithmx.rules.lib.spring.util.Assert;
import org.algorithmx.rules.util.reflect.ReflectionUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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

        loadPreCondition(ruleSetClass, target);
        loadPreAction(ruleSetClass, target);
        loadStopCondition(ruleSetClass, target);
        loadErrorCondition(ruleSetClass, target);
        loadPostAction(ruleSetClass, target);
    }

    protected void loadPreCondition(Class<?> ruleSetClass, Object target) {
        Condition[] preConditions = ConditionBuilder.build(target, PreCondition.class, 1);
        // Load Pre-Condition
        if (preConditions.length == 1) {
            preCondition(preConditions[0]);
        }
    }

    protected void loadPreAction(Class<?> ruleSetClass, Object target) {
        Action[] preActions = ActionBuilder.build(target, PreAction.class, 1);
        // Load Pre-Action
        if (preActions.length == 1) {
            preAction(preActions[0]);
        }
    }

    protected void loadStopCondition(Class<?> ruleSetClass, Object target) {
        Condition[] stopConditions = ConditionBuilder.build(target, StopCondition.class, 1);
        // Load Stop-Condition
        if (stopConditions.length == 1) {
            stopWhen(stopConditions[0]);
        }
    }

    protected void loadErrorCondition(Class<?> ruleSetClass, Object target) {
        Condition[] errorConditions = ConditionBuilder.build(target, ErrorCondition.class, 1);
        // Load error-Condition
        if (errorConditions.length == 1) {
            errorHandler(errorConditions[0]);
        }
    }

    protected void loadPostAction(Class<?> ruleSetClass, Object target) {
        Action[] postActions = ActionBuilder.build(target, PostAction.class, 1);
        // Load Post-Action
        if (postActions.length == 1) {
            postAction(postActions[0]);
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
            if (candidate.getParameterCount() > 0
                    || !(candidate.getReturnType().equals(Rule[].class)
                    || Collection.class.isAssignableFrom(candidate.getReturnType()))) {
                throw new UnrulyException("Rules method must take no args and return either return Rule[] or of type Collection<Rule>."
                        + clazz.getSimpleName() + " method " + candidate
                        + "] returns a [" + candidate.getReturnType() + "]");
            }

        }

        List<Rule> rules = new ArrayList<>();

        if (candidates.length == 1) {
            Object value;

            try {
                value = candidates[0].invoke(target);
            } catch (Exception e) {
                throw new UnrulyException("Unexpected error loading Rules from RuleSet", e);
            }

            if (value == null) return;

            if (value.getClass().isArray()) {
                for (int i = 0; i < Array.getLength(value); i++) {
                    Object element = Array.get(value, i);
                    addRule(candidates[0], element, rules);
                }
            } else if (value instanceof Collection) {
                for (Object element : (Collection) value) {
                    addRule(candidates[0], element, rules);
                }
            } else {
                throw new UnrulyException("Rules method [" + candidates[0] + "] should return a Rule. Got back ["
                        + value.getClass() + "]");
            }
        }

        addAll(rules);
    }

    private static void addRule(Method ruleMethod, Object element, List<Rule> rules) {
        if (element == null) return;

        if (!(element instanceof Rule)) {
            throw new UnrulyException("@Rules method [" + ruleMethod + " must return Rules. Got back ["
                    + element.getClass() + "]");
        }

        rules.add((Rule) element);
    }

    public Object getTarget() {
        return target;
    }
}
