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

import org.algorithmx.rulii.bind.Binding;
import org.algorithmx.rulii.bind.BindingBuilder;
import org.algorithmx.rulii.bind.Bindings;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.core.context.RuleContextBuilder;
import org.algorithmx.rulii.core.ruleset.RuleSet;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.lib.spring.util.ConcurrentReferenceHashMap;
import org.algorithmx.rulii.lib.spring.util.ReflectionUtils;

import java.util.Map;

public class DefaultBeanValidator implements BeanValidator {

    private final Map<Class<?>, BeanValidationRules> rulesMap = new ConcurrentReferenceHashMap<>();

    public DefaultBeanValidator() {
        super();
    }

    @Override
    public void validate(Object bean, boolean includeAnnotatedRules, RuleSet ruleSet) {
        Assert.notNull(bean, "bean cannot be null.");
    }

    protected RuleContext createRuleContext(Bindings bindings) {
        return RuleContextBuilder.with(bindings).build();
    }

    protected Bindings buildBeanBindings(Object bean) {
        Bindings result = Bindings.create();

        ReflectionUtils.doWithFields(bean.getClass(), field -> {
            ReflectionUtils.makeAccessible(field);
            Object value = field.get(bean);
            Binding fieldBinding = BindingBuilder
                    .with(field.getName())
                    .type(field.getGenericType())
                    .value(value)
                    .editable(false)
                    .build();
            result.bind(fieldBinding);
        });

        // TODO : Properties ?

        return result;
    }

    protected BeanValidationRules getBeanValidationRules(Class<?> beanClass) {
        BeanValidationRules result = rulesMap.get(beanClass);

        if (result == null) {
            result = buildBeanValidationRules(beanClass);
            rulesMap.putIfAbsent(beanClass, result);
        }

        return result;
    }

    protected BeanValidationRules buildBeanValidationRules(Class<?> beanClass) {
        return BeanValidationRuleBuilder
                .with(beanClass)
                .loadAnnotatedFields()
                .loadAnnotatedMethods()
                .loadAnnotatedConstructors()
                .build();
    }
}
