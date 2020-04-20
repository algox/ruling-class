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
package org.algorithmx.rules.bind;

import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.core.model.MethodDefinition;
import org.algorithmx.rules.core.model.ParameterDefinition;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Binding Exception containing all the parameters avail during the attempted Bind.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class BindingException extends UnrulyException {

    /**
     * Creates a BindException with the following parameters.
     *
     * @param message basic message (first line).
     * @param parameterDefinition parameter details.
     * @param methodDefinition method details.
     * @param matches bind candidates.
     * @param bindings all the bindings that are avail.
     */
    public BindingException(String message, MethodDefinition methodDefinition, ParameterDefinition parameterDefinition,
                            Map<String, Binding<Object>> matches,
                            Bindings bindings) {
        super(generateMessage(message, methodDefinition, parameterDefinition, matches, bindings));
    }

    private static String generateMessage(String message, MethodDefinition methodDefinition,
                                          ParameterDefinition parameterDefinition, Map<String, Binding<Object>> matches,
                                          Bindings bindings) {
        return message + System.lineSeparator()
                + "Class : "  + methodDefinition.getMethod().getDeclaringClass() + System.lineSeparator()
                + "Method : "  + methodDefinition.getSignature() + System.lineSeparator()
                + "Parameter(index = " + parameterDefinition.getIndex() + ") : " + parameterDefinition.getTypeName () + " "
                + parameterDefinition.getName() + System.lineSeparator()
                + "Possible Matches : {"
                + matchesText(matches) + "}" + System.lineSeparator()
                + bindings;
    }

    private static String matchesText(Map<String, Binding<Object>> matches) {
        if (matches == null || matches.size() == 0) return "";
        return matches.values().stream()
                .map(m -> m.getSimpleDescription())
                .collect(Collectors.joining(", "));
    }
}