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
package org.algorithmx.rules.core.model;

import org.algorithmx.rules.bind.match.BindingMatchingStrategy;
import org.algorithmx.rules.lib.spring.util.Assert;

import java.lang.reflect.Type;

public class ParameterDefinitionEditor<T> {

    private final ParameterDefinition target;
    private final T returnType;

    public ParameterDefinitionEditor(ParameterDefinition target, T returnType) {
        super();
        Assert.notNull(target, "target cannot be null.");
        Assert.notNull(returnType, "returnType cannot be null.");
        this.target = target;
        this.returnType = returnType;
    }

    public static <T> ParameterDefinitionEditor with(ParameterDefinition target, T returnType) {
        return new ParameterDefinitionEditor(target, returnType);
    }

    public ParameterDefinitionEditor<T> name(String name) {
        target.setName(name);
        return this;
    }

    public ParameterDefinitionEditor<T> index(int index) {
        target.setIndex(index);
        return this;
    }

    public ParameterDefinitionEditor<T> type(Type type) {
        target.setType(type);
        return this;
    }

    public ParameterDefinitionEditor<T> optional(boolean value) {
        target.setOptional(value);
        return this;
    }

    public ParameterDefinitionEditor<T> description(String description) {
        target.setDescription(description);
        return this;
    }

    public ParameterDefinitionEditor<T> defaultValueText(String defaultValueText) {
        target.setDefaultValueText(defaultValueText);
        return this;
    }

    public ParameterDefinitionEditor<T> defaultValue(Object defaultValue) {
        target.setDefaultValue(defaultValue);
        return this;
    }

    public ParameterDefinitionEditor<T> bindUsing(Class<? extends BindingMatchingStrategy> bindUsing) {
        target.setBindUsing(bindUsing);
        return this;
    }

    public ParameterDefinition getParameterDefinition() {
        return target;
    }

    public T build() {
        target.validate();
        return returnType;
    }
}
