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

public interface TypedValueExtractor<T> {

    /**
     * Returns the container type.
     *
     * @return container type.
     */
    Class<?> getContainerType();

    int getIndex();

    TypedValueExtractorKey getKey();

    /**
     * Determines whether this extractor can be used to extract the desired value from the given container at
     * the specified index type
     *
     * @param containerType container type.
     * @param index Type index.
     * @return true if this extractor can be used to extract the desired value; false otherwise.
     */
    boolean canExtract(Class<?> containerType, int index);

    /**
     * Extract the value from the container at the specified type index.
     *
     * @param container container value.
     * @param processor listener to handle the results of the extraction.
     */
    void extract(T container, TypedValueProcessor processor);
}
