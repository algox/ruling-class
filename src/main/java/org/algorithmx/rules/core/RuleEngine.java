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

import org.algorithmx.rules.bind.BindingDeclaration;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.impl.DefaultRuleEngine;

/**
 *
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public interface RuleEngine {

    static RuleEngine defaultRuleEngine() {
        return new DefaultRuleEngine();
    }

    default String getName() {
        return getClass().getSimpleName();
    }

    void run(Rule rule, RuleExecutionContext ctx) throws UnrulyException;

    void run(RuleSet rule, RuleExecutionContext ctx) throws UnrulyException;

    default RuleExecutionContext run(Rule rule, BindingDeclaration...bindings) {
        RuleExecutionContext result = RuleExecutionContext.create(Bindings.defaultBindings().bind(bindings));
        run(rule, result);
        return result;
    }

    default RuleExecutionContext run(Rule rule, Bindings bindings) throws UnrulyException {
        RuleExecutionContext result = RuleExecutionContext.create(bindings);
        run(rule, result);
        return result;
    }

    default RuleExecutionContext run(RuleSet rule, BindingDeclaration...bindings) {
        RuleExecutionContext result = RuleExecutionContext.create(Bindings.defaultBindings().bind(bindings));
        run(rule, result);
        return result;
    }

    default RuleExecutionContext run(RuleSet rule, Bindings bindings) throws UnrulyException {
        RuleExecutionContext result = RuleExecutionContext.create(bindings);
        run(rule, result);
        return result;
    }
}
