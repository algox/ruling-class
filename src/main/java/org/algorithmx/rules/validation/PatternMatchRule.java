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

import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * Validation Rule to make sure the String value matches the given regex pattern.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("String value matches the given regex pattern.")
public class PatternMatchRule extends BindingValidationRule<String> {

    private final String pattern;

    /**
     * Ctor taking the error code and name of the Binding.
     *
     * @param pattern desired pattern.
     * @param errorCode error code.
     * @param bindingName name of the Binding.
     */
    public PatternMatchRule(String pattern, String errorCode, String bindingName) {
        super(errorCode, Severity.FATAL, null, value -> matches(value, Pattern.compile(pattern)), bindingName);
        this.pattern = pattern;
    }

    /**
     * Ctor taking the error code and name of the Binding.
     *
     * @param pattern desired regex pattern.
     * @param errorCode error code.
     * @param supplier Binding.
     */
    public PatternMatchRule(String pattern, String errorCode, Supplier<Binding<String>> supplier) {
        super(errorCode, Severity.FATAL, null, value -> matches(value, Pattern.compile(pattern)), supplier);
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    /**
     * Determines whether the given value matches the desired regex pattern.
     *
     * @param value text value.
     * @param pattern desired pattern.
     * @return true if the text matches the pattern; false otherwise.
     */
    private static boolean matches(String value, Pattern pattern) {
        return value != null && pattern.matcher(value).matches();
    }

    @Override
    public String getErrorMessage() {
        if (super.getErrorMessage() != null) return super.getErrorMessage();
        String bindingName = getBindingName();
        if (bindingName == null) bindingName = "NOT BOUND";
        return "Regex Pattern not matched by [" + bindingName + "], it must be match [" + pattern + "]. Given {" + bindingName + "}";
    }
}
