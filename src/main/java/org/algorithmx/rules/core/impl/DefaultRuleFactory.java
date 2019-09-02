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
package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.core.ObjectFactory;
import org.algorithmx.rules.core.Rule;
import org.algorithmx.rules.core.RuleFactory;
import org.algorithmx.rules.model.RuleDefinition;
import org.algorithmx.rules.spring.util.Assert;

public final class DefaultRuleFactory implements RuleFactory {

    private final ObjectFactory objectFactory;

    public DefaultRuleFactory(ObjectFactory objectFactory) {
        super();
        Assert.notNull(objectFactory, "objectFactory cannot be null.");
        this.objectFactory = objectFactory;
    }

    public Rule rule(RuleDefinition ruleDefinition) {
        return new DefaultRule(ruleDefinition,
                ruleDefinition.isStatic()
                        ? null
                        : objectFactory.create(ruleDefinition.getRulingClass()));
    }
}
