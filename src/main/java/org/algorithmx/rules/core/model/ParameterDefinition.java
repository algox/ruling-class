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
package org.algorithmx.rules.core.model;

import org.algorithmx.rules.annotation.Match;
import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Nullable;
import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.bind.convert.Converter;
import org.algorithmx.rules.bind.match.BindingMatchingStrategy;
import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.lib.spring.util.Assert;
import org.algorithmx.rules.util.reflect.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
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
    private final String defaultValueText;
    private final Class<? extends BindingMatchingStrategy> bindUsing;
    private final Annotation[] annotations;
    private Type bindingType;

    private Object defaultValue = null;

    private ParameterDefinition(int index, String name, Type type, String description, boolean required,
                                String defaultValueText, Class<? extends BindingMatchingStrategy> bindUsing,
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
        this.defaultValueText = defaultValueText;
        this.bindUsing = bindUsing;
        setBindingType(type);
        validate();
    }

    private void validate() {
        if (isBinding() && getDefaultValueText() != null) {
            throw new UnrulyException("Bindable parameters Binding<?> cannot have default values. " +
                    "For example : @Nullable(defaultValue = \"10\") Binding<Integer> value" + toString());
        }
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
            String defaultValueText = getDefaultValueText(method, i);
            Description descriptionAnnotation = method.getParameters()[i].getAnnotation(Description.class);
            result[i] = new ParameterDefinition(i, parameterNames[i], method.getGenericParameterTypes()[i],
                    descriptionAnnotation != null ? descriptionAnnotation.value() : null, required, defaultValueText,
                    getBindUsing(method, i), method.getParameterAnnotations()[i]);
        }

        return result;
    }

    private static boolean isRequired(Method method, int index) {
        return method.getParameters()[index].getAnnotation(Nullable.class) == null;
    }

    private static String getDefaultValueText(Method method, int index) {
        Nullable nullable = method.getParameters()[index].getAnnotation(Nullable.class);
        return nullable == null ? null
                : Nullable.NOT_APPLICABLE.equals(nullable.defaultValue())
                    ? null
                    : nullable.defaultValue();
    }

    private static Class<? extends BindingMatchingStrategy> getBindUsing(Method method, int index) {
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
     * Overrides the Type of the parameter.
     *
     * @param type desired type.
     */
    public void setType(Type type) {
        Assert.notNull(type, "type cannot be null.");
        this.type = type;
        setBindingType(type);
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
     * Default value text for this parameter if one is specified.
     *
     * @return default value text if specified; null otherwise.
     * @see Nullable
     */
    public String getDefaultValueText() {
        return defaultValueText;
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

        if (!converter.canConvert(String.class, getType())) {
            throw new UnrulyException("Invalid Converter supplied [" + converter
                    + "]. It cannot convert String -> " + getType() + "]");
        }

        this.defaultValue = converter.convert(getDefaultValueText(), getType());

        return defaultValue;
    }

    /**
     * Determines whether this parameter is using custom Match. This done by specifying @Match on the parameter.
     *
     * @return true if it is using Match; false otherwise.
     * @see Match
     */
    public boolean isBindSpecified() {
        return bindUsing != null;
    }

    /**
     * Return the strategy class to use when custom Match is specified.
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
    public boolean isBinding() {
        return bindingType != null;
    }

    /**
     * Get the actual type of a Binding.
     *
     * @return actual Type of the Binding.
     * @throws UnrulyException if type isn't a Binding.
     */
    public Type getBindingType() {
        return bindingType;
    }

    private void setBindingType(Type type) {
        this.bindingType = deriveIsBinding(type) ? deriveBindingType(type) : null;
    }

    private boolean deriveIsBinding(Type type) {
        if (Binding.class.equals(type)) return true;
        if (!(type instanceof ParameterizedType)) return false;
        ParameterizedType parameterizedType = (ParameterizedType) type;
        return Binding.class.equals(parameterizedType.getRawType());
    }


    private Type deriveBindingType(Type type) {
        if (Binding.class.equals(type)) return Object.class;
        if (!(type instanceof ParameterizedType)) throw new UnrulyException("Not a Binding Type [" + type + "]");
        ParameterizedType parameterizedType = (ParameterizedType) type;

        if (!Binding.class.equals(parameterizedType.getRawType())) {
            throw new UnrulyException("Not a Binding Type [" + type + "]");
        }

        return parameterizedType.getActualTypeArguments()[0];
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
                ", required=" + required +
                ", annotations=" + Arrays.toString(annotations) +
                '}';
    }

}
