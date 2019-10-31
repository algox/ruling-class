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

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.bind.Binding;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Validation Rule based on the given Function.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Validation Rule based on the given Function.")
public class FunctionalValidationRule<T> extends ValidationRuleTemplate<T> {

    private final Function<Binding<T>, Boolean> condition;

    /**
     * Ctor taking in the error, rule name, binding name, and the validating function.
     *
     * @param supplier Binding supplier.
     * @param condition validating function.
     * @param error validation error.
     */
    public FunctionalValidationRule(Supplier<Binding<T>> supplier, Function<Binding<T>, Boolean> condition, ValidationError error) {
        super(supplier, error);
        this.condition = condition;
    }

    @Override
    protected final boolean when(Binding<T> binding) {
        return condition.apply(binding);
    }
}
