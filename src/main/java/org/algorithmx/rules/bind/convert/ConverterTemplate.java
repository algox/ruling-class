/**
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 1999-2019, Live Software & Consultants Inc (rules@algorithmx.org)
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
package org.algorithmx.rules.bind.convert;

import org.algorithmx.rules.lib.apache.reflect.TypeUtils;
import org.algorithmx.rules.lib.spring.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Parent Template class for Converts.
 *
 * @param <T> source type.
 * @param <R> target type.
 */
public abstract class ConverterTemplate<T, R> implements Converter<T, R> {

    private final Type sourceType;
    private final Type targetType;

    /**
     * Ctor to determine the source/target types from the Class generic definition.
     */
    protected ConverterTemplate() {
        super();
        this.sourceType = captureType(0);
        this.targetType = captureType(1);
    }

    /**
     * Ctor taking source and target types.
     *
     * @param sourceType source type.
     * @param targetType target type.
     */
    protected ConverterTemplate(Type sourceType, Type targetType) {
        super();
        Assert.notNull(sourceType, "sourceType cannot be null.");
        Assert.notNull(targetType, "targetType cannot be null.");
        this.sourceType = sourceType;
        this.targetType = targetType;
    }

    @Override
    public final Type getSourceType() {
        return sourceType;
    }

    @Override
    public final Type getTargetType() {
        return targetType;
    }

    @Override
    public final boolean canConvert(Type sourceType, Type targetType) {
        return this.sourceType.equals(sourceType) && TypeUtils.isAssignable(targetType, this.targetType);
    }

    protected Type captureType(int index) {
        Type superClass = getClass().getGenericSuperclass();
        return ((ParameterizedType) superClass).getActualTypeArguments()[index];
    }
}
