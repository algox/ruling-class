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

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;

public class SimpleAnnotatedTypeDefinition extends AbstractAnnotatedTypeDefinition<AnnotatedType> {

    private final Class<?> type;

    public SimpleAnnotatedTypeDefinition(AnnotatedType annotatedType,
                                         Annotation traversalAnnotation,
                                         MarkedAnnotation[] ruleAnnotations) {
        super(annotatedType, AnnotatedTypeKind.SIMPLE_TYPE, ruleAnnotations, traversalAnnotation,
                false, false);
        this.type = deriveType(annotatedType);
    }

    public Class<?> getType() {
        return type;
    }

    private static Class<?> deriveType(AnnotatedType annotatedType) {
        return (Class<?>) annotatedType.getType();
    }

    @Override
    public AnnotatedTypeDefinition[] getAllChildren() {
        return new AnnotatedTypeDefinition[0];
    }

    public String getSignatureX() {
        return "<" + getDeclaredRuleSignature() + type.getSimpleName() + ">";
    }

    @Override
    public String toString() {
        return "SimpleTypeDefinition{" +
                super.toString() +
                "type=" + type +
                '}';
    }
}
