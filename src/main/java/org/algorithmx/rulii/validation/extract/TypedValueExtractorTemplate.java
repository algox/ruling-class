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

package org.algorithmx.rulii.validation.extract;

import org.algorithmx.rulii.lib.spring.util.Assert;

public abstract class TypedValueExtractorTemplate<T> implements TypedValueExtractor<T> {

    private final int index;
    private final Class<?> containerType;
    private final TypedValueExtractorKey key;

    protected TypedValueExtractorTemplate(Class<?> containerType, int index) {
        super();
        Assert.isTrue(index >= 0, "index must be >= 0.");
        this.index = index;
        this.containerType = containerType;
        this.key = new TypedValueExtractorKey(containerType, index);
    }

    @Override
    public boolean canExtract(Class<?> containerType, int index) {
        return this.index == index && this.containerType.isAssignableFrom(containerType);
    }

    @Override
    public Class<?> getContainerType() {
        return containerType;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public TypedValueExtractorKey getKey() {
        return key;
    }
}
