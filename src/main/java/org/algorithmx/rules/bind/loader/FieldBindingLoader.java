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
package org.algorithmx.rules.bind.loader;

import org.algorithmx.rules.bind.BindingBuilder;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.lib.spring.util.Assert;
import org.algorithmx.rules.util.reflect.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Loads the desired fields in the give Bean as separate Bindings.
 *
 * You do have the option to control which fields get added by using the (Filter/IgnoreFields/IncludeFields)
 * You also have the option to change the BindingName using the NameGenerator property.
 *
 * @param <T> Bean Type.
 * @author Max Arulananthan
 * @since 1.0
 */
public class FieldBindingLoader<T> implements BindingLoader<T> {

    private Function<Field, Boolean> filter = null;
    private Function<Field, String> nameGenerator = null;

    public FieldBindingLoader() {
        super();
    }

    /**
     * Function use to change the output BindingName,
     *
     * @param nameGenerator Function that takes a Field and changes the name of the Property into the desired Binding name,
     */
    public void setNameGenerator(Function<Field, String> nameGenerator) {
        this.nameGenerator = nameGenerator;
    }

    /**
     * Filter to restrict which fields become Bindings.
     *
     * @param filter restricting Filter.
     */
    public void setFilter(Function<Field, Boolean> filter) {
        this.filter = filter;
    }

    /**
     * Array of field names that won't get Bindings.
     *
     * @param names blacklisted field names.
     */
    public void setIgnoreFields(String...names) {
        Assert.notNull(names, "names cannot be null.");
        Set<String> nameSet = Arrays.stream(names).collect(Collectors.toSet());
        this.filter = field -> !nameSet.contains(field.getName());
    }

    /**
     * Array of field names that will get Bindings.
     *
     * @param names whitelisted field names.
     */
    public void setIncludeFields(String...names) {
        Assert.notNull(names, "names cannot be null.");
        Set<String> nameSet = Arrays.stream(names).collect(Collectors.toSet());
        this.filter = field -> nameSet.contains(field.getName());
    }

    @Override
    public void load(Bindings bindings, T bean) {
        Assert.notNull(bean, "bean cannot be null.");

        ReflectionUtils.traverseFields(bean.getClass(), null, field -> {
            if (filter != null && !filter.apply(field)) return;
            try {
                Object value = field.get(bean);
                String bindingName = nameGenerator != null ? nameGenerator.apply(field) : field.getName();
                bindings.bind(BindingBuilder.with(bindingName)
                        .type(field.getGenericType())
                        .value(value).build());
            } catch (IllegalAccessException e) {
                // Couldn't get the value
                throw new UnrulyException("Error trying to retrieve field [" + field.getName()
                        + "] on Bean class [" + bean.getClass() + "]", e);
            }
        });

    }
}
