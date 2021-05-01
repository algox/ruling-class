/*
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 1999-2021, Algorithmx Inc.
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

package org.algorithmx.rulii.core.rule;

import org.algorithmx.rulii.annotation.Description;
import org.algorithmx.rulii.annotation.Given;
import org.algorithmx.rulii.annotation.Order;
import org.algorithmx.rulii.annotation.Otherwise;
import org.algorithmx.rulii.annotation.PreCondition;
import org.algorithmx.rulii.annotation.Then;
import org.algorithmx.rulii.core.Ordered;
import org.algorithmx.rulii.core.action.Action;
import org.algorithmx.rulii.core.action.ActionBuilder;
import org.algorithmx.rulii.core.condition.Condition;
import org.algorithmx.rulii.core.condition.ConditionBuilder;
import org.algorithmx.rulii.lib.spring.util.Assert;

/**
 * Builder class for all Class based Rule(s).
 *
 * @author Max Arulananthan.
 * @since 1.0
 */
public class ClassBasedRuleBuilder<T> extends AbstractRuleBuilder<T> {

    private ClassBasedRuleBuilder(Class<T> ruleClass, T target) {
        super();
        load(ruleClass, target);
    }

    public static <T> ClassBasedRuleBuilder<T> with(Class<T> ruleClass, T target) {
        return new ClassBasedRuleBuilder(ruleClass, target);
    }

    public static <T> String getRuleName(Class<T> ruleClass) {
        // Try and locate the Rule annotation on the class
        org.algorithmx.rulii.annotation.Rule rule = ruleClass.getAnnotation(org.algorithmx.rulii.annotation.Rule.class);

        String ruleName = rule == null ? ruleClass.getSimpleName() :
                org.algorithmx.rulii.annotation.Rule.NOT_APPLICABLE.equals(rule.name())
                        ? ruleClass.getSimpleName()
                        : rule.name();

        return ruleName;
    }

    public static <T> String getRuleDescription(Class<T> ruleClass) {
        Description descriptionAnnotation = ruleClass.getAnnotation(Description.class);
        return descriptionAnnotation != null ? descriptionAnnotation.value() : null;
    }

    public static <T> Integer getRuleOrder(Class<T> ruleClass) {
        Order orderAnnotation = ruleClass.getAnnotation(Order.class);
        return orderAnnotation != null ? orderAnnotation.value() : Ordered.LOWEST_PRECEDENCE;
    }

    /**
     * Loads the given Rule class. The Rule class must be annotated with @Rule and must define a single "given" method
     * which returns a boolean. The when method can take a arbitrary number of arguments.
     *
     * @param ruleClass desired Rule class.
     * @param target rule implementation.
     */
    protected void load(Class<T> ruleClass, T target) {
        Assert.notNull(ruleClass, "ruleClass cannot be null.");
        Assert.notNull(target, "target cannot be null.");

        ruleClass(ruleClass);
        target(target);

        name(getRuleName(ruleClass));
        description(getRuleDescription(ruleClass));
        order(getRuleOrder(ruleClass));
        loadPreCondition(ruleClass, target);
        loadCondition(ruleClass, target);
        loadThenActions(target);
        loadOtherwiseAction(target);
    }

    protected void loadPreCondition(Class<T> ruleClass, Object target) {
        Condition[] preConditions = ConditionBuilder.build(target, PreCondition.class, 1);
        // Load Pre-Condition
        if (preConditions.length == 1) {
            preCondition(preConditions[0]);
        }
    }

    protected void loadCondition(Class<T> ruleClass, Object target) {
        Condition[] givenConditions = ConditionBuilder.build(target, Given.class, 1);
        // Load Given-Condition
        if (givenConditions.length == 1) {
            given(givenConditions[0]);
        } else {
            given(ConditionBuilder.TRUE());
        }
    }

    /**
     * Loads all the @Then actions in the given class. A method is considered an Action if takes arbitrary number
     * of arguments and returns nothing (ie: void) and the method is annotated with @ActionConsumer.
     *
     * @param target rule target.
     */
    protected void loadThenActions(Object target) {
        Action[] thenActions = ActionBuilder.build(target, Then.class, null);
        // Load Then-Actions
        for (Action thenAction : thenActions) {
            then(thenAction);
        }
    }

    /**
     * Loads the @Otherwise action in the given class. A method is considered an Action if takes arbitrary number
     * of arguments and returns nothing (ie: void) and the method is annotated with @Otherwise.
     *
     * @param target rule target.
     */
    protected void loadOtherwiseAction(Object target) {
        Action[] otherwiseActions = ActionBuilder.build(target, Otherwise.class, 1);
        // Load Otherwise-Action
        if (otherwiseActions.length == 1) {
            otherwise(otherwiseActions[0]);
        }
    }
}
