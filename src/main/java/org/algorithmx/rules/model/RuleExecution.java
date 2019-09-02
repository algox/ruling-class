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

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Rule audit details. Contains all the details of a Rule Execution. It contains the details of the rule, all the associated
 * rule parameters, result or error and the time of the execution.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public final class RuleExecution implements Comparable<RuleExecution> {

    // Make sure we don't hold onto the actual rule definition
    private final WeakReference<RuleDefinition> ruleDefinition;
    private final Map<String, String> params = new LinkedHashMap<>();
    private final Date time = new Date();
    private Boolean result;
    private Exception error;

    public RuleExecution(RuleDefinition ruleDefinition) {
        super();
        Assert.notNull(ruleDefinition, "ruleDefinition cannot be null.");
        this.ruleDefinition = new WeakReference<>(ruleDefinition);
    }

    /**
     * Returns the assocated Rule Definition. Could be null.
     *
     * @return rule definition (if avail).
     */
    public RuleDefinition getRuleDefinition() {
        return ruleDefinition.get();
    }

    /**
     * Adds all the associated rule parameters.
     *
     * @param bindings rule parameters.
     */
    public void add(Binding<Object>...bindings) {
        if (bindings == null || bindings.length == 0) return;
        Arrays.stream(bindings).forEach(this::add);
    }

    /**
     * Adds a rule parameter.
     *
     * @param binding rule parameter.
     */
    public void add(Binding<Object> binding) {
        if (binding == null) return;
        params.put(binding.getName(), binding.getValue() != null ? binding.getValue().toString() : null);
    }

    /**
     * Retrieves all the rule parameters.
     *
     * @return rule parameters.
     */
    public Map<String, String> getParams() {
        return params;
    }

    /**
     * Result of the Rule execution.
     *
     * @return true if the rule passed; false otherwise.
     */
    public Boolean getResult() {
        return result;
    }

    /**
     * Return the exception that occurred during the execution of the rule/
     * @return exception.
     */
    public Exception getError() {
        return error;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public void setError(Exception error) {
        this.error = error;
    }

    public Date getTime() {
        return time;
    }

    public boolean isError() {
        return error != null;
    }

    @Override
    public int compareTo(RuleExecution execution) {
        return getTime().compareTo(execution.getTime());
    }

    @Override
    public String toString() {
        return "RuleExecution{" +
                "ruleDefinition=" + (ruleDefinition.get() != null ? ruleDefinition.get().getName() : "n/a") +
                ", args=" + params +
                ", result=" + result +
                ", error=" + error +
                ", time=" + time +
                '}';
    }
}
