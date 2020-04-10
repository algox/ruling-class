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

import org.algorithmx.rules.bind.match.BindingMatchingStrategy;
import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.core.model.ParameterDefinition;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Binding Exception containing all the parameters avail during the attempted Bind.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class BindingException extends UnrulyException {

    /**
     * Binding Exception with just the message.
     *
     * @param message error message.
     */
    public BindingException(String message) {
        super(message);
    }

    /**
     * Binding Exception with a root cause.
     *
     * @param cause root cause.
     */
    public BindingException(Throwable cause) {
        super(cause);
    }

    /**
     * Binding Exception with a message and a root cause.
     *
     * @param message error message.
     * @param cause root cause.
     */
    public BindingException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a BindException with the following parameters.
     *
     * @param parameterDefinition parameter details.
     * @param method method details.
     * @param matches bind candidates.
     * @param matchingStrategy matching strategy used.
     * @param bindings all the bindings that are avail.
     */
    public BindingException(ParameterDefinition parameterDefinition,
                            Method method, Map<String, Binding<Object>> matches,
                            BindingMatchingStrategy matchingStrategy,
                            Bindings bindings) {
        super(generateMessage(null, parameterDefinition, method, matches, matchingStrategy, bindings));
    }

    /**
     * Creates a BindException with the following parameters.
     *
     * @param message basic message (first line).
     * @param parameterDefinition parameter details.
     * @param method method details.
     * @param matches bind candidates.
     * @param matchingStrategy matching strategy used.
     * @param bindings all the bindings that are avail.
     */
    public BindingException(String message, ParameterDefinition parameterDefinition,
                            Method method, Map<String, Binding<Object>> matches,
                            BindingMatchingStrategy matchingStrategy,
                            Bindings bindings) {
        super(generateMessage(message, parameterDefinition, method, matches, matchingStrategy, bindings));
    }

    private static String generateMessage(String message, ParameterDefinition parameterDefinition,
                                          Method method, Map<String, Binding<Object>> matches,
                                          BindingMatchingStrategy matchingStrategy,
                                          Bindings bindings) {
        return message != null ? (message + System.lineSeparator()) : ""
                + "Binding error trying to bind parameter [" + parameterDefinition.getName() + "]" + System.lineSeparator()
                + "Method ["  + method + "]" + System.lineSeparator()
                + "Matching strategy used [" + matchingStrategy.getClass().getSimpleName() + "]" + System.lineSeparator()
                + "Matches found [" + matches + "]" + System.lineSeparator()
                + "Bindings [" + bindings + "]" + System.lineSeparator();
    }
}
