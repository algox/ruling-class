/*
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

package org.algorithmx.rulii.event;

import org.algorithmx.rulii.bind.match.ParameterMatch;
import org.algorithmx.rulii.core.function.Function;
import org.algorithmx.rulii.core.model.MethodDefinition;
import org.algorithmx.rulii.lib.spring.util.Assert;

import java.util.Arrays;

public class FunctionExecution<T> {

    private final Function<T> function;
    private final T result;
    private final Exception error;
    private final MethodDefinition methodDefinition;
    private final ParameterMatch[] parameterMatches;
    private final Object[] values;

    public FunctionExecution(Function<T> function, T result, MethodDefinition methodDefinition,
                             ParameterMatch[] parameterMatches, Object[] values) {
        this(function, result, null, methodDefinition, parameterMatches, values);
    }

    public FunctionExecution(Function<T> function, Exception error, MethodDefinition methodDefinition,
                             ParameterMatch[] parameterMatches, Object[] values) {
        this(function, null, error, methodDefinition, parameterMatches, values);
    }

    private FunctionExecution(Function<T> function, T result, Exception error, MethodDefinition methodDefinition,
                             ParameterMatch[] parameterMatches, Object[] values) {
        super();
        Assert.notNull(function, "function cannot be null.");
        this.function = function;
        this.result = result;
        this.error = error;
        this.methodDefinition = methodDefinition;
        this.parameterMatches = parameterMatches;
        this.values = values;
    }

    public Function<T> getFunction() {
        return function;
    }

    public T getResult() {
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
        return "FunctionExecution{" +
                "function=" + function +
                ", result=" + result +
                ", methodDefinition=" + methodDefinition +
                ", parameterMatches=" + Arrays.toString(parameterMatches) +
                ", values=" + Arrays.toString(values) +
                '}';
    }
}
