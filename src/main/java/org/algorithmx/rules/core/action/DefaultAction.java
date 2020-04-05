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
package org.algorithmx.rules.core.action;

import org.algorithmx.rules.util.reflect.BindableMethodExecutor;
import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.spring.util.Assert;

import java.util.Arrays;

/**
 * Default Action implementation.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class DefaultAction implements Action {

    private BindableMethodExecutor methodExecutor = BindableMethodExecutor.create();
    private final ActionDefinition actionDefinition;
    private final Object target;

    /**
     * Ctor taking meta information and the target object.
     *
     * @param actionDefinition meta info.
     * @param target action target.
     */
    public DefaultAction(ActionDefinition actionDefinition, Object target) {
        super();
        Assert.notNull(actionDefinition, "actionDefinition cannot be null.");
        this.actionDefinition = actionDefinition;
        this.target = target;
    }

    @Override
    public void execute(Object... args) {
        try {
            // Execute the Action Method
            methodExecutor.execute(target, actionDefinition.getMethodDefinition(), args);
        } catch (UnrulyException e) {
            throw e;
        } catch (Exception e) {
            UnrulyException ex = new UnrulyException("Error trying to execute rule action ["
                    + getActionDefinition().getActionName()
                    + "] Method [" + getActionDefinition().getMethodDefinition().getMethod()
                    + "] Args [" + Arrays.toString(args) + "]", e);
            throw ex;
        }
    }

    @Override
    public ActionDefinition getActionDefinition() {
        return actionDefinition;
    }

    @Override
    public Object getTarget() {
        return target;
    }

    public void setMethodExecutor(BindableMethodExecutor methodExecutor) {
        Assert.notNull(methodExecutor, "methodExecutor cannot be null.");
        this.methodExecutor = methodExecutor;
    }
}