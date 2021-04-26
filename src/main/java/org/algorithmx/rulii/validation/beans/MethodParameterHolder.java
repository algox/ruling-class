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

import org.algorithmx.rulii.core.model.ParameterDefinition;
import org.algorithmx.rulii.validation.types.AnnotatedTypeDefinition;

import java.lang.reflect.Method;

public class MethodParameterHolder implements SourceHolder<Method> {

    private final Method method;
    private final ParameterDefinition parameterDefinition;
    private final AnnotatedTypeDefinition typeDefinition;
    private final Object value;

    public MethodParameterHolder(Method method, ParameterDefinition parameterDefinition,
                                 AnnotatedTypeDefinition typeDefinition, Object value) {
        super();
        this.method = method;
        this.parameterDefinition = parameterDefinition;
        this.typeDefinition = typeDefinition;
        this.value = value;
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
        return parameterDefinition.getName();
    }

    @Override
    public SourceHolder<Method> copy(AnnotatedTypeDefinition definition) {
        return new MethodParameterHolder(method, parameterDefinition, definition, value);
    }

    @Override
    public Object getValue(Object target) {
        return value;
    }

    public Method getMethod() {
        return method;
    }

    public ParameterDefinition getParameterDefinition() {
        return parameterDefinition;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "MethodParameterHolder{" +
                "method=" + method +
                ", parameterDefinition=" + parameterDefinition +
                ", typeDefinition=" + typeDefinition +
                ", value=" + value +
                '}';
    }
}
