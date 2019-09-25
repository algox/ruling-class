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

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.error.UnrulyException;
import org.algorithmx.rules.spring.util.Assert;
import org.algorithmx.rules.spring.util.ClassUtils;
import org.algorithmx.rules.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * Defines a parameter within a method that is to be isPass dynamically (such as "when" and "then")
 *
 * The definition stores the parameter index, name of parameter (automatically discovered), generic type,
 * whether the parameter is required and any associated annotations.
 *
 * A parameter is deemed not required if it is annotated @Nullable or it is declared with an Optional type.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public final class ParameterDefinition {

    private final int index;
    private final String name;
    private final String description;
    private final Type type;
    private final boolean required;
    private final Annotation[] annotations;

    private ParameterDefinition(int index, String name, Type type, String description, boolean required, Annotation...annotations) {
        super();
        Assert.isTrue(index >= 0, "Parameter index must be >= 0");
        Assert.notNull(name, "Parameter name cannot be null");
        Assert.notNull(type, "Parameter type cannot be null");

        this.name = name;
        this.type = type;
        this.description = description;
        this.index = index;
        this.annotations = annotations;
        this.required = required;
    }

    /**
     * Loads the parameter definitions for the given method.
     *
     * @param method desired method
     * @return all the parameter definitions for the given method.
     */
    public static ParameterDefinition[] load(Method method) {
        String[] parameterNames = ReflectionUtils.getParameterNames(method);
        Assert.isTrue(parameterNames.length == method.getParameterTypes().length,
                "parameterNames length does not match parameter types length");
        ParameterDefinition[] result = new ParameterDefinition[method.getParameterTypes().length];

        for (int i = 0; i < method.getGenericParameterTypes().length; i++) {
            boolean required = isRequired(method, i);

            // Make sure the parameter isn't primitive
            if (!required && method.getParameters()[i].getType().isPrimitive()) {
                throw new UnrulyException("Primitive types cannot be optional. Method [" + method + "] Param ["
                        + parameterNames[i] + "] is declared Nullable however the type is primitive ["
                        + method.getParameters()[i].getType() + "] Use ["
                        + ClassUtils.getWrapperClass(method.getParameters()[i].getType()) + "] instead");
            }

            Description descriptionAnnotation = method.getParameters()[i].getAnnotation(Description.class);
            result[i] = new ParameterDefinition(i, parameterNames[i], method.getGenericParameterTypes()[i],
                    descriptionAnnotation != null ? descriptionAnnotation.value() : null, required,
                    method.getParameterAnnotations()[i]);
        }

        return result;
    }

    private static boolean isRequired(Method method, int index) {
        return method.getParameters()[index].getType().isPrimitive();
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public boolean isRequired() {
        return required;
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }

    @Override
    public String toString() {
        return "ParameterDefinition{" +
                "index=" + index +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", required=" + required +
                ", annotations=" + Arrays.toString(annotations) +
                '}';
    }
}
