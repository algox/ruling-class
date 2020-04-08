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
package org.algorithmx.rules.bind.match;

import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.util.TypeReference;
import org.algorithmx.rules.spring.util.Assert;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Composite Strategy class that aggregates the matches by running the given delegator Strategies. The exit point can
 * be setup during the creation of the CompositeBindingMatchingStrategy.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class CompositeBindingMatchingStrategy implements BindingMatchingStrategy {

    private final boolean stopWhenMatched;
    private final BindingMatchingStrategy[] strategies;

    /**
     * Creates CompositeBindingMatchingStrategy with the given delegating sub BindingMatchingStrategies.
     *
     * @param stopWhenMatched determines if this CompositeBindingMatchingStrategy will stop once a match(es) are found.
     * @param strategies delegating sub BindingMatchingStrategies
     */
    public CompositeBindingMatchingStrategy(boolean stopWhenMatched, BindingMatchingStrategy ... strategies) {
        super();
        Assert.notNull(strategies, "strategies cannot be null");
        Assert.isTrue(strategies.length > 1, "CompositeBindingMatchingStrategy takes in at least 2 BindingMatchingStrategies");

        this.stopWhenMatched = stopWhenMatched;
        this.strategies = strategies;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Set<Binding<T>> match(Bindings bindings, String name, TypeReference<T> type) {
        Set<Binding<T>> result = new HashSet<>();

        for (BindingMatchingStrategy strategy : strategies) {
            // Add all the matches
            result.addAll(strategy.match(bindings, name, type));
            // Check to see if we should stop
            if (stopWhenMatched && result.size() > 0) break;
        }

        return Collections.unmodifiableSet(result);
    }
}
