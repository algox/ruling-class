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

package org.algorithmx.rulii.validation.beans;

import org.algorithmx.rulii.core.UnrulyException;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.validation.types.AnnotatedTypeDefinition;

import java.lang.reflect.Field;

public class FieldHolder implements SourceHolder<Field> {

    private final Field field;
    private final AnnotatedTypeDefinition definition;

    public FieldHolder(Field field, AnnotatedTypeDefinition definition) {
        super();
        Assert.notNull(field, "field cannot be null.");
        Assert.notNull(definition, "definition cannot be null.");
        this.field = field;
        this.definition = definition;
        field.setAccessible(true);
    }

    @Override
    public Field getSource() {
        return field;
    }

    @Override
    public AnnotatedTypeDefinition getDefinition() {
        return definition;
    }

    @Override
    public String getName() {
        return field.getDeclaringClass().getSimpleName() + "." + field.getName();
    }

    @Override
    public SourceHolder<Field> copy(AnnotatedTypeDefinition definition) {
        return new FieldHolder(field, definition);
    }

    public Object getValue(Object parent) {
        try {
            // TODO : setAccessible() ?
            return field.get(parent);
        } catch (IllegalAccessException e) {
            throw new UnrulyException("Unable to get field value for field [" + field.getName()
                    + "] on class [" + field.getDeclaringClass().getName() + "]", e);
        }
    }

    @Override
    public String toString() {
        return "FieldHolder{" +
                "field=" + field +
                ", definition=" + definition +
                '}';
    }
}
