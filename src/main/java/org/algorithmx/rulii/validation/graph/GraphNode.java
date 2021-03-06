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

import org.algorithmx.rulii.validation.beans.SourceHolder;
import org.algorithmx.rulii.validation.types.AnnotatedTypeDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class GraphNode {

    private static final AtomicInteger count = new AtomicInteger();

    private final int id;
    private final Object target;
    private final SourceHolder holder;
    private GraphNode parent;

    public GraphNode(Object target, SourceHolder holder) {
        super();
        this.id = count.addAndGet(1);
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

    public int getId() {
        return id;
    }

    public String getName() {
        return holder != null ? holder.getName() : null;
    }

    public boolean isNull() {
        return getTarget() == null;
    }

    public GraphNode getParent() {
        return parent;
    }

    public boolean hasParent() {
        return parent != null;
    }

    void setParent(GraphNode parent) {
        this.parent = parent;
    }

    public String getPath() {
        List<String> path = new ArrayList<>();
        GraphNode parent = this;

        while (parent != null) {
            String nodeName = parent.getSourceHolder() != null
                    ? parent.getSourceHolder().getName()
                    :  parent.getTarget().getClass().getSimpleName();
            path.add(0, nodeName);

            parent = parent.getParent();
        }

        String result = path.stream().collect(Collectors.joining("."));
        return result;
    }

    @Override
    public String toString() {
        return "TraversalCandidate{" +
                "id=" + id +
                " Source [" + (getSourceHolder() != null ? getSourceHolder().getName() : "n/a") + "]" +
                ", parent=" + (parent != null ? parent.getId() : "n/a") +
                " target=" + target +
                '}';
    }
}
