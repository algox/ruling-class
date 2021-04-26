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

package org.algorithmx.rulii.core.model;

import org.algorithmx.rulii.annotation.Default;
import org.algorithmx.rulii.annotation.Description;
import org.algorithmx.rulii.annotation.Match;
import org.algorithmx.rulii.bind.match.BindingMatchingStrategy;
import org.algorithmx.rulii.convert.Converter;
import org.algorithmx.rulii.core.UnrulyException;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.util.RuleUtils;
import org.algorithmx.rulii.util.reflect.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Defines a parameter within a method that is to be isPass dynamically (such as "when" and "then")
 *
 * The definition stores the parameter index, name of parameter (automatically discovered), generic type,
 * whether the parameter is required and any associated annotations.
 *
 * A parameter is deemed not required if it is annotated @Optional or it is declared with an Optional type.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public final class ParameterDefinition implements Definition {

    private static final Map<Method, ParameterDefinition[]> CACHE = Collections.synchronizedMap(new IdentityHashMap<>());

    private int index;
    private String name;
    private String description;
    private Type type;
    private AnnotatedType annotatedType;
    private String defaultValueText;
    private Class<? extends BindingMatchingStrategy> matchUsing;
    private final Annotation[] annotations;
    private boolean bindingType;
    private Type underlyingType;

    private Object defaultValue = null;

    private ParameterDefinition(int index, String name, Type type, AnnotatedType annotatedType, String description,
                                String defaultValueText, Class<? extends BindingMatchingStrategy> matchUsing,
                                Annotation...annotations) {
        super();
        Assert.isTrue(index >= 0, "Parameter index must be >= 0");
        setName(name);
        setType(type);
        setDescription(description);
        this.annotatedType = annotatedType;
        this.index = index;
        this.annotations = annotations;
        this.defaultValueText = defaultValueText;
        this.matchUsing = matchUsing;
        validate();
    }

    public void validate() {

        if (isBindingType() && getDefaultValueText() != null) {
            throw new UnrulyException("Bindable parameters Binding<?> cannot have default values. " +
                    "For example : @Optional(defaultValue = \"10\") Binding<Integer> value" + toString());
        }
    }

    public static ParameterDefinition[] load(Method method) {
        Assert.notNull(method, "method cannot be null.");
        return CACHE.computeIfAbsent(method, m -> loadInternal(m));
    }

    /**
     * Loads the parameter definitions for the given method.
     *
     * @param method desired method
     * @return all the parameter definitions for the given method.
     */
    public static ParameterDefinition[] loadInternal(Method method) {
        String[] parameterNames = ReflectionUtils.getParameterNames(method);
        Assert.isTrue(parameterNames.length == method.getParameterTypes().length,
                "parameterNames length does not match parameter types length");
        ParameterDefinition[] result = new ParameterDefinition[method.getParameterTypes().length];

        for (int i = 0; i < method.getGenericParameterTypes().length; i++) {
            String defaultValueText = getDefaultValueText(method, i);
            Description descriptionAnnotation = method.getParameters()[i].getAnnotation(Description.class);
            result[i] = new ParameterDefinition(i, parameterNames[i], method.getGenericParameterTypes()[i],
                    method.getAnnotatedParameterTypes()[i],
                    descriptionAnnotation != null ? descriptionAnnotation.value() : null, defaultValueText,
                    getMatchUsing(method, i), method.getParameterAnnotations()[i]);
        }

        return result;
    }

    private static String getDefaultValueText(Method method, int index) {
        Default defaultAnnotation = method.getParameters()[index].getAnnotation(Default.class);
        return defaultAnnotation != null ? defaultAnnotation.value() : null;
    }

    private static Class<? extends BindingMatchingStrategy> getMatchUsing(Method method, int index) {
        Match result = method.getParameters()[index].getAnnotation(Match.class);
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

    public void setIndex(int index) {
        Assert.isTrue(index >= 0, "index must be >= 0");
        this.index = index;
    }

    /**
     * Returns the name of the parameter.
     *
     * @return name of the parameter.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the parameter description.
     *
     * @return parameter description.
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Sets the name of the parameter.
     *
     * @param name name of the parameter.
     */
    public void setName(String name) {
        Assert.isTrue(RuleUtils.isValidName(name), "Parameter name must match ["
                + RuleUtils.NAME_REGEX + "] Given [" + name + "]");
        this.name = name;
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
        Assert.notNull(type, "type cannot be null.");
        this.type = type;
        this.underlyingType = type;

        if (ReflectionUtils.isBinding(type)) {
            this.bindingType = true;
            this.underlyingType = ReflectionUtils.getUnderlyingBindingType(type);
        }
    }

    /**
     * Returns the name of the parameter type. In case of classes it returns the simple name otherwise the full type name.
     *
     * @return name of the parameter type.
     */
    public String getTypeName() {
        if (type == null) return null;
        if (type instanceof Class) return ((Class) type).getSimpleName();
        return type.getTypeName();
    }

    /**
     * Type and Name.
     * @return Type and Name.
     */
    public String getTypeAndName() {
        return getTypeName() + " " + getName();
    }

    public AnnotatedType getAnnotatedType() {
        return annotatedType;
    }

    /**
     * Sets the description of this parameter.
     *
     * @param description description of this parameter.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Default value text for this parameter if one is specified.
     *
     * @return default value text if specified; null otherwise.
     * @see Default
     */
    public String getDefaultValueText() {
        return defaultValueText;
    }

    public void setDefaultValueText(String defaultValueText) {
        this.defaultValueText = defaultValueText;
    }

    /**
     * Returns the default value for this parameter definition.
     *
     * @param converter converter to be used.
     * @return default value if one exists; null otherwise.
     */
    public Object getDefaultValue(Converter<String, ?> converter) {

        if (defaultValue != null) return defaultValue;

        if (getDefaultValueText() == null) return null;

        this.defaultValue = converter.convert(getDefaultValueText(), getType());

        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Determines whether this parameter is using custom Match. This done by specifying @Match on the parameter.
     *
     * @return true if it is using Match; false otherwise.
     * @see Match
     */
    public boolean isMatchSpecified() {
        return matchUsing != null;
    }

    /**
     * Return the strategy class to use when custom Match is specified.
     *
     * @return BindingMatchingStrategy class is one is specified; null otherwise.
     */
    public Class<? extends BindingMatchingStrategy> getMatchUsing() {
        return matchUsing;
    }

    public void setMatchUsing(Class<? extends BindingMatchingStrategy> matchUsing) {
        this.matchUsing = matchUsing;
    }

    /**
     * Returns true if this is a Binding.
     *
     * @return true if this is a Binding; false otherwise.
     */
    public boolean isBindingType() {
        return bindingType;
    }

    public Type getUnderlyingType() {
        return underlyingType;
    }

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
                ", defaultValueText='" + defaultValueText + '\'' +
                ", matchUsing=" + matchUsing +
                ", annotations=" + Arrays.toString(annotations) +
                ", bindingType=" + bindingType +
                ", defaultValue=" + defaultValue +
                '}';
    }
}
