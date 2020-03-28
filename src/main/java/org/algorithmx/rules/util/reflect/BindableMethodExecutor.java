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
package org.algorithmx.rules.util.reflect;

import org.algorithmx.rules.model.MethodDefinition;

/**
 * Method executor responsible for executing methods with Bindable arguments. Here are the sequence of steps:
 *
 * First resolve all the parameters (as per the MethodDefinition) and Bind values to them using the given matching strategy.
 * Validate and make sure all required params are met
 * Execute the BindableMethod with the derived args from above
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public interface BindableMethodExecutor {

    /**
     * Creates the default implementation of BindableMethodExecutor.
     *
     * @return default BindableMethodExecutor implementation.
     */
    static BindableMethodExecutor defaultBindableMethodExecutor() {
        return new DefaultBindableMethodExecutor();
    }

    /**
     *  Executes the BindableMethod and returns its result.
     *
     * @param target target object.
     * @param definition definition of the method being executed.
     * @param userArgs method arguments.
     * @param <T> generic type of the result.
     * @return the result of the execution.
     */
    <T> T execute(Object target, MethodDefinition definition, Object...userArgs);
}
