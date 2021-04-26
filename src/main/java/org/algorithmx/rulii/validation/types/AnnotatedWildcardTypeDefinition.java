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

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedWildcardType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class AnnotatedWildcardTypeDefinition extends AbstractAnnotatedTypeDefinition<AnnotatedWildcardType> {

    private final AnnotatedTypeDefinition[] lowerbounds;
    private final AnnotatedTypeDefinition[] upperbounds;

    public AnnotatedWildcardTypeDefinition(AnnotatedWildcardType annotatedType,
                                           Extract extract,
                                           Annotation traversalAnnotation,
                                           MarkedAnnotation[] ruleAnnotations,
                                           AnnotatedTypeDefinition[] lowerbounds,
                                           AnnotatedTypeDefinition[] upperbounds) {
        super(annotatedType, AnnotatedTypeKind.WILDCARD_TYPE,
                extract,
                traversalAnnotation,
                ruleAnnotations,
                childrenHaveRuleAnnotations(lowerbounds) || childrenHaveRuleAnnotations(upperbounds),
                childrenRequireIntrospection(lowerbounds)
                        || childrenRequireIntrospection(upperbounds));
        this.lowerbounds = lowerbounds != null ? lowerbounds : new AnnotatedTypeDefinition[0];
        this.upperbounds = upperbounds != null ? upperbounds : new AnnotatedTypeDefinition[0];
        establishParent(this, this.lowerbounds);
        establishParent(this, this.upperbounds);
    }

    public AnnotatedTypeDefinition[] getLowerBounds() {
        return lowerbounds;
    }

    public AnnotatedTypeDefinition[] getUpperBounds() {
        return upperbounds;
    }

    @Override
    public AnnotatedTypeDefinition[] getAllChildren() {
        List<AnnotatedTypeDefinition> result = new ArrayList<>();
        result.addAll(Arrays.asList(lowerbounds));
        result.addAll(Arrays.asList(upperbounds));

        result.stream()
                .filter(b -> b != null)
                .forEach(b -> result.addAll(Arrays.asList(b.getAllChildren())));

        return result.toArray(new AnnotatedTypeDefinition[result.size()]);
    }

    private Type[] getBounds(AnnotatedTypeDefinition[] bounds) {
        return Arrays.stream(bounds)
                .filter(def -> def != null && def.getAnnotatedType() != null)
                .map(def -> def.getAnnotatedType().getType())
                .toArray(Type[]::new);
    }

    public String getSignatureX() {
        Type[] lowerBounds = getBounds(getLowerBounds());
        Type[] bounds = lowerBounds;
        StringBuilder sb = new StringBuilder();

        if (lowerBounds.length > 0)
            sb.append("? super ");
        else {
            Type[] upperBounds = getBounds(getUpperBounds());

            if (upperBounds.length > 0 && !upperBounds[0].equals(Object.class) ) {
                bounds = upperBounds;
                sb.append("? extends ");
            } else
                return "?";
        }

        StringJoiner sj = new StringJoiner(" & ");

        for (Type bound: bounds) {
            sj.add(bound.getTypeName());
        }

        sb.append(sj.toString());

        return sb.toString();
    }

}

