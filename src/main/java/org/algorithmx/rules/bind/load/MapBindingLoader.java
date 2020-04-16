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
package org.algorithmx.rules.bind.load;

import org.algorithmx.rules.bind.BindingBuilder;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.lib.spring.util.Assert;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Loads the desired keys in the give Map as separate Bindings.
 *
 * You do have the option to control which keys get added by using the (Filter/IgnoreKeys/IncludeKeys)
 * You also have the option to change the BindingName using the NameGenerator property.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class MapBindingLoader implements BindingLoader<Map<String, ?>> {

    private Function<String, Boolean> filter = null;
    private Function<String, String> nameGenerator = null;

    public MapBindingLoader() {
        super();
    }

    /**
     * Function use to change the output BindingName,
     *
     * @param nameGenerator Function that takes a Key and changes into the desired Binding name,
     */
    public void setNameGenerator(Function<String, String> nameGenerator) {
        this.nameGenerator = nameGenerator;
    }

    /**
     * Filter to restrict which keys become Bindings.
     *
     * @param filter restricting Filter.
     */
    public void setFilter(Function<String, Boolean> filter) {
        this.filter = filter;
    }

    /**
     * Array of keys that won't get Bindings.
     *
     * @param names blacklisted keys.
     */
    public void setIgnoreKeys(String...names) {
        Assert.notNull(names, "names cannot be null.");
        Set<String> nameSet = Arrays.stream(names).collect(Collectors.toSet());
        this.filter = key -> !nameSet.contains(key);
    }

    /**
     * Array of keys that will get Bindings.
     *
     * @param names whitelisted keys.
     */
    public void setIncludeKeys(String...names) {
        Assert.notNull(names, "names cannot be null.");
        Set<String> nameSet = Arrays.stream(names).collect(Collectors.toSet());
        this.filter = key -> nameSet.contains(key);
    }

    @Override
    public void load(Bindings bindings, Map<String, ?> map) {
        Assert.notNull(map, "map cannot be null.");

        for (Map.Entry<String, ?> entry : map.entrySet()) {
            if (filter != null && !filter.apply(entry.getKey())) return;
            String bindingName = nameGenerator != null ? nameGenerator.apply(entry.getKey()) : entry.getKey();
            bindings.bind(BindingBuilder.with(bindingName).value(entry.getValue()).build());
        }
    }
}
