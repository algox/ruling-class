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

import org.algorithmx.rules.spring.util.Assert;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Class containing all the details of a Validation Error. It contains Rule Name, error code, error message and all
 * relevant parameters.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class ValidationError {

    private final String ruleName;
    private final String errorCode;
    private final Severity severity;
    private final String errorMessage;
    private final Map<String, String> params = new LinkedHashMap<>();

    public ValidationError(String ruleName, String errorCode, String errorMessage) {
        this(ruleName, errorCode, Severity.ERROR, errorMessage);
    }

    public ValidationError(String ruleName, String errorCode, Severity severity, String errorMessage) {
        super();
        Assert.notNull(ruleName, "ruleName cannot be null.");
        Assert.notNull(errorCode, "errorCode cannot be null.");
        Assert.notNull(severity, "severity cannot be null.");
        this.ruleName = ruleName;
        this.errorCode = errorCode;
        this.severity = severity == null ? Severity.ERROR : severity;
        this.errorMessage = errorMessage;
    }

    /**
     * Name of the rule.
     *
     * @return rule name.
     */
    public String getRuleName() {
        return ruleName;
    }

    /**
     * Associated Error code.
     *
     * @return validation error code.
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Severity of this error.
     *
     * @return error severity.
     */
    public Severity getSeverity() {
        return severity;
    }

    /**
     * Optional error message.
     *
     * @return validation error message.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Associated rule parameters.
     *
     * @return validation rule parameters.
     */
    public Map<String, String> getParameters() {
        if (params.size() == 0) return null;
        return Collections.unmodifiableMap(params);
    }

    /**
     * Adds a new rule parameter.
     *
     * @param name parameter name.
     * @param value parameter value. Null or toString() value.
     * @return error.
     */
    public ValidationError param(String name, Object value) {
        params.put(name, value != null ? value.toString() : null);
        return this;
    }

    /**
     * Adds a new rule parameter.
     *
     * @param name parameter name.
     * @param value parameter value.
     * @return error.
     */
    public ValidationError param(String name, String value) {
        params.put(name, value);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidationError that = (ValidationError) o;
        return ruleName.equals(that.ruleName) &&
                errorCode.equals(that.errorCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ruleName, errorCode);
    }

    @Override
    public String toString() {
        return "ValidationError{" +
                "ruleName='" + ruleName + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", severity=" + severity +
                ", errorMessage='" + errorMessage + '\'' +
                ", params=" + params +
                '}';
    }
}
