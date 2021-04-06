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
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.lib.spring.util.ConcurrentReferenceHashMap;
import org.algorithmx.rulii.util.reflect.ReflectionUtils;
import org.algorithmx.rulii.validation.AnnotatedRunnableBuilder;
import org.algorithmx.rulii.validation.RuleViolations;
import org.algorithmx.rulii.validation.graph.ObjectGraph;
import org.algorithmx.rulii.validation.graph.ObjectVisitorTemplate;
import org.algorithmx.rulii.validation.graph.TraversalCandidate;
import org.algorithmx.rulii.validation.types.AnnotatedTypeDefinition;
import org.algorithmx.rulii.validation.types.AnnotatedTypeValueExtractor;
import org.algorithmx.rulii.validation.types.ExtractedTypeValue;
import org.algorithmx.rulii.validation.types.MarkedAnnotation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanValidator extends ObjectVisitorTemplate {

    private static final Map<Class<?>, AnnotatedBeanTypeDefinition> definitionMap = new ConcurrentReferenceHashMap<>();

    private RuleContext context;
    private RuleViolations violations;

    public BeanValidator() {
        super();
    }

    public RuleViolations validate(RuleContext context, Object bean) {
        Assert.notNull(context, "context cannot be null.");
        Assert.notNull(bean, "bean cannot be null.");

        this.context = context;
        this.violations = new RuleViolations();

        context.getBindings().bind("errors", violations);

        ObjectGraph graph = new ObjectGraph();
        graph.traverse(new TraversalCandidate(bean, null), this);
        System.err.println("XXX Violations [" + violations + "]");
        return violations;
    }

    public RuleViolations getViolations() {
        return violations;
    }

    public TraversalCandidate[] visitObjectStart(TraversalCandidate candidate) {
        Bindings beanScope = createBeanBindings(candidate.getTarget());
        beanScope.bind("$value", candidate.getTarget());
        RuleViolations violations = new RuleViolations();
        beanScope.bind("errors", violations);

        try {
            context.getBindings().addScope("beanScope", beanScope);
            runRules(candidate.getTypeDefinition());
        } finally {
            context.getBindings().removeScope(beanScope);
            if (violations.size() > 0) {
                // TODO : Decorate the error
                Arrays.stream(violations.getViolations()).forEach(v -> this.violations.add(v));
            }
        }

        if (candidate.isNull()) return null;
        List<TraversalCandidate> result = introspectCandidate(candidate);
        return result != null ? result.toArray(new TraversalCandidate[result.size()]) : null;
    }

    protected void runRules(AnnotatedTypeDefinition definition) {
        List<Runnable> rules = createRules(context, definition, "$value");

        if (rules != null) {
            //System.err.println();

            for (Runnable runnable : rules) {
                runnable.run(context);
                //System.err.println(runnable);
            }
        }
    }

    protected List<TraversalCandidate> introspectCandidate(TraversalCandidate candidate) {

        /*System.err.println("XXX Object Start [" + candidate.getTarget().getClass().getSimpleName() + "] Field ["
                + candidate.getName() + "] Value [" + candidate.getTarget() + "]  Introspect ["
                + (candidate.getTypeDefinition() != null ? candidate.getTypeDefinition().requiresIntrospection() : false)
                + "] Rules [" + (candidate.getTypeDefinition() != null ? candidate.getTypeDefinition().hasDeclaredRules() : false) + "]");*/

        if (candidate.isNull()) return null;
        if (candidate.getTypeDefinition() != null && !candidate.getTypeDefinition().requiresIntrospection()) return null;
        // TODO : Handle @Validate for Collection/Arrays or any extractable type
        if (ReflectionUtils.isJavaCoreClass(candidate.getTarget().getClass())) return null;

        AnnotatedBeanTypeDefinition typeDefinition = getAnnotatedBeanTypeDefinition(candidate.getTarget().getClass());

        List<TraversalCandidate> result = new ArrayList<>();

        result.addAll(findCandidates(typeDefinition.getFields(), candidate.getTarget()));
        result.addAll(findCandidates(typeDefinition.getProperties(), candidate.getTarget()));

        return result;
    }

    protected List<TraversalCandidate> findCandidates(SourceHolder[] fields, Object target) {
        List<TraversalCandidate> result = new ArrayList<>();

        for (SourceHolder holder : fields) {
            List<TraversalCandidate> candidates = extractCandidates(holder, holder.getValue(target), holder.getDefinition());
            candidates.stream()
                    .filter(c -> c.getTypeDefinition().requiresProcessing())
                    .forEach(c -> result.add(c));
        }

        return result;
    }

    protected List<TraversalCandidate> extractCandidates(SourceHolder sourceHolder, Object target,
                                                         AnnotatedTypeDefinition typeDefinition) {
        AnnotatedTypeValueExtractor valueExtractor = new AnnotatedTypeValueExtractor();
        List<ExtractedTypeValue> extractedValues = valueExtractor.extract(typeDefinition, target, context.getExtractorRegistry());
        List<TraversalCandidate> result = new ArrayList<>();

        for (ExtractedTypeValue extractedValue : extractedValues) {
            if (extractedValue.getDefinition().hasDeclaredRules() || extractedValue.getDefinition().requiresIntrospection())
                result.add(new TraversalCandidate(extractedValue.getObject(), sourceHolder.copy(extractedValue.getDefinition())));
        }

        return result;
    }

    protected AnnotatedBeanTypeDefinition getAnnotatedBeanTypeDefinition(Class<?> type) {
        AnnotatedBeanTypeDefinition result = definitionMap.get(type);

        if (result != null) return result;

        AnnotatedBeanTypeDefinitionBuilder builder = AnnotatedBeanTypeDefinitionBuilder
                .with(type, Validate.class, ValidationMarker.class);

        builder.loadFields();
        builder.loadProperties();
        builder.loadMethods();

        result = builder.build();
        definitionMap.putIfAbsent(type, result);

        return result;
    }

    protected List<Runnable> createRules(RuleContext context, AnnotatedTypeDefinition definition, String bindingName) {
        if (definition == null || !definition.hasDeclaredRules()) return null;

        List<Runnable> result = new ArrayList<>();

        for (MarkedAnnotation marker : definition.getDeclaredRuleAnnotations()) {
            ValidationMarker validationRule = (ValidationMarker) marker.getMarker();

            if (validationRule == null) {
                // TODO : log it
                continue;
            }

            AnnotatedRunnableBuilder builder = context.getObjectFactory().create(validationRule.value(), true);
            result.addAll(Arrays.asList(builder.build(marker.getOwner(), bindingName)));
        }

        return result;
    }

    protected Bindings createBeanBindings(Object bean) {
        if (bean == null) return Bindings.create();

        Bindings result = Bindings.create();
        FieldBindingLoader fieldLoader = new FieldBindingLoader();
        fieldLoader.load(result, bean);

        Set<String> names = result.getNames();

        PropertyBindingLoader propertyLoader = new PropertyBindingLoader();
        propertyLoader.setIgnoreProperties(names);
        propertyLoader.load(result, bean);

        return result;
    }

}
