/**
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 2019, algorithmx.org (dev@algorithmx.org)
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
package org.algorithmx.rules.validation;

import org.algorithmx.rules.annotation.Bind;
import org.algorithmx.rules.annotation.Given;
import org.algorithmx.rules.annotation.Otherwise;
import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.bind.BindingMatchingStrategyType;
import org.algorithmx.rules.core.impl.RulingClass;
import org.algorithmx.rules.spring.util.Assert;

import java.util.function.Supplier;

/**
 * Template class for writing Validation rules based on a single Binding.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public abstract class ValidationRuleTemplate<T> extends RulingClass {

    private final ValidationError error;
    private final Supplier<Binding<T>> supplier;

    /**
     * Ctor taking in the error, rule name and the binding.
     *
     * @param supplier Binding supplier.
     * @param error validation error.
     */
    protected ValidationRuleTemplate(Supplier<Binding<T>> supplier, ValidationError error) {
        super();
        Assert.notNull(error, "error cannot be null.");
        Assert.notNull(supplier, "supplier cannot be null.");
        this.error = error;
        this.supplier = supplier;
    }

    /**
     * Rule condition : Check if the Binding is present and value is not null.
     *
     * @return true if the validation condition is met; false otherwise.
     */
    @Given
    public boolean when() {
        return when(supplier.get());
    }

    /**
     * Delegating method to do the actual condition check.
     *
     * @param value Binding value.
     * @return true if condition is satisfied; false otherwise.
     */
    protected abstract boolean when(Binding<T> value);

    /**
     * Rule Action : add the desired error code and message to the error container.
     *
     * @param errors error container.
     */
    @Otherwise
    public void otherwise(@Bind(using = BindingMatchingStrategyType.MATCH_BY_TYPE) ValidationErrorContainer errors) {
        Binding<T> binding = supplier.get();

        if (binding != null) {
            error.param(binding.getName(), binding != null && binding.getValue() != null
                    ? binding.getValue().toString()
                    : null);
        }

        errors.add(error);
    }

    @Override
    public String getName() {
        return supplier.get() != null ? supplier.get().getName() + "_" + super.getName() : super.getName();
    }

    /**
     * Retrieves the name of the Binding if one is avail.
     *
     * @param supplier Binding supplier.
     * @param <T> generic type of the Binding.
     * @return name of the Binding if one is avail; blank otherwise.
     */
    protected static <T> String getBindingName(Supplier<Binding<T>> supplier) {
        if (supplier == null) return "";
        return supplier.get() != null ? supplier.get().getName() : "'";
    }
}
