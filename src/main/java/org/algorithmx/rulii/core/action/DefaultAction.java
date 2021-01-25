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

import org.algorithmx.rulii.bind.match.ParameterMatch;
import org.algorithmx.rulii.core.UnrulyException;
import org.algorithmx.rulii.core.model.MethodDefinition;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.event.ActionExecution;
import org.algorithmx.rulii.event.EventType;
import org.algorithmx.rulii.event.ExecutionEvent;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.util.RuleUtils;
import org.algorithmx.rulii.util.reflect.MethodExecutor;

import java.util.Arrays;

/**
 * Default Action implementation.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class DefaultAction implements Action {

    private final MethodDefinition methodDefinition;
    private MethodExecutor methodExecutor;
    private final Object target;

    /**
     * Ctor taking meta information and the target object.
     *
     * @param methodDefinition meta info.
     * @param target action target.
     */
    public DefaultAction(Object target, MethodDefinition methodDefinition) {
        super();
        Assert.notNull(methodDefinition, "methodDefinition cannot be null.");
        this.methodDefinition = methodDefinition;
        this.target = target;
        this.methodExecutor = MethodExecutor.create(methodDefinition.getMethod());
    }

    @Override
    public Void run(RuleContext context) throws ActionExecutionException {
        Assert.notNull(context, "context cannot be null.");

        if (!context.isActive()) throw new UnrulyException("RuleContext is not Active. Perhaps it was stopped earlier ? "
                + "Create a new RuleContext and try again.");

        ParameterMatch[] matches = null;
        Object[] values = null;
        ExecutionEvent<ActionExecution> event = null;

        try {
            matches = context.match(getMethodDefinition());
            values = context.resolve(matches, getMethodDefinition());
            run(values);
            event = new ExecutionEvent(EventType.ON_ACTION, new ActionExecution(this, getMethodDefinition(),
                    RuleUtils.immutable(matches), values));
        } catch (Exception e) {
            event = new ExecutionEvent(EventType.ON_ACTION, new ActionExecution(this, e, getMethodDefinition(),
                    RuleUtils.immutable(matches), values));
            throw new ActionExecutionException("Unexpected error occurred trying to execute Action.",
                    e, this, matches, values);
        } finally {
            if (event != null) context.getEventProcessor().fireListeners(event);
        }

        return null;
    }

    @Override
    public void run(Object... args) {
        try {
            // Execute the Action Method
            methodExecutor.execute(target, args);
        } catch (Exception e) {
            UnrulyException ex = new UnrulyException("Error trying to execute action ["
                    + getMethodDefinition().getSignature() + "] Args [" + Arrays.toString(args) + "]", e);
            throw ex;
        }
    }

    @Override
    public final MethodDefinition getMethodDefinition() {
        return methodDefinition;
    }

    @Override
    public final Object getTarget() {
        return target;
    }

    public void setMethodExecutor(MethodExecutor methodExecutor) {
        Assert.notNull(methodExecutor, "methodExecutor cannot be null.");
        this.methodExecutor = methodExecutor;
    }
}