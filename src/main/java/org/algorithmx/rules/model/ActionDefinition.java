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
import org.algorithmx.rules.spring.util.Assert;
import org.algorithmx.rules.util.LambdaUtils;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Indicates the annotated (@Action) method is an Action and is eligible to be execute based on the result
 * of the associated Rule. An Action is always associated to a Rule and does not exist ow its own.
 *
 * The only requirement for an Action method is the @Action annotation.The method can take arbitrary number of
 * arguments but must return nothing (aka : void).
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public final class ActionDefinition {

    public static final String THEN_METHOD_NAME = "then";

    // Action class
    private final Class<?> actionClass;
    // Description of the Action
    private final String description;
    // then method
    private final MethodDefinition action;

    private ActionDefinition(Class<?> actionClass, String description, MethodDefinition action) {
        super();
        Assert.notNull(actionClass, "action class cannot be null");
        Assert.notNull(action, "action cannot be null");

        this.actionClass = actionClass;
        this.description = description;
        this.action = action;
    }

    /**
     * Loads all the actions in the given class. A method is considered an Action if takes arbitrary number
     * of arguments and returns nothing (ie: void) and the method is annotated with @Action.
     *
     * @param c desired class
     * @return all the associated actions
     */
    public static ActionDefinition load(Class<?> c) {
        MethodDefinition[] actions = MethodDefinition.load(c, (Method method) ->
                void.class.equals(method.getReturnType()) && Modifier.isPublic(method.getModifiers())
                        && THEN_METHOD_NAME.equals(method.getName()));

        Assert.isTrue(!(actions.length == 0), "Action Method " + THEN_METHOD_NAME + " not defined on class ["
                + c + "]. Please define method public void " + THEN_METHOD_NAME + "(...)");
        Assert.isTrue(actions.length == 1, "multiple "+ THEN_METHOD_NAME + " methods " + actions
                + "] defined on class [" + c + "]. Please define only a single "+ THEN_METHOD_NAME
                + " method public void " + THEN_METHOD_NAME + "(...)");

        Description descriptionAnnotation = actions[0].getMethod().getAnnotation(Description.class);
        return new ActionDefinition(c, descriptionAnnotation != null ? descriptionAnnotation.value() : null, actions[0]);
    }

    /**
     * Loads the action in the given Lambda. A method is considered an Action if takes arbitrary number
     * of arguments and returns nothing (ie: void) and the method name is "then".
     *
     * @param lambda supplied lambda.
     * @param description action description.
     * @return ActionDefinition.
     */
    public static ActionDefinition load(SerializedLambda lambda, String description) {
        Class<?> implementationClass = LambdaUtils.getImplementationClass(lambda);
        Assert.notNull(implementationClass, "implementationClass cannot be null");
        Method implementationMethod = LambdaUtils.getImplementationMethod(lambda, implementationClass);
        Assert.notNull(implementationMethod, "implementationMethod cannot be null");
        Assert.isTrue(void.class.equals(implementationMethod.getReturnType()),
                "Action Lambda not defined correctly. Please define method public void then(...)");

        MethodDefinition[] actions = MethodDefinition.load(implementationClass, implementationMethod);
        return new ActionDefinition(implementationClass, description, actions[0]);
    }

    public Class<?> getActionClass() {
        return actionClass;
    }

    public String getDescription() {
        return description;
    }

    public MethodDefinition getAction() {
        return action;
    }

    /**
     * Determines if the action is a statically implemented method call (such as a lambda).
     *
     * @return true if statically implemented; false otherwise.
     */
    public boolean isStatic() {
        return action.isStatic();
    }

    @Override
    public String toString() {
        return "ActionDefinition{" +
                "actionClass=" + actionClass +
                ", description='" + description + '\'' +
                ", action=" + action +
                '}';
    }
}
