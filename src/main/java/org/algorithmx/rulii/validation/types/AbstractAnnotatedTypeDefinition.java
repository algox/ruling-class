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

package org.algorithmx.rulii.validation.types;

import org.algorithmx.rulii.annotation.Extract;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.util.reflect.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.util.Arrays;
import java.util.Objects;

public abstract class AbstractAnnotatedTypeDefinition<T extends AnnotatedType> implements AnnotatedTypeDefinition<T> {

    private final T annotatedType;
    private final AnnotatedTypeKind kind;
    private final Extract extractAnnotation;
    private final Annotation introspectionAnnotation;
    private final MarkedAnnotation[] ruleAnnotations;
    private final boolean childrenHaveRules;
    private final boolean childrenRequireIntrospection;
    private AnnotatedTypeDefinition parent;

    protected AbstractAnnotatedTypeDefinition(T annotatedType, AnnotatedTypeKind kind,
                                              Extract extractAnnotation,
                                              Annotation introspectionAnnotation,
                                              MarkedAnnotation[] ruleAnnotations,
                                              boolean childrenHaveRules,
                                              boolean childrenRequireIntrospection) {
        super();
        Assert.notNull(annotatedType, "annotatedType cannot be null.");
        Assert.notNull(kind, "kind cannot be null.");
        this.annotatedType = annotatedType;
        this.kind = kind;
        this.extractAnnotation = extractAnnotation;
        this.introspectionAnnotation = introspectionAnnotation;
        this.ruleAnnotations = ruleAnnotations;
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
    public Annotation getIntrospectionAnnotation() {
        return introspectionAnnotation;
    }

    @Override
    public Extract getExtractAnnotation() {
        return extractAnnotation;
    }

    @Override
    public MarkedAnnotation[] getDeclaredRuleAnnotations() {
        return ruleAnnotations;
    }

    @Override
    public AnnotatedTypeDefinition getParent() {
        return parent;
    }

    void setParent(AnnotatedTypeDefinition parent) {
        this.parent = parent;
    }

    @Override
    public boolean childrenHaveRules() {
        return childrenHaveRules;
    }

    @Override
    public boolean childrenRequireIntrospection() {
        return childrenRequireIntrospection;
    }

    protected static boolean childrenHaveRuleAnnotations(AnnotatedTypeDefinition...types) {
        if (types == null) return false;

        boolean result = false;

        for (AnnotatedTypeDefinition typeArgument : types) {
            result = typeArgument.hasDeclaredRules() || typeArgument.childrenHaveRules();
            if (result) break;
        }

        return result;
    }

    protected static void establishParent(AnnotatedTypeDefinition parent, AnnotatedTypeDefinition...types) {
        if (types == null) return;

        for (AnnotatedTypeDefinition typeDefinition : types) {
            ((AbstractAnnotatedTypeDefinition) typeDefinition).setParent(parent);
        }
    }


    protected static boolean childrenRequireIntrospection(AnnotatedTypeDefinition...types) {
        if (types == null) return false;

        boolean result = false;

        for (AnnotatedTypeDefinition typeArgument : types) {
            result = typeArgument.isIntrospectionRequired() || typeArgument.childrenRequireIntrospection();
            if (result) break;
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractAnnotatedTypeDefinition<?> that = (AbstractAnnotatedTypeDefinition<?>) o;
        return annotatedType.equals(that.annotatedType) &&
                kind == that.kind;
    }

    @Override
    public int hashCode() {
        return Objects.hash(annotatedType, kind);
    }

    @Override
    public final String getSignature() {
        return "<" + getDeclaredRuleSignature() + getAnnotatedType().getType().toString() + ">";
    }

    protected String getDeclaredRuleSignature() {
        StringBuilder result = new StringBuilder();

        if (hasDeclaredRules()) {
            for (MarkedAnnotation annotation : getDeclaredRuleAnnotations()) {
                if (annotation.getOwner() != null) {
                    result.append("@" + ReflectionUtils.getAnnotationText(annotation.getOwner()) + " ");
                }
            }
        }

        return result.toString();
    }

    @Override
    public String toString() {
        return "{" +
                "kind=" + kind +
                ", ruleAnnotations=" + Arrays.toString(ruleAnnotations) +
                ", extractAnnotation=" + extractAnnotation +
                ", introspectionAnnotation=" + introspectionAnnotation +
                ", childrenHaveRules=" + childrenHaveRules +
                ", childrenRequireIntrospection=" + childrenRequireIntrospection +
                '}';
    }
}
