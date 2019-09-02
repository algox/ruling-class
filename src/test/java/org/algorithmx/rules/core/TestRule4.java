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

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Rule;

import java.util.Date;
import java.util.List;

@Rule(name = "TestRule4")
public class TestRule4 {

    public TestRule4() {
        super();
    }

    public boolean when() {
        return true;
    }

    @Description("calculate")
    public void then(int id, Date birthDate, List<Integer> values) {

    }

}
