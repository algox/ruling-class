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

import org.algorithmx.rules.core.model.MethodDefinition;
import org.algorithmx.rules.lib.spring.util.Assert;
import org.algorithmx.rules.util.RuleUtils;

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
    private final Class<?> ruleClass;
    // PreCondition method details
    private final MethodDefinition preConditionDefinition;
    // Given method details
    private final MethodDefinition conditionDefinition;
    // Associated Then actions
    private MethodDefinition[] thenActionDefinitions;
    // Otherwise action
    private MethodDefinition otherwiseActionDefinition;

    // Name of the Rule
    private String name;
    // Description of the Rule
    private String description;

    /**
     * Creates a RuleDefinition taking in all the required parameters.
     *
     * @param ruleClass Rule implementation class.
     * @param name Rule name.
     * @param description Rule description.
     * @param preConditionDefinition Pre-Condition meta information.
     * @param conditionDefinition Given condition meta information.
     * @param thenActionDefinitions Then Action(s) meta information.
     * @param otherwiseActionDefinition Otherwise Action meta information.
     */
    public RuleDefinition(Class<?> ruleClass, String name, String description,
                          MethodDefinition preConditionDefinition,
                          MethodDefinition conditionDefinition,
                          MethodDefinition[] thenActionDefinitions,
                          MethodDefinition otherwiseActionDefinition) {
        super();
        Assert.notNull(ruleClass, "Rule class cannot be null.");
        Assert.notNull(conditionDefinition, "conditionDefinition cannot be null.");
        this.ruleClass = ruleClass;
        this.description = description;
        this.preConditionDefinition = preConditionDefinition;
        this.conditionDefinition = conditionDefinition;
        this.thenActionDefinitions = thenActionDefinitions;
        this.otherwiseActionDefinition = otherwiseActionDefinition;
        setName(name);
    }

    /**
     * The implementing Rule class.
     *
     * @return Rule class.
     */
    public Class<?> getRuleClass() {
        return ruleClass;
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

    public void setName(String name) {
        Assert.isTrue(RuleUtils.isValidName(name), "Rule name must match ["
                + RuleUtils.NAME_REGEX + "] Given [" + name + "]");
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MethodDefinition getPreConditionDefinition() {
        return preConditionDefinition;
    }

    /**
     * Condition details.
     *
     * @return meta information rule implementing method.
     */
    public MethodDefinition getConditionDefinition() {
        return conditionDefinition;
    }

    /**
     * Then Action details.
     *
     * @return meta information about the associated actions.
     */
    public MethodDefinition[] getThenActionDefinitions() {
        return thenActionDefinitions;
    }

    /**
     * Otherwise Action details.
     *
     * @return meta information about the otherwise action.
     */
    public MethodDefinition getOtherwiseActionDefinition() {
        return otherwiseActionDefinition;
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
        return ruleClass.equals(that.ruleClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ruleClass);
    }

    @Override
    public String toString() {
        return "RuleDefinition{" +
                "ruleClass=" + ruleClass +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", conditionDefinition=" + conditionDefinition +
                '}';
    }
}
