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

import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.lib.spring.util.Assert;
import org.algorithmx.rules.util.reflect.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
     *
     * @param c
     * @return
     */
    public static Method findActionableMethod(Class<?> c) {
        Assert.notNull(c, "c cannot be null");

        if (Modifier.isAbstract(c.getModifiers())) {
            throw new UnrulyException("Actionable classes cannot be abstract [" + c + "]");
        }

        Method[] candidates = ReflectionUtils.getMethodsWithAnnotation(c, org.algorithmx.rules.annotation.Action.class);

        if (candidates == null || candidates.length == 0) {
            return null;
        }

        // Too many Actions declared
        if (candidates.length > 1) {
            throw new UnrulyException("Too many actionable methods found on class [" + c + "]. Candidates ["
                    + Arrays.toString(candidates) + "]");
        }

        Method candidate = candidates[0];

        // Actions do not return anything
        if (!void.class.equals(candidate.getReturnType())) {
            throw new UnrulyException("Actionable methods must return a void. [" + candidate + "]");
        }

        // Found a candidate and but it's not public
        if (!Modifier.isPublic(candidate.getModifiers())) {
            throw new UnrulyException("Actionable methods must be Public. [" + candidate + "]");
        }

        // We found the one
        // TODO : Handle overridden methods with generics
        if (!Modifier.isAbstract(candidate.getModifiers())) return candidate;

        // Looks like we have an abstract method; let's find the implementation
        List<Method> matches = new ArrayList<>();

        for (Method method : c.getMethods()) {
            if (candidate.equals(method)) continue;
            if (method.isBridge()) continue;
            if (!void.class.equals(method.getReturnType())) continue;
            if (!candidate.getName().equals(method.getName())) continue;
            if (method.getParameterCount() != candidate.getParameterCount()) continue;
            if (Modifier.isAbstract(method.getModifiers())) continue;

            matches.add(method);
        }

        return matches.size() == 1 ? matches.get(0) : candidate;
    }
}
