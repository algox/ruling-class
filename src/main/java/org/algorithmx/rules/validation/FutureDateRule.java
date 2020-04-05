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

import java.util.Date;
import java.util.function.Supplier;

/**
 * Validation Rule to make sure the Date binding is in the future.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Date binding is in the future.")
public class FutureDateRule extends BindingValidationRule<Date> {

    /**
     * Ctor taking the error code and name of the Binding.
     *
     * @param errorCode error code.
     * @param bindingName name of the Binding.
     */
    public FutureDateRule(String errorCode, String bindingName) {
        super(errorCode, Severity.FATAL, null, date -> isFuture(date), bindingName);
    }

    /**
     * Ctor taking the error code and name of the Binding.
     *
     * @param errorCode error code.
     * @param supplier Binding.
     */
    public FutureDateRule(String errorCode, Supplier<Binding<Date>> supplier) {
        super(errorCode, Severity.FATAL, null, date -> isFuture(date), supplier);
    }

    /**
     * Determines if the given date is in the future.
     *
     * @param date given date.
     * @return true if the given date is in the future; false otherwise.
     */
    private static boolean isFuture(Date date) {
        Date currentDate = new Date();
        return date != null && currentDate.before(date);
    }

    @Override
    public String getErrorMessage() {
        if (super.getErrorMessage() != null) return super.getErrorMessage();
        String bindingName = getBindingName();
        if (bindingName == null) bindingName = "NOT BOUND";
        return "Date [" + bindingName + "] must be in the future. Given {" + bindingName + "}";
    }
}
