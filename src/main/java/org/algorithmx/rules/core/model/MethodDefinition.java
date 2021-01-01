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

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Order;
import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.lib.spring.util.Assert;
import org.algorithmx.rules.util.RuleUtils;
import org.algorithmx.rules.util.reflect.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Defines a method is to be isPass dynamically (such as "when" and "then")
 *
 * The definition stores the reflective method and its assocated parameter definitions.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public final class MethodDefinition implements Comparable<MethodDefinition> {

    private final Method method;
    private final ParameterDefinition[] parameterDefinitions;
    private final Map<String, ParameterDefinition> paramNameMap = new HashMap<>();

    // Name of the action
    private String name;
    // Order of the Action
    private int order;
    // Description of the Action
    private String description;
    // Return type of the method
    private Type returnType;

    public MethodDefinition(Method method, int order, String description, ParameterDefinition...parameterDefinitions) {
        super();
        Assert.notNull(method, "method cannot be null");
        this.method = method;
        this.name = method.getName();
        this.order = order;
        this.description = description;
        this.returnType = method.getGenericReturnType();
        this.parameterDefinitions = parameterDefinitions != null ? parameterDefinitions : new ParameterDefinition[0];
        createParameterNameIndex();
        validate();
    }

    public void validate() {
        HashSet<Integer> indexRange = IntStream.range(0, parameterDefinitions.length).collect(HashSet::new, Set::add, Set::addAll);

        for (ParameterDefinition definition : parameterDefinitions) {
            if (!paramNameMap.containsKey(definition.getName())) {
                throw new UnrulyException("Could not find parameter name [" + definition.getName()
                        + "] in index [" + paramNameMap + "]. Was the index created?");
            }
            indexRange.remove(definition.getIndex());
        }

        if (indexRange.size() > 0) {
            throw new UnrulyException("Parameter indexes should be contiguous. Invalid index [" + indexRange + "]");
        }
    }

    /**
     * Loads all the associated method definition that match the given predicate.
     *
     * @param c container class.
     * @param predicate matcher.
     * @return all matching MethodDefinitions
     */
    public static MethodDefinition[] load(Class<?> c, Predicate<Method> predicate) {
        Method[] matches = ReflectionUtils.getMethods(c, predicate);
        List<MethodDefinition> result = new ArrayList<>(matches.length);
        Arrays.stream(matches).forEach(m -> result.add(load(m)));
        return result.toArray(new MethodDefinition[matches.length]);
    }

    public static MethodDefinition load(Method method) {
        Assert.notNull(method, "method cannot be null");
        Description descriptionAnnotation = method.getAnnotation(Description.class);
        Order orderAnnotation = method.getAnnotation(Order.class);
        MethodDefinition result = new MethodDefinition(method, orderAnnotation != null ? orderAnnotation.value() : 0,
                descriptionAnnotation != null ? descriptionAnnotation.value() : null, ParameterDefinition.load(method));
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
     * Parameter Definition at the given index.
     *
     * @param index parameter index.
     * @return Parameter Definition.
     */
    public ParameterDefinition getParameterDefinition(int index) {

        if (getParameterDefinitions().length == 0) {
            throw new UnrulyException("There are no args found in this function. [" + method + "]");
        }

        if (index < 0 || index >= getParameterDefinitions().length) {
            throw new UnrulyException("Invalid parameter index [" + index + "] it must be between [0, "
                    + getParameterDefinitions().length + "] method [" + method + "]");
        }

        return getParameterDefinitions()[index];
    }

    /**
     * Parameter Definition with the given name.
     *
     * @param name parameter name.
     * @return Parameter Definition.
     */
    public ParameterDefinition getParameterDefinition(String name) {
        return paramNameMap.get(name);
    }

    /**
     * Retrieves the name of this definition.
     *
     * @return name of the method or overridden value.
     */
    public String getName() {
        return name;
    }

    /**
     * Overrides the name of the method.
     *
     * @param name new value/
     */
    public void setName(String name) {
        Assert.isTrue(RuleUtils.isValidName(name), "Invalid method name [" + name + "] regex [" + RuleUtils.NAME_REGEX + "]");
        this.name = name;
    }

    /**
     * Order of this method in a group.
     *
     * @return order.
     */
    public Integer getOrder() {
        return order;
    }

    /**
     * Sets the order of this method in a group.
     *
     * @param order value
     */
    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * Description of this method.
     *
     * @return method description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of this method.
     *
     * @param description method description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Return type of this method.
     *
     * @return method return type.
     */
    public Type getReturnType() {
        return returnType;
    }

    /**
     * Overridden method return type with the given value.
     *
     * @param returnType new value.
     */
    public void setReturnType(Type returnType) {
        Assert.notNull(returnType, "returnType cannot be null.");
        this.returnType = returnType;
    }

    /**
     * Determines whether the method is static.
     *
     * @return return true if its a static method; false otherwise.
     */
    public boolean isStatic() {
        return Modifier.isStatic(method.getModifiers());
    }

    /**
     * Returns a simplified version of the method signature.
     *
     * @return simplified method signature.
     */
    public String getSignature() {
        StringBuilder result = new StringBuilder(getMethod().getDeclaringClass().getSimpleName() + "."
                + getName() + "(");
        result.append(Arrays.stream(parameterDefinitions)
                .map(p -> p.getTypeName() + " " + p.getName())
                .collect(Collectors.joining(", ")));
        result.append(")");
        return result.toString();
    }

    /**
     * Creates a lookup between the parameter and it's name.
     */
    public void createParameterNameIndex() {
        if (parameterDefinitions == null || parameterDefinitions.length == 0) return;
        paramNameMap.clear();
        // Create the param name map
        Arrays.stream(parameterDefinitions).forEach(param -> paramNameMap.put(param.getName(), param));
    }

    @Override
    public int compareTo(MethodDefinition other) {
        return getOrder().compareTo(other.getOrder());
    }

    @Override
    public String toString() {
        return "MethodDefinition{" +
                "method=" + method +
                ", parameterDefinitions=" + Arrays.toString(parameterDefinitions) +
                ", paramNameMap=" + paramNameMap +
                ", name='" + name + '\'' +
                ", order=" + order +
                ", description='" + description + '\'' +
                '}';
    }
}
