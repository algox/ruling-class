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

import org.algorithmx.rulii.core.ruleset.RuleSet;
import org.algorithmx.rulii.lib.spring.util.Assert;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

class DefaultBeanValidationRules implements BeanValidationRules {
    private final Class<?> beanClass;
    private final Map<Object, RuleHolder> ruleMap = new LinkedHashMap<>();

    public DefaultBeanValidationRules(Class<?> beanClass) {
        super();
        this.beanClass = beanClass;
    }

    public void add(RuleHolder holder) {
        Assert.notNull(holder, "holder cannot be null.");
        holder.build();
        this.ruleMap.put(holder.getType(), holder);
    }

    @Override
    public Class<?> getBeanType() {
        return beanClass;
    }

    @Override
    public RuleSet getBeanRules() {
        RuleHolder result = ruleMap.get(beanClass);
        return result != null ? result.getRuleSet() : null;
    }

    @Override
    public RuleSet getFieldRules(Field field) {
        RuleHolder result = ruleMap.get(field);
        return result != null ? result.getRuleSet() : null;
    }

    @Override
    public RuleSet getConstructorRules(Constructor ctor) {
        RuleHolder result = ruleMap.get(ctor);
        return result != null ? result.getRuleSet() : null;
    }

    @Override
    public RuleSet getConstructorParameterRules(int index, Constructor ctor) {
        CtorHolder result = (CtorHolder) ruleMap.get(ctor);
        return result != null ? result.getParameterRules(index) : null;
    }

    @Override
    public RuleSet getMethodRules(Method method) {
        RuleHolder result = ruleMap.get(method);
        return result != null ? result.getRuleSet() : null;
    }

    @Override
    public RuleSet getMethodParameterRules(int index, Method method) {
        MethodHolder result = (MethodHolder) ruleMap.get(method);
        return result != null ? result.getParameterRules(index) : null;
    }

    public String prettyPrint() {
        StringBuilder result = new StringBuilder();

        result.append("Bean Rules for : " + beanClass.getName());
        result.append(System.lineSeparator());

        for (RuleHolder ruleHolder : ruleMap.values()) {

            if (ruleHolder instanceof ClassHolder) {
                result.append("Class Rules " + ruleHolder.getName());
                result.append(System.lineSeparator());
                result.append(ruleHolder.getRuleSet());
                result.append(System.lineSeparator());
            }

            if (ruleHolder instanceof FieldHolder) {
                result.append("Field Rules " + ruleHolder.getName());
                result.append(System.lineSeparator());
                result.append(ruleHolder.getRuleSet());
                result.append(System.lineSeparator());
            }

            if (ruleHolder instanceof MethodHolder) {
                MethodHolder methodHolder = (MethodHolder) ruleHolder;
                result.append("Method Rules " + methodHolder.getName());
                result.append(System.lineSeparator());
                result.append("Getter ? " + methodHolder.isGetter());
                result.append(System.lineSeparator());
                result.append(methodHolder.getRuleSet());
                result.append(System.lineSeparator());

                for (int i = 0; i < methodHolder.getParameterCount(); i++) {
                    result.append("Method Parameter : " + i);
                    result.append(System.lineSeparator());
                    result.append(methodHolder.getParameterRules(i));
                    result.append(System.lineSeparator());
                }

            }
        }

        return result.toString();
    }

    @Override
    public String toString() {
        return prettyPrint();
    }
}
