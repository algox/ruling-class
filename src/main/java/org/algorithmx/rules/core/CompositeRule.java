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

import org.algorithmx.rules.core.impl.DefaultCompositeRule;

/**
 *
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Deprecated
public interface CompositeRule extends Rule {

    static CompositeRule AND(final Rule...allRules) {
        return new DefaultCompositeRule(allRules, (rules, ctx) -> {
            for (Rule rule : rules) {
                if (!rule.isPass(ctx)) return false;
            }
            return true;
        });
    }

    static CompositeRule OR(final Rule...allRules) {
        return new DefaultCompositeRule(allRules, (rules, ctx) -> {
            for (Rule rule : rules) {
                if (rule.isPass(ctx)) return true;
            }
            return false;
        });
    }

    static CompositeRule NONE(final Rule...allRules) {
        return new DefaultCompositeRule(allRules, (rules, ctx) -> {
            for (Rule rule : rules) {
                if (rule.isPass(ctx)) return false;
            }
            return true;
        });
    }

    Rule[] getRules();
}
