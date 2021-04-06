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

package org.algorithmx.rulii.validation.beans;

import org.algorithmx.rulii.validation.types.AnnotatedTypeDefinition;

import java.lang.reflect.Method;
import java.util.Arrays;

public class MethodHolder implements SourceHolder<Method> {

    private Method method;
    private AnnotatedTypeDefinition typeDefinition;
    private AnnotatedTypeDefinition[] parameterDefinitions;

    public MethodHolder(Method method, AnnotatedTypeDefinition typeDefinition) {
        super();
        this.method = method;
        this.typeDefinition = typeDefinition;
        this.parameterDefinitions = new AnnotatedTypeDefinition[method.getParameterCount()];
    }

    @Override
    public Method getSource() {
        return method;
    }

    @Override
    public AnnotatedTypeDefinition getDefinition() {
        return typeDefinition;
    }

    @Override
    public String getName() {
        return method.getDeclaringClass().getSimpleName() + "." + method.getName();
    }

    @Override
    public Object getValue(Object target) {
        return null;
    }

    @Override
    public SourceHolder<Method> copy(AnnotatedTypeDefinition definition) {
        return new MethodHolder(method, definition);
    }

    public AnnotatedTypeDefinition getParameterTypeDefinition(int index) {
        return parameterDefinitions[index];
    }

    public void setParameter(int index, AnnotatedTypeDefinition definition) {
        this.parameterDefinitions[index] = definition;
    }

    @Override
    public String toString() {
        return "MethodHolder{" +
                "method=" + method +
                ", typeDefinition=" + typeDefinition +
                ", parameterDefinitions=" + Arrays.toString(parameterDefinitions) +
                '}';
    }
}