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

import org.algorithmx.rulii.traverse.extract.standard.AtomicReferenceValueExtractor;
import org.algorithmx.rulii.traverse.extract.standard.CollectionValueExtractor;
import org.algorithmx.rulii.traverse.extract.standard.DictionaryKeyExtractor;
import org.algorithmx.rulii.traverse.extract.standard.DictionaryValueExtractor;
import org.algorithmx.rulii.traverse.extract.standard.IterableValueExtractor;
import org.algorithmx.rulii.traverse.extract.standard.MapKeyValueExtractor;
import org.algorithmx.rulii.traverse.extract.standard.MapValueValueExtractor;
import org.algorithmx.rulii.traverse.extract.standard.OptionalValueExtractor;
import org.algorithmx.rulii.traverse.extract.standard.ReferenceValueExtractor;
import org.algorithmx.rulii.traverse.extract.standard.ThreadLocalValueExtractor;
import org.algorithmx.rulii.lib.spring.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultExtractorRegistry implements ExtractorRegistry {

    private final List<TypedValueExtractor<?>> extractors = Collections.synchronizedList(new ArrayList<>(256));
    private final Map<TypedValueExtractorKey, TypedValueExtractor<?>> extractorCache = new ConcurrentHashMap();

    public DefaultExtractorRegistry() {
        this(true);
    }

    public DefaultExtractorRegistry(boolean registerDefaults) {
        super();
        if (registerDefaults) init();
    }

    @Override
    public <T> void register(TypedValueExtractor<T> extractor) {
        Assert.notNull(extractor, "Extractor cannot be null.");
        extractors.add(extractor);
    }

    @Override
    public <T> TypedValueExtractor<T> find(Class<?> container, int index) {
        TypedValueExtractor result = extractorCache.get(new TypedValueExtractorKey(container, index));

        if (result != null) return result;

        int size = extractors.size();

        for (int i = size - 1; i >=0; i--) {
            if (extractors.get(i).canExtract(container, index)) {
                result = extractors.get(i);
                break;
            }
        }

        if (result != null) extractorCache.put(new TypedValueExtractorKey(container, index), result);

        return result;
    }

    protected void init() {
        register(new CollectionValueExtractor());
        register(new MapKeyValueExtractor());
        register(new MapValueValueExtractor());
        register(new IterableValueExtractor());
        register(new OptionalValueExtractor());
        register(new ReferenceValueExtractor());
        register(new ThreadLocalValueExtractor());
        register(new AtomicReferenceValueExtractor());
        register(new DictionaryKeyExtractor());
        register(new DictionaryValueExtractor());
    }
}
