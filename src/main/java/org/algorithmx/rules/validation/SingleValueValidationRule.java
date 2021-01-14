package org.algorithmx.rules.validation;

public abstract class SingleValueValidationRule extends ValidationRule {

    public SingleValueValidationRule(String errorCode, Severity severity, String defaultMessage) {
        super(errorCode, severity, defaultMessage);
    }

    public SingleValueValidationRule(String errorCode, Severity severity, String errorMessage, String defaultMessage) {
        super(errorCode, severity, errorMessage, defaultMessage);
    }

    public abstract Class<?>[] getSupportedTypes();

    public boolean isSupported(Class<?> type) {
        boolean result = false;

        for (Class<?> c : getSupportedTypes()) {
            if (c.isAssignableFrom(type)) {
                result = true;
                break;
            }
        }

        return result;
    }
}
