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
        ActionDefinition[] actions = ActionDefinition.load(rulingClass);

        if (actions != null) {
            // Sort the Action so that we have a predictable order to the execution of the Actions.
            Arrays.sort(actions);
            Arrays.stream(actions).forEach(action -> result.then(ActionUtils.create(action, result.getTarget())));
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
     * Creates a new Rule based on the given Condition.
     *
     * @param condition When Condition (usually implemented as a Lambda).
     * @return a new Rule Instance.
     */
    default Rule rule(Condition.Condition0 condition) {
        return rule((Condition) condition);
    }

    /**
     * Creates a new Rule based on the given Condition with one argument.
     *
     * @param condition When Condition (usually implemented as a Lambda).
     * @param <A> generic type of the first parameter.
     * @return a new Rule Instance.
     */
    default <A> Rule rule(Condition.Condition1<A> condition) {
        return rule((Condition) condition);
    }
    /**
     * Creates a new Rule based on the given Condition with two arguments.
     *
     * @param condition When Condition (usually implemented as a Lambda).
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @return a new Rule Instance.
     */
    default <A, B> Rule rule(Condition.Condition2<A, B> condition) {
        return rule((Condition) condition);
    }

    /**
     * Creates a new Rule based on the given Condition with three arguments.
     *
     * @param condition When Condition (usually implemented as a Lambda).
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @return a new Rule Instance.
     */
    default <A, B, C> Rule rule(Condition.Condition3<A, B, C> condition) {
        return rule((Condition) condition);
    }

    /**
     * Creates a new Rule based on the given Condition with four arguments.
     *
     * @param condition When Condition (usually implemented as a Lambda).
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @return a new Rule Instance.
     */
    default <A, B, C, D> Rule rule(Condition.Condition4<A, B, C, D> condition) {
        return rule((Condition) condition);
    }

    /**
     * Creates a new Rule based on the given Condition with five arguments.
     *
     * @param condition When Condition (usually implemented as a Lambda).
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @return a new Rule Instance.
     */
    default <A, B, C, D, E> Rule rule(Condition.Condition5<A, B, C, D, E> condition) {
        return rule((Condition) condition);
    }

    /**
     * Creates a new Rule based on the given Condition with six arguments.
     *
     * @param condition When Condition (usually implemented as a Lambda).
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @return a new Rule Instance.
     */
    default <A, B, C, D, E, F> Rule rule(Condition.Condition6<A, B, C, D, E, F> condition) {
        return rule((Condition) condition);
    }

    /**
     * Creates a new Rule based on the given Condition with seven arguments.
     *
     * @param condition When Condition (usually implemented as a Lambda).
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @param <G> generic type of the seventh parameter.
     * @return a new Rule Instance.
     */
    default <A, B, C, D, E, F, G> Rule rule(Condition.Condition7<A, B, C, D, E, F, G> condition) {
        return rule((Condition) condition);
    }

    /**
     * Creates a new Rule based on the given Condition with eight arguments.
     *
     * @param condition When Condition (usually implemented as a Lambda).
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @param <G> generic type of the seventh parameter.
     * @param <H> generic type of the eighth parameter.
     * @return a new Rule Instance.
     */
    default <A, B, C, D, E, F, G, H> Rule rule(Condition.Condition8<A, B, C, D, E, F, G, H> condition) {
        return rule((Condition) condition);
    }

    /**
     * Creates a new Rule based on the given Condition with nine arguments.
     *
     * @param condition When Condition (usually implemented as a Lambda).
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @param <G> generic type of the seventh parameter.
     * @param <H> generic type of the eighth parameter.
     * @param <I> generic type of the ninth parameter.
     * @return a new Rule Instance.
     */
    default <A, B, C, D, E, F, G, H, I> Rule rule(Condition.Condition9<A, B, C, D, E, F, G, H, I> condition) {
        return rule((Condition) condition);
    }

    /**
     * Creates a new Rule based on the given Condition with ten arguments.
     *
     * @param condition When Condition (usually implemented as a Lambda).
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @param <G> generic type of the seventh parameter.
     * @param <H> generic type of the eighth parameter.
     * @param <I> generic type of the ninth parameter.
     * @param <J> generic type of the ninth parameter.
     * @return a new Rule Instance.
     */
    default <A, B, C, D, E, F, G, H, I, J> Rule rule(Condition.Condition10<A, B, C, D, E, F, G, H, I, J> condition) {
        return rule((Condition) condition);
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
