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
import java.lang.reflect.AnnotatedArrayType;

public class AnnotatedArrayTypeDefinition extends AbstractAnnotatedTypeDefinition<AnnotatedArrayType> {

    private final AnnotatedTypeDefinition componentType;

    public AnnotatedArrayTypeDefinition(AnnotatedArrayType annotatedType,
                                        RuleAnnotations[] ruleAnnotations,
                                        Annotation traversalAnnotation,
                                        AnnotatedTypeDefinition componentType) {
        super(annotatedType, AnnotatedTypeKind.ARRAY_TYPE, ruleAnnotations,
                traversalAnnotation, doChildrenHaveRuleAnnotations(componentType),
                doChildrenRequireIntrospection(componentType));
        Assert.notNull(componentType, "componentType cannot be null.");
        this.componentType = componentType;
    }

    public AnnotatedTypeDefinition getComponentType() {
        return componentType;
    }

    @Override
    public String toString() {
        return "ArrayTypeDefinition{" +
                "componentType=" + componentType +
                '}';
    }
}
