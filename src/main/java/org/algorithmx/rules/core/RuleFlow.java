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

import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.error.UnrulyException;

/**
 * Rule Flow : Work in progress
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@FunctionalInterface
public interface RuleFlow<T> {

    T run();

    default Bindings bindings() {
        throw new UnrulyException("RuleFlow must be executed in a managed environment.");
    }

    default boolean rule(String name) {
        throw new UnrulyException("RuleFlow must be executed in a managed environment.");
    }

    default RuleSet ruleSet(String name) {
        throw new UnrulyException("RuleFlow must be executed in a managed environment.");
    }

    default boolean run(Rule rule) {
        throw new UnrulyException("RuleFlow must be executed in a managed environment.");
    }

    default boolean all(String ruleSet) {
        throw new UnrulyException("RuleFlow must be executed in a managed environment.");
    }

    default boolean all(final Rule[] allRules) {
        throw new UnrulyException("RuleFlow must be executed in a managed environment.");
    }

    default boolean any(String ruleSet) {
        throw new UnrulyException("RuleFlow must be executed in a managed environment.");
    }

    default boolean any(final Rule[] allRules) {
        throw new UnrulyException("RuleFlow must be executed in a managed environment.");
    }

    default boolean none(String ruleSet) {
        throw new UnrulyException("RuleFlow must be executed in a managed environment.");
    }

    default boolean none(final Rule[] allRules) {
        throw new UnrulyException("RuleFlow must be executed in a managed environment.");
    }
}
