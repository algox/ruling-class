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
package org.algorithmx.rules.model;

import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.spring.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Container for all validation errors.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class ValidationErrorContainer implements Iterable<ValidationError> {

    private final List<ValidationError> errors = Collections.synchronizedList(new ArrayList<>());

    public ValidationErrorContainer() {
        super();
    }

    /**
     * Adds a new validation error.
     *
     * @param error error to be added.
     */
    public void add(ValidationError error) {
        Assert.notNull(error, "error cannot be null.");
        errors.add(error);
    }

    /**
     * Adds a new Validation Error with the given name and error code.
     *
     * @param ruleName rule name.
     * @param errorCode validation error code.
     * @return newly created Validation error.
     */
    public ValidationError add(String ruleName, String errorCode) {
        return add(ruleName, errorCode, (String) null);
    }

    /**
     * Adds a new Validation Error with the given name, error code and error message.
     *
     * @param ruleName rule name.
     * @param errorCode validation error code.
     * @param errorMessage validation error message.
     * @return newly created Validation error.
     */
    public ValidationError add(String ruleName, String errorCode, String errorMessage) {
        ValidationError result = new ValidationError(ruleName, errorCode, errorMessage);
        errors.add(result);
        return result;
    }

    /**
     * Adds a new Validation Error with the given name, error code and the parameters.
     *
     * @param ruleName rule name.
     * @param errorCode validation error code.
     * @param params rule parameters.
     * @return newly created Validation error.
     */
    public ValidationError add(String ruleName, String errorCode, Binding<Object>...params) {
        return add(ruleName, errorCode, null, params);
    }

    /**
     * Adds a new Validation Error with the given name, error code, error message and the parameters.
     *
     * @param ruleName rule name.
     * @param errorCode validation error code.
     * @param errorMessage validation error message.
     * @param params rule parameters.
     * @return newly created Validation error.
     */
    public ValidationError add(String ruleName, String errorCode, String errorMessage, Binding<Object>...params) {
        ValidationError result = add(ruleName, errorCode, errorMessage);

        if (params != null) {
            Arrays.stream(params).forEach(binding -> result.param(binding.getName(), binding.getTextValue()));
        }

        return result;
    }

    /**
     * Determines if this container has any associated errors.
     *
     * @return true if this containers has any errors; false otherwise.
     */
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    /**
     * Returns all the associated validation errors.
     *
     * @return validation errors.
     */
    public ValidationError[] getErrors() {
        if (errors.isEmpty()) return null;
        return errors.toArray(new ValidationError[errors.size()]);
    }

    @Override
    public Iterator<ValidationError> iterator() {
        return errors.iterator();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Errors [" + System.lineSeparator());
        errors.stream().forEach(error -> result.append(error.toString() + System.lineSeparator()));
        result.append("]" + System.lineSeparator());
        return result.toString();
    }
}
