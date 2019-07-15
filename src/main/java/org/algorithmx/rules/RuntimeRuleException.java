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
package org.algorithmx.rules;

/**
 * Parent class of all Rule Runtime exceptions.
 * RuleRuntimeExceptions can be thrown anytime throughout the lifecycle of the Rule.
 * <p>
 * There are dedicated subclasses to encapsulate specific exception types.
 * <p>
 * These type of exceptions are generally fatal and not retry-able.
 * They indicate unresolvable error conditions and should be resolved before proceeding.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class RuntimeRuleException extends RuntimeException {

    /**
     * Ctor taking a message.
     *
     * @param message error message.
     */
    public RuntimeRuleException(String message) {
        super(message);
    }

    /**
     * Ctor taking a source exception.
     *
     * @param cause exception.
     */
    public RuntimeRuleException(Throwable cause) {
        super(cause);
    }

    /**
     * Ctor taking a message and a source exception.
     *
     * @param message error messge.
     * @param cause exception.
     */
    public RuntimeRuleException(String message, Throwable cause) {
        super(message, cause);
    }

}
