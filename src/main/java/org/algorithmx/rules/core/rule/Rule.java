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
package org.algorithmx.rules.core.rule;

import org.algorithmx.rules.core.Identifiable;
import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.core.action.Action;
import org.algorithmx.rules.core.condition.Condition;
import org.algorithmx.rules.core.context.RuleContext;

/**
 * Rule class encapsulates all the properties/methods of a Rule within the framework. A Rule consists of two parts
 * a Condition and a list of associated Actions. You can think of it as a If (Condition).. then Action(s).
 *
 * The Condition is stateless and should be able to execute many times without any side effects (idempotent).
 * The Action(s) can be stateful.
 *
 * A Rule Condition can be tested via :
 * the isPass(..), isFail(...) and test() methods. Those methods must be given the arguments the rule requires. These
 * methods are solely there for the purpose of executing them manually to test the Rule. You should use the RuleEngine
 * to automate the process of checking the Condition and running the associated Actions.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public interface Rule<T> extends Identifiable {

    // Rule Name Pattern.
    String NAME_REGEX = "^[a-zA-Z][a-zA-Z0-9]*?$";

    /**
     * Executes the Rule Condition based on the RuleContext. If the result is true then any associated Actions are executed;
     * if the result is false then the Otherwise condition will be executed (if one exists).
     *
     * @param ctx used to derive the parameters required for this Rule.
     * @return execution status of the rule.
     * @throws UnrulyException thrown if there are any runtime errors during the execution.
     */
    RuleResult run(RuleContext ctx) throws UnrulyException;

    /**
     * The actual Rule implementation instance.
     *
     * @return Rule instance.
     */
    T getTarget();

    /**
     * Meta information about the Rule.
     *
     * @return Rule meta information.
     */
    RuleDefinition getRuleDefinition();

    /**
     * Rule Pre-Condition.
     *
     * @return Rule Condition.
     */
    Condition getPreCondition();

    /**
     * Rule Condition.
     *
     * @return Rule Condition.
     */
    Condition getCondition();

    /**
     * Any associated Actions.
     *
     * @return associated actions.
     */
    Action[] getActions();

    /**
     * Otherwise Action.
     *
     * @return otherwise Action for this Rule.
     */
    Action getOtherwiseAction();
}
