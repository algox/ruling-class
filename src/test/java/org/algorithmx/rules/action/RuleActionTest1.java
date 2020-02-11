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
package org.algorithmx.rules.action;

import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.Actions;
import org.algorithmx.rules.core.ConditionConsumer;
import org.algorithmx.rules.core.Rule;
import org.algorithmx.rules.core.RuleFactory;
import org.algorithmx.rules.util.RuleUtils;
import org.junit.Test;

import static org.algorithmx.rules.core.Conditions.cond2;

public class RuleActionTest1 {

    public RuleActionTest1() {
        super();
    }

    @Test
    public void test1() {
        Bindings bindings = Bindings.defaultBindings();
        bindings.bind("x", String.class, "value");
        bindings.bind("y", Integer.class, 17);
        bindings.bind("z", Integer.class, 200);

        RuleFactory ruleFactory = RuleFactory.defaultFactory();
        Rule ruleWithAction = ruleFactory.rule()
                .given(cond2((String x, Integer y) -> y > 10))
                .then(Actions.act1((Integer z) -> System.err.println("YASS! [" + z + "]")))
                .then(Actions.act1((String x) -> System.err.println("MAN! [" + x + "]")))
                .build();

        RuleUtils.load((ConditionConsumer.ConditionConsumer3<Integer, String, Integer>) (a, b, c) -> a > 10, "test", "");
    }
}
