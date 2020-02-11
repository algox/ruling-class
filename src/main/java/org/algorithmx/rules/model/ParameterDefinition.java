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
import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.bind.BindingMatchingStrategyType;
import org.algorithmx.rules.error.UnrulyException;
import org.algorithmx.rules.spring.util.Assert;
import org.algorithmx.rules.spring.util.ClassUtils;
import org.algorithmx.rules.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Optional;

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

    private static final SpecialParameter[] SPECIAL_PARAMETERS = new SpecialParameter[4];

    // Special Handling class handlers
    static {
        SPECIAL_PARAMETERS[0] = new ParameterizedBindingType();
        SPECIAL_PARAMETERS[1] = new ParameterizedOptionalType();
        SPECIAL_PARAMETERS[2] = new BindingType();
        SPECIAL_PARAMETERS[3] = new OptionalType();
    }

    private final int index;
    private final String name;
    private final String description;
    private Type type;
    private final boolean required;
    private final Annotation[] annotations;
    private final SpecialParameter specialParameter;
    private final BindingMatchingStrategyType customMatchingStrategyType;

    private ParameterDefinition(int index, String name, Type type, String description, boolean required, Annotation...annotations) {
        super();
        Assert.isTrue(index >= 0, "Parameter index must be >= 0");
        Assert.notNull(name, "Parameter name cannot be null");
        Assert.notNull(type, "Parameter type cannot be null");

        SpecialParameter specialParameter = findSpecialParameter(type);

        this.name = name;
        this.type = specialParameter != null ? specialParameter.deriveType(type) : type;
        this.description = description;
        this.index = index;
        this.annotations = annotations;
        this.required = specialParameter != null ? specialParameter.isRequired() : required;
        this.specialParameter = specialParameter;
        this.customMatchingStrategyType = getCustomMatchingStrategyType(annotations);
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
        Nullable nullableAnnotation = method.getParameters()[index].getAnnotation(Nullable.class);
        // There is a Nullable annotation; It is optional.
        if (nullableAnnotation != null) return false;
        return method.getParameters()[index].getType().isPrimitive();
    }

    /**
     * See if the parameter needs special handling.
     *
     * @param type param type.
     * @return true if param requires special handling; false otherwise.
     */
    private SpecialParameter findSpecialParameter(Type type) {
        SpecialParameter result = null;

        for (SpecialParameter sp : SPECIAL_PARAMETERS) {
            if (sp.isApplicable(type)) {
                result = sp;
                break;
            }
        }

        return result;
    }

    /**
     * Finds the custom matching strategy if one is present.
     *
     * @param annotations all the annotation on the parameter.
     * @return matching strategy if one is present; null otherwise.
     */
    private BindingMatchingStrategyType getCustomMatchingStrategyType(Annotation...annotations) {
        BindingMatchingStrategyType result = null;

        for (Annotation annotation : annotations) {
            if (annotation instanceof Bind) {
                result = ((Bind) annotation).using();
                break;
            }
        }

        return result;
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
     * Returns whether this parameter requires special handling (Binding, Optional etc).
     *
     * @return true if it requires special handling; false otherwise.
     */
    public boolean isSpecialParameter() {
        return specialParameter != null;
    }

    /**
     * Returns the special parameter if one is found.
     *
     * @return special parameter if one is found; null otherwise.
     */
    public SpecialParameter getSpecialParameter() {
        return specialParameter;
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
     * Returns true if this is a Binding.
     *
     * @return true if this is a Binding; false otherwise.
     */
    public boolean isBinding() {
        return specialParameter instanceof ParameterizedBindingType || specialParameter instanceof BindingType;
    }

    /**
     * Returns true if this is Optional.
     *
     * @return true if this is a Optional; false otherwise.
     */
    public boolean isOptional() {
        return specialParameter instanceof ParameterizedOptionalType || specialParameter instanceof OptionalType;
    }

    /**
     * Returns all the associated annotations for this parameter.
     *
     * @return all annotations for this parameter.
     */
    public Annotation[] getAnnotations() {
        return annotations;
    }

    public BindingMatchingStrategyType getCustomMatchingStrategyType() {
        return customMatchingStrategyType;
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
                ", specialParameter=" + specialParameter +
                ", customMatchingStrategyType=" + customMatchingStrategyType +
                '}';
    }

    /**
     * Defines the properties of Parameter that has some special handling.
     *
     */
    public interface SpecialParameter {

        /**
         * Determines if the parameter is special parameter.
         *
         * @param param input parameter.
         * @return true if it is applicable; false otherwise.
         */
        boolean isApplicable(Object param);

        /**
         * Derive the bindable type.
         *
         * @param param input param.
         * @return derived param.
         */
        Type deriveType(Object param);

        /**
         * Returns if this parameter is required.
         * @return true if it is required; false otherwise.
         */
        boolean isRequired();

        /**
         * Retrive the value from the given Binding.
         *
         * @param binding input binding.
         * @param <B> Binding Type.
         * @return coerced value.
         */
        <B> Object getValue(Binding<B> binding);
    }

    /**
     * Binding with a parameterized type.
     */
    private static class ParameterizedBindingType implements SpecialParameter {

        public ParameterizedBindingType() {
            super();
        }

        @Override
        public boolean isApplicable(Object param) {
            return param instanceof ParameterizedType && Binding.class.equals(((ParameterizedType) param).getRawType());
        }

        @Override
        public Type deriveType(Object param) {
            return ((ParameterizedType) param).getActualTypeArguments()[0];
        }

        @Override
        public boolean isRequired() {
            return true;
        }

        @Override
        public <B> Object getValue(Binding<B> binding) {
            return binding;
        }
    }

    /**
     * Optional with a parameterized type.
     */
    private static class ParameterizedOptionalType implements SpecialParameter {

        public ParameterizedOptionalType() {
            super();
        }

        @Override
        public boolean isApplicable(Object param) {
            return param instanceof ParameterizedType && Optional.class.equals(((ParameterizedType) param).getRawType());
        }

        @Override
        public Type deriveType(Object param) {
            return ((ParameterizedType) param).getActualTypeArguments()[0];
        }

        @Override
        public boolean isRequired() {
            return false;
        }

        @Override
        public <B> Object getValue(Binding<B> binding) {
            Object result = binding.getValue();
            return result == null ? Optional.empty() : Optional.of(result);
        }
    }

    /**
     * Just the Binding class (ie: generic info not available). This usually happens with compiler does not store the
     * generic info for the lambda. JavaC!
     */
    private static class BindingType implements SpecialParameter {

        public BindingType() {
            super();
        }

        @Override
        public boolean isApplicable(Object param) {
            return Binding.class.equals(param);
        }

        @Override
        public Type deriveType(Object param) {
            return Object.class;
        }

        @Override
        public boolean isRequired() {
            return true;
        }

        @Override
        public <B> Object getValue(Binding<B> binding) {
            return binding.get();
        }
    }

    /**
     * Just the Optional class (ie: generic info not available). This usually happens with compiler does not store the
     * generic info for the lambda. JavaC!
     */
    private static class OptionalType implements SpecialParameter {

        public OptionalType() {
            super();
        }

        @Override
        public boolean isApplicable(Object param) {
            return Optional.class.equals(param);
        }

        @Override
        public Type deriveType(Object param) {
            return Object.class;
        }

        @Override
        public boolean isRequired() {
            return false;
        }

        @Override
        public <B> Object getValue(Binding<B> binding) {
            Object result = binding.getValue();
            return result == null ? Optional.empty() : Optional.of(result);
        }
    }
}
