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
package org.algorithmx.rules.core.condition;

import org.algorithmx.rules.util.reflect.BindableMethodExecutor;
import org.algorithmx.rules.error.UnrulyException;
import org.algorithmx.rules.model.ConditionDefinition;
import org.algorithmx.rules.spring.util.Assert;

import java.util.Arrays;

/**
 * Default Condition implementation.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class DefaultCondition implements Condition {

    private BindableMethodExecutor methodExecutor = BindableMethodExecutor.defaultBindableMethodExecutor();
    private final ConditionDefinition conditionDefinition;
    private final Object target;

    /**
     * Ctor taking meta information and the target object.
     *
     * @param conditionDefinition meta info.
     * @param target action target.
     */
    public DefaultCondition(ConditionDefinition conditionDefinition, Object target) {
        super();
        Assert.notNull(conditionDefinition, "conditionDefinition cannot be null.");
        this.conditionDefinition = conditionDefinition;
        this.target = target;
    }

    @Override
    public boolean isPass(Object...args) throws UnrulyException {
        try {
            return methodExecutor.execute(target, conditionDefinition.getMethodDefinition(), args);
        } catch (UnrulyException e) {
            throw e;
        } catch (Exception e) {
            UnrulyException ex = new UnrulyException("Error trying to execute rule condition method ["
                    + getConditionDefinition().getMethodDefinition().getMethod()
                    + "] Args [" + Arrays.toString(args) + "]", e);
            throw ex;
        }
    }

    @Override
    public ConditionDefinition getConditionDefinition() {
        return conditionDefinition;
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
