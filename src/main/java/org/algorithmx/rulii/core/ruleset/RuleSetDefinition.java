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

package org.algorithmx.rulii.core.ruleset;

import org.algorithmx.rulii.core.model.Definition;
import org.algorithmx.rulii.core.model.MethodDefinition;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.util.RuleUtils;

import java.util.Arrays;

public final class RuleSetDefinition implements Definition {

    // Name of the RuleSet
    private String name;
    // Description of the RuleSet
    private final String description;
    // PreCondition method details
    private final MethodDefinition preConditionDefinition;
    // StopAction method details
    private final MethodDefinition stopActionDefinition;
    private final Definition[] definitions;

    public RuleSetDefinition(String name, String description,
                             MethodDefinition preConditionDefinition,
                             MethodDefinition stopActionDefinition,
                             Definition...definitions) {
        super();
        setName(name);
        this.description = description;
        this.preConditionDefinition = preConditionDefinition;
        this.stopActionDefinition = stopActionDefinition;
        this.definitions = definitions;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public MethodDefinition getPreConditionDefinition() {
        return preConditionDefinition;
    }

    public MethodDefinition getStopActionDefinition() {
        return stopActionDefinition;
    }

    public Definition[] getDefinitions() {
        return definitions;
    }

    void setName(String name) {
        Assert.isTrue(RuleUtils.isValidName(name), "RuleSet name must match ["
                + RuleUtils.NAME_REGEX + "] Given [" + name + "]");
        this.name = name;
    }

    @Override
    public String toString() {
        return "RuleSetDefinition{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", preConditionDefinition=" + preConditionDefinition +
                ", stopActionDefinition=" + stopActionDefinition +
                ", definitions=" + Arrays.toString(definitions) +
                '}';
    }
}
