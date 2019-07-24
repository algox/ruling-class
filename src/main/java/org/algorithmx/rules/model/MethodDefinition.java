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
package org.algorithmx.rules.model;

import org.algorithmx.rules.spring.util.Assert;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.function.Predicate;

/**
 * Defines a method is to be run dynamically (such as "when" and "then")
 *
 * The definition stores the reflective method and its assocated parameter definitions.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public final class MethodDefinition {

    private final Method method;
    private final MethodHandle methodHandle;
    private final ParameterDefinition[] parameterDefinitions;

    private MethodDefinition(Method method, MethodHandle methodHandle, ParameterDefinition...parameterDefinitions) {
        super();
        Assert.notNull(method, "method cannot be null");
        Assert.notNull(method, "methodHandle cannot be null");

        this.method = method;
        this.methodHandle = methodHandle;
        this.parameterDefinitions = parameterDefinitions;
    }

    /**
     * Loads all the associated method definition that match the given predicate.
     *
     * @param c container class.
     * @param predicate matcher.
     * @return all matching MethodDefinitions
     */
    public static MethodDefinition[] load(Class<?> c, Predicate<Method> predicate) {
        Method[] matches = Arrays.stream(c.getDeclaredMethods())
                .filter(predicate)
                .toArray(size -> new Method[size]);
        return load(c, matches);
    }

    /**
     * Loads all the associated method definition for the given methods.
     *
     * @param c container class.
     * @param methods desired methods.
     * @return MethodDefinitions.
     */
    public static MethodDefinition[] load(Class<?> c, Method...methods) {
        MethodDefinition[] result = new MethodDefinition[methods.length];
        MethodHandles.Lookup lookup = MethodHandles.lookup().in(c);

        for (int i = 0; i < methods.length; i++) {
            Method match = methods[i];
            match.setAccessible(true);

            try {
                result[i] = new MethodDefinition(match, lookup.unreflect(match), ParameterDefinition.load(methods[i]));
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Unable to getBinding MethodHandle for method [" + match + "]", e);
            }
        }

        return result;
    }

    /**
     * Reflective method behind the Method Definition.
     *
     * @return reflective method.
     */
    public Method getMethod() {
        return method;
    }

    /**
     * All the parameter definitions for this method definition.
     *
     * @return parameter meta information.
     */
    public ParameterDefinition[] getParameterDefinitions() {
        return parameterDefinitions;
    }

    /**
     * Method Handle for optimized execution of the method at runtime.
     *
     * @return method handle.
     */
    public MethodHandle getMethodHandle() {
        return methodHandle;
    }

    /**
     * Determines whether the method is static.
     *
     * @return return true if its a static method; false otherwise.
     */
    public boolean isStatic() {
        return Modifier.isStatic(method.getModifiers());
    }

    @Override
    public String toString() {
        return "MethodDefinition{" +
                "method=" + method +
                ", methodHandle=" + methodHandle +
                ", parameterDefinitions=" + Arrays.toString(parameterDefinitions) +
                '}';
    }
}
