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
package org.algorithmx.rules.core.condition;

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Given;
import org.algorithmx.rules.core.model.MethodDefinition;
import org.algorithmx.rules.spring.util.Assert;
import org.algorithmx.rules.util.LambdaUtils;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Indicates the annotated (@Given) method is an Condition.
 *
 * The only requirement for an Condition method is the @Given annotation. The method can take arbitrary number of
 * arguments but must return boolean.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class ConditionDefinition {

    // Condition class
    private final Class<?> conditionClass;
    // given method
    private final MethodDefinition condition;
    // Description of the Condition
    private String description;

    private ConditionDefinition(Class<?> conditionClass, String description, MethodDefinition condition) {
        super();
        Assert.notNull(conditionClass, "condition class cannot be null");
        Assert.notNull(condition, "condition cannot be null");
        this.conditionClass = conditionClass;
        this.description = description;
        this.condition = condition;
    }

    /**
     * Loads the condition in the given class. A method is considered a Condition if takes arbitrary number
     * of arguments and returns a boolean and the method is annotated with @Given.
     *
     * @param c desired class
     * @return ConditionDefinition if found.
     */
    public static ConditionDefinition load(Class<?> c) {
        MethodDefinition[] conditions = MethodDefinition.load(c, (Method method) ->
                boolean.class.equals(method.getReturnType()) && Modifier.isPublic(method.getModifiers())
                        && method.getAnnotation(Given.class) != null);

        Assert.isTrue(!(conditions.length == 0), "Rule Condition method not defined on Rule class ["
                + c + "]. Please define method public boolean someMethod(...) and annotate it @Given.");
        Assert.isTrue(conditions.length == 1, "Multiple methods annotated with @Given [" + conditions
                + "] defined on Rule class [" + c + "]. Please define only a single method with @Given. public boolean "
                + "someMethod(...)");

        Description descriptionAnnotation = conditions[0].getMethod().getAnnotation(Description.class);
        return new ConditionDefinition(c, descriptionAnnotation != null ? descriptionAnnotation.value() : null,
                    conditions[0]);
    }

    /**
     * Loads the condition in the given Lambda. A method is considered an Condition if takes arbitrary number
     * of arguments and returns boolean.
     *
     * @param lambda supplied lambda.
     * @param description condition description.
     * @return ConditionDefinition.
     */
    public static ConditionDefinition load(SerializedLambda lambda, String description) {
        Class<?> implementationClass = LambdaUtils.getImplementationClass(lambda);
        Assert.notNull(implementationClass, "implementationClass cannot be null");
        Method implementationMethod = LambdaUtils.getImplementationMethod(lambda, implementationClass);
        Assert.notNull(implementationMethod, "implementationMethod cannot be null");
        Assert.isTrue(boolean.class.equals(implementationMethod.getReturnType()),
                "Condition Lambda not defined correctly. Please define method public void then(...)");
        MethodDefinition[] conditions = MethodDefinition.load(implementationClass, implementationMethod);
        return new ConditionDefinition(implementationClass, description, conditions[0]);
    }

    public Class<?> getConditionClass() {
        return conditionClass;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MethodDefinition getMethodDefinition() {
        return condition;
    }

    /**
     * Determines if the Condition is a statically implemented method call (such as a lambda).
     *
     * @return true if statically implemented; false otherwise.
     */
    public boolean isStatic() {
        return condition.isStatic();
    }

    @Override
    public String toString() {
        return "ConditionDefinition{" +
                "conditionClass=" + conditionClass +
                ", description='" + description + '\'' +
                ", condition=" + condition +
                '}';
    }
}
