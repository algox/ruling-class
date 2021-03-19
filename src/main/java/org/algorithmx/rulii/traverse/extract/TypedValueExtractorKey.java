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

import java.util.Objects;

public class TypedValueExtractorKey {

    private final Class<?> containerType;
    private final int index;
    private final int hashCode;

    public TypedValueExtractorKey(Class<?> containerType, int index) {
        super();
        this.containerType = containerType;
        this.index = index;
        this.hashCode = Objects.hash(containerType, index);
    }

    public Class<?> getContainerType() {
        return containerType;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypedValueExtractorKey that = (TypedValueExtractorKey) o;
        return index == that.index &&
                containerType.equals(that.containerType);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}
