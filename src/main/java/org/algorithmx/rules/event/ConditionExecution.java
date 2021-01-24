/**
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 1999-2021, Algorithmx Inc.
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
package org.algorithmx.rules.event;

import org.algorithmx.rules.bind.match.ParameterMatch;
import org.algorithmx.rules.core.condition.Condition;
import org.algorithmx.rules.core.model.MethodDefinition;
import org.algorithmx.rules.lib.spring.util.Assert;

import java.util.Arrays;

public class ConditionExecution {

    private final Condition condition;
    private final Boolean result;
    private final Exception error;
    private final MethodDefinition methodDefinition;
    private final ParameterMatch[] parameterMatches;
    private final Object[] values;

    public ConditionExecution(Condition condition, boolean result, MethodDefinition methodDefinition,
                              ParameterMatch[] parameterMatches, Object[] values) {
        this(condition, result, null, methodDefinition, parameterMatches, values);
    }

    public ConditionExecution(Condition condition, Exception error, MethodDefinition methodDefinition,
                              ParameterMatch[] parameterMatches, Object[] values) {
        this(condition, null, error, methodDefinition, parameterMatches, values);
    }

    private ConditionExecution(Condition condition, Boolean result, Exception error, MethodDefinition methodDefinition,
                               ParameterMatch[] parameterMatches, Object[] values) {
        super();
        Assert.notNull(condition, "condition cannot be null.");
        this.condition = condition;
        this.result = result;
        this.error = error;
        this.methodDefinition = methodDefinition;
        this.parameterMatches = parameterMatches;
        this.values = values;
    }

    public Condition getCondition() {
        return condition;
    }

    public Boolean getResult() {
        return result;
    }

    public boolean isError() {
        return error != null;
    }

    public boolean isSuccess() {
        return error == null;
    }

    public MethodDefinition getMethodDefinition() {
        return methodDefinition;
    }

    public ParameterMatch[] getParameterMatches() {
        return parameterMatches;
    }

    public Object[] getValues() {
        return values;
    }

    @Override
    public String toString() {
        return "ConditionExecution{" +
                "condition=" + condition +
                ", result=" + result +
                ", methodDefinition=" + methodDefinition +
                ", parameterMatches=" + Arrays.toString(parameterMatches) +
                ", values=" + Arrays.toString(values) +
                '}';
    }
}
