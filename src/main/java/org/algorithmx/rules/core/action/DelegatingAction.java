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

import org.algorithmx.rules.error.UnrulyException;
import org.algorithmx.rules.model.ActionDefinition;
import org.algorithmx.rules.spring.util.Assert;

/**
 * As the name suggests this Action delegates most of the functionality to its target Action. This class gives
 * the creator the ability to override the ActionDefinition. This is useful for Functional Actions where you
 * cannot override the definition.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class DelegatingAction implements Action {

    private final Action targetAction;
    private final ActionDefinition definition;

    public DelegatingAction(Action targetAction, ActionDefinition definition) {
        super();
        Assert.notNull(targetAction, "targetCondition cannot be null.");
        Assert.notNull(definition, "definition cannot be null.");
        this.targetAction = targetAction;
        this.definition = definition;
    }

    @Override
    public void execute(Object...params) throws UnrulyException {
        targetAction.execute(params);
    }

    @Override
    public ActionDefinition getActionDefinition() {
        return definition;
    }

    @Override
    public Object getTarget() {
        return targetAction.getTarget();
    }
}
