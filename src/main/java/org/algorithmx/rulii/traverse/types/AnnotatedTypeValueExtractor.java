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

package org.algorithmx.rulii.traverse.types;

import org.algorithmx.rulii.annotation.Validate;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.traverse.extract.DefaultExtractorRegistry;
import org.algorithmx.rulii.traverse.extract.ExtractorRegistry;
import org.algorithmx.rulii.traverse.extract.TypedValueExtractor;
import org.algorithmx.rulii.traverse.extract.TypedValueProcessor;
import org.algorithmx.rulii.validation.rules.notnull.NotNull;
import org.algorithmx.rulii.validation.types.ValidationAnnotatedTypeSupport;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotatedTypeValueExtractor {

    public AnnotatedTypeValueExtractor() {
        super();
    }

    public List<ExtractedTypeValue> extract(AnnotatedTypeDefinition definition, Object object, ExtractorRegistry extractorRegistry) {
        List<ExtractedTypeValue> extractedValues = new ArrayList<>();
        extractInternal(definition, object, extractorRegistry, extractedValues);
        return extractedValues;
    }

    public void extractInternal(AnnotatedTypeDefinition definition, Object object, ExtractorRegistry extractorRegistry,
                         List<ExtractedTypeValue> extractedValues) {
        Assert.notNull(definition, "definition cannot be null.");
        Assert.notNull(extractorRegistry, "extractorRegistry cannot be null.");

        // Add the candidate ?
        addExtractedValue(definition, object, extractedValues);

        if (object == null || (!definition.doChildrenRequireIntrospection() && !definition.doChildrenHaveRules())) return;

        if (definition.getKind() == AnnotatedTypeKind.PARAMETERIZED_TYPE) {
            visit((AnnotatedParameterizedTypeDefinition) definition, object, extractorRegistry, extractedValues);
        } else if (definition.getKind() == AnnotatedTypeKind.WILDCARD_TYPE) {
            visit((AnnotatedWildcardTypeDefinition) definition, object, extractorRegistry, extractedValues);
        } else if (definition.getKind() == AnnotatedTypeKind.TYPE_VARIABLE_TYPE) {
            visit((AnnotatedTypeVariableDefinition) definition, object, extractorRegistry, extractedValues);
        } else if (definition.getKind() == AnnotatedTypeKind.ARRAY_TYPE) {
            visit((AnnotatedArrayTypeDefinition) definition, object, extractorRegistry, extractedValues);
        }
    }

    public void visit(AnnotatedParameterizedTypeDefinition definition, Object object, ExtractorRegistry extractorRegistry,
                      List<ExtractedTypeValue> extractedValues) {
        for (int i = 0; i < definition.getTypeArguments().length; i++) {
            extractAndProcess(object, i, definition.getTypeArguments()[i], extractorRegistry, extractedValues);
        }
    }

    public void visit(AnnotatedWildcardTypeDefinition definition, Object object, ExtractorRegistry extractorRegistry,
                      List<ExtractedTypeValue> extractedValues) {
        for (int i = 0; i < definition.getBounds().length; i++) {
            extractAndProcess(object, i, definition.getBounds()[i], extractorRegistry, extractedValues);
        }
    }

    public void visit(AnnotatedTypeVariableDefinition definition, Object object, ExtractorRegistry extractorRegistry,
                      List<ExtractedTypeValue> extractedValues) {
        for (int i = 0; i < definition.getBounds().length; i++) {
            extractAndProcess(object, i, definition.getBounds()[i], extractorRegistry, extractedValues);
        }
    }

    public void visit(AnnotatedArrayTypeDefinition definition, Object object, ExtractorRegistry extractorRegistry,
                      List<ExtractedTypeValue> extractedValues) {
        extractAndProcess(object, 0, definition.getComponentType(), extractorRegistry, extractedValues);
    }

    protected void addExtractedValue(AnnotatedTypeDefinition definition, Object value, List<ExtractedTypeValue> extractedValues) {
        // Add the candidate ?
        if (definition.requiresIntrospection() || definition.hasDeclaredRules()) {
            extractedValues.add(new ExtractedTypeValue(definition, value));
        }
    }

    protected void extractAndProcess(Object container, int index, AnnotatedTypeDefinition definition,
                                     ExtractorRegistry extractorRegistry, List<ExtractedTypeValue> extractedValues) {
        if (container == null) return;

        TypedValueExtractor extractor = extractorRegistry.find(container.getClass(), index);

        if (extractor == null) {
            // TODO : Log
            return;
        }

        try {
            SimpleTypeValueCollector collector = new SimpleTypeValueCollector();
            extractor.extract(container, collector);

            for (Object extractedValue : collector.result) {
                extractInternal(definition, extractedValue, extractorRegistry, extractedValues);
            }

        } catch (Exception e) {
            // TODO : Unable to extract
            e.printStackTrace();
        }
    }

    private static class SimpleTypeValueCollector implements TypedValueProcessor {

        private List<Object> result = new ArrayList<>();

        @Override
        public void value(String name, Object value) {
            result.add(value);
        }

        @Override
        public void indexedValue(String name, int index, Object value) {
            result.add(value);
        }

        public List<Object> getResult() {
            return result;
        }
    }

    @Validate
    private Map<@NotNull String, @NotNull Map<@NotNull @Validate ? extends List<@NotNull ?>, @Validate @NotNull ?>> field1 = new HashMap<>();
    private List<@NotNull List<@NotNull String>> field2 = new ArrayList<>();

    public static void main(String[] args) throws NoSuchFieldException {
        Field field = AnnotatedTypeValueExtractor.class.getDeclaredField("field1");
        AnnotatedType annotatedType = field.getAnnotatedType();
        AnnotatedTypeSupport support = new ValidationAnnotatedTypeSupport();
        AnnotatedTypeDefinitionBuilder builder = new AnnotatedTypeDefinitionBuilder();
        AnnotatedTypeDefinition definition = builder.build(annotatedType, support);

        AnnotatedTypeValueExtractor valueExtractor = new AnnotatedTypeValueExtractor();
        Map<List<?>, String> internalMap1 = new HashMap<>();
        List<String> list1 = Arrays.asList("1", "2", "3", "4", "5");
        internalMap1.put(list1, "list-1");
        valueExtractor.field1.put("value-1", internalMap1);

        valueExtractor.field2.add(Arrays.asList("a"));
        valueExtractor.field2.add(Arrays.asList("b"));
        valueExtractor.field2.add(Arrays.asList("c"));
        valueExtractor.field2.add(Arrays.asList("d"));
        valueExtractor.field2.add(Arrays.asList("e"));

        List<ExtractedTypeValue> extractedValues = valueExtractor.extract(definition, valueExtractor.field1, new DefaultExtractorRegistry());

        System.err.println("XXX Extracted Values");
        for (ExtractedTypeValue value : extractedValues) {
            System.err.println("Extracted ["+ value.getObject() + "] Defn [" + value.getObject().getClass() + "]");
        }
    }
}
