/**
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 2019, algorithmx.org (dev@algorithmx.org)
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

import org.algorithmx.rules.error.UnrulyException;
import org.algorithmx.rules.model.ActionDefinition;

/**
 * Action that associated with a Rule Condition. A Rule Action is executed when the Rule Condition it true.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public interface Action {

    /**
     * Executes thr Action given all the arguments it needs.
     *
     * @param params Action parameters in necessary order.
     * @throws UnrulyException thrown if there are any runtime errors during the execution.
     */
    void execute(Object...params);

    /**
     * Meta information about the Action.
     *
     * @return Action meta information.
     */

    ActionDefinition getActionDefinition();

    /**
     * The actual target instance the Action is associated to (usually a Rule).
     *
     * @return target instance.
     */
    Object getTarget();
}
