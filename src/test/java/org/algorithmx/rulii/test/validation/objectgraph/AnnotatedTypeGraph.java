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

package org.algorithmx.rulii.test.validation.objectgraph;

import org.algorithmx.rulii.test.validation.Car;
import org.algorithmx.rulii.test.validation.Person;
import org.algorithmx.rulii.validation.annotation.Validate;
import org.algorithmx.rulii.validation.rules.notempty.NotEmpty;
import org.algorithmx.rulii.validation.rules.notnull.NotNull;

import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.AnnotatedTypeVariable;
import java.lang.reflect.AnnotatedWildcardType;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class AnnotatedTypeGraph<T extends A & @Validate B & List<T>> {

    private Map<@NotEmpty String, Map<@NotNull Person, @NotNull Car[]>> map = new HashMap<>();

    private Map<Person, Map<?, ?>> field1 = new HashMap<>();
    private List<? extends T> field2 = new ArrayList<>();
    private T field3;

    private static final Map<Object, Class<?>> breadCrumbs = new IdentityHashMap<>();

    public AnnotatedTypeGraph() {
        super();
    }

    private static void printAnnotatedType(AnnotatedType annotatedType, int level) {
        //if (breadCrumbs.containsKey(annotatedType.getType())) return;

        //breadCrumbs.put(annotatedType.getType(), annotatedType.getClass());

        if (annotatedType instanceof AnnotatedParameterizedType) {
            printAnnotatedParameterizedType((AnnotatedParameterizedType) annotatedType, level);
        } else if (annotatedType instanceof AnnotatedWildcardType) {
            printAnnotatedWildcardType((AnnotatedWildcardType) annotatedType, level);
        } else if (annotatedType instanceof AnnotatedTypeVariable) {
            printAnnotatedTypeVariable((AnnotatedTypeVariable) annotatedType, level);
        } else if (annotatedType instanceof AnnotatedTypeVariable) {
            printAnnotatedArrayType((AnnotatedArrayType) annotatedType, level);
        } else {
            printDefaultAnnotatedType(annotatedType, level);
        }
    }

    private static void printAnnotatedParameterizedType(AnnotatedParameterizedType annotatedType, int level) {
        AnnotatedType[] aTypes = annotatedType.getAnnotatedActualTypeArguments();
        print(level, "AnnotatedParameterizedType : " + annotatedType.getType());

        for (AnnotatedType aType : aTypes) {
            printAnnotatedType(aType, level + 1);
        }
    }

    private static void printAnnotatedWildcardType(AnnotatedWildcardType annotatedWildcardType, int level) {
        print(level, "AnnotatedWildcardType : " + annotatedWildcardType.getType());
        AnnotatedType[] lBounds = annotatedWildcardType.getAnnotatedLowerBounds();
        //print(level, "AnnotatedWildcardType#getAnnotatedLowerBounds(): " + Arrays.toString(lBounds));

        for (AnnotatedType lBound : lBounds) {
            //print(level, "AnnotatedWildcardType#getAnnotatedLowerBound: " + lBound);
            printAnnotatedType(lBound, level + 1);
        }

        AnnotatedType[] uBounds = annotatedWildcardType.getAnnotatedUpperBounds();
        //print(level, "AnnotatedWildcardType#getAnnotatedUpperBounds(): " + Arrays.toString(uBounds));
        for (AnnotatedType uBound : uBounds) {
            //print(level, "AnnotatedWildcardType#getAnnotatedUpperBound: " + uBound);
            printAnnotatedType(uBound, level + 1);
        }
    }

    private static void printAnnotatedTypeVariable(AnnotatedTypeVariable annotatedTypeVariable, int level) {
        print(level, "TypeVariable [" + annotatedTypeVariable.getAnnotatedBounds().length + "]");
        for (int i = 0; i < annotatedTypeVariable.getAnnotatedBounds().length; i++) {
            printAnnotatedType(annotatedTypeVariable.getAnnotatedBounds()[i], level + 1);
        }
    }

    private static void printAnnotatedArrayType(AnnotatedArrayType annotatedArrayType, int level) {
        print(level, "ArrayType [" + annotatedArrayType.getAnnotatedGenericComponentType() + "]");
    }

    private static void printDefaultAnnotatedType(AnnotatedType annotatedType, int level) {
        print(level, "Type = " + annotatedType.getType().getTypeName());
        print(level, "Annotations: " + Arrays.toString(annotatedType.getAnnotations()));
        print(level, "Declared Annotations: " + Arrays.toString(annotatedType.getDeclaredAnnotations()));
        //print(level, "AnnotatedType class: " + annotatedType.getClass().getName());
        //print(level, "AnnotatedType class implementing interfaces: " + Arrays.toString(annotatedType.getClass().getInterfaces()));
        print(level, System.lineSeparator());
    }

    private static void print(int level, String string) {
        System.out.printf("%" + (level * 4 - 3) + "s\u00A6- %s%n", "", string);
    }

    public static void main(String[] args) throws Exception {
        Field field = AnnotatedTypeGraph.class.getDeclaredField("field1");
        AnnotatedType annotatedType = field.getAnnotatedType();
        printAnnotatedType(annotatedType, 1);
    }
}
