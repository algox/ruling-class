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

package org.algorithmx.rulii.core.function;

import org.algorithmx.rulii.bind.match.ParameterMatch;
import org.algorithmx.rulii.core.UnrulyException;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.util.RuleUtils;

public class FunctionExecutionException extends UnrulyException {

    private Function function;
    private ParameterMatch[] matches;
    private Object[] values;

    public FunctionExecutionException(String message, Throwable cause, Function function, ParameterMatch[] matches,
                                      Object[] values) {
        super(message + System.lineSeparator() + RuleUtils.getMethodDescription(function.getMethodDefinition(),
                matches, values, ""), cause);
        Assert.notNull(function, "function cannot be null.");
        this.function = function;
        this.matches = matches;
        this.values = values;
    }

    public Function getFunction() {
        return function;
    }

    public ParameterMatch[] getMatches() {
        return matches;
    }

    public Object[] getValues() {
        return values;
    }
}
