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
import org.algorithmx.rules.util.RuleUtils;

import java.util.Objects;
import java.util.regex.Pattern;

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

    private static final Pattern NAME_PATTERN   = Pattern.compile(org.algorithmx.rules.core.Rule.NAME_REGEX);

    // Rule class
    private final Class<?> ruleClass;
    // Given method details
    private final ConditionDefinition conditionDefinition;
    // Associated Then actions
    private ActionDefinition[] thenActionDefinitions;
    // Else action
    private ActionDefinition elseActionDefinition;

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
     * @param conditionDefinition Given condition meta information.
     * @param thenActionDefinitions Then Action(s) meta information.
     * @param elseActionDefinition Otherwise Action meta information.
     */
    public RuleDefinition(Class<?> ruleClass, String name, String description,
                          ConditionDefinition conditionDefinition,
                          ActionDefinition[] thenActionDefinitions,
                          ActionDefinition elseActionDefinition) {
        super();
        Assert.notNull(ruleClass, "Rule class cannot be null.");
        Assert.isTrue(name.trim().length() > 0, "name length must be > 0");
        Assert.isTrue(NAME_PATTERN.matcher(name).matches(), "Rule name must match [" + NAME_PATTERN + "]");
        Assert.notNull(conditionDefinition, "conditionDefinition cannot be null.");
        this.ruleClass = ruleClass;
        this.description = description;
        this.conditionDefinition = conditionDefinition;
        this.thenActionDefinitions = thenActionDefinitions;
        this.elseActionDefinition = elseActionDefinition;
        setName(name);
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
                : null, ConditionDefinition.load(c), ActionDefinition.loadThenActions(c), ActionDefinition.loadElseActions(c));
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
        Assert.isTrue(name == null || name.trim().length() > 0, "name length must be > 0");
        Assert.isTrue(name == null || RuleUtils.isValidRuleName(name), "Rule name must match ["
                + RuleUtils.RULE_NAME_REGEX + "] Given [" + name + "]");
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
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
     * Then Action details.
     *
     * @return meta information about the associated actions.
     */
    public ActionDefinition[] getThenActionDefinitions() {
        return thenActionDefinitions;
    }

    /**
     * Otherwise Action details.
     *
     * @return meta information about the otherwise action.
     */
    public ActionDefinition getElseActionDefinition() {
        return elseActionDefinition;
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
