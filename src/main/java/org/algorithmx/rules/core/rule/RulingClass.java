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
package org.algorithmx.rules.core.rule;

import org.algorithmx.rules.core.Identifiable;
import org.algorithmx.rules.core.action.Action;
import org.algorithmx.rules.core.condition.Condition;
import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.model.RuleDefinition;
import org.algorithmx.rules.spring.util.Assert;

import java.util.Collections;
import java.util.List;

/**
 * Default Rule Implementation (implements Identifiable).
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class RulingClass implements Rule, Identifiable {

    private final RuleDefinition ruleDefinition;
    private final Object target;
    private final Condition condition;
    private final List<Action> actions;
    private final Action otherwiseAction;

    /**
     * Rule defined with all the given properties.
     *
     * @param ruleDefinition meta information.
     * @param target target Rule class.
     * @param condition given condition.
     * @param thenActions all the Then actions.
     * @param otherwiseAction the Otherwise action (optional);
     */
    public RulingClass(RuleDefinition ruleDefinition, Object target, Condition condition,
                       List<Action> thenActions, Action otherwiseAction) {
        super();
        Assert.notNull(ruleDefinition, "ruleDefinition cannot be null");
        Assert.notNull(condition, "condition cannot be null");
        this.ruleDefinition = ruleDefinition;
        this.target = target;
        this.condition = condition;

        // Then actions (optional)
        if (thenActions != null) {
            Collections.sort(thenActions);
            this.actions = Collections.unmodifiableList(thenActions);
        } else {
            this.actions = Collections.emptyList();
        }

        // Otherwise action (Optional)
        this.otherwiseAction = otherwiseAction;
    }

    @Override
    public void run(RuleContext ctx) throws UnrulyException {

        // Check to make sure we are still running
        if (!ctx.getState().isRunning()) {
            throw new UnrulyException("Error trying to run Rule [" + getName()
                    + "]. Invalid execution state [" + ctx.getState() + "]. RuleContext is not a running state. " +
                    "Try running withe a new RuleContext.");
        }

        boolean result = getCondition().isPass(ctx);

        // The Condition passed
        if (result) {
            // Execute any associated Actions.
            for (Action action : getActions()) {
                action.execute(ctx);
            }
        } else if (getOtherwiseAction() != null) {
            // Condition failed
            getOtherwiseAction().execute(ctx);
        }
    }

    @Override
    public Condition getCondition() {
        return condition;
    }

    @Override
    public RuleDefinition getRuleDefinition() {
        return ruleDefinition;
    }

    @Override
    public String getName() {
        return ruleDefinition.getName();
    }

    @Override
    public String getDescription() {
        return ruleDefinition.getDescription();
    }

    @Override
    public Object getTarget() {
        return target;
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
    public final boolean isIdentifiable() {
        return true;
    }

    @Override
    public String toString() {
        return "RulingClass{" +
                "ruleDefinition=" + ruleDefinition +
                '}';
    }
}
