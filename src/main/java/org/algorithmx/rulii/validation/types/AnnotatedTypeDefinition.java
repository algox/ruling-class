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
import java.util.Arrays;

public interface AnnotatedTypeDefinition<T extends AnnotatedType> {

    T getAnnotatedType();

    AnnotatedTypeKind getKind();

    boolean childrenHaveRules();

    default boolean childrenRequireIntrospection() {
        return false;
    }

    Annotation getIntrospectionAnnotation();

    default boolean requiresIntrospection() {
        return getIntrospectionAnnotation() != null;
    }

    MarkedAnnotation[] getDeclaredRuleAnnotations();

    default boolean hasDeclaredRules() {
         return getDeclaredRuleAnnotations() != null &&  getDeclaredRuleAnnotations().length > 0;
     }

    AnnotatedTypeDefinition[] getAllChildren();

    default String getSignature() {
        StringBuilder result = new StringBuilder();

        if (hasDeclaredRules()) {
             for (MarkedAnnotation annotation : getDeclaredRuleAnnotations()) {
                 result.append(annotation.getOwner() + System.lineSeparator());
             }
        }

        result.append(getAnnotatedType().getType().toString());

        return result.toString();
    }

    default boolean requiresProcessing() {
        return hasDeclaredRules() || requiresIntrospection();
    }
}
