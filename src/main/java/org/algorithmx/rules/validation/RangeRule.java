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
 * Validation Rule to make sure the the value is in between the desired (min,max) range (inclusive).
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value is in between the desired (min,max) range (inclusive).")
public class RangeRule extends BindingValidationRule<Object> {

    private final long min;
    private final long max;

    /**
     * Ctor taking the error code and name of the Binding.
     *
     * @param min desired Min value.
     * @param max desired Max value.
     * @param errorCode error code.
     * @param bindingName name of the Binding.
     */
    public RangeRule(long min, long max, String errorCode, String bindingName) {
        super(errorCode, Severity.FATAL, null, value -> isInRange(value, min, max), bindingName);
        this.min = min;
        this.max = max;
    }

    /**
     * Ctor taking the error code and name of the Binding.
     *
     * @param min desired Min value.
     * @param max desired Max value.
     * @param errorCode error code.
     * @param supplier Binding.
     */
    public RangeRule(long min, long max, String errorCode, Supplier<Binding<Object>> supplier) {
        super(errorCode, Severity.FATAL, null, value -> isInRange(value, min, max), supplier);
        this.min = min;
        this.max = max;
    }

    public long getMin() {
        return min;
    }

    public long getMax() {
        return max;
    }

    /**
     * Determines if the given object (size/length) is greater than or equal to the Min value and less than or equal to
     * the Max value.
     *
     * @param value Object.
     * @param min given Object.
     * @param min Minimum value.
     * @param max Maximum value.
     * @return
     */
    private static boolean isInRange(Object value, long min, long max) {
        if (value == null) return false;

        if (value instanceof Number) {
            Number number = (Number) value;
            return number.longValue() >= min && number.longValue() <= max;
        } else if (value instanceof CharSequence) {
            CharSequence text = (CharSequence) value;
            return text.length() >= min && text.length() <= max;
        } else if (value instanceof Collection) {
            Collection collection = (Collection) value;
            return collection.size() >= min && collection.size() <= max;
        } else if (value instanceof Map) {
            Map map = (Map) value;
            return map.size() >= min && map.size() <= max;
        } else if (value.getClass().isArray()) {
            return Array.getLength(value) >= min && Array.getLength(value) <= max;
        }

        throw new UnrulyException("RangeRule is not supported on type [" + value.getClass()
                + "] only supported on numbers, string, collections, maps and arrays");
    }

    @Override
    public String getErrorMessage() {
        if (super.getErrorMessage() != null) return super.getErrorMessage();
        String bindingName = getBindingName();
        if (bindingName == null) bindingName = "NOT BOUND";
        return "Value for [" + bindingName + "] is out of range, it must be greater than [" + min
                + "] and less than [" + max + "]. Given {" + bindingName + "}";
    }
}
