package org.algorithmx.rules.model;

import org.algorithmx.rules.spring.util.Assert;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class ValidationError {

    private final String ruleName;
    private final String errorCode;
    private final String errorMessage;
    private final Map<String, String> params = new LinkedHashMap<>();

    public ValidationError(String ruleName, String errorCode) {
        this(ruleName, errorCode, null);
    }

    public ValidationError(String ruleName, String errorCode, String errorMessage) {
        super();
        Assert.notNull(ruleName, "ruleName cannot be null.");
        Assert.notNull(errorCode, "errorCode cannot be null.");
        this.ruleName = ruleName;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getRuleName() {
        return ruleName;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Map<String, String> getParameters() {
        if (params.size() == 0) return null;
        return Collections.unmodifiableMap(params);
    }

    public ValidationError param(String name, String value) {
        params.put(name, value);
        return this;
    }
}
