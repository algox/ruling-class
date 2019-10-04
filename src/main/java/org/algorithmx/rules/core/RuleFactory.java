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
package org.algorithmx.rules.core;

import org.algorithmx.rules.core.impl.DefaultObjectFactory;
import org.algorithmx.rules.core.impl.DefaultRuleFactory;
import org.algorithmx.rules.core.impl.DefaultRuleSet;
import org.algorithmx.rules.error.UnrulyException;
import org.algorithmx.rules.model.ActionDefinition;
import org.algorithmx.rules.model.RuleDefinition;
import org.algorithmx.rules.util.ActionUtils;

import java.util.Arrays;

import static org.algorithmx.rules.util.RuleUtils.load;

/**
 * Factory to produce Rules.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public interface RuleFactory {

    /**
     * Creates a instance of the default implementation of the RuleFactory.
     *
     * @return new RuleFactory instance.
     */
    static RuleFactory defaultFactory() {
        return new DefaultRuleFactory(new DefaultObjectFactory());
    }

    /**
     * Creates a new RuleSet with the given name and this RuleFactory.
     *
     * @param name RuleSet name.
     * @return a new RuleSet.
     */
    default RuleSet rules(String name) {
        return new DefaultRuleSet(name, null, this);
    }

    /**
     * Creates a new RuleSet with the given name, description and this RuleFactory.
     *
     * @param name RuleSet name.
     * @param description description of the RuleSet.
     * @return a new RuleSet.
     */
    default RuleSet rules(String name, String description) {
        return new DefaultRuleSet(name, description, this);
    }

    /**
     * Creates a Rule from RuleDefinition.
     *
     * @param ruleDefinition rule meta information.
     * @return a new Rule Instance.
     */
    Rule rule(RuleDefinition ruleDefinition);

    /**
     * Creates a Rule from the given implementation class. The Actions will be ordered by the number of arguments they have.
     *
     * @param rulingClass Rule Implementation class/
     * @return a new Rule Instance.
     */
    default Rule rule(Class<?> rulingClass) {
        Rule result = rule(RuleDefinition.load(rulingClass));
        ActionDefinition[] thenActions = ActionDefinition.loadThenActions(rulingClass);

        if (thenActions != null) {
            // Sort the Action so that we have a predictable order to the execution of the Actions.
            Arrays.sort(thenActions);
            Arrays.stream(thenActions).forEach(action -> result.then(ActionUtils.create(action, result.getTarget())));
        }

        ActionDefinition[] elseActions = ActionDefinition.loadElseActions(rulingClass);

        if (elseActions != null && elseActions.length > 1) {
            StringBuilder names = new StringBuilder();
            Arrays.stream(elseActions).forEach(action -> names.append(action.getActionName() + " "));
                throw new UnrulyException("Multiple otherwise conditions found on Rule [" + rulingClass.getName()
                        + "]. A Rule can only have one otherwise action. Found [" + names + "]");
        } else if (elseActions != null && elseActions.length == 1) {
            result.otherwise(ActionUtils.create(elseActions[0], result.getTarget()));
        }

        return result;
    }

    /**
     * Creates a new Rule based on the given Condition.
     *
     * @param condition When Condition (usually implemented as a Lambda).
     * @return a new Rule Instance.
     */
    default Rule rule(Condition condition) {
        return rule(load(condition, null, null));
    }

    /**
     * Creates a Identifiable Rule given a name and condition.
     *
     * @param name Rule name.
     * @param condition when Condition.
     * @return a new Identifiable Rule Instance.
     */
    default Rule rule(String name, Condition condition) {
        return rule(load(condition, name, null));
    }

    /**
     * Creates a Identifiable Rule given a name and condition.
     *
     * @param name Rule name.
     * @param condition when Condition.
     * @param description Rule description.
     * @return a new Identifiable Rule Instance.
     */
    default Rule rule(String name, Condition condition, String description) {
        return rule(load(condition, name, description));
    }
}
