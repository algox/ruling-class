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
package org.algorithmx.rules.build;

import org.algorithmx.rules.core.RuleSet;
import org.algorithmx.rules.core.impl.DefaultRuleSet;

public class RuleSetBuilder {

    private String name;
    private String description;

    private RuleSetBuilder(String name) {
        super();
        this.name = name;
    }

    private RuleSetBuilder(String name, String description) {
        super();
        this.name = name;
        this.description = description;
    }

    public static RuleSetBuilder with(String name) {
        return new RuleSetBuilder(name);
    }

    public static RuleSetBuilder with(String name, String description) {
        return new RuleSetBuilder(name, description);
    }

    public RuleSetBuilder description(String description) {
        this.description = description;
        return this;
    }

    public RuleSet build() {
        return new DefaultRuleSet(name, description);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}