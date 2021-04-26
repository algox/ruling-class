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
package org.algorithmx.rulii.validation;

import org.algorithmx.rulii.lib.spring.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Container for all Rule Violations.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class RuleViolations implements Iterable<RuleViolation> {

    private final List<RuleViolation> errors = Collections.synchronizedList(new ArrayList<>());

    public RuleViolations() {
        super();
    }

    /**
     * Adds a new violation.
     *
     * @param error error to be added.
     */
    public void add(RuleViolation error) {
        Assert.notNull(error, "error cannot be null.");
        errors.add(error);
    }

    public void add(RuleViolation...errors) {
        Assert.notNull(errors, "error cannot be null.");
        this.errors.addAll(Arrays.asList(errors));
    }

    /**
     * Adds a new violation with the given name and error code.
     *
     * @param ruleName rule name.
     * @param errorCode error code.
     * @return newly created violation.
     */
    public RuleViolation add(String ruleName, String errorCode) {
        return add(ruleName, errorCode, Severity.ERROR, null);
    }

    /**
     * Adds a new violation with the given name, error code and error message.
     *
     * @param ruleName rule name.
     * @param errorCode error code.
     * @param severity severity of the error.
     * @param errorMessage error message.
     * @return newly created violation.
     */
    public RuleViolation add(String ruleName, String errorCode, Severity severity, String errorMessage) {
        RuleViolation result = new RuleViolation(ruleName, errorCode, severity, errorMessage);
        errors.add(result);
        return result;
    }

    /**
     * Determines if this container has any associated errors (errors of severity FATAL or ERROR).
     *
     * @return true if this containers has any errors (errors of severity FATAL or ERROR); false otherwise.
     */
    public boolean hasSevereErrors() {
        return errors.stream().filter(e -> e.getSeverity() == Severity.FATAL || e.getSeverity() == Severity.ERROR).count() > 0;
    }

    /**
     * Determines if this container has any associated errors.
     *
     * @return true if this containers has any errors; false otherwise.
     */
    public boolean hasErrors() {
        return getErrorCount(Severity.ERROR) > 0;
    }

    /**
     * Returns the number of errors with the desired severity.
     *
     * @param severity desired severity.
     * @return number of errors with the desired severity.
     */
    public long getErrorCount(Severity severity) {
        return errors.stream().filter(e -> e.getSeverity() == severity).count();
    }

    /**
     * Determines if there are any FATAL errors in this container.
     *
     * @return true if there are any FATAL errors; false otherwise.
     */
    public boolean hasFataErrors() {
        return getErrorCount(Severity.FATAL) > 0;
    }

    /**
     * Determines if there are any WARNINGS in this container.
     *
     * @return true if there are any WARNINGS; false otherwise.
     */
    public boolean hasWarnings() {
        return getErrorCount(Severity.WARNING) > 0;
    }

    /**
     * Determines if there are any INFO messages in this container.
     *
     * @return true if there are any INFO messages; false otherwise.
     */
    public boolean hasInfoMessages() {
        return getErrorCount(Severity.INFO) > 0;
    }

    /**
     * Returns all the associated violation.
     *
     * @return Rule Violations.
     */
    public RuleViolation[] getViolations() {
        if (errors.isEmpty()) return new RuleViolation[0];
        return errors.toArray(new RuleViolation[errors.size()]);
    }

    /**
     * Returns the number of errors in this container.
     *
     * @return size of the container.
     */
    public int size() {
        return errors.size();
    }

    @Override
    public Iterator<RuleViolation> iterator() {
        return errors.iterator();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Violations [");
        errors.stream().forEach(error -> result.append(error.toString() + System.lineSeparator()));
        result.append("]");
        return result.toString();
    }
}
