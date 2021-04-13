/*
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 1999-2021, Algorithmx Inc.
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
package org.algorithmx.rulii.core.test.context;

import org.algorithmx.rulii.bind.Bindings;
import org.algorithmx.rulii.bind.InvalidBindingException;
import org.algorithmx.rulii.bind.ReservedBindings;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.core.context.RuleContextBuilder;
import org.junit.Test;

public class RuleContextTest {

    public RuleContextTest() {
        super();
    }

    @Test(expected = InvalidBindingException.class)
    public void test1() {
        RuleContext context = RuleContextBuilder.build(Bindings.create());
        context.getBindings().bind(ReservedBindings.RULE_CONTEXT.getName(), 25);
    }
}
