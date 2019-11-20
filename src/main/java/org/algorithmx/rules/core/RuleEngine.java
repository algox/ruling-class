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
package org.algorithmx.rules.core;

import org.algorithmx.rules.core.impl.DefaultRuleEngine;
import org.algorithmx.rules.error.UnrulyException;

/**
 * RuleEngine is responsible for executing the given Rules. It also keeps an audit trail of the execution.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public interface RuleEngine {

    /**
     * Create the default implementation of the Rule Engine.
     *
     * @return a new instance of the default Rule Engine.
     */
    static RuleEngine defaultRuleEngine() {
        return new DefaultRuleEngine();
    }

    /**
     * Name of the Rule Engine.
     *
     * @return name.
     */
    default String getName() {
        return getClass().getSimpleName();
    }

    /**
     * Executes the Rule Condition based on the RuleContext. If the result is true then any associated Actions are executed;
     * if the result is false then the Otherwise condition will be executed (if one exists).
     *
     * @param ctx used to derive the parameters required for this Rule.
     * @param rule Rule to run.
     * @throws UnrulyException thrown if there are any runtime errors during the execution.
     */
    void run(RuleContext ctx, Rule rule) throws UnrulyException;

    /**
     * Executes the given Rules. If the Rule Condition is true then any associated Actions will be executed.
     *
     * @param ctx state management for the Rule execution.
     * @param rules rules to execute.
     * @throws UnrulyException thrown if there are any runtime errors during the execution.
     */
    void run(RuleContext ctx, RuleSet...rules) throws UnrulyException;
}
