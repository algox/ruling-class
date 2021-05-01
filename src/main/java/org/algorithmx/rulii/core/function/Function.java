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

import org.algorithmx.rulii.bind.BindingDeclaration;
import org.algorithmx.rulii.bind.Bindings;
import org.algorithmx.rulii.core.Identifiable;
import org.algorithmx.rulii.core.Runnable;
import org.algorithmx.rulii.core.UnrulyException;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.core.context.RuleContextBuilder;
import org.algorithmx.rulii.core.model.MethodDefinition;

/**
 * Represents a function that accepts argument(s) and produces a result.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public interface Function<T> extends Runnable<T>, Identifiable {

    @Override
    default T run(RuleContext context) throws FunctionExecutionException {
        return apply(context);
    }

    @Override
    default T run(Bindings bindings) throws FunctionExecutionException {
        return run(RuleContextBuilder.build(bindings != null ? bindings : Bindings.create()));
    }

    @Override
    default T run(BindingDeclaration...params) throws FunctionExecutionException {
        Bindings bindings = params != null ? Bindings.create().bind(params) : Bindings.create();
        return run(RuleContextBuilder.build(bindings));
    }

    /**
     * Derives all the arguments and executes this Function.
     *
     * @param context Rule Context.
     * @return result of the function.
     * @throws FunctionExecutionException thrown if there are any errors during the Function execution.
     */
    T apply(RuleContext context) throws FunctionExecutionException;

    /**
     * Executes the Function given all the arguments it needs.
     *
     * @param params parameters in order.
     * @return result of the function.
     * @throws UnrulyException thrown if there are any runtime errors during the execution.
     */
    T apply(Object...params) throws UnrulyException;

    /**
     * Derives all the arguments and executes this Function.
     *
     * @param params Function Parameters.
     * @return result of the function.
     * @throws FunctionExecutionException thrown if there are any errors during the Condition execution.
     */
    default T apply(BindingDeclaration...params) throws FunctionExecutionException {
        Bindings bindings = params != null ? Bindings.create().bind(params) : Bindings.create();
        return apply(RuleContextBuilder.build(bindings));
    }

    /**
     * Meta information about the Function.
     *
     * @return Function meta information.
     */
    MethodDefinition getMethodDefinition();

    /**
     * The actual target instance the Function is associated to.
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
}
