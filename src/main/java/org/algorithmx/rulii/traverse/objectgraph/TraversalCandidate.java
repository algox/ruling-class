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

package org.algorithmx.rulii.traverse.objectgraph;

import org.algorithmx.rulii.traverse.types.AnnotatedTypeDefinition;

import java.util.Collection;
import java.util.Map;

public class TraversalCandidate {

    private final Object target;
    private final AnnotatedTypeDefinition typeDefinition;

    TraversalCandidate(Object target, AnnotatedTypeDefinition typeDefinition) {
        super();
        this.target = target;
        this.typeDefinition = typeDefinition;
    }

    public boolean isNull() {
        return target == null;
    }

    public boolean isArray() {
        if (isNull()) return false;
        return target.getClass().isArray();
    }

    public boolean isCollection() {
        if (isNull()) return false;
        return target instanceof Collection;
    }

    public boolean isMap() {
        if (isNull()) return false;
        return target instanceof Map;
    }

    public boolean isIterable() {
        if (isNull()) return false;
        return target instanceof Map;
    }

    public Object getTarget() {
        return target;
    }

    public AnnotatedTypeDefinition getTypeDefinition() {
        return typeDefinition;
    }

    @Override
    public String toString() {
        return "TraversalCandidate{" +
                "target=" + target +
                ", typeDefinition=" + typeDefinition +
                '}';
    }
}
