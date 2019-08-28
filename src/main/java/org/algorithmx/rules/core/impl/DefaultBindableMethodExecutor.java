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
package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.core.BindableMethodExecutor;
import org.algorithmx.rules.model.MethodDefinition;

import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * Default Implementation of BindableMethodExecutor.
 *
 * @author Max Arulananthan
 * @since 1.0
 * @see BindableMethodExecutor
 */
public class DefaultBindableMethodExecutor implements BindableMethodExecutor {

    /**
     * Default Ctor.
     */
    public DefaultBindableMethodExecutor() {
        super();
    }

    @Override
    public <T> T execute(Object target, MethodDefinition definition, Object...userArgs) {

        if (definition.getParameterDefinitions().length != (userArgs == null ? 0 : userArgs.length)) {
            throw new UnrulyException("Invalid number of args passed to Method call [" + definition.getMethod()
                    + "] required [" + definition.getParameterDefinitions().length + "]");
        }

        boolean staticMethod = Modifier.isStatic(definition.getMethod().getModifiers());
        int index = 0;

        // Do no include target if its a static method call
        Object[] args = new Object[staticMethod
                ? definition.getParameterDefinitions().length
                : definition.getParameterDefinitions().length + 1];

        if (!staticMethod) args[index++] = target;

        for (int i = 0; i < userArgs.length; i++) {
            args[index++] = userArgs[i];
        }

        try {
            // Execute the method with the derived parameters
            return (T) definition.getMethodHandle().invokeWithArguments(args);
        } catch (Throwable e) {
            // Something went wrong with the execution
            throw new UnrulyException("Error trying to execute [" + definition.getMethod()
                    + "] with arguments [" + Arrays.toString(args) + "]", e);
        }
    }
}
