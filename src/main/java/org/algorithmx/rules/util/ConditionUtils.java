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
package org.algorithmx.rules.util;

import org.algorithmx.rules.core.Condition;
import org.algorithmx.rules.core.ConditionConsumer;
import org.algorithmx.rules.core.impl.DefaultCondition;
import org.algorithmx.rules.model.ConditionDefinition;

import java.lang.invoke.SerializedLambda;

/**
 * Utility class to provide convenience methods for Conditions.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public final class ConditionUtils {

    private ConditionUtils() {
        super();
    }

    /**
     * Loads a new ConditionDefinition from the given Condition Lambda and Description.
     *
     * @param consumer then Condition lambda.
     * @param description Condition Description.
     * @return new Condition Definition.
     */
    public static ConditionDefinition load(ConditionConsumer consumer, String description) {
        SerializedLambda serializedLambda = LambdaUtils.getSerializedLambda(consumer);
        return ConditionDefinition.load(serializedLambda, description);
    }

    /**
     * Creates a new Condition from the given ConditionDefinition and the target object.
     *
     * @param conditionDefinition condition definition.
     * @param target target object.
     * @return Condition Object.
     */
    public static Condition create(ConditionDefinition conditionDefinition, Object target) {
        return new DefaultCondition(conditionDefinition, target);
    }

    /**
     * Creates a new Condition from the given ConditionConsumer lambda and the target object.
     *
     * @param lambda ConditionConsumer lambda.
     * @param description Condition description.
     * @param target target object.
     * @return Condition Object.
     */
    public static Condition create(ConditionConsumer lambda, String description, Object target) {
        ConditionDefinition conditionDefinition = load(lambda, description);
        return create(conditionDefinition, target);
    }
}
