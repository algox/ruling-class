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

/**
 * RuleSet is a logical grouping of Rules.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public interface RuleSet extends Identifiable, Iterable<Rule> {

    /**
     * Ruleset name.
     *
     * @return name.
     */
    String getName();

    /**
     * RuleSet description.
     *
     * @return description.
     */
    String getDescription();

    /**
     * Returns the RuleFactory that is associated with this RulSet.
     *
     * @return associated RuleFactory.
     */
    RuleFactory getRuleFactory();

    /**
     * Retrieves a Rule with the given name in this RuleSet.
     *
     * @param ruleName desired rule name.
     * @return Rule if found; null otherwise.
     */
    Rule getRule(String ruleName);

    /**
     * Size of this RuleSet (ie. number of Rules in this RuleSet)
     *
     * @return number of Rules in this RuleSet.
     */
    int size();

    /**
     * Returns all the Rules in this RuleSet.
     *
     * @return rules.
     */
    Rule[] getRules();

    /**
     * Adds the given Rule to this RuleSet.
     *
     * @param rule Rule to be added.
     * @return this RuleSet (for fluency).
     */
    RuleSet add(Rule rule);

    /**
     * Adds a new Rule to this RuleSet.
     *
     * @param ruleClass Rule class to be added.
     * @return this RuleSet (for fluency).
     */
    RuleSet add(Class<?> ruleClass);

    /**
     * Adds the given Rule to this RuleSet with the desired name.
     *
     * @param name desired name.
     * @param rule Rule to be added.
     * @return this RuleSet (for fluency).
     */
    RuleSet add(String name, Rule rule);

    /**
     * Creates a new Rule based on the given Condition and adds it to this RuleSet.
     *
     * @param condition When Condition (usually implemented as a Lambda).
     * @return a new Rule Instance.
     */
    default Rule add(Condition.Condition0 condition) {
        return getRuleFactory().rule(condition);
    }

    /**
     * Creates a new Rule based on the given Condition with one argument and adds it to this RuleSet.
     *
     * @param condition When Condition (usually implemented as a Lambda).
     * @param <A> generic type of the first parameter.
     * @return a new Rule Instance.
     */
    default <A> Rule add(Condition.Condition1<A> condition) {
        return getRuleFactory().rule(condition);
    }
    /**
     * Creates a new Rule based on the given Condition with two arguments and adds it to this RuleSet.
     *
     * @param condition When Condition (usually implemented as a Lambda).
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @return a new Rule Instance.
     */
    default <A, B> Rule add(Condition.Condition2<A, B> condition) {
        return getRuleFactory().rule(condition);
    }

    /**
     * Creates a new Rule based on the given Condition with three arguments and adds it to this RuleSet.
     *
     * @param condition When Condition (usually implemented as a Lambda).
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @return a new Rule Instance.
     */
    default <A, B, C> Rule add(Condition.Condition3<A, B, C> condition) {
        return getRuleFactory().rule(condition);
    }

    /**
     * Creates a new Rule based on the given Condition with four arguments and adds it to this RuleSet.
     *
     * @param condition When Condition (usually implemented as a Lambda).
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @return a new Rule Instance.
     */
    default <A, B, C, D> Rule add(Condition.Condition4<A, B, C, D> condition) {
        return getRuleFactory().rule(condition);
    }

    /**
     * Creates a new Rule based on the given Condition with five arguments and adds it to this RuleSet.
     *
     * @param condition When Condition (usually implemented as a Lambda).
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @return a new Rule Instance.
     */
    default <A, B, C, D, E> Rule add(Condition.Condition5<A, B, C, D, E> condition) {
        return getRuleFactory().rule(condition);
    }

    /**
     * Creates a new Rule based on the given Condition with six arguments and adds it to this RuleSet.
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
    default <A, B, C, D, E, F> Rule add(Condition.Condition6<A, B, C, D, E, F> condition) {
        return getRuleFactory().rule(condition);
    }

    /**
     * Creates a new Rule based on the given Condition with seven arguments and adds it to this RuleSet.
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
    default <A, B, C, D, E, F, G> Rule add(Condition.Condition7<A, B, C, D, E, F, G> condition) {
        return getRuleFactory().rule(condition);
    }

    /**
     * Creates a new Rule based on the given Condition with eight arguments and adds it to this RuleSet.
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
    default <A, B, C, D, E, F, G, H> Rule add(Condition.Condition8<A, B, C, D, E, F, G, H> condition) {
        return getRuleFactory().rule(condition);
    }

    /**
     * Creates a new Rule based on the given Condition with nine arguments and adds it to this RuleSet.
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
    default <A, B, C, D, E, F, G, H, I> Rule add(Condition.Condition9<A, B, C, D, E, F, G, H, I> condition) {
        return getRuleFactory().rule(condition);
    }

    /**
     * Creates a new Rule based on the given Condition with ten arguments and adds it to this RuleSet.
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
    default <A, B, C, D, E, F, G, H, I, J> Rule add(Condition.Condition10<A, B, C, D, E, F, G, H, I, J> condition) {
        return getRuleFactory().rule(condition);
    }
}

