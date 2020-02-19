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
import org.algorithmx.rules.core.RuleContext;
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

    public void run(RuleContext ctx) throws UnrulyException {

        // Check to make sure we are still running
        if (!ctx.getState().isRunning()) {
            throw new UnrulyException("Cannot run Rule [" + getName() + "] Invalid RuleContext state [" + ctx.getState() + "]");
        }

        // Check to see if we need to stop the execution
        if (ctx.getStopWhen() != null && ctx.getStopWhen().isPass(ctx)) {
            ctx.stop();
            return;
        }

        boolean result;

        try {
            // Execute the Rule
            result = getCondition().isPass(ctx);
        } catch (UnrulyException e) {
            throw e;
        } catch (Exception e) {
            UnrulyException ex = new UnrulyException("Error trying to execute rule condition [" + getName() + "]", e);
            throw ex;
        }

        // The Condition passed
        if (result) {
            // Execute any associated Actions.
            for (Action action : getActions()) {
                runAction(ctx, action);
            }
        } else if (getOtherwiseAction() != null) {
            // Condition failed
            runAction(ctx, getOtherwiseAction());
        }
    }

    /**
     * Executes the Action associated with the Condition.
     *
     * @param ctx Rule Context.
     * @param action desired action to execute.
     */
    protected void runAction(RuleContext ctx, Action action) {

        try {
            // Execute the Action
            action.execute(ctx);
        } catch (UnrulyException e) {
            throw e;
        } catch (Exception e) {
            UnrulyException ex = new UnrulyException("Error trying to execute rule action ["
                    + action.getActionDefinition().getActionName() + "]", e);
            throw ex;
        }
    }

    @Override
    public Action[] getActions() {
        return actions.toArray(new Action[actions.size()]);
    }

    @Override
    public Action getOtherwiseAction() {
        return otherwiseAction;
    }

    /**
     * Associates a new Action to the Rule.
     *
     * @param action desired action.
     * @return this so other Actions can be associated fluently.
     */
    protected Rule then(Action action) {
        actions.add(action);
        return this;
    }

    /**
     * Associates a new Action to the Rule. The ActionConsumer Lambda is converted into an Action.
     *
     * @param action desired action.
     * @return this so other Actions can be associated fluently.
     */
    protected Rule then(ActionConsumer action) {
        return then(action, null);
    }

    /**
     * Associates a new Action to the Rule. The ActionConsumer Lambda is converted into an Action.
     *
     * @param action desired action.
     * @param description description of the Action.
     * @return this so other Actions can be associated fluently.
     */
    protected Rule then(ActionConsumer action, String description) {
        actions.add(ActionUtils.create(action, description, getTarget()));
        return this;
    }

    /**
     * Associates a OtherwiseAction to the Rule (if one isn't present already).
     *
     * @param action desired action.
     * @throws UnrulyException if an otherwise action is already associated to this Rule.
     */
    protected void otherwise(Action action) {
        if (otherwiseAction != null) {
            throw new UnrulyException("This Rule already has an otherwise action associated to it.");
        }
        this.otherwiseAction = action;
    }

    /**
     * Associates a new Action to the Rule (if one isn't present already). The ActionConsumer Lambda is converted into an Action.
     *
     * @param action desired action.
     */
    protected void otherwise(ActionConsumer action) {
        otherwise(ActionUtils.create(action, null, getTarget()));
    }

    /**
     * Associates a new Action to the Rule (if one isn't present already). The ActionConsumer Lambda is converted into an Action.
     *
     * @param action desired action.
     * @param description description of the Action.
     */
    protected void otherwise(ActionConsumer action, String description) {
        otherwise(ActionUtils.create(action, description, getTarget()));
    }

    /**
     * Loads all the declared actions from the given class and attaches them to this Rule.
     *
     * @param actionClass class with all the actions.
     */
    protected void loadActions(Class<?> actionClass) {
        ActionDefinition[] thenActions = ActionDefinition.loadThenActions(actionClass);

        if (thenActions != null) {
            // Sort the Action so that we have a predictable order to the execution of the Actions.
            Arrays.sort(thenActions);
            Arrays.stream(thenActions).forEach(action -> then(ActionUtils.create(action, this.getTarget())));
        }

        ActionDefinition elseAction = ActionDefinition.loadElseActions(actionClass);

        if (elseAction != null) {
            otherwise(ActionUtils.create(elseAction, this.getTarget()));
        }
    }
}