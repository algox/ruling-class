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

package org.algorithmx.rulii.core.action;

import org.algorithmx.rulii.bind.BindingDeclaration;
import org.algorithmx.rulii.bind.Bindings;
import org.algorithmx.rulii.core.Identifiable;
import org.algorithmx.rulii.core.Ordered;
import org.algorithmx.rulii.core.Runnable;
import org.algorithmx.rulii.core.UnrulyException;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.core.context.RuleContextBuilder;
import org.algorithmx.rulii.core.model.MethodDefinition;

/**
 * Represents an operation that accepts input arguments and returns no result.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public interface Action extends Runnable<Void>, Identifiable, Ordered {

    /**
     * Derives all the arguments and executes this Action.
     *
     * @param context Rule Context.
     * @throws ActionExecutionException thrown if there are any errors during the Action execution.
     */
    Void run(RuleContext context) throws ActionExecutionException;

    @Override
    default Void run(Bindings bindings) throws ActionExecutionException {
        return run(RuleContextBuilder.build(bindings != null ? bindings : Bindings.create()));
    }

    @Override
    default Void run(BindingDeclaration...params) throws ActionExecutionException {
        Bindings bindings = params != null ? Bindings.create().bind(params) : Bindings.create();
        return run(RuleContextBuilder.build(bindings));
    }

    /**
     * Executes thr Action given all the arguments it needs.
     *
     * @param params Action parameters in order.
     * @throws UnrulyException thrown if there are any runtime errors during the execution.
     */
    void run(Object...params) throws UnrulyException;

    /**
     * Meta information about the Action.
     *
     * @return Action meta information.
     */
    MethodDefinition getMethodDefinition();

    /**
     * The actual target instance the Action is associated to.
     *
     * @return target instance.
     */
    Object getTarget();

    @Override
    default String getName() {
        MethodDefinition methodDefinition = getMethodDefinition();
        return methodDefinition != null ? methodDefinition.getName() : null;
    }

    @Override
    default String getDescription() {
        MethodDefinition methodDefinition = getMethodDefinition();
        return methodDefinition != null ? methodDefinition.getDescription() : null;
    }

    @Override
    default int getOrder() {
        return getMethodDefinition().getOrder();
    }
}
