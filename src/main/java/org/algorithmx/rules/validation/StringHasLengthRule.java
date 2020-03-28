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
package org.algorithmx.rules.validation;

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.model.Severity;

import java.util.function.Supplier;

/**
 * Validation Rule to make sure the String value has length.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("String value has length.")
public class StringHasLengthRule extends BindingValidationRule<String> {

    /**
     * Ctor taking the error code and name of the Binding.
     *
     * @param errorCode error code.
     * @param bindingName name of the Binding.
     */
    public StringHasLengthRule(String errorCode, String bindingName) {
        super(errorCode, Severity.FATAL, null, value -> hasLength(value), bindingName);
    }

    /**
     * Ctor taking the error code and name of the Binding.
     *
     * @param errorCode error code.
     * @param supplier Binding.
     */
    public StringHasLengthRule(String errorCode, Supplier<Binding<String>> supplier) {
        super(errorCode, Severity.FATAL, null, value -> hasLength(value), supplier);
    }

    /**
     * Determines whether the given text is not empty.
     *
     * @param text given text.
     * @return true if not empty; false otherwise.
     */
    private static boolean hasLength(String text) {
        return (text != null && !text.isEmpty());
    }

    @Override
    public String getErrorMessage() {
        if (super.getErrorMessage() != null) return super.getErrorMessage();
        String bindingName = getBindingName();
        if (bindingName == null) bindingName = "NOT BOUND";
        return "Binding [" + bindingName + "] does not have any length. Given {" + bindingName + "}";
    }
}
