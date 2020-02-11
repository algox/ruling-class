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
import org.algorithmx.rules.annotation.Otherwise;
import org.algorithmx.rules.annotation.Then;
import org.algorithmx.rules.spring.util.Assert;
import org.algorithmx.rules.util.LambdaUtils;

import java.lang.annotation.Annotation;
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
public final class ActionDefinition implements Comparable<ActionDefinition> {

    // Action class
    private final Class<?> actionClass;
    // then method
    private final MethodDefinition action;
    // Description of the Action
    private String description;

    private ActionDefinition(Class<?> actionClass, String description, MethodDefinition action) {
        super();
        Assert.notNull(actionClass, "action class cannot be null");
        Assert.notNull(action, "action cannot be null");
        this.actionClass = actionClass;
        this.description = description;
        this.action = action;
    }

    /**
     * Loads all the ActionConsumer actions in the given class. A method is considered an Action if takes arbitrary number
     * of arguments and returns nothing (ie: void) and the method is annotated with @ActionConsumer.
     *
     * @param c desired class
     * @return all the associated actions
     */
    public static ActionDefinition[] loadThenActions(Class<?> c) {
        return load(c, Then.class);
    }

    /**
     * Loads all the ActionConsumer actions in the given class. A method is considered an Action if takes arbitrary number
     * of arguments and returns nothing (ie: void) and the method is annotated with @Otherwise.
     *
     * @param c desired class
     * @return all the associated actions
     */
    public static ActionDefinition[] loadElseActions(Class<?> c) {
        return load(c, Otherwise.class);
    }

    /**
     * Loads all the actions in the given class. A method is considered an Action if takes arbitrary number
     * of arguments and returns nothing (ie: void) and the method is annotated with given annotation.
     *
     * @param c desired class
     * @param annotationClass desired Annotation Class.
     * @param <T> Annotation Type.
     * @return all the associated actions
     */
    private static <T extends Annotation> ActionDefinition[] load(Class<?> c, Class<T> annotationClass) {
        MethodDefinition[] actions = MethodDefinition.load(c, (Method method) ->
                void.class.equals(method.getReturnType()) && Modifier.isPublic(method.getModifiers())
                        && method.getAnnotation(annotationClass) != null);
        if (actions == null || actions.length == 0) return null;

        ActionDefinition[] result = new ActionDefinition[actions.length];

        for (int i = 0; i < result.length; i++) {
            Description descriptionAnnotation = actions[i].getMethod().getAnnotation(Description.class);
            result[i] = new ActionDefinition(c, descriptionAnnotation != null ? descriptionAnnotation.value() : null,
                    actions[i]);
        }

        return result;
    }

    /**
     * Loads the action in the given Lambda. A method is considered an Action if takes arbitrary number
     * of arguments and returns nothing (ie: void).
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

    @Override
    public int compareTo(ActionDefinition other) {
        Integer parameterCount = action.getMethod().getParameterCount();
        return parameterCount.compareTo(other.action.getMethod().getParameterCount());
    }

    public Class<?> getActionClass() {
        return actionClass;
    }

    public String getActionName() {
        return action.getMethod().getName();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MethodDefinition getMethodDefinition() {
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
                "actionName=" + getActionName() +
                ", description='" + description + '\'' +
                ", action=" + action +
                '}';
    }
}
