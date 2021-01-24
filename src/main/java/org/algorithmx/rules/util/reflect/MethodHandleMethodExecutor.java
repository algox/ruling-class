/**
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 1999-2021, Algorithmx Inc.
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

import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.lib.spring.util.Assert;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class MethodHandleMethodExecutor implements MethodExecutor {

    private final Method method;
    private final MethodHandle methodHandle;

    public MethodHandleMethodExecutor(Method method) throws IllegalAccessException {
        super();
        Assert.notNull(method, "method cannot be null.");
        this.methodHandle = ReflectionUtils.getMethodHandle(method);
        this.method = method;
    }

    @Override
    public <T> T execute(Object target, Object... userArgs) {
        if (method.getParameterCount() != (userArgs == null ? 0 : userArgs.length)) {
            throw new UnrulyException("Invalid number of args passed to Method call [" + getMethod()
                    + "] required [" + method.getParameterCount() + "]");
        }

        boolean staticMethod = Modifier.isStatic(getMethod().getModifiers());
        int index = 0;

        // Do no include target if its a static method call
        Object[] args = new Object[staticMethod
                ? method.getParameterCount()
                : method.getParameterCount() + 1];

        if (!staticMethod) args[index++] = target;

        for (int i = 0; i < userArgs.length; i++) {
            args[index++] = userArgs[i];
        }

        try {
            // Execute the method with the derived parameters
            return (T) methodHandle.invokeWithArguments(args);
        } catch (Throwable e) {
            // Something went wrong with the execution
            throw new UnrulyException("Unexpected error trying to execute [" + getMethod()
                    + "] with arguments " + Arrays.toString(args), e);
        }
    }

    @Override
    public final Method getMethod() {
        return method;
    }
}
