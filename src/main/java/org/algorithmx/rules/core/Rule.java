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

import org.algorithmx.rules.model.RuleDefinition;

import java.util.function.Predicate;

/**
 * Rule class encapsulates all the properties/methods of a Rule within the framework. A Rule consists of two parts
 * a Condition and a list of associated Actions. You can think of it as a If (Condition).. then Action(s).
 *
 * The Condition is stateless and can be executed many times without any side effects.
 * The Action(s) can be stateful.
 *
 * A Rule Condition can be tested via :
 * the isPass(..), isFail(...) and test() methods. Those methods must be given the arguments the rule requires. These
 * methods are solely there for the purpose of executing them manually to test the Rule. You should use the RuleEngine
 * to automate the process of checking the Condition and running the associated Actions.
 *
 * @author Max Arulananthan
 * @since 1.0
 * @see RuleEngine
 */
public interface Rule extends Predicate<Object[]> {

    /**
     * Executes thr Rule Condition given all the arguments it needs.
     *
     * @param params Rule Condition parameters in necessary order.
     * @return true if the Rule Condition is true; false otherwise.
     * @throws UnrulyException thrown if there are any runtime errors during the execution.
     */
    boolean isPass(Object...params) throws UnrulyException;

    /**
     * Executes thr Rule Condition given all the arguments it needs.
     *
     * @param params Rule Condition parameters in necessary order.
     * @return true if the Rule Condition is false; false otherwise.
     * @throws UnrulyException thrown if there are any runtime errors during the execution.
     */
    default boolean isFail(Object...params) throws UnrulyException {
        return !isPass(params);
    }

    /**
     * Executes thr Rule Condition given all the arguments it needs.
     *
     * @param params Rule Condition parameters in necessary order.
     * @return true if the Rule Condition is true; false otherwise.
     * @throws UnrulyException thrown if there are any runtime errors during the execution.
     */
    default boolean test(Object...params) throws UnrulyException {
        return isPass(params);
    }

    /**
     * Determines if this Rule can be Identified with a name.
     *
     * @return true if the Rule implements Identifiable.
     */
    default boolean isIdentifiable() {
        return this instanceof Identifiable;
    }

    /**
     * The actual Rule implementation instance.
     *
     * @return Rule instance.
     */
    Object getTarget();

    /**
     * Meta information about the Rule.
     *
     * @return Rule meta information.
     */
    RuleDefinition getRuleDefinition();

    /**
     * Any associated Actions.
     *
     * @return associated actions.
     */
    Action[] getActions();

    /**
     * Associates a new Action to the Rule.
     *
     * @param action desired action.
     * @return this so other Actions can be associated fluently.
     */
    Rule then(Action action);

    /**
     * Associates a new Action to the Rule. The Then Lambda is converted into an Action.
     *
     * @param action desired action.
     * @return this so other Actions can be associated fluently.
     */
    Rule then(Then action);

    /**
     * Associates a new Action to the Rule. The Then Lambda is converted into an Action.
     *
     * @param action desired action.
     * @param description description of the Action.
     * @return this so other Actions can be associated fluently.
     */
    Rule then(Then action, String description);

    /**
     * Associates a new Action to the Rule. The Then Lambda (with no parameters) is converted into an Action.
     *
     * @param action desired action.
     * @return this so other Actions can be associated fluently.
     */
    Rule then(Then.Then0 action);

    /**
     * Associates a new Action to the Rule. The Then Lambda (with no parameters) is converted into an Action.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @return this so other Actions can be associated fluently.
     */
    <A> Rule then(Then.Then1<A> action);

    /**
     * Associates a new Action to the Rule. The Then Lambda (with two parameters) is converted into an Action.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @return this so other Actions can be associated fluently.
     */
    <A, B> Rule then(Then.Then2<A, B> action);

    /**
     * Associates a new Action to the Rule. The Then Lambda (with three parameters) is converted into an Action.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @return this so other Actions can be associated fluently.
     */
    <A, B, C> Rule then(Then.Then3<A, B, C> action);

    /**
     * Associates a new Action to the Rule. The Then Lambda (with four parameters) is converted into an Action.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @return this so other Actions can be associated fluently.
     */
    <A, B, C, D> Rule then(Then.Then4<A, B, C, D> action);

    /**
     * Associates a new Action to the Rule. The Then Lambda (with five parameters) is converted into an Action.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @return this so other Actions can be associated fluently.
     */
    <A, B, C, D, E> Rule then(Then.Then5<A, B, C, D, E> action);

    /**
     * Associates a new Action to the Rule. The Then Lambda (with six parameters) is converted into an Action.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @return this so other Actions can be associated fluently.
     */
    <A, B, C, D, E, F> Rule then(Then.Then6<A, B, C, D, E, F> action);

    /**
     * Associates a new Action to the Rule. The Then Lambda (with seven parameters) is converted into an Action.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @param <G> generic type of the seventh parameter.
     * @return this so other Actions can be associated fluently.
     */
    <A, B, C, D, E, F, G> Rule then(Then.Then7<A, B, C, D, E, F, G> action);

    /**
     * Associates a new Action to the Rule. The Then Lambda (with eight parameters) is converted into an Action.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @param <G> generic type of the seventh parameter.
     * @param <H> generic type of the eighth parameter.
     * @return this so other Actions can be associated fluently.
     */
    <A, B, C, D, E, F, G, H> Rule then(Then.Then8<A, B, C, D, E, F, G, H> action);

    /**
     * Associates a new Action to the Rule. The Then Lambda (with nine parameters) is converted into an Action.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @param <G> generic type of the seventh parameter.
     * @param <H> generic type of the eighth parameter.
     * @param <I> generic type of the ninth parameter.
     * @return this so other Actions can be associated fluently.
     */
    <A, B, C, D, E, F, G, H, I> Rule then(Then.Then9<A, B, C, D, E, F, G, H, I> action);

    /**
     * Associates a new Action to the Rule. The Then Lambda (with ten parameters) is converted into an Action.
     *
     * @param action desired action.
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
     * @return this so other Actions can be associated fluently.
     */
    <A, B, C, D, E, F, G, H, I, J> Rule then(Then.Then10<A, B, C, D, E, F, G, H, I, J> action);

}
