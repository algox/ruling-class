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

package org.algorithmx.rulii.core.function;

import org.algorithmx.rulii.config.RuliiSystem;
import org.algorithmx.rulii.core.UnrulyException;
import org.algorithmx.rulii.core.model.MethodDefinition;
import org.algorithmx.rulii.core.model.ParameterDefinition;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.util.LambdaUtils;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

public abstract class ExecutableBuilder {

    private Object target;
    private MethodDefinition definition;

    protected ExecutableBuilder(Object target, MethodDefinition definition) {
        super();
        Assert.notNull(definition, "actionMethod cannot be null.");
        this.target = target;
        this.definition = definition;
    }

    /**
     * Loads the given target.
     *
     * @param target target object.
     * @param targetMethod target method.
     *
     * @return matched method.
     */
    public static MethodInfo load(Object target, Method targetMethod) {
        Assert.notNull(target, "function cannot be null.");
        Assert.notNull(targetMethod, "targetMethod cannot be null.");

        SerializedLambda serializedLambda = LambdaUtils.getSafeSerializedLambda(target);

        if (serializedLambda != null) {
            return loadLambda(target, targetMethod, serializedLambda);
        }

        // See if we need to find the implementation (so we have can access the parameter names);
        Method implementationMethod = getImplementationMethod(target.getClass(), targetMethod);
        // If this happens we have a bug in getImplementationMethod().
        if (implementationMethod == null) throw new UnrulyException("Unable to find implementation method for [" + targetMethod + "]");

        return new MethodInfo(target, MethodDefinition.load(implementationMethod));
    }

    /**
     * Loads the given functional lambda.
     *
     * @param function target function.
     * @param functionMethod implementation method.
     * @param serializedLambda serialized function.
     * @return matched method.
     */
    protected static MethodInfo loadLambda(Object function, Method functionMethod, SerializedLambda serializedLambda) {
        Assert.notNull(functionMethod, "functionMethod cannot be null.");
        Assert.notNull(serializedLambda, "serializedLambda cannot be null.");
        MethodDefinition methodDefinition = null;

        try {
            Class<?> implementationClass = LambdaUtils.getImplementationClass(serializedLambda);
            Method implementationMethod = LambdaUtils.getImplementationMethod(serializedLambda, implementationClass);
            MethodDefinition implementationMethodDefinition = MethodDefinition.load(implementationMethod);

            ParameterDefinition[] parameterDefinitions = new ParameterDefinition[functionMethod.getParameterCount()];
            int delta = implementationMethod.getParameterCount() - functionMethod.getParameterCount();

            for (int i = delta; i < implementationMethod.getParameterCount(); i++) {
                int index = i - delta;
                parameterDefinitions[index] = implementationMethodDefinition.getParameterDefinition(i);
                parameterDefinitions[index].setIndex(index);
            }

            methodDefinition = new MethodDefinition(functionMethod, implementationMethodDefinition.getOrder(),
                    implementationMethodDefinition.getDescription(), parameterDefinitions);
            methodDefinition.setReturnType(implementationMethod.getGenericReturnType());

        } catch (Exception e) {
            // Log
        }

        if (methodDefinition == null) {
            methodDefinition = MethodDefinition.load(functionMethod);
        }

        return new MethodInfo(function, methodDefinition);
    }

    public Object getTarget() {
        return target;
    }

    public MethodDefinition getDefinition() {
        return definition;
    }

    private static Method getImplementationMethod(Class<?> c, Method candidate) {
        return RuliiSystem.getInstance().getMethodResolver().getImplementationMethod(c, candidate);
    }

    protected static class MethodInfo {
        private Object target;
        private MethodDefinition definition;

        public MethodInfo(Object target, MethodDefinition definition) {
            super();
            this.target = target;
            this.definition = definition;
        }

        public Object getTarget() {
            return target;
        }

        public MethodDefinition getDefinition() {
            return definition;
        }
    }
}
