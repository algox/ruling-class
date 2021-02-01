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

package org.algorithmx.rulii.core.ruleset;

import org.algorithmx.rulii.bind.BindingDeclaration;
import org.algorithmx.rulii.bind.Bindings;
import org.algorithmx.rulii.core.Identifiable;
import org.algorithmx.rulii.core.Runnable;
import org.algorithmx.rulii.core.condition.Condition;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.core.context.RuleContextBuilder;

/**
 * RuleSet is a logical grouping of Rules.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public interface RuleSet extends Runnable<RuleSetResult>, Identifiable, Iterable<Runnable>, Comparable<RuleSet> {

    RuleSetResult run(RuleContext context) throws RuleSetExecutionException;

    @Override
    default RuleSetResult run(Bindings bindings) throws RuleSetExecutionException {
        return run(RuleContextBuilder.build(bindings != null ? bindings : Bindings.create()));
    }

    @Override
    default RuleSetResult run(BindingDeclaration...params) throws RuleSetExecutionException {
        Bindings bindings = params != null ? Bindings.create().bind(params) : Bindings.create();
        return run(RuleContextBuilder.build(bindings));
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

    RuleSet getParent();

    /**
     * Returns the Condition (if one exists) to be met before the execution of the Rules.
     *
     * @return pre check before execution of the Rules.
     */
    Condition getPreCondition();

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

    <T extends Runnable> T get(int index, Class<T> type);

    <T extends Runnable> T get(String name, Class<T> type);

    Runnable[] getRuleSetItems();

    @Override
    default int compareTo(RuleSet other) {
        return getRuleSetDefinition().compareTo(other.getRuleSetDefinition());
    }
}

