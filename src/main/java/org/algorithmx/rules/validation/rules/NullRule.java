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
package org.algorithmx.rules.validation.rules;

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.model.Severity;
import org.algorithmx.rules.validation.FunctionalValidationRule;
import org.algorithmx.rules.validation.ValidationError;

import java.util.function.Supplier;

/**
 * Null validation Rule. Check to make sure the binding is either not defined or is null.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Given binding must be null.")
public class NullRule extends FunctionalValidationRule<Object> {

    /**
     * Ctor taking in the binding supplier and error code.
     *
     * @param supplier binding supplier.
     * @param errorCode error code to be returned.
     */
    public NullRule(Supplier<Binding<Object>> supplier, String errorCode) {
        this(supplier, errorCode, Severity.FATAL, "["
                + getBindingName(supplier) + "] must be null.");
    }

    /**
     * Ctor taking in the binding supplier, error code, severity and error message.
     *
     * @param supplier binding supplier.
     * @param errorCode error code to be returned.
     * @param severity severity of the error.
     * @param errorMessage error message to be returned.
     */
    public NullRule(Supplier<Binding<Object>> supplier, String errorCode, Severity severity, String errorMessage) {
        this(supplier, new ValidationError(getBindingName(supplier) + "_" + NullRule.class.getSimpleName(),
                errorCode, severity, errorMessage));
    }

    /**
     * Ctor taking in the binding supplier and error.
     *
     * @param supplier binding supplier.
     * @param error validation error.
     */
    public NullRule(Supplier<Binding<Object>> supplier, ValidationError error) {
        super(supplier, binding -> binding != null && binding.get() == null, error);
    }
}
