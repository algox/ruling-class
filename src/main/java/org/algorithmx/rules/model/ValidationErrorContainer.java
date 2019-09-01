package org.algorithmx.rules.model;

import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.spring.util.Assert;

import java.util.*;

public class ValidationErrorContainer implements Iterable<ValidationError> {

    private final List<ValidationError> errors = Collections.synchronizedList(new ArrayList<>());

    public ValidationErrorContainer() {
        super();
    }

    public void add(ValidationError error) {
        Assert.notNull(error, "error cannot be null.");
        errors.add(error);
    }

    public ValidationError add(String ruleName, String errorCode) {
        return add(ruleName, errorCode, (String) null);
    }

    public ValidationError add(String ruleName, String errorCode, String errorMessage) {
        ValidationError result = new ValidationError(ruleName, errorCode, errorMessage);
        errors.add(result);
        return result;
    }

    public ValidationError add(String ruleName, String errorCode, Binding<Object>...params) {
        return add(ruleName, errorCode, null, params);
    }

    public ValidationError add(String ruleName, String errorCode, String errorMessage, Binding<Object>...params) {
        ValidationError result = add(ruleName, errorCode, errorMessage);

        if (params != null) {
            Arrays.stream(params).forEach(binding -> result.param(binding.getName(), binding.getTextValue()));
        }

        return result;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public ValidationError[] getErrors() {
        if (errors.isEmpty()) return null;
        return errors.toArray(new ValidationError[errors.size()]);
    }

    @Override
    public Iterator<ValidationError> iterator() {
        return errors.iterator();
    }
}
