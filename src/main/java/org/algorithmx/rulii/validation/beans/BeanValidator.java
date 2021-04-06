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
import org.algorithmx.rulii.validation.AnnotatedRunnableBuilder;
import org.algorithmx.rulii.validation.RuleViolations;
import org.algorithmx.rulii.validation.graph.ObjectGraph;
import org.algorithmx.rulii.validation.graph.ObjectVisitorTemplate;
import org.algorithmx.rulii.validation.graph.TraversalCandidate;
import org.algorithmx.rulii.validation.types.AnnotatedTypeDefinition;
import org.algorithmx.rulii.validation.types.MarkedAnnotation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanValidator extends ObjectVisitorTemplate {

    private static final String DEFAULT_VALUE_BINDING_NAME = "$value";
    private static final Map<Class<?>, AnnotatedBeanTypeDefinition> definitionMap = new ConcurrentReferenceHashMap<>();

    private RuleContext context;
    private RuleViolations violations;
    private String valueBindingName = DEFAULT_VALUE_BINDING_NAME;

    public BeanValidator() {
        super();
    }

    public RuleViolations validateBean(RuleContext context, Object bean) {
        return validateBean(context, new TraversalCandidate(bean, null));
    }

    public RuleViolations validateBean(RuleContext context, TraversalCandidate candidate) {
        Assert.notNull(context, "context cannot be null.");
        Assert.notNull(candidate, "candidate cannot be null.");

        this.context = context;
        this.violations = new RuleViolations();

        context.getBindings().bind("errors", violations);

        ObjectGraph graph = new ObjectGraph(ValidationMarker.class, Validate.class, context.getExtractorRegistry());
        graph.traverse(candidate, this);
        System.err.println("XXX Violations [" + violations + "]");
        return violations;
    }

    public boolean visitCandidate(TraversalCandidate candidate) {
        Bindings beanScope = createBeanBindings(candidate.getTarget());
        RuleViolations violations = new RuleViolations();

        beanScope.bind(getValueBindingName(), candidate.getTarget());
        beanScope.bind("errors", violations);

        try {
            context.getBindings().addScope("beanScope", beanScope);
            runRules(candidate.getTypeDefinition());
            decorateAndTransferViolations(violations, this.violations, candidate.getSourceHolder());
        } finally {
            context.getBindings().removeScope(beanScope);
        }

        if (candidate.isNull()) return false;

        return candidate.getTypeDefinition() == null || candidate.getTypeDefinition().requiresIntrospection();
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

    protected void decorateAndTransferViolations(RuleViolations source, RuleViolations target, SourceHolder holder) {
        if (source != null && source.size() > 0) {
            Arrays.stream(source.getViolations())
                    .forEach(v -> {
                        v.param("field", holder.getName());
                        target.add(v);
                    });
        }
    }

    public String getValueBindingName() {
        return valueBindingName;
    }

    public void setValueBindingName(String valueBindingName) {
        Assert.notNull(valueBindingName, "valueBindingName cannot be null.");
        this.valueBindingName = valueBindingName;
    }
}
