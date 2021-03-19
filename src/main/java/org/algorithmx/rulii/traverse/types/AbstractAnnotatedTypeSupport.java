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

import org.algorithmx.rulii.lib.spring.core.annotation.MergedAnnotation;
import org.algorithmx.rulii.lib.spring.core.annotation.MergedAnnotations;
import org.algorithmx.rulii.lib.spring.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAnnotatedTypeSupport<M extends Annotation, T extends Annotation> implements AnnotatedTypeSupport<M, T> {

    private final Class<? extends M> ruleMarkerAnnotationType;
    private final Class<? extends T> traverseAnnotationType;

    public AbstractAnnotatedTypeSupport(Class<? extends M> ruleMarkerAnnotationType) {
        this(ruleMarkerAnnotationType, null);
    }

    public AbstractAnnotatedTypeSupport(Class<? extends M> ruleMarkerAnnotationType,
                                        Class<? extends T> traverseAnnotationType) {
        super();
        Assert.notNull(ruleMarkerAnnotationType, "ruleMarkerAnnotationType cannot be null.");
        Assert.notNull(traverseAnnotationType, "traverseAnnotation cannot be null.");
        this.ruleMarkerAnnotationType = ruleMarkerAnnotationType;
        this.traverseAnnotationType = traverseAnnotationType;
    }

    @Override
    public Class<? extends M> getRuleMarkerAnnotationType() {
        return ruleMarkerAnnotationType;
    }

    @Override
    public Class<? extends T> getTraverseAnnotationType() {
        return traverseAnnotationType;
    }

    @Override
    public RuleAnnotations<M>[] extractRuleAnnotations(AnnotatedType annotatedType) {
        List<RuleAnnotations> result = new ArrayList<>();

        MergedAnnotations.from(annotatedType, MergedAnnotations.SearchStrategy.DIRECT)
                .stream(getRuleMarkerAnnotationType())
                .forEach(a -> {
                    result.add(new RuleAnnotations(a.synthesize(), a.getMetaSource().synthesize()));
                });

        return result.toArray(new RuleAnnotations[result.size()]);
    }

    public T extractTraversalAnnotation(AnnotatedType annotatedType) {
        if (getTraverseAnnotationType() == null) return null;

        MergedAnnotation<?> mergedAnnotation = MergedAnnotations.from(annotatedType).get(getTraverseAnnotationType());
        return mergedAnnotation != null && mergedAnnotation.isPresent() ? (T) mergedAnnotation.synthesize() : null;
    }
}
