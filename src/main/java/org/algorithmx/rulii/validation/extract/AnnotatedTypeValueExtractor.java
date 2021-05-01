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

import org.algorithmx.rulii.annotation.Extract;
import org.algorithmx.rulii.annotation.Validate;
import org.algorithmx.rulii.annotation.ValidationMarker;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.util.reflect.ObjectFactory;
import org.algorithmx.rulii.validation.rules.notnull.NotNull;
import org.algorithmx.rulii.validation.types.AnnotatedArrayTypeDefinition;
import org.algorithmx.rulii.validation.types.AnnotatedParameterizedTypeDefinition;
import org.algorithmx.rulii.validation.types.AnnotatedTypeDefinition;
import org.algorithmx.rulii.validation.types.AnnotatedTypeDefinitionBuilder;
import org.algorithmx.rulii.validation.types.AnnotatedTypeKind;
import org.algorithmx.rulii.validation.types.AnnotatedTypeVariableDefinition;
import org.algorithmx.rulii.validation.types.AnnotatedWildcardTypeDefinition;

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

    public List<ExtractedTypeValue> extract(AnnotatedTypeDefinition definition, Object object,
                                            ExtractorRegistry extractorRegistry, ObjectFactory objectFactory) {
        List<ExtractedTypeValue> extractedValues = new ArrayList<>();
        extractInternal(definition, object, extractorRegistry, objectFactory, extractedValues);
        return extractedValues;
    }

    public void extractInternal(AnnotatedTypeDefinition definition, Object object,
                                ExtractorRegistry extractorRegistry, ObjectFactory objectFactory,
                         List<ExtractedTypeValue> extractedValues) {
        Assert.notNull(definition, "definition cannot be null.");
        Assert.notNull(extractorRegistry, "extractorRegistry cannot be null.");

        // Add the candidate ?
        addExtractedValue(definition, object, extractedValues);

        if (object == null || (!definition.childrenRequireIntrospection() && !definition.childrenHaveRules())) return;

        if (definition.getKind() == AnnotatedTypeKind.PARAMETERIZED_TYPE) {
            visit((AnnotatedParameterizedTypeDefinition) definition, object, extractorRegistry, objectFactory, extractedValues);
        } else if (definition.getKind() == AnnotatedTypeKind.WILDCARD_TYPE) {
            visit((AnnotatedWildcardTypeDefinition) definition, object, extractorRegistry, objectFactory, extractedValues);
        } else if (definition.getKind() == AnnotatedTypeKind.TYPE_VARIABLE_TYPE) {
            visit((AnnotatedTypeVariableDefinition) definition, object, extractorRegistry, objectFactory, extractedValues);
        } else if (definition.getKind() == AnnotatedTypeKind.ARRAY_TYPE) {
            visit((AnnotatedArrayTypeDefinition) definition, object, extractorRegistry, objectFactory, extractedValues);
        }
    }

    public void visit(AnnotatedParameterizedTypeDefinition definition, Object object,
                      ExtractorRegistry extractorRegistry, ObjectFactory objectFactory,
                      List<ExtractedTypeValue> extractedValues) {
        for (int i = 0; i < definition.getTypeArguments().length; i++) {
            extractAndProcess(object, i, definition.getTypeArguments()[i], extractorRegistry, objectFactory, extractedValues);
        }
    }

    public void visit(AnnotatedWildcardTypeDefinition definition, Object object,
                      ExtractorRegistry extractorRegistry, ObjectFactory objectFactory,
                      List<ExtractedTypeValue> extractedValues) {
        for (int i = 0; i < definition.getLowerBounds().length; i++) {
            extractAndProcess(object, i, definition.getLowerBounds()[i], extractorRegistry, objectFactory, extractedValues);
        }

        for (int i = 0; i < definition.getUpperBounds().length; i++) {
            extractAndProcess(object, i, definition.getUpperBounds()[i], extractorRegistry, objectFactory, extractedValues);
        }
    }

    public void visit(AnnotatedTypeVariableDefinition definition, Object object,
                      ExtractorRegistry extractorRegistry, ObjectFactory objectFactory,
                      List<ExtractedTypeValue> extractedValues) {
        for (int i = 0; i < definition.getBounds().length; i++) {
            extractAndProcess(object, i, definition.getBounds()[i], extractorRegistry, objectFactory, extractedValues);
        }
    }

    public void visit(AnnotatedArrayTypeDefinition definition, Object object,
                      ExtractorRegistry extractorRegistry, ObjectFactory objectFactory,
                      List<ExtractedTypeValue> extractedValues) {
        extractAndProcess(object, 0, definition.getComponentType(), extractorRegistry, objectFactory, extractedValues);
    }

    protected void addExtractedValue(AnnotatedTypeDefinition definition, Object value, List<ExtractedTypeValue> extractedValues) {
        // Add the candidate ?
        if (definition.isIntrospectionRequired() || definition.hasDeclaredRules()) {
            extractedValues.add(new ExtractedTypeValue(definition, value));
        }
    }

    protected void extractAndProcess(Object container, int index, AnnotatedTypeDefinition definition,
                                     ExtractorRegistry extractorRegistry,
                                     ObjectFactory objectFactory,
                                     List<ExtractedTypeValue> extractedValues) {
        if (container == null) return;

        TypedValueExtractor extractor = findTypedValueExtractor(container.getClass(), index, definition,
                extractorRegistry, objectFactory);

        if (extractor == null) {
            // TODO : Log
            return;
        }

        try {
            SimpleTypeValueCollector collector = new SimpleTypeValueCollector();
            extractor.extract(container, collector);

            for (Object extractedValue : collector.result) {
                extractInternal(definition, extractedValue, extractorRegistry, objectFactory, extractedValues);
            }

        } catch (Exception e) {
            // TODO : Unable to extract
            e.printStackTrace();
        }
    }

    protected TypedValueExtractor findTypedValueExtractor(Class<?> containerType, int index,
                                                          AnnotatedTypeDefinition definition,
                                                          ExtractorRegistry extractorRegistry,
                                                          ObjectFactory objectFactory) {

        TypedValueExtractor result = null;

        // Check use
        Extract extract = definition.getExtractAnnotation();

        // Check declaration
        if (extract == null) extract = findExtractOnTypeDeclaration(containerType, index);

        try {
            if (extract != null) {
                result = objectFactory.create(definition.getExtractAnnotation().using(), true);
            }
        } catch (Exception e) {
            // log it
        }

        if (result == null) {
            result = extractorRegistry.find(containerType, index);
        }

        return result;
    }

    protected Extract findExtractOnTypeDeclaration(Class<?> containerType, int index) {
        Assert.isTrue(index >= 0, "index must be >= 0");
        return index < containerType.getAnnotatedInterfaces().length
                ? containerType.getAnnotatedInterfaces()[index].getAnnotation(Extract.class)
                : null;
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
        AnnotatedTypeDefinitionBuilder builder = AnnotatedTypeDefinitionBuilder.with(annotatedType, Validate.class, ValidationMarker.class);
        AnnotatedTypeDefinition definition = builder.build();

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

        List<ExtractedTypeValue> extractedValues = valueExtractor.extract(definition, valueExtractor.field1,
                ExtractorRegistry.create(), ObjectFactory.create());

        System.err.println("XXX Extracted Values");
        for (ExtractedTypeValue value : extractedValues) {
            System.err.println("Extracted ["+ value.getValue() + "] Defn [" + value.getValue().getClass() + "]");
        }
    }
}
