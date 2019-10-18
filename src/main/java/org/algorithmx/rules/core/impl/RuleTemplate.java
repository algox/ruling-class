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
package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.core.Action;
import org.algorithmx.rules.core.ActionConsumer;
import org.algorithmx.rules.core.Identifiable;
import org.algorithmx.rules.core.Rule;
import org.algorithmx.rules.error.UnrulyException;
import org.algorithmx.rules.model.ActionDefinition;
import org.algorithmx.rules.util.ActionUtils;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Template class for creating Rules.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public abstract class RuleTemplate implements Rule, Identifiable {

    private final LinkedList<Action> actions = new LinkedList();
    private Action otherwiseAction = null;

    protected RuleTemplate() {
        super();
    }

    @Override
    public Action[] getActions() {
        return actions.toArray(new Action[actions.size()]);
    }

    @Override
    public Action getOtherwiseAction() {
        return otherwiseAction;
    }

    @Override
    public Rule then(Action action) {
        actions.add(action);
        return this;
    }

    @Override
    public Rule then(ActionConsumer action) {
        return then(action, null);
    }

    @Override
    public Rule then(ActionConsumer action, String description) {
        actions.add(ActionUtils.create(action, description, getTarget()));
        return this;
    }

    @Override
    public void otherwise(Action action) {
        if (otherwiseAction != null) {
            throw new UnrulyException("This Rule already has an otherwise action associated to it.");
        }
        this.otherwiseAction = action;
    }

    @Override
    public void otherwise(ActionConsumer action) {
        otherwise(ActionUtils.create(action, null, getTarget()));
    }

    @Override
    public void otherwise(ActionConsumer action, String description) {
        otherwise(ActionUtils.create(action, description, getTarget()));
    }

    @Override
    public void loadActions(Class<?> actionClass) {
        ActionDefinition[] thenActions = ActionDefinition.loadThenActions(actionClass);

        if (thenActions != null) {
            // Sort the Action so that we have a predictable order to the execution of the Actions.
            Arrays.sort(thenActions);
            Arrays.stream(thenActions).forEach(action -> then(ActionUtils.create(action, this.getTarget())));
        }

        ActionDefinition[] elseActions = ActionDefinition.loadElseActions(actionClass);

        if (elseActions != null && elseActions.length > 1) {
            StringBuilder names = new StringBuilder();
            Arrays.stream(elseActions).forEach(action -> names.append(action.getActionName() + " "));
            throw new UnrulyException("Multiple otherwise conditions found on Rule [" + getName()
                    + "]. A Rule can only have one otherwise action. Found [" + names + "]");
        } else if (elseActions != null && elseActions.length == 1) {
            otherwise(ActionUtils.create(elseActions[0], this.getTarget()));
        }
    }
}
