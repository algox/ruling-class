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

import org.algorithmx.rules.annotation.Bind;
import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Given;
import org.algorithmx.rules.annotation.Otherwise;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.bind.BindingMatchingStrategyType;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.impl.RulingClass;
import org.algorithmx.rules.spring.util.Assert;

/**
 * Not Null validation Rule. Check to make sure the binding is declared and the value is not null.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Given binding cannot be null.")
public class NotNullRule extends RulingClass {

    private final String bindingName;
    private final String errorCode;
    private final String errorMessage;

    /**
     * Ctor taking in the binding name and error code.
     *
     * @param bindingName name of the Binding.
     * @param errorCode error code to be returned.
     */
    public NotNullRule(String bindingName, String errorCode) {
        this(bindingName, errorCode, "Binding [" + bindingName + "] cannot be null.");
    }

    /**
     * Ctor taking in the binding name, error code and error message.
     *
     * @param bindingName name of the Binding.
     * @param errorCode error code to be returned.
     * @param errorMessage error message to be returned.
     */
    public NotNullRule(String bindingName, String errorCode, String errorMessage) {
        super();
        Assert.notNull(bindingName, "bindingName cannot be null.");
        Assert.notNull(errorCode, "errorCode cannot be null.");
        this.bindingName = bindingName;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    /**
     * Rule condition : Check if the Binding is present and value is not null.
     *
     * @param bindings takes in all the Bindings.
     * @return true if the Binding is present and the value is not null.
     */
    @Given
    public boolean when(@Bind(using = BindingMatchingStrategyType.MATCH_BY_TYPE) Bindings bindings) {
        Binding binding = bindings.getBinding(bindingName);
        return binding != null && binding.get() != null;
    }

    /**
     * Rule Action : add the desired error code and message to the error container.
     *
     * @param errors error container.
     */
    @Otherwise
    public void otherwise(@Bind(using = BindingMatchingStrategyType.MATCH_BY_TYPE) ValidationErrorContainer errors) {
        errors.add(getName(), errorCode, errorMessage).param(bindingName, null);
    }
}
