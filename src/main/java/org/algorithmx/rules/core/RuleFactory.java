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
        return new DefaultRuleSet(name, null);
    }

    /**
     * Creates a new RuleSet with the given name, description and this RuleFactory.
     *
     * @param name RuleSet name.
     * @param description description of the RuleSet.
     * @return a new RuleSet.
     */
    default RuleSet rules(String name, String description) {
        return new DefaultRuleSet(name, description);
    }
}
