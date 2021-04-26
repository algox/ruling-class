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
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.core.model.MethodDefinition;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.validation.RuleViolations;
import org.algorithmx.rulii.validation.types.AnnotatedTypeDefinition;
import org.algorithmx.rulii.validation.types.AnnotatedTypeDefinitionBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class MethodValidator {

    private RuleViolations violations;
    private MethodDefinition methodDefinition;

    public MethodValidator() {
        super();
    }

    public RuleViolations validateBefore(RuleContext context, Method method, Object...args) {
        Assert.notNull(context, "context cannot be null.");
        Assert.notNull(method, "method cannot be null.");

        this.violations = new RuleViolations();
        this.methodDefinition = MethodDefinition.load(method);

        Assert.isTrue((methodDefinition.getParameterCount() == 0 && (args == null || args.length == 0))
                || methodDefinition.getParameterCount() == args.length, "Args dont match the required method params");

        Bindings rootMethodScope = createRootMethodScope(methodDefinition, args);
        context.getBindings().addScope("rootMethodScope", rootMethodScope);

        try {
            for (int i = 0; i < methodDefinition.getParameterCount(); i++) {
                RuleViolations parameterViolations = validateParam(context, methodDefinition, i, args[i]);
                this.violations.add(parameterViolations.getViolations());
            }
        } catch (Exception e) {
            throw new MethodValidationException(method, violations, "Error trying to validate method [" + method + "]", e);
        } finally {
            context.getBindings().removeScope(rootMethodScope);
        }

        return violations;
    }

    public RuleViolations validateAfter(RuleContext context, Method method, Object result, Object...args) {
        Assert.notNull(context, "context cannot be null.");
        Assert.notNull(method, "method cannot be null.");

        this.violations = new RuleViolations();
        this.methodDefinition = MethodDefinition.load(method);

        if (methodDefinition.getReturnType().equals(void.class) || methodDefinition.getReturnType().equals(Void.class)) {
            return violations;
        }

        Assert.isTrue((methodDefinition.getParameterCount() == 0 && (args == null || args.length == 0))
                || methodDefinition.getParameterCount() == args.length, "Args dont match the required method params");

        Bindings rootMethodScope = createRootMethodScope(methodDefinition, args);
        bindResult(rootMethodScope, result);
        context.getBindings().addScope("rootMethodScope", rootMethodScope);

        AnnotatedTypeDefinition typeDefinition = AnnotatedTypeDefinitionBuilder
                .with(methodDefinition.getReturnTypeDefinition().getAnnotatedType(),
                        getIntrospectionAnnotation(), getMarkerAnnotation())
                .build();
        MethodReturnTypeHolder holder = new MethodReturnTypeHolder(methodDefinition.getMethod(),
                methodDefinition.getReturnTypeDefinition(), typeDefinition, result);
        BeanValidator beanValidator = new BeanValidator();

        return beanValidator.validate(context, result, holder);
    }

    public RuleViolations validateParam(RuleContext context, MethodDefinition methodDefinition, int index, Object value) {
        AnnotatedTypeDefinition typeDefinition = AnnotatedTypeDefinitionBuilder
                .with(methodDefinition.getParameterDefinition(index).getAnnotatedType(),
                        getIntrospectionAnnotation(), getMarkerAnnotation())
                .build();
        MethodParameterHolder holder = new MethodParameterHolder(methodDefinition.getMethod(),
                methodDefinition.getParameterDefinition(index), typeDefinition, value);
        BeanValidator beanValidator = new BeanValidator();

        return beanValidator.validate(context, value, holder);
    }

    protected Bindings createRootMethodScope(MethodDefinition definition, Object...args) {
        Bindings result = Bindings.create();

        if (definition.getParameterCount() == 0) return result;

        for (int i = 0; i < definition.getParameterCount(); i++) {
            result.bind(definition.getParameterDefinition(i).getName(), args[i]);
        }

        return result;
    }

    protected void bindResult(Bindings bindings, Object result) {
        bindings.bind("result", result);
    }

    protected Class<? extends Annotation> getMarkerAnnotation() {
        return ValidationMarker.class;
    }

    protected Class<? extends Annotation> getIntrospectionAnnotation() {
        return Validate.class;
    }

}
