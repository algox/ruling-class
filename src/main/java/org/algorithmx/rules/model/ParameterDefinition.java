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

import org.algorithmx.rules.annotation.Bind;
import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Nullable;
import org.algorithmx.rules.bind.BindingMatchingStrategy;
import org.algorithmx.rules.spring.util.Assert;
import org.algorithmx.rules.util.reflect.ReflectionUtils;

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
    private Type type;
    private final boolean required;
    private final String defaultValue;
    private final Class<? extends BindingMatchingStrategy> bindUsing;
    private final Annotation[] annotations;

    private ParameterDefinition(int index, String name, Type type, String description, boolean required,
                                String defaultValue, Class<? extends BindingMatchingStrategy> bindUsing,
                                Annotation...annotations) {
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
        this.defaultValue = defaultValue;
        this.bindUsing = bindUsing;
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
            String defaultValue = getDefaultValue(method, i);
            Description descriptionAnnotation = method.getParameters()[i].getAnnotation(Description.class);
            result[i] = new ParameterDefinition(i, parameterNames[i], method.getGenericParameterTypes()[i],
                    descriptionAnnotation != null ? descriptionAnnotation.value() : null, required, defaultValue,
                    getBindUsing(method, i), method.getParameterAnnotations()[i]);
        }

        return result;
    }

    private static boolean isRequired(Method method, int index) {
        return method.getParameters()[index].getAnnotation(Nullable.class) == null;
    }

    private static String getDefaultValue(Method method, int index) {
        Nullable nullable = method.getParameters()[index].getAnnotation(Nullable.class);
        return nullable == null ? null
                : Nullable.NOT_APPLICABLE.equals(nullable.defaultValue())
                    ? null
                    : nullable.defaultValue();
    }

    private static Class<? extends BindingMatchingStrategy> getBindUsing(Method method, int index) {
        Bind result = method.getParameters()[index].getAnnotation(Bind.class);
        return result != null ? result.using() : null;
    }

    /**
     * Returns the index of the parameter.
     *
     * @return Parameter index.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Returns the name of the parameter.
     *
     * @return name of the parameter.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the Type of the parameter.
     *
     * @return type of the parameter.
     */
    public Type getType() {
        return type;
    }

    /**
     * Overrides the Type of the parameter.
     *
     * @param type desired type.
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Returns the parameter description.
     *
     * @return parameter description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Determines if this parameter is required.
     *
     * @return true if required; false otherwise.
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * Default value for this parameter if one is specified.
     *
     * @return default vslue if specified; null otherwise.
     * @see Nullable
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * Determines whether this parameter is using custom Bind. This done by specifying @Bind on the parameter.
     *
     * @return true if it is using Bind; false otherwise.
     * @see Bind
     */
    public boolean isBindable() {
        return bindUsing != null;
    }

    /**
     * Return the strategy class to use when custom Bind is specified.
     *
     * @return BindingMatchingStrategy class is one is specified; null otherwise.
     */
    public Class<? extends BindingMatchingStrategy> getBindUsing() {
        return bindUsing;
    }

    /**
     * Returns true if this is a Binding.
     *
     * @return true if this is a Binding; false otherwise.
     */
    /*public boolean isBinding() {
        return specialParameter instanceof ParameterizedBindingType || specialParameter instanceof BindingType;
    }*/

    /**
     * Returns true if this is Optional.
     *
     * @return true if this is a Optional; false otherwise.
     */
    /*public boolean isOptional() {
        return specialParameter instanceof ParameterizedOptionalType || specialParameter instanceof OptionalType;
    }*/

    /**
     * Returns all the associated annotations for this parameter.
     *
     * @return all annotations for this parameter.
     */
    public Annotation[] getAnnotations() {
        return annotations;
    }

    @Override
    public String toString() {
        return "ParameterDefinition{" +
                "index=" + index +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", required=" + required +
                ", annotations=" + Arrays.toString(annotations) +
                '}';
    }

}
