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
import org.algorithmx.rules.build.ActionBuilder;
import org.algorithmx.rules.build.ConditionBuilder;
import org.algorithmx.rules.build.RuleBuilder;
import org.algorithmx.rules.core.Rule;
import org.junit.Test;

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

        Rule ruleWithAction = RuleBuilder
                .withCondition(ConditionBuilder.with2Args((String x, Integer y) -> y > 10).build())
                .then(ActionBuilder.with1Arg((Integer z) -> System.err.println("YASS! [" + z + "]")).build())
                .then(ActionBuilder.with1Arg((String x) -> System.err.println("MAN! [" + x + "]")).build())
                .build();

        Rule rule2 = RuleBuilder
                .withCondition(
                        ConditionBuilder.with3Args((Integer a, String b, Integer c) -> a > 10).build())
                .name("testrule")
                .description("description")
                .build();
    }
}
