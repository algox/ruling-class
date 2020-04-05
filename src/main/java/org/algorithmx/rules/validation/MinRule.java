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
import org.algorithmx.rules.core.UnrulyException;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Validation Rule to make sure the the value is less the desired Min.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value is less than the desired Min.")
public class MinRule extends BindingValidationRule<Object> {

    private final long min;

    /**
     * Ctor taking the error code and name of the Binding.
     *
     * @param min desired Min value.
     * @param errorCode error code.
     * @param bindingName name of the Binding.
     */
    public MinRule(long min, String errorCode, String bindingName) {
        super(errorCode, Severity.FATAL, null, value -> isGreaterThanMin(value, min), bindingName);
        this.min = min;
    }

    /**
     * Ctor taking the error code and name of the Binding.
     *
     * @param min desired Min value.
     * @param errorCode error code.
     * @param supplier Binding.
     */
    public MinRule(long min, String errorCode, Supplier<Binding<Object>> supplier) {
        super(errorCode, Severity.FATAL, null, value -> isGreaterThanMin(value, min), supplier);
        this.min = min;
    }

    public long getMin() {
        return min;
    }

    /**
     * Determines if the given object (size/length) is greater than or equal to the Min value.
     *
     * @param value given Object.
     * @param min Minimum size.
     * @return given object (size/length) greater than the size of the Object.
     */
    private static boolean isGreaterThanMin(Object value, long min) {
        if (value == null) return false;

        if (value instanceof Number) {
            Number number = (Number) value;
            return number.longValue() >= min;
        } else if (value instanceof CharSequence) {
            CharSequence text = (CharSequence) value;
            return text.length() >= min;
        } else if (value instanceof Collection) {
            Collection collection = (Collection) value;
            return collection.size() >= min;
        } else if (value instanceof Map) {
            Map map = (Map) value;
            return map.size() >= min;
        } else if (value.getClass().isArray()) {
            return Array.getLength(value) >= min;
        }

        throw new UnrulyException("MinRule is not supported on type [" + value.getClass()
                + "] only supported on numbers, strings, collections, maps and arrays");
    }

    @Override
    public String getErrorMessage() {
        if (super.getErrorMessage() != null) return super.getErrorMessage();
        String bindingName = getBindingName();
        if (bindingName == null) bindingName = "NOT BOUND";
        return "Min value reached for [" + bindingName + "], it must be greater than [" + min + "]. Given {" + bindingName + "}";
    }
}
