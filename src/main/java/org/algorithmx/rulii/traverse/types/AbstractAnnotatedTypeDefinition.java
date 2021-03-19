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

package org.algorithmx.rulii.traverse.types;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.util.Arrays;

public abstract class AbstractAnnotatedTypeDefinition<T extends AnnotatedType> implements AnnotatedTypeDefinition<T> {

    private final T annotatedType;
    private final AnnotatedTypeKind kind;
    private final RuleAnnotations[] ruleAnnotations;
    private final Annotation introspectionAnnotation;
    private final boolean childrenHaveRules;
    private final boolean childrenRequireIntrospection;

    protected AbstractAnnotatedTypeDefinition(T annotatedType, AnnotatedTypeKind kind,
                                              RuleAnnotations[] ruleAnnotations,
                                              Annotation introspectionAnnotation,
                                              boolean childrenHaveRules,
                                              boolean childrenRequireIntrospection) {
        super();
        this.annotatedType = annotatedType;
        this.kind = kind;
        this.ruleAnnotations = ruleAnnotations;
        this.introspectionAnnotation = introspectionAnnotation;
        this.childrenHaveRules = childrenHaveRules;
        this.childrenRequireIntrospection = childrenRequireIntrospection;
    }

    @Override
    public T getAnnotatedType() {
        return annotatedType;
    }

    @Override
    public AnnotatedTypeKind getKind() {
        return kind;
    }

    @Override
    public RuleAnnotations[] getDeclaredRuleAnnotations() {
        return ruleAnnotations;
    }

    @Override
    public Annotation getIntrospectionAnnotation() {
        return introspectionAnnotation;
    }

    @Override
    public boolean doChildrenHaveRules() {
        return childrenHaveRules;
    }

    @Override
    public boolean doChildrenRequireIntrospection() {
        return childrenRequireIntrospection;
    }

    protected static boolean doChildrenHaveRuleAnnotations(AnnotatedTypeDefinition...types) {
        boolean result = false;

        for (AnnotatedTypeDefinition typeArgument : types) {
            result = typeArgument.hasDeclaredRules() || typeArgument.doChildrenHaveRules();
            if (result) break;
        }

        return result;
    }

    protected static boolean doChildrenRequireIntrospection(AnnotatedTypeDefinition...types) {
        boolean result = false;

        for (AnnotatedTypeDefinition typeArgument : types) {
            result = typeArgument.requiresIntrospection() || typeArgument.doChildrenRequireIntrospection();
            if (result) break;
        }

        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "kind=" + kind +
                ", ruleAnnotations=" + Arrays.toString(ruleAnnotations) +
                ", introspectionAnnotation=" + introspectionAnnotation +
                ", childrenHaveRules=" + childrenHaveRules +
                ", childrenRequireIntrospection=" + childrenRequireIntrospection +
                '}';
    }
}
