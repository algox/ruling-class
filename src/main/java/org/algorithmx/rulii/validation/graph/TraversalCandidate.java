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

package org.algorithmx.rulii.validation.graph;

import org.algorithmx.rulii.validation.types.AnnotatedTypeDefinition;
import org.algorithmx.rulii.validation.beans.SourceHolder;

import java.util.Collection;
import java.util.Map;

public class TraversalCandidate {

    private final Object target;
    private final SourceHolder holder;

    public TraversalCandidate(Object target, SourceHolder holder) {
        super();
        this.target = target;
        this.holder = holder;
    }

    public Object getTarget() {
        return target;
    }

    public SourceHolder getSourceHolder() {
        return holder;
    }

    public Object getSource() {
        return holder != null ? holder.getSource() : null;
    }

    public AnnotatedTypeDefinition getTypeDefinition() {
        return holder != null ? holder.getDefinition() : null;
    }

    public String getName() {
        return holder != null ? holder.getName() : null;
    }

    public boolean isNull() {
        return getTarget() == null;
    }

    public boolean isArray() {
        if (isNull()) return false;
        return getTarget().getClass().isArray();
    }

    public boolean isCollection() {
        if (isNull()) return false;
        return getTarget() instanceof Collection;
    }

    public boolean isMap() {
        if (isNull()) return false;
        return getTarget() instanceof Map;
    }

    public boolean isIterable() {
        if (isNull()) return false;
        return getTarget() instanceof Map;
    }
}
