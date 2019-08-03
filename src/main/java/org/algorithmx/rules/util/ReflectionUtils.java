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

import org.algorithmx.rules.spring.core.DefaultParameterNameDiscoverer;
import org.algorithmx.rules.spring.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;

/**
 * Reflection related utility methods.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public final class ReflectionUtils {

    private static final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    private ReflectionUtils() {
        super();
    }

    /**
     * Retrieves the method parameter names. First it looks for @Param to get the parameter information.
     * If that failed then we use standard Java Reflection techniques to retrieve the information and If that fails
     * we use ASM to load the bytecode to find the method information.
     *
     * @param method target method
     * @return method parameter names.
     */
    public static String[] getParameterNames(Method method) {
        return parameterNameDiscoverer.getParameterNames(method);
    }
}
