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

import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.traverse.extract.TypedValueExtractor;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedTypeVariable;
import java.util.Arrays;

public class AnnotatedTypeVariableDefinition extends AbstractAnnotatedTypeDefinition<AnnotatedTypeVariable> {

    private final AnnotatedTypeDefinition[] bounds;

    public AnnotatedTypeVariableDefinition(AnnotatedTypeVariable annotatedType,
                                           RuleAnnotations[] ruleAnnotations,
                                           Annotation traversalAnnotation,
                                           AnnotatedTypeDefinition...bounds) {
        super(annotatedType, AnnotatedTypeKind.TYPE_VARIABLE_TYPE, ruleAnnotations,
                traversalAnnotation, doChildrenHaveRuleAnnotations(bounds),
                doChildrenRequireIntrospection(bounds));
        Assert.notNull(bounds, "bounds cannot be null.");
        this.bounds = bounds;
    }

    public AnnotatedTypeDefinition[] getBounds() {
        return bounds;
    }

    @Override
    public String toString() {
        return "TypeVariableDefinition{" +
                "bounds=" + Arrays.toString(bounds) +
                '}';
    }
}
