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

package org.algorithmx.rulii.traverse.extract;

import org.algorithmx.rulii.lib.spring.util.Assert;

import java.util.Arrays;

public class ExtractedValue<T, R> {

    private String name;
    private final T parent;
    private final int index;
    private final boolean container;
    private final R[] values;

    public ExtractedValue(String name, T parent, int index, boolean container, R...values) {
        super();
        Assert.notNull(name, "name cannot be null.");
        Assert.isTrue(index >= 0, "index must be > 0.");
        this.name = name;
        this.parent = parent;
        this.index = index;
        this.container = container;
        this.values = values;
    }

    public String getName() {
        return name;
    }

    public T getParent() {
        return parent;
    }

    public int getIndex() {
        return index;
    }

    public boolean isContainer() {
        return container;
    }

    public R[] getValues() {
        return values;
    }

    @Override
    public String toString() {
        return "ExtractedValue{" +
                "name='" + name + '\'' +
                ", parent=" + parent +
                ", index=" + index +
                ", values=" + Arrays.toString(values) +
                '}';
    }
}
