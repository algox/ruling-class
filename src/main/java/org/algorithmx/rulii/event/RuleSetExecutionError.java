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

package org.algorithmx.rulii.event;

import org.algorithmx.rulii.core.ruleset.RuleSet;
import org.algorithmx.rulii.lib.spring.util.Assert;

public class RuleSetExecutionError {

    private final RuleSet rules;
    private final Exception error;

    public RuleSetExecutionError(RuleSet rules, Exception error) {
        super();
        Assert.notNull(rules, "rules cannot be null.");
        this.rules = rules;
        this.error = error;
    }

    public RuleSet getRules() {
        return rules;
    }

    public Exception getError() {
        return error;
    }

    @Override
    public String toString() {
        return "RuleSetExecutionError{" +
                "rules=" + rules +
                ", error=" + error +
                '}';
    }
}
