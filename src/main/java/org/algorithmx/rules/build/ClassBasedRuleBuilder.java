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
package org.algorithmx.rules.build;

import org.algorithmx.rules.core.ObjectFactory;
import org.algorithmx.rules.core.Rule;
import org.algorithmx.rules.core.impl.RulingClass;
import org.algorithmx.rules.model.RuleDefinition;
import org.algorithmx.rules.spring.util.Assert;

public class ClassBasedRuleBuilder {

    private final Class<?> ruleClass;
    private final RuleDefinition definition;
    private ObjectFactory objectFactory = ObjectFactory.create();

    private ClassBasedRuleBuilder(Class<?> ruleClass) {
        super();
        Assert.notNull(ruleClass, "ruleClass cannot be null.");
        this.ruleClass = ruleClass;
        this.definition = RuleDefinition.load(ruleClass);
    }

    public static ClassBasedRuleBuilder withClass(Class<?> ruleClass) {
        return new ClassBasedRuleBuilder(ruleClass);
    }

    public Class<?> getRuleClass() {
        return ruleClass;
    }

    public RuleDefinition getDefinition() {
        return definition;
    }

    public ClassBasedRuleBuilder name(String name) {
        definition.setName(name);
        return this;
    }

    public ClassBasedRuleBuilder description(String description) {
        definition.setDescription(description);
        return this;
    }

    public ClassBasedRuleBuilder objectFactory(ObjectFactory objectFactory) {
        Assert.notNull(objectFactory, "objectFactory cannot be null.");
        this.objectFactory = objectFactory;
        return this;
    }

    public Rule build() {
        return new RulingClass(definition, objectFactory.create(definition.getRulingClass()));
    }
}
