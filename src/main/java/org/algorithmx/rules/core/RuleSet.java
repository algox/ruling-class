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
}

