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

import org.algorithmx.rules.bind.BindingDeclaration;
import org.algorithmx.rules.bind.Bindings;
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
     * Executes the given Rules. If the Rule Condition is true then any associated Actions will be executed.
     *
     * @param rule rules to execute.
     * @param ctx state management for the Rule execution.
     * @throws UnrulyException thrown if there are any runtime errors during the execution.
     */
    void run(RuleSet rule, RuleContext ctx) throws UnrulyException;

    /**
     * Executes the given Rules. If the Rule Condition is true then any associated Actions will be executed.
     *
     * @param rule rules to execute.
     * @param bindings bindings to use for the Rule execution.
     * @return RuleContext that was used for the execution.
     * @throws UnrulyException thrown if there are any runtime errors during the execution.
     */
    default RuleContext run(RuleSet rule, BindingDeclaration...bindings) {
        RuleContext result = new RuleContext(Bindings.defaultBindings().bind(bindings));
        run(rule, result);
        return result;
    }

    /**
     * Executes the given Rules. If the Rule Condition is true then any associated Actions will be executed.
     *
     * @param rule rules to execute.
     * @param bindings bindings to use for the Rule execution.
     * @return RuleContext that was used for the execution.
     * @throws UnrulyException thrown if there are any runtime errors during the execution.
     */
    default RuleContext run(RuleSet rule, Bindings bindings) throws UnrulyException {
        RuleContext result = new RuleContext(bindings);
        run(rule, result);
        return result;
    }
}
