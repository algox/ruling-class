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
import org.algorithmx.rules.model.Severity;

import java.util.function.Supplier;

/**
 * Must Not Be Defined validation Rule. Check to make sure the binding does not exist.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Given binding must not exist.")
public class MustNotBeDefinedRule extends BindingValidationRule<Object> {

    public MustNotBeDefinedRule(String pattern, String errorCode, String bindingName) {
        super(errorCode, Severity.FATAL, null, value -> true, bindingName);
    }

    public MustNotBeDefinedRule(String pattern, String errorCode, Supplier<Binding<Object>> supplier) {
        super(errorCode, Severity.FATAL, null, value -> true, supplier);
    }

    @Override
    protected boolean when(Binding<Object> binding) {
        return binding == null;
    }

    @Override
    public String getErrorMessage() {
        if (super.getErrorMessage() != null) return super.getErrorMessage();
        String bindingName = getBindingName();
        if (bindingName == null) bindingName = "NOT BOUND";
        return "Binding [" + bindingName + "] is already defined.";
    }
}