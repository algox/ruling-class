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

import org.algorithmx.rules.core.Action;
import org.algorithmx.rules.core.Then;
import org.algorithmx.rules.core.impl.SimpleAction;
import org.algorithmx.rules.model.ActionDefinition;

import java.lang.invoke.SerializedLambda;

/**
 * Utility class to provide convenience methods for Actions.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public final class ActionUtils {

    private ActionUtils() {
        super();
    }

    /**
     * Loads a new ActionDefinition from the given Action Lambda and Description.
     *
     * @param lambda then action lambda.
     * @param description Action Description.
     * @return new Action Definition.
     */
    public static ActionDefinition load(Then lambda, String description) {
        SerializedLambda serializedLambda = LambdaUtils.getSerializedLambda(lambda);
        return ActionDefinition.load(serializedLambda, description);
    }

    public static Action create(ActionDefinition actionDefinition, Object target) {
        return new SimpleAction(actionDefinition, target);
    }

    public static Action create(Then lambda, String description, Object target) {
        ActionDefinition actionDefinition = load(lambda, description);
        return create(actionDefinition, target);
    }
}
