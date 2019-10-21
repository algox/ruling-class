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
import org.algorithmx.rules.validation.ValidationRuleTemplate;

/**
 * Must Be Defined validation Rule. Check to make sure the binding exists.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Given binding must exist.")
public class MustExistRule extends ValidationRuleTemplate {

    /**
     * Ctor taking in the binding name and error code.
     *
     * @param bindingName name of the Binding.
     * @param errorCode error code to be returned.
     */
    public MustExistRule(String bindingName, String errorCode) {
        this(bindingName, errorCode, Severity.FATAL, "[" + bindingName + "] must be defined.");
    }

    /**
     * Ctor taking in the binding name, error code and error message.
     *
     * @param bindingName name of the Binding.
     * @param errorCode error code to be returned.
     * @param severity severity of the error.
     * @param errorMessage error message to be returned.
     */
    public MustExistRule(String bindingName, String errorCode, Severity severity, String errorMessage) {
        super(bindingName + "_" + MustExistRule.class.getSimpleName(), bindingName, errorCode, severity, errorMessage);
    }

    @Override
    protected boolean when(Binding binding) {
        return binding != null;
    }
}
