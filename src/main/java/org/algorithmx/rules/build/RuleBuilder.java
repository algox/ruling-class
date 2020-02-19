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

import org.algorithmx.rules.core.Condition;
import org.algorithmx.rules.core.ObjectFactory;

public class RuleBuilder {

    private static ObjectFactory objectFactory = ObjectFactory.create();

    public static ClassBasedRuleBuilder withClass(Class<?> ruleClass) {
        return new ClassBasedRuleBuilder(ruleClass, getObjectFactory());
    }

    public static ClassBasedRuleBuilder withClassAndFactory(Class<?> ruleClass, ObjectFactory objectFactory) {
        return new ClassBasedRuleBuilder(ruleClass, objectFactory);
    }

    public static LambdaBasedRuleBuilder withCondition(Condition condition) {
        return new LambdaBasedRuleBuilder(condition);
    }

    public static ObjectFactory getObjectFactory() {
        return objectFactory;
    }

    public static void setObjectFactory(ObjectFactory objectFactory) {
        RuleBuilder.objectFactory = objectFactory;
    }
}
