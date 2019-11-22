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
package org.algorithmx.rules.model;

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.spring.util.Assert;
import org.algorithmx.rules.util.LambdaUtils;
import org.algorithmx.rules.util.RuleUtils;

import java.lang.invoke.SerializedLambda;
import java.util.Objects;

/**
 * Indicates the class with this annotation is Rule and it will follow the "rules" of a being a Rule.
 *
 * The only requirement for a class to be considered a Rule is to have a "when" method (aka Condition in standard Rule terms).
 * The when method can take arbitrary number of arguments but must return a boolean value. The boolean is the result of
 * the conditionDefinition of the rule.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public final class RuleDefinition {

    // Rule class
    private final Class<?> rulingClass;
    // Name of the Rule
    private final String name;
    // Description of the Rule
    private final String description;
    // when method
    //private final MethodDefinition conditionDefinition;
    private final ConditionDefinition conditionDefinition;

    public RuleDefinition(Class<?> rulingClass, String name, String description, ConditionDefinition conditionDefinition) {
        super();
        Assert.notNull(rulingClass, "Rule class cannot be null.");
        Assert.isTrue(name == null || name.trim().length() > 0, "name length must be > 0");
        Assert.isTrue(name == null || RuleUtils.isValidRuleName(name), "Rule name must match ["
                + RuleUtils.RULE_NAME_REGEX + "] Given [" + name + "]");
        this.rulingClass = rulingClass;
        this.name = name;
        this.description = description;
        this.conditionDefinition = conditionDefinition;
    }

    /**
     * Loads the given Rule class. The Rule class must be annotated with @Rule and must define a single "when" method
     * which returns a boolean. The when method can take a arbitrary number of arguments.
     *
     * @param c desired Rule class.
     * @return RuleDefinition of the supplied Rule class.
     */
    public static RuleDefinition load(Class<?> c) {
        // Try and locate the Rule annotation on the class
        Rule rule = c.getAnnotation(Rule.class);
        // Looks like the class isn't annotated with @Rule
        Assert.notNull(rule, "Desired Rule class [" + c.getName() + "] is not annotated with @Rule");

        String ruleName = Rule.NOT_APPLICABLE.equals(rule.name()) ? c.getSimpleName() : rule.name();
        Description descriptionAnnotation = c.getAnnotation(Description.class);
        return new RuleDefinition(c, ruleName, descriptionAnnotation != null
                ? descriptionAnnotation.value()
                : null, ConditionDefinition.load(c));
    }

    /**
     * Loads a Rule Definition from a Lambda. The Lambda must must define a single "when" method which
     * returns a boolean. The when method can take a arbitrary number of arguments.
     *
     * @param lambda Rule Lambda expression.
     * @param ruleName name of the rule.
     * @param ruleDescription description of the rule.
     * @return RuleDefinition of the supplied Lambda.
     */
    public static RuleDefinition load(SerializedLambda lambda, String ruleName, String ruleDescription) {
        Class<?> implementationClass = LambdaUtils.getImplementationClass(lambda);
        return new RuleDefinition(implementationClass, ruleName, ruleDescription,
                ConditionDefinition.load(lambda, null));
    }

    /**
     * The implementing Rule class.
     *
     * @return Rule class.
     */
    public Class<?> getRulingClass() {
        return rulingClass;
    }

    /**
     * Name of the Rule.
     *
     * @return name of rule. If not specified the simple class name is used.
     */
    public String getName() {
        return name;
    }

    /**
     * Rule description.
     *
     * @return description of what the rule does.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Condition details.
     *
     * @return meta information rule implementing method.
     */
    public ConditionDefinition getConditionDefinition() {
        return conditionDefinition;
    }

    /**
     * Determines if the conditionDefinition is a statically implemented method call (such as a lambda).
     *
     * @return true if statically implemented; false otherwise.
     */
    public boolean isStatic() {
        return conditionDefinition.isStatic();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuleDefinition that = (RuleDefinition) o;
        return rulingClass.equals(that.rulingClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rulingClass);
    }

    @Override
    public String toString() {
        return "RuleDefinition{" +
                "rulingClass=" + rulingClass +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", conditionDefinition=" + conditionDefinition +
                '}';
    }
}
