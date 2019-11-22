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

import org.algorithmx.rules.annotation.Bind;
import org.algorithmx.rules.annotation.Otherwise;
import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.bind.BindingMatchingStrategyType;
import org.algorithmx.rules.bind.ParameterResolver;
import org.algorithmx.rules.core.RuleContext;
import org.algorithmx.rules.core.impl.RulingClass;
import org.algorithmx.rules.model.Severity;
import org.algorithmx.rules.spring.util.Assert;
import org.algorithmx.rules.util.FormattedText;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Template class for Validation rules. Attempts to generalize the boilerplate code associated with Validation Rules.
 * This class will automatically add a ValidationError to the ValidationErrorContainer when the Validation condition is
 * not met.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public abstract class ValidationRule extends RulingClass {

    private Severity severity;
    private String errorMessage;
    private final String errorCode;

    /**
     * Ctor taking in the error code.
     *
     * @param errorCode error code.
     */
    protected ValidationRule(String errorCode) {
        this(errorCode, Severity.ERROR, null);
    }

    /**
     * Ctor taking in the error code and the severity of the error.
     *
     * @param errorCode error code.
     * @param severity error severity.
     */
    protected ValidationRule(String errorCode, Severity severity) {
        this(errorCode, severity, null);
    }

    /**
     * Ctor taking in the error code and the message of the error.
     *
     * @param errorCode error code.
     * @param errorMessage error message.
     */
    protected ValidationRule(String errorCode, String errorMessage) {
        this(errorCode, Severity.ERROR, errorMessage);
    }

    /**
     * Ctor taking in the error code, severity of the error and the message of the error.
     *
     * @param errorCode error code.
     * @param severity severity of the error.
     * @param errorMessage error message.
     */
    protected ValidationRule(String errorCode, Severity severity, String errorMessage) {
        super();
        Assert.notNull(errorCode, "errorCode cannot be null.");
        Assert.notNull(severity, "severity cannot be null.");
        this.errorCode = errorCode;
        this.severity = severity;
        this.errorMessage = errorMessage;
    }

    /**
     * Automatically add a validation error when the Validation condition is not met.
     *
     * @param ctx current rule context.
     * @param errors container of errors (must be defined).
     */
    @Otherwise
    public void otherwise(@Bind(using = BindingMatchingStrategyType.MATCH_BY_TYPE) RuleContext ctx,
                          @Bind(using = BindingMatchingStrategyType.MATCH_BY_TYPE) ValidationErrorContainer errors) {
        Map<String, Binding> matches = resolveParameters(ctx);
        ValidationError error = createValidationError(getErrorCode(), getSeverity(), getErrorMessage(), matches);
        if (error != null) errors.add(error);
    }

    /**
     * Resolves the method parameters used by the @Given condition.
     *
     * @param ctx current rule context.
     * @return parameters used by the @Given condition.
     */
    protected Map<String, Binding> resolveParameters(RuleContext ctx) {
        ParameterResolver.ParameterMatch[] matches = ctx.getParameterResolver().resolveAsBindings(
                getRuleDefinition().getConditionDefinition().getMethodDefinition(), ctx.getBindings(), ctx.getMatchingStrategy());

        Map<String, Binding> result = new LinkedHashMap<>();

        if (matches != null) {
            for (ParameterResolver.ParameterMatch match : matches) {
                if (match == null) continue;
                result.put(match.getDefinition().getName(), match.getBinding());
            }
        }

        return result;
    }

    /**
     * Create the validation error based on the parameters used by the @Given condition.
     *
     * @param errorCode validation error code.
     * @param severity severity of the validation error.
     * @param errorMessage error message.
     * @param matches matched parameter values of the @Given condition.
     * @return newly created Validation Error.
     */
    protected ValidationError createValidationError(String errorCode, Severity severity, String errorMessage,
                                                    Map<String, Binding> matches) {
        ValidationError result = new ValidationError(getName(), errorCode, severity, formatErrorMessage(errorMessage, matches));
        addParameters(result, matches);
        return result;
    }

    /**
     * Formats the error message. Replaces the place holders with proper values.
     *
     * @param errorMessage formatted text of the error message.
     * @param matches matched parameter values.
     * @see FormattedText
     * @return error message (with replaced values).
     */
    protected String formatErrorMessage(String errorMessage, Map<String, Binding> matches) {
        FormattedText formattedText = FormattedText.parse(errorMessage);

        if (!formattedText.requiresFormatting()) return errorMessage;

        Map<String, Object> markers = new HashMap<>();

        if (matches != null) {
            for (Map.Entry<String, Binding> match : matches.entrySet()) {
                markers.put(match.getKey(), match.getValue() != null ? match.getValue().getValue() : null);
            }
        }

        return formattedText.format(markers);
    }

    /**
     * Adds the parameters to the Validation error.
     *
     * @param error existing error.
     * @param matches parameter matches.
     */
    protected void addParameters(ValidationError error, Map<String, Binding> matches) {
        Assert.notNull(error, "ValidationError cannot be null.");

        if (matches != null) {
            for (Map.Entry<String, Binding> match : matches.entrySet()) {
                error.param(match.getKey(), match.getValue() != null ? match.getValue().getTextValue() : null);
            }
        }
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Severity getSeverity() {
        return severity;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
