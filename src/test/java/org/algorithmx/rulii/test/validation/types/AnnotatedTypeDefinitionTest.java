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

package org.algorithmx.rulii.test.validation.types;

import org.algorithmx.rulii.extract.Extractor;
import org.algorithmx.rulii.extract.TypedValueExtractorTemplate;
import org.algorithmx.rulii.extract.TypedValueProcessor;
import org.algorithmx.rulii.validation.annotation.Validate;
import org.algorithmx.rulii.validation.rules.min.DecimalMin;
import org.algorithmx.rulii.validation.rules.notnull.NotNull;
import org.algorithmx.rulii.validation.types.AnnotatedArrayTypeDefinition;
import org.algorithmx.rulii.validation.types.AnnotatedParameterizedTypeDefinition;
import org.algorithmx.rulii.validation.types.AnnotatedTypeDefinition;
import org.algorithmx.rulii.validation.types.AnnotatedTypeDefinitionBuilder;
import org.algorithmx.rulii.validation.types.AnnotatedTypeKind;
import org.algorithmx.rulii.validation.types.AnnotatedTypeVariableDefinition;
import org.algorithmx.rulii.validation.types.AnnotatedWildcardTypeDefinition;
import org.algorithmx.rulii.validation.types.SimpleAnnotatedTypeDefinition;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnnotatedTypeDefinitionTest<T extends Number, R extends List> {

    private Integer field1;
    private Map<Integer, String> field2;
    private Map<T, R> field3;
    private T field4;
    private Set<? extends BigDecimal> field5;
    private List<String[]> field6;
    private List<@NotNull @DecimalMin(25.00) @Extractor(TestClassExtractor.class) BigDecimal> field7;
    @Validate
    private TestClass<@Extractor(TestClassExtractor.class) String> field8;

    public AnnotatedTypeDefinitionTest() {
        super();
    }

    @Test
    public void test1() {
        AnnotatedTypeDefinitionBuilder builder = new AnnotatedTypeDefinitionBuilder();
        AnnotatedTypeDefinition definition = builder.build(getAnnotatedType("field1"));
        Assert.assertNotNull(definition);
        Assert.assertTrue(definition.getKind() == AnnotatedTypeKind.SIMPLE_TYPE);
        SimpleAnnotatedTypeDefinition simpleAnnotatedTypeDefinition = (SimpleAnnotatedTypeDefinition) definition;
        Assert.assertTrue(simpleAnnotatedTypeDefinition.getType().equals(Integer.class));
    }

    @Test
    public void test2() {
        AnnotatedTypeDefinitionBuilder builder = new AnnotatedTypeDefinitionBuilder();
        AnnotatedTypeDefinition definition = builder.build(getAnnotatedType("field2"));
        Assert.assertNotNull(definition);
        Assert.assertTrue(definition.getKind() == AnnotatedTypeKind.PARAMETERIZED_TYPE);
        AnnotatedParameterizedTypeDefinition annotatedParameterizedTypeDefinition = (AnnotatedParameterizedTypeDefinition) definition;
        Assert.assertTrue(annotatedParameterizedTypeDefinition.getType().equals(Map.class));
        Assert.assertTrue(((SimpleAnnotatedTypeDefinition) annotatedParameterizedTypeDefinition.getTypeArguments()[0]).getType().equals(Integer.class));
        Assert.assertTrue(((SimpleAnnotatedTypeDefinition) annotatedParameterizedTypeDefinition.getTypeArguments()[1]).getType().equals(String.class));
    }

    @Test
    public void test3() {
        AnnotatedTypeDefinitionBuilder builder = new AnnotatedTypeDefinitionBuilder();
        AnnotatedTypeDefinition definition = builder.build(getAnnotatedType("field3"));
        Assert.assertNotNull(definition);
        Assert.assertTrue(definition.getKind() == AnnotatedTypeKind.PARAMETERIZED_TYPE);
        AnnotatedParameterizedTypeDefinition annotatedTypeVariableDefinition = (AnnotatedParameterizedTypeDefinition) definition;
        Assert.assertTrue(annotatedTypeVariableDefinition.getType().equals(Map.class));
        Assert.assertTrue(annotatedTypeVariableDefinition.getTypeArguments()[0].getKind() == AnnotatedTypeKind.TYPE_VARIABLE_TYPE);
        Assert.assertTrue(annotatedTypeVariableDefinition.getTypeArguments()[1].getKind() == AnnotatedTypeKind.TYPE_VARIABLE_TYPE);
        AnnotatedTypeVariableDefinition typeVariable0 = (AnnotatedTypeVariableDefinition) annotatedTypeVariableDefinition.getTypeArguments()[0];
        AnnotatedTypeVariableDefinition typeVariable1 = (AnnotatedTypeVariableDefinition) annotatedTypeVariableDefinition.getTypeArguments()[1];
        Assert.assertTrue(((SimpleAnnotatedTypeDefinition) typeVariable0.getBounds()[0]).getType().equals(Number.class));
        Assert.assertTrue(((SimpleAnnotatedTypeDefinition) typeVariable1.getBounds()[0]).getType().equals(List.class));
    }

    @Test
    public void test4() {
        AnnotatedTypeDefinitionBuilder builder = new AnnotatedTypeDefinitionBuilder();
        AnnotatedTypeDefinition definition = builder.build(getAnnotatedType("field4"));
        Assert.assertNotNull(definition);
        Assert.assertTrue(definition.getKind() == AnnotatedTypeKind.TYPE_VARIABLE_TYPE);
    }

    @Test
    public void test5() {
        AnnotatedTypeDefinitionBuilder builder = new AnnotatedTypeDefinitionBuilder();
        AnnotatedTypeDefinition definition = builder.build(getAnnotatedType("field5"));
        Assert.assertNotNull(definition);
        Assert.assertTrue(definition.getKind() == AnnotatedTypeKind.PARAMETERIZED_TYPE);
        AnnotatedParameterizedTypeDefinition annotatedTypeVariableDefinition = (AnnotatedParameterizedTypeDefinition) definition;
        Assert.assertTrue(annotatedTypeVariableDefinition.getType().equals(Set.class));
        Assert.assertTrue(annotatedTypeVariableDefinition.getTypeArguments()[0].getKind() == AnnotatedTypeKind.WILDCARD_TYPE);
        AnnotatedWildcardTypeDefinition wildcardTypeDefinition = (AnnotatedWildcardTypeDefinition) annotatedTypeVariableDefinition.getTypeArguments()[0];
        Assert.assertTrue(wildcardTypeDefinition.isUpperBound());
        Assert.assertTrue(((SimpleAnnotatedTypeDefinition) wildcardTypeDefinition.getBounds()[0]).getType().equals(BigDecimal.class));
    }

    @Test
    public void test6() {
        AnnotatedTypeDefinitionBuilder builder = new AnnotatedTypeDefinitionBuilder();
        AnnotatedTypeDefinition definition = builder.build(getAnnotatedType("field6"));
        Assert.assertNotNull(definition);
        Assert.assertTrue(definition.getKind() == AnnotatedTypeKind.PARAMETERIZED_TYPE);
        AnnotatedParameterizedTypeDefinition annotatedParameterizedTypeDefinition = (AnnotatedParameterizedTypeDefinition) definition;
        AnnotatedArrayTypeDefinition arrayTypeDefinition = (AnnotatedArrayTypeDefinition) annotatedParameterizedTypeDefinition.getTypeArguments()[0];
        Assert.assertTrue(((SimpleAnnotatedTypeDefinition) arrayTypeDefinition.getComponentType()).getType().equals(String.class));
    }

    @Test
    public void test7() {
        AnnotatedTypeDefinitionBuilder builder = new AnnotatedTypeDefinitionBuilder();
        AnnotatedTypeDefinition definition = builder.build(getAnnotatedType("field7"));
        Assert.assertNotNull(definition);
        AnnotatedParameterizedTypeDefinition annotatedTypeVariableDefinition = (AnnotatedParameterizedTypeDefinition) definition;
        Assert.assertTrue(annotatedTypeVariableDefinition.getTypeArguments()[0].getCustomExtractor().equals(TestClassExtractor.class));
    }

    @Test
    public void test8() {
        AnnotatedTypeDefinitionBuilder builder = new AnnotatedTypeDefinitionBuilder();
        AnnotatedTypeDefinition definition = builder.build(getAnnotatedType("field8"));
        Assert.assertNotNull(definition);
        AnnotatedParameterizedTypeDefinition annotatedTypeVariableDefinition = (AnnotatedParameterizedTypeDefinition) definition;
        Assert.assertTrue(annotatedTypeVariableDefinition.getTypeArguments()[0].getCustomExtractor().equals(TestClassExtractor.class));
    }

    private AnnotatedType getAnnotatedType(String fieldName) {
        try {
            Field field = getClass().getDeclaredField(fieldName);
            return field.getAnnotatedType();
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static class TestClassExtractor extends TypedValueExtractorTemplate<TestClass<?>> {

        public TestClassExtractor() {
            super(TestClass.class, 0);
        }

        @Override
        public void extract(TestClass<?> container, TypedValueProcessor processor) {
            processor.value("", container.getValue());
        }
    }
}
