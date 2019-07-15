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
package org.algorithmx.rules.annotation;

import org.algorithmx.rules.types.ActionType;

import java.lang.annotation.*;

/**
 * Indicates the class with this annotation is an Action and it will follow the rules of a being an Action.
 *
 * The only requirement for a class to be considered an Action is to have a public "then" method.
 * The then method can take arbitrary number of arguments but must return nothing (void).
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Action {

    String NOT_APPLICABLE = "";

    /**
     * Determines when the action will be executed. The default value is on ON_PASS of the condition.
     *
     * ON_PASS - Default. Only executed when the condition passes.
     * ON_ANY - Action will be executed always.
     * ON_FAIL - Only executed when the condition fails.
     * ON_PASS_OR_FAIL - Only executed when the condition passes or fails (ie: no error)
     * ON_ERROR - Only executed when there was an error.
     *
     * @return action type.
     */
    ActionType type() default ActionType.ON_PASS;

    /**
     * Text describing what the Rule does.
     *
     * @return Description of the Rule.
     */
    String description() default NOT_APPLICABLE;

    /**
     * Priority order of the action. Default there is no order (ie: all are zero)
     *
     * @return priority order.
     */
    int order() default 0;
}
