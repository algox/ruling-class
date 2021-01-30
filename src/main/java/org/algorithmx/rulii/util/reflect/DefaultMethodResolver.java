/*
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

package org.algorithmx.rulii.util.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class DefaultMethodResolver implements MethodResolver {

    public DefaultMethodResolver() {
        super();
    }

    @Override
    public Method getImplementationMethod(Class<?> c, Method candidate) {
        // We found the one
        if (!Modifier.isAbstract(candidate.getModifiers())) return candidate;

        // Looks like we have an abstract method; let's find the implementation
        List<Method> matches = new ArrayList<>();

        for (Method method : c.getMethods()) {
            if (candidate.equals(method)) continue;
            if (Modifier.isAbstract(method.getModifiers())) continue;
            if (method.isBridge() || method.isSynthetic()) continue;
            if (!candidate.getReturnType().isAssignableFrom(method.getReturnType())) continue;
            if (!candidate.getName().equals(method.getName())) continue;
            if (method.getParameterCount() != candidate.getParameterCount()) continue;

            boolean match = true;

            for (int i = 0; i < method.getParameterTypes().length; i++) {
                if (!candidate.getParameterTypes()[i].isAssignableFrom(method.getParameterTypes()[i]))
                    match = false;
                break;
            }

            if (match) matches.add(method);
        }

        /**
         * TODO : Currently this does not do an exact match of the implementation with the all generic types of the implementing
         * TODO : interface. We should really match the generic types declared on the interface with the method.
         **/
        return matches.size() >= 1 ? matches.get(0) : null;
    }
}
