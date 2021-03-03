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
package org.algorithmx.rulii.validation.rules.binding;

import org.algorithmx.rulii.annotation.Description;
import org.algorithmx.rulii.annotation.Rule;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.validation.BindingValidationRule;
import org.algorithmx.rulii.validation.Severity;

/**
 * Validation Rule to make sure the the given BindingName is defined.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Binding Name must exist.")
public class MustBeDefinedRule extends BindingValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {String.class};

    private static final String ERROR_CODE      = "rulii.validation.rules.MustBeDefinedRule.errorCode";
    private static final String DEFAULT_MESSAGE = "{0} not defined.";

    public MustBeDefinedRule(String bindingName) {
        this(bindingName, ERROR_CODE, Severity.ERROR, null);
    }

    public MustBeDefinedRule(String bindingName, String errorCode, Severity severity, String errorMessage) {
        super(bindingName, errorCode, severity, errorMessage, DEFAULT_MESSAGE);
    }

    @Override
    protected boolean isValid(RuleContext context, Object value) {
        return context.getBindings().contains(getBindingName());
    }

    @Override
    public Class<?>[] getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    @Override
    public String toString() {
        return "MustBeDefinedRule{" +
                "bindingName=" + getBindingName() +
                '}';
    }
}
