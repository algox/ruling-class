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

import org.algorithmx.rulii.annotation.Validate;
import org.algorithmx.rulii.annotation.ValidationMarker;
import org.algorithmx.rulii.bind.Bindings;
import org.algorithmx.rulii.bind.load.FieldBindingLoader;
import org.algorithmx.rulii.bind.load.PropertyBindingLoader;
import org.algorithmx.rulii.core.Runnable;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.core.ruleset.RuleSet;
import org.algorithmx.rulii.core.ruleset.RuleSetBuilder;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.lib.spring.util.StringUtils;
import org.algorithmx.rulii.util.reflect.ReflectionUtils;
import org.algorithmx.rulii.validation.AnnotatedRunnableBuilder;
import org.algorithmx.rulii.validation.RuleViolations;
import org.algorithmx.rulii.validation.graph.ObjectGraph;
import org.algorithmx.rulii.validation.graph.ObjectVisitorTemplate;
import org.algorithmx.rulii.validation.graph.TraversalCandidate;
import org.algorithmx.rulii.validation.types.AnnotatedTypeDefinition;
import org.algorithmx.rulii.validation.types.MarkedAnnotation;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BeanValidator extends ObjectVisitorTemplate {

    private static final String DEFAULT_VALUE_BINDING_NAME = "$value";
    private static final Map<AnnotatedTypeDefinition, RuleSet> RULE_CACHE = new ConcurrentHashMap<>();

    private RuleContext context;
    private RuleViolations violations;
    private String valueBindingName = DEFAULT_VALUE_BINDING_NAME;

    public BeanValidator() {
        super();
    }

    public RuleViolations validateBean(RuleContext context, Object bean, Class<?> type) {
        return validateBean(context, bean, new BeanHolder(bean, type));
    }

    public RuleViolations validateBean(RuleContext context, Object bean, SourceHolder source) {
        Assert.notNull(context, "context cannot be null.");
        Assert.notNull(source, "source cannot be null.");

        this.context = context;
        this.violations = new RuleViolations();

        Bindings rootBeanScope = createRootBeanScope(bean, violations);
        context.getBindings().addScope("rootBeanScope", rootBeanScope);

        try {
            ObjectGraph graph = new ObjectGraph();
            TraversalCandidate candidate = new TraversalCandidate(bean, source);
            graph.traverse(candidate, this);
        } finally {
            context.getBindings().removeScope(rootBeanScope);
        }

        System.err.println();
        System.err.println("XXX Violations [" + violations + "]");
        return violations;
    }

    @Override
    protected Class<? extends Annotation> getIntrospectionAnnotation() {
        return Validate.class;
    }

    public boolean isIntrospectionRequired(TraversalCandidate candidate) {
        if (candidate.getTypeDefinition() == null) return true;
        Validate validate = (Validate) candidate.getTypeDefinition().getIntrospectionAnnotation();
        return validate != null && (validate.includeAnnotatedRules() || StringUtils.hasText(validate.using()));
    }

    @Override
    public Collection<TraversalCandidate> visitCandidate(TraversalCandidate candidate) {
        if (candidate.getTypeDefinition() != null && !candidate.getTypeDefinition().hasDeclaredRules()) return Collections.emptyList();

        System.err.println("XXX About to validate [" + candidate.getTarget() + "] Source [" + (candidate.getSourceHolder() != null ? candidate.getSourceHolder().getName() : "N/A") + "]");
        Bindings beanScope = createBeanScope(candidate.getTarget());
        RuleViolations violations = new RuleViolations();

        beanScope.bind(getValueBindingName(), candidate.getTarget());
        beanScope.bind("$violations", violations);

        try {
            context.getBindings().addScope("beanScope", beanScope);
            runRules(candidate.getTypeDefinition());
            decorateAndTransferViolations(violations, this.violations, candidate);
        } finally {
            context.getBindings().removeScope(beanScope);
        }

        List<TraversalCandidate> result = Collections.emptyList();

        if (isIntrospectionRequired(candidate)) {
            result = introspectCandidate(candidate, context.getExtractorRegistry());
        }

        return result;
    }

    protected void runRules(AnnotatedTypeDefinition definition) {
        if (definition == null) return;

        Validate validate = (Validate) definition.getIntrospectionAnnotation();

        if (validate == null || validate.includeAnnotatedRules()) {
            RuleSet rules = getAnnotatedRules(context, definition, "$value");
            if (rules != null) {
                rules.run(context);
            } else {
                // TODO : Log
            }
        }

        if (validate != null) {
            if (!Validate.NOT_APPLICABLE.equals(validate.using())) {
                Runnable rules = context.getRuleRegistry().get(validate.using());
                if (rules != null) {
                    rules.run(context);
                } else {
                    // TODO : Log
                    System.err.println("XXX No rules named [" + validate.using() + "] found ! [" + definition + "]");
                }
            }
        }
    }

    protected RuleSet getAnnotatedRules(RuleContext context, AnnotatedTypeDefinition definition, String bindingName) {
        return definition != null
                ? RULE_CACHE.computeIfAbsent(definition, d -> createAnnotatedRules(context, d, bindingName))
                : null;
    }

    protected RuleSet createAnnotatedRules(RuleContext context, AnnotatedTypeDefinition definition, String bindingName) {
        if (definition == null || !definition.hasDeclaredRules()) return null;

        RuleSetBuilder result = RuleSetBuilder.with("validation_rules");

        for (MarkedAnnotation marker : definition.getDeclaredRuleAnnotations()) {
            ValidationMarker validationRule = (ValidationMarker) marker.getMarker();

            if (validationRule == null) {
                // TODO : log it
                continue;
            }

            AnnotatedRunnableBuilder builder = context.getObjectFactory().create(validationRule.value(), false);
            Runnable[] runnables = builder.build(marker.getOwner(), bindingName);

            if (runnables != null) {
                result.add(runnables);
            }
        }

        return result.size() > 0 ? result.build() : null;
    }

    protected Bindings createRootBeanScope(Object bean, RuleViolations violations) {
        Bindings result = Bindings.create();

        result.bind("$root", bean);
        result.bind("$violations", violations);

        return result;
    }

    protected Bindings createBeanScope(Object bean) {
        Bindings result = Bindings.create();

        if (bean == null) return result;

        FieldBindingLoader fieldLoader = new FieldBindingLoader();
        fieldLoader.load(result, bean);

        Set<String> names = result.getNames();

        PropertyBindingLoader propertyLoader = new PropertyBindingLoader();
        propertyLoader.setIgnoreProperties(names);
        propertyLoader.load(result, bean);

        return result;
    }

    protected void decorateAndTransferViolations(RuleViolations source, RuleViolations target, TraversalCandidate candidate) {
        if (source != null && source.size() > 0) {
            Arrays.stream(source.getViolations())
                    .forEach(v -> {
                        v.param("field", getTraversedPath(candidate));
                        //v.param("type", candidate.getTypeDefinition().getAnnotatedType().getType());
                        target.add(v);
                    });
        }
    }

    protected String getTraversedPath(TraversalCandidate candidate) {
        List<String> result = new ArrayList<>();
        TraversalCandidate parent = candidate;

        while (parent != null) {
            String nodeName = parent.getSourceHolder() != null
                    ? parent.getSourceHolder().getName()
                    :  parent.getTarget().getClass().getSimpleName();
            //parent.getTypeDefinition().getAnnotatedType().getType().toString();
            result.add(0, nodeName);

            parent = parent.getParent();
        }

        return result.stream().collect(Collectors.joining("."));
    }

    public String getValueBindingName() {
        return valueBindingName;
    }

    public void setValueBindingName(String valueBindingName) {
        Assert.notNull(valueBindingName, "valueBindingName cannot be null.");
        this.valueBindingName = valueBindingName;
    }

    @Override
    protected Predicate<Class<?>> getClassFilter() {
        return c -> !ReflectionUtils.isJavaCoreClass(c);
    }

    @Override
    protected Predicate<Field> getFieldFilter() {
        return f -> !Modifier.isStatic(f.getModifiers());
    }

    @Override
    protected Predicate<PropertyDescriptor> getPropertyFilter() {
        return null;
    }

    @Override
    protected Predicate<Method> getMethodFilter() {
        return m -> !Modifier.isStatic(m.getModifiers());
    }
}
