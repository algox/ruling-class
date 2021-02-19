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

import org.algorithmx.rulii.bind.Bindings;
import org.algorithmx.rulii.bind.load.FieldBindingLoader;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.core.ruleset.RuleSet;
import org.algorithmx.rulii.lib.spring.core.annotation.AnnotationUtils;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.lib.spring.util.ConcurrentReferenceHashMap;
import org.algorithmx.rulii.util.objectgraph.ObjectVisitorTemplate;
import org.algorithmx.rulii.validation.RuleViolations;
import org.algorithmx.rulii.validation.annotation.Validate;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class BeanGraphValidator extends ObjectVisitorTemplate {

    private static final Map<Class<?>, BeanValidationRules> rulesMap = new ConcurrentReferenceHashMap<>();

    private final Predicate<Field> validateCheck = field -> AnnotationUtils.getAnnotation(field, Validate.class) != null;
    private final RuleContext context;

    private final RuleViolations violations = new RuleViolations();

    private Bindings bindings;
    private BeanValidationRules rules;

    public BeanGraphValidator(RuleContext context) {
        super();
        Assert.notNull(context, "context cannot be null.");
        this.context = context;
        init();
    }

    private void init() {
        // Need a check?
        context.getBindings().bind("errors", violations);
    }

    public RuleViolations getViolations() {
        return violations;
    }

    @Override
    public boolean visitObjectStart(Object target) {
        Bindings bindings = buildBeanBindings(target);
        setRules(getBeanValidationRules(target.getClass()));
        getContext().getBindings().addScope(System.identityHashCode(target) + "-scope", bindings);
        setBindings(bindings);
        return true;
    }

    @Override
    public void visitObjectEnd(Object target) {
        try {
            run(getRules().getBeanRules(), getContext());
        } finally {
            getContext().getBindings().removeScope(getBindings());
            this.bindings = null;
        }

    }

    @Override
    public boolean visitField(Field field, Object value, Object parent) {
        run(getRules().getFieldRules(field), getContext());
        return validateCheck.test(field);
    }

    @Override
    public boolean visitArray(Field field, Object values, Object parent) {
        run(getRules().getFieldRules(field), getContext());
        return validateCheck.test(field);
    }

    @Override
    public boolean visitCollection(Field field, Collection<?> values, Object parent) {
        run(getRules().getFieldRules(field), getContext());
        return validateCheck.test(field);
    }

    @Override
    public boolean visitMap(Field field, Map<?, ?> values, Object parent) {
        run(getRules().getFieldRules(field), getContext());
        return true;
    }

    @Override
    public boolean visitMapKeys(Field field, Set<?> keys, Object parent) {
        // TODO : Check to see if AnnotatedType is there for keys
        return validateCheck.test(field);
    }

    @Override
    public boolean visitMapValues(Field field, Collection<?> keys, Object parent) {
        // TODO : Check to see if AnnotatedType is there for values
        return validateCheck.test(field);
    }

    protected void run(RuleSet ruleSet, RuleContext context) {
        if (ruleSet == null || ruleSet.size() == 0) return;
        ruleSet.run(context);
    }

    protected RuleContext getContext() {
        return context;
    }

    protected Bindings getBindings() {
        return bindings;
    }

    protected BeanValidationRules getRules() {
        return rules;
    }

    public void setBindings(Bindings bindings) {
        this.bindings = bindings;
    }

    public void setRules(BeanValidationRules rules) {
        this.rules = rules;
    }

    protected Bindings buildBeanBindings(Object bean) {
        Bindings result = Bindings.create();
        result.bindUsing(new FieldBindingLoader<>(), bean);
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
