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
package org.algorithmx.rules.core;

import org.algorithmx.rules.annotation.Action;
import org.algorithmx.rules.annotation.Nullable;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.bind.Bindings;

import java.util.Date;
import java.util.List;

@Rule(name = "Test Rule", description = "Test Description 1")
public class TestRule5 {

    public TestRule5() {
        super();
    }

    public boolean when(int id, @Nullable Date closingDate, List<String> values) {
        return id > 100;
    }

    @Action(order = 0)
    public void calculateId(Bindings bindings) {
        int x = bindings.get("result");
        bindings.set("result", ++x);
    }

    @Action(order = 1)
    public void makePayment(Bindings bindings) {
        int x = bindings.get("result");
        bindings.set("result", ++x);
    }
}
