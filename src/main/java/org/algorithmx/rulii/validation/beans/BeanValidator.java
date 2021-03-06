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
import org.algorithmx.rulii.util.RunnableComparator;
import org.algorithmx.rulii.util.reflect.ObjectFactory;
import org.algorithmx.rulii.util.reflect.ReflectionUtils;
import org.algorithmx.rulii.validation.AnnotatedRunnableBuilder;
import org.algorithmx.rulii.validation.RuleViolations;
import org.algorithmx.rulii.validation.graph.AbstractObjectVisitor;
import org.algorithmx.rulii.validation.graph.GraphNode;
import org.algorithmx.rulii.validation.graph.ObjectGraph;
import org.algorithmx.rulii.validation.types.AnnotatedTypeDefinition;
import org.algorithmx.rulii.validation.types.MarkedAnnotation;

import java.beans.FeatureDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class BeanValidator extends AbstractObjectVisitor {

    private static final Map<AnnotatedTypeDefinition, RuleSet> RULE_CACHE = Collections.synchronizedMap(new IdentityHashMap<>());

    private RuleContext context;
    private RuleViolations violations;

    public BeanValidator() {
        super();
    }

    public RuleViolations validate(RuleContext context, Object bean, SourceHolder source) throws BeanValidationException {
        Assert.notNull(context, "context cannot be null.");
        Assert.notNull(source, "source cannot be null.");

        this.context = context;
        this.violations = new RuleViolations();

        Bindings rootBeanScope = createRootBeanScope(bean, violations);
        context.getBindings().addScope("rootBeanScope", rootBeanScope);
        GraphNode candidate = new GraphNode(bean, source);

        try {
            ObjectGraph graph = new ObjectGraph();
            graph.traverse(candidate, this);
        } catch (BeanValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new BeanValidationException(candidate, violations, "Error trying to validate [" + candidate + "]", e);
        } finally {
            context.getBindings().removeScope(rootBeanScope);
        }

        return violations;
    }

    @Override
    protected Class<? extends Annotation> getIntrospectionAnnotation() {
        return Validate.class;
    }

    @Override
    public Collection<GraphNode> visitCandidate(GraphNode node) {
        return process(node);
    }

    protected Collection<GraphNode> process(GraphNode node) {
        List<GraphNode> result = Collections.emptyList();
        RuleViolations violations = new RuleViolations();
        Bindings beanScope = null;

        try {
            beanScope = createBeanScope(node.getTarget());
            beanScope.bind("violations", violations);
            getContext().getBindings().addScope("beanScope", beanScope);
            getContext().getBindings().addScope("candidateScope", createNodeScope("$value", node.getTarget()));

            runRules(getContext(), node.getTypeDefinition(), "$value");
            decorateAndTransferViolations(violations, this.violations, node);

            if (isIntrospectionRequired(node)) {
                result = introspectCandidate(node, context.getExtractorRegistry(), context.getObjectFactory());
            }

        } catch (Exception e) {
            throw new BeanValidationException(node, violations, "Error trying to validate [" + node + "]", e);
        } finally {
            if (beanScope != null) context.getBindings().removeScope(beanScope);
        }

        return result;
    }

    protected boolean isIntrospectionRequired(GraphNode candidate) {
        if (candidate.getTypeDefinition() == null) return true;
        Validate validate = (Validate) candidate.getTypeDefinition().getIntrospectionAnnotation();
        //return validate != null && (validate.includeAnnotatedRules() || StringUtils.hasText(validate.using()));
        return validate != null;
    }

    protected void runRules(RuleContext context, AnnotatedTypeDefinition definition, String bindingName) {
        if (definition == null) return;

        RuleSet rules = getAnnotatedRules(context.getObjectFactory(), definition, bindingName);

        if (rules == null) {
            // TODO : Log
        } else {
            rules.run(context);
        }
    }

    protected RuleSet getAnnotatedRules(ObjectFactory objectFactory, AnnotatedTypeDefinition definition,
                                        String bindingName) {
        return definition != null
                ? RULE_CACHE.computeIfAbsent(definition, d -> createAnnotatedRules(objectFactory, d, bindingName))
                : null;
    }

    protected RuleSet createAnnotatedRules(ObjectFactory objectFactory,
                                           AnnotatedTypeDefinition definition,
                                           String bindingName) {
        Assert.notNull(bindingName, "bindingName cannot be null.");

        if (definition == null || !definition.hasDeclaredRules()) return null;

        RuleSetBuilder result = RuleSetBuilder.with(bindingName + "ValidationRules");
        List<Runnable> runnables = new ArrayList<>();

        for (MarkedAnnotation marker : definition.getDeclaredRuleAnnotations()) {
            ValidationMarker validationRule = (ValidationMarker) marker.getMarker();

            if (validationRule == null) {
                // TODO : log it
                continue;
            }

            AnnotatedRunnableBuilder builder = objectFactory.create(validationRule.value(), false);
            Runnable runnable = builder.build(marker.getOwner(), bindingName);

            if (runnable != null) {
                runnables.add(runnable);
            }
        }

        // Sort them in case order was specified
        Collections.sort(runnables, new RunnableComparator());
        result.addAll(runnables);
        return result.size() > 0 ? result.build() : null;
    }

    protected Bindings createRootBeanScope(Object bean, RuleViolations violations) {
        Bindings result = Bindings.create();

        result.bind("root", bean);
        result.bind("violations", violations);

        return result;
    }

    protected Bindings createBeanScope(Object bean) {
        // TODO : Optimize this so we don't keep creating the same bean bindings?
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

    protected Bindings createNodeScope(String bindingName, Object bean) {
        Bindings result = Bindings.create();
        result.bind(bindingName, bean);
        return result;
    }

    protected void decorateAndTransferViolations(RuleViolations source, RuleViolations target, GraphNode candidate) {
        if (source != null && source.size() > 0) {
            Arrays.stream(source.getViolations())
                    .forEach(v -> {
                        v.param("field", candidate.getPath());
                        v.param("description", candidate.getTypeDefinition().getSignature());
                        target.add(v);
                    });
        }
    }

    protected RuleContext getContext() {
        return context;
    }

    protected RuleViolations getViolations() {
        return violations;
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
    protected Comparator<Field> getFieldComparator() {
        return Comparator.comparing(Field::getName);
    }

    @Override
    protected Predicate<PropertyDescriptor> getPropertyFilter() { return p -> p.getReadMethod() != null; }

    @Override
    protected Comparator<PropertyDescriptor> getPropertyComparator() {
        return Comparator.comparing(FeatureDescriptor::getName);
    }

    @Override
    protected Predicate<Method> getMethodFilter() {
        return m -> !Modifier.isStatic(m.getModifiers());
    }

    @Override
    protected Comparator<Method> getMethodComparator() {
        return Comparator.comparing(Method::getName);
    }
}
