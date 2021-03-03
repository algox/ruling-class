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

import org.algorithmx.rulii.extract.Extractor;
import org.algorithmx.rulii.extract.TypedValueExtractor;
import org.algorithmx.rulii.lib.spring.core.annotation.MergedAnnotation;
import org.algorithmx.rulii.lib.spring.core.annotation.MergedAnnotations;
import org.algorithmx.rulii.validation.annotation.Validate;
import org.algorithmx.rulii.validation.annotation.ValidationRule;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractAnnotatedTypeDefinition<T extends AnnotatedType> implements AnnotatedTypeDefinition<T> {

    protected final T annotatedType;
    protected final AnnotatedTypeKind kind;
    protected final Class<? extends TypedValueExtractor> extractor;
    protected final Annotation[] ruleAnnotations;
    protected final boolean requiresValidation;
    protected final boolean requiresTraversal;

    protected AbstractAnnotatedTypeDefinition(T annotatedType, AnnotatedTypeKind kind, boolean requiresTraversal) {
        super();
        this.annotatedType = annotatedType;
        this.kind = kind;
        this.extractor = findExtractor(annotatedType);
        this.ruleAnnotations = findRuleAnnotations(annotatedType);
        this.requiresValidation = isAnnotatedWithValid(annotatedType);
        this.requiresTraversal = requiresTraversal;
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
    public Class<? extends TypedValueExtractor> getCustomExtractor() {
        return extractor;
    }

    @Override
    public Annotation[] getDeclaredRuleAnnotations() {
        return ruleAnnotations;
    }

    @Override
    public boolean requiresValidation() {
        return requiresValidation;
    }

    @Override
    public boolean requiresTraversal() {
        return requiresTraversal;
    }

    protected static Class<? extends TypedValueExtractor> findExtractor(AnnotatedType annotatedType) {
        Extractor result = annotatedType.getAnnotation(Extractor.class);
        return result != null ? result.value() : null;
    }

    protected static Annotation[] findRuleAnnotations(AnnotatedType annotatedType) {
        List<Annotation> result = new ArrayList<>();
        MergedAnnotations.from(annotatedType, MergedAnnotations.SearchStrategy.DIRECT)
                .stream(ValidationRule.class)
                .forEach(a -> result.add(a.synthesize()));
        return result.toArray(new Annotation[result.size()]);
    }

    protected static boolean deriveRequiresTraversal(AnnotatedTypeDefinition...types) {
        boolean result = false;

        for (AnnotatedTypeDefinition typeArgument : types) {
            result = typeArgument.hasDeclaredRuleAnnotations() || typeArgument.requiresTraversal();
            if (result) break;
        }

        return result;
    }

    protected static boolean isAnnotatedWithValid(AnnotatedType annotatedType) {
        MergedAnnotation<Validate> mergedAnnotation = MergedAnnotations.from(annotatedType).get(Validate.class);
        Validate result = mergedAnnotation != null && mergedAnnotation.isPresent() ? mergedAnnotation.synthesize() : null;
        return result != null;
    }

    @Override
    public String toString() {
        return "kind=" + kind +
                ", extractor=" + extractor +
                ", ruleAnnotations=" + Arrays.toString(ruleAnnotations) +
                ", requiresValidation=" + requiresValidation +
                ", requiresTraversal=" + requiresTraversal + " ";
    }
}
