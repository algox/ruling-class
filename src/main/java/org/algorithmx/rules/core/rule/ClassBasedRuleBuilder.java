/**
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 1999-2019, Live Software & Consultants Inc (rules@algorithmx.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.algorithmx.rules.core.rule;

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Given;
import org.algorithmx.rules.annotation.Otherwise;
import org.algorithmx.rules.annotation.Then;
import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.core.action.ActionBuilder;
import org.algorithmx.rules.core.condition.ConditionBuilder;
import org.algorithmx.rules.core.model.MethodDefinition;
import org.algorithmx.rules.lib.spring.util.Assert;
import org.algorithmx.rules.util.reflect.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Builder class for all Class based Rule(s).
 *
 * @author Max Arulananthan.
 * @since 1.0
 */
public class ClassBasedRuleBuilder extends RuleBuilder {

    private ClassBasedRuleBuilder(Class<?> ruleClass, Object target) {
        super();
        load(ruleClass, target);
    }

    public static ClassBasedRuleBuilder with(Class<?> ruleClass, Object target) {
        return new ClassBasedRuleBuilder(ruleClass, target);
    }

    /**
     * Loads the given Rule class. The Rule class must be annotated with @Rule and must define a single "given" method
     * which returns a boolean. The when method can take a arbitrary number of arguments.
     *
     * @param ruleClass desired Rule class.
     * @param target rule implementation.
     */
    protected void load(Class<?> ruleClass, Object target) {
        Assert.notNull(ruleClass, "ruleClass cannot be null.");
        Assert.notNull(target, "target cannot be null.");

        ruleClass(ruleClass);
        target(target);

        // Try and locate the Rule annotation on the class
        org.algorithmx.rules.annotation.Rule rule = ruleClass.getAnnotation(org.algorithmx.rules.annotation.Rule.class);

        String ruleName = rule == null ? ruleClass.getSimpleName() :
                org.algorithmx.rules.annotation.Rule.NOT_APPLICABLE.equals(rule.name())
                ? ruleClass.getSimpleName()
                : rule.name();

        Description descriptionAnnotation = ruleClass.getAnnotation(Description.class);

        name(ruleName);
        description(descriptionAnnotation != null ? descriptionAnnotation.value() : null);

        loadCondition(ruleClass);
        loadThenActions(ruleClass);
        loadOtherwiseAction(ruleClass);
    }

    protected void loadCondition(Class<?> ruleClass) {
        Method[] candidates = ReflectionUtils.getMethodsWithAnnotation(ruleClass, Given.class);

        if (candidates.length > 1) {
            // Too many @Given methods
            throw new UnrulyException("Rule class [" + ruleClass.getName() + "] has too many condition methods. " +
                    "There can be at most 1 condition method (Annotated with @Given). " +
                    "Currently there are [" + candidates.length + "] candidates [" + Arrays.toString(candidates) + "]");
        }

        if (candidates.length == 1) {
            if (!candidates[0].getReturnType().equals(boolean.class)) {
                throw new UnrulyException("Rule Condition must return a boolean. Rule [" + ruleClass + "] Condition ["
                        + candidates[0] + "] returns a [" + candidates[0].getReturnType() +"]");
            }
        }

        given(candidates.length == 1 ? ConditionBuilder.with(getTarget(), MethodDefinition.load(candidates[0])).build()
                : ConditionBuilder.TRUE().build());
    }

    /**
     * Loads all the @Then actions in the given class. A method is considered an Action if takes arbitrary number
     * of arguments and returns nothing (ie: void) and the method is annotated with @ActionConsumer.
     *
     * @param ruleClass desired class
     */
    protected void loadThenActions(Class<?> ruleClass) {
        Method[] thenActions = ReflectionUtils.getMethodsWithAnnotation(ruleClass, Then.class);

        if (thenActions != null) {
            for (Method thenAction : thenActions) {
                then(ActionBuilder.with(getTarget(), MethodDefinition.load(thenAction)).build());
            }
        }
    }

    /**
     * Loads the @Otherwise action in the given class. A method is considered an Action if takes arbitrary number
     * of arguments and returns nothing (ie: void) and the method is annotated with @Otherwise.
     *
     * @param ruleClass desired class
     */
    protected void loadOtherwiseAction(Class<?> ruleClass) {
        Method[] otherwiseActions = ReflectionUtils.getMethodsWithAnnotation(ruleClass, Otherwise.class);

        if (otherwiseActions.length > 1) {
            // Too many @Otherwise methods
            throw new UnrulyException("Rule class [" + ruleClass.getName() + "] has too many otherwise (@Otherwise) methods. " +
                    "There can be at most 1 otherwise method. Currently there are [" + otherwiseActions.length + "] " +
                    "[" + Arrays.toString(otherwiseActions) + "]");
        }

        if (otherwiseActions.length == 1) {
            otherwise(ActionBuilder.with(getTarget(), MethodDefinition.load(otherwiseActions[0])).build());
        }
    }
}
