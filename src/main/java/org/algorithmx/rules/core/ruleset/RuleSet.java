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
package org.algorithmx.rules.core.ruleset;

import org.algorithmx.rules.bind.BindingDeclaration;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.Identifiable;
import org.algorithmx.rules.core.action.Action;
import org.algorithmx.rules.core.condition.Condition;
import org.algorithmx.rules.core.context.RuleContext;
import org.algorithmx.rules.core.context.RuleContextBuilder;
import org.algorithmx.rules.core.rule.Rule;

/**
 * RuleSet is a logical grouping of Rules.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public interface RuleSet extends Identifiable, Iterable<Rule> {

    RuleResultSet run(RuleContext ctx) throws RuleSetExecutionException;

    /**
     * Derives all the arguments and executes this RuleSet.
     *
     * @param bindings RuleSet Bindings.
     * @return execution result of the RuleSet.
     * @throws RuleSetExecutionException thrown if there are any runtime errors during the execution.
     */
    default RuleResultSet run(Bindings bindings) throws RuleSetExecutionException {
        return run(RuleContextBuilder.create(bindings != null ? bindings : Bindings.create()));
    }

    /**
     * Derives all the arguments and executes this RuleSet.
     *
     * @param params RuleSet Parameters.
     * @return execution result of the RuleSet.
     * @throws RuleSetExecutionException thrown if there are any runtime errors during the execution.
     */
    default RuleResultSet run(BindingDeclaration...params) throws RuleSetExecutionException {
        Bindings bindings = params != null ? Bindings.create().bind(params) : Bindings.create();
        return run(RuleContextBuilder.create(bindings));
    }

    RuleSetDefinition getRuleSetDefinition();

    /**
     * Ruleset name.
     *
     * @return name.
     */
    String getName();

    /**
     * RuleSet description.
     *
     * @return description.
     */
    String getDescription();

    /**
     * Returns the Condition (if one exists) to be met before the execution of the Rules.
     *
     * @return pre check before execution of the Rules.
     */
    Condition getPreCondition();

    /**
     * Returns the action to be performed before the execution of the Rules.
     *
     * @return pre action before the execution of the Rules.
     */
    Action getPreAction();

    /**
     * Returns the action to be performed after the execution of the Rules.
     *
     * @return post action after the execution of the Rules.
     */
    Action getPostAction();

    /**
     * Returns the Condition that determines when execution should stop.
     *
     * @return stopping condition.
     */
    Condition getStopCondition();

    /**
     * Size of this RuleSet (ie. number of Rules in this RuleSet)
     *
     * @return number of Rules in this RuleSet.
     */
    int size();

    /**
     * Returns all the Rules in this RuleSet.
     *
     * @return rules.
     */
    Rule[] getRules();

    Rule get(int index);

    Rule get(String ruleName);

    /**
     * Responsible to handling errors during Rule Execution. It determines whether to proceed or not.
     *
     * @return Error Condition that decides whether the execution should proceed or not.
     */
    Condition getErrorCondition();
}

