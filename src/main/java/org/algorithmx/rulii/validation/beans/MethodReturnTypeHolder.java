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

import org.algorithmx.rulii.core.model.ReturnTypeDefinition;
import org.algorithmx.rulii.validation.types.AnnotatedTypeDefinition;

import java.lang.reflect.Method;

public class MethodReturnTypeHolder implements SourceHolder<Method> {

    private final Method method;
    private final ReturnTypeDefinition returnTypeDefinition;
    private final AnnotatedTypeDefinition typeDefinition;
    private final Object value;

    public MethodReturnTypeHolder(Method method, ReturnTypeDefinition returnTypeDefinition,
                                  AnnotatedTypeDefinition typeDefinition, Object value) {
        super();
        this.method = method;
        this.returnTypeDefinition = returnTypeDefinition;
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
        return "result";
    }

    @Override
    public SourceHolder<Method> copy(AnnotatedTypeDefinition definition) {
        return new MethodReturnTypeHolder(method, returnTypeDefinition, definition, value);
    }

    @Override
    public Object getValue(Object target) {
        return value;
    }

    public Method getMethod() {
        return method;
    }

    public ReturnTypeDefinition getReturnTypeDefinition() {
        return returnTypeDefinition;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "MethodParameterHolder{" +
                "method=" + method +
                ", returnTypeDefinition=" + returnTypeDefinition +
                ", typeDefinition=" + typeDefinition +
                ", value=" + value +
                '}';
    }
}
