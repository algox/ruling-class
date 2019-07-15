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
package org.algorithmx.rules.core;

import org.algorithmx.rules.annotation.Action;
import org.algorithmx.rules.spring.util.Assert;
import org.algorithmx.rules.types.ActionType;
import org.algorithmx.rules.util.LambdaUtils;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

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
    private final Class actionClass;
    // Action Type;
    private final ActionType actionType;
    // Description of the Action
    private final String description;
    // Execution Order
    private final Integer order;
    // then method
    private final MethodDefinition action;

    private ActionDefinition(Class actionClass, ActionType actionType, String description, int order, MethodDefinition action) {
        super();
        Assert.notNull(actionClass, "action class cannot be null");
        Assert.notNull(action, "action cannot be null");

        this.actionClass = actionClass;
        this.actionType = actionType;
        this.description = description;
        this.order = order;
        this.action = action;
    }

    /**
     * Loads all the actions in the given class. A method is considered an Action if takes arbitrary number
     * of arguments and returns nothing (ie: void) and the method is annotated with @Action.
     *
     * @param c desired class
     * @return all the associated actions
     */
    public static ActionDefinition[] load(Class c) {
        MethodDefinition[] actions = MethodDefinition.load(c, (Method method) ->
                void.class.equals(method.getReturnType()) && Modifier.isPublic(method.getModifiers())
                        && method.getAnnotation(Action.class) != null);

        ActionDefinition[] result = new ActionDefinition[actions.length];

        for (int i = 0; i < actions.length; i++) {
            Action action = actions[i].getMethod().getAnnotation(Action.class);
            result[i] = new ActionDefinition(c, action.type(), action.description(), action.order(), actions[i]);
        }

        // Sort by order
        Arrays.sort(result);

        return result;
    }

    /**
     * Loads the action in the given Lambda. A method is considered an Action if takes arbitrary number
     * of arguments and returns nothing (ie: void) and the method name is "then".
     *
     * @param lambda supplied lambda.
     * @param description action description.
     * @param order order of the action (default 0).
     * @return ActionDefinition.
     */
    public static ActionDefinition load(SerializedLambda lambda, ActionType actionType, String description, Integer order) {
        Class implementationClass = LambdaUtils.getImplementationClass(lambda);
        Assert.notNull(implementationClass, "implementationClass cannot be null");
        Method implementationMethod = LambdaUtils.getImplementationMethod(lambda, implementationClass);
        Assert.notNull(implementationMethod, "implementationMethod cannot be null");
        Assert.isTrue(void.class.equals(implementationMethod.getReturnType()),
                "Action Lambda not defined correctly. Please define method public void then(...)");

        MethodDefinition[] actions = MethodDefinition.load(implementationClass, implementationMethod);

        return new ActionDefinition(implementationClass, actionType == null ? ActionType.ON_PASS : actionType,
                description, order == null ? 0 : order, actions[0]);
    }

    @Override
    public int compareTo(ActionDefinition o) {
        return order.compareTo(o.order);
    }

    public Class getActionClass() {
        return actionClass;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public String getDescription() {
        return description;
    }

    public int getOrder() {
        return order;
    }

    public MethodDefinition getAction() {
        return action;
    }

    @Override
    public String toString() {
        return "ActionDefinition{" +
                "actionClass=" + actionClass +
                ", actionType=" + actionType +
                ", description='" + description + '\'' +
                ", order=" + order +
                ", action=" + action +
                '}';
    }
}
