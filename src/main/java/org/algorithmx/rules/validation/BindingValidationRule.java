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

import org.algorithmx.rules.annotation.Match;
import org.algorithmx.rules.annotation.Given;
import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.bind.match.MatchByTypeMatchingStrategy;
import org.algorithmx.rules.core.rule.RuleContext;
import org.algorithmx.rules.core.rule.Severity;
import org.algorithmx.rules.lib.spring.util.Assert;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Validation rules for Binding(s).
 *
 * @param <T> Binding Type.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class BindingValidationRule<T> extends ValidationRule {

    private final String bindingName;
    private final Supplier<Binding<T>> supplier;
    private final Function<T, Boolean> condition;

    /**
     * Ctor taking error code, severity, error message, validation condition and the name of the Binding.
     *
     * @param errorCode validation error code.
     * @param severity validation error severity.
     * @param errorMessage validation error message (could be FormattedText).
     * @param condition Validation Rule Condition.
     * @param bindingName name of the Binding (this Validation Rule is for).
     */
    public BindingValidationRule(String errorCode, Severity severity, String errorMessage,
                                 Function<T, Boolean> condition, String bindingName) {
        super(errorCode, severity, errorMessage);
        Assert.notNull(condition, "condition cannot be null.");
        Assert.notNull(bindingName, "bindingName cannot be null.");
        this.condition = condition;
        this.bindingName = bindingName;
        this.supplier = null;
    }

    /**
     * Ctor taking error code, severity, error message, validation condition and the name of the Binding.
     *
     * @param errorCode validation error code.
     * @param severity validation error severity.
     * @param errorMessage validation error message (could be FormattedText).
     * @param condition Validation Rule Condition.
     * @param supplier Binding (this Validation Rule is for).
     */
    public BindingValidationRule(String errorCode, Severity severity, String errorMessage,
                                 Function<T, Boolean> condition, Supplier<Binding<T>> supplier) {
        super(errorCode, severity, errorMessage);
        Assert.notNull(condition, "condition cannot be null.");
        Assert.notNull(supplier, "supplier cannot be null.");
        this.condition = condition;
        this.supplier = supplier;
        this.bindingName = null;
    }

    /**
     * Rule condition : Check if the Binding is present and value is not null.
     *
     * @param ruleContext current rule context.
     * @return true if the validation condition is met; false otherwise.
     */
    @Given
    public boolean when(@Match(using = MatchByTypeMatchingStrategy.class) RuleContext ruleContext) {
        Binding<T> binding = supplier != null ? supplier.get() : ruleContext.getBindings().getBinding(bindingName);
        return when(binding);
    }

    protected boolean when(Binding<T> binding) {
        return binding != null ? when(binding.getValue()) : false;
    }

    /**
     * Delegating method to do the actual condition check.
     *
     * @param value Binding value.
     * @return true if condition is satisfied; false otherwise.
     */
    protected boolean when(T value) {
        return condition.apply(value);
    }

    /**
     * Retrieves the value of the Binding (if one can be found).
     *
     * @param ctx current rule context.
     * @return Binding value based on either the Binding name or the supplier.
     */
    @Override
    protected Map<String, Binding> resolveParameters(RuleContext ctx) {
        Map<String, Binding> result = new LinkedHashMap<>();

        if (bindingName != null) {
            result.put(bindingName, ctx.getBindings().getBinding(bindingName));
        } else {
            Binding binding = supplier.get();
            if (binding != null) result.put(binding.getName(), binding);
        }

        return result;
    }

    /**
     * Retrieves the Binding Name this Validation Rule is for.
     *
     * @return Binding Name if resolvable; null otherwise.
     */
    protected String getBindingName() {
        if (bindingName != null) return bindingName;
        Binding binding = supplier.get();
        return binding != null ? binding.getName() : "";
    }
}
