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

package org.algorithmx.rulii.validation.actions.rules;

import org.algorithmx.rulii.annotation.Action;
import org.algorithmx.rulii.annotation.Match;
import org.algorithmx.rulii.bind.match.MatchByTypeMatchingStrategy;
import org.algorithmx.rulii.core.Runnable;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.lib.spring.util.Assert;

public class RulesAction {

    private final String ruleSetName;

    public RulesAction(String ruleSetName) {
        super();
        Assert.notNull(ruleSetName, "ruleSetName cannot be null.");
        this.ruleSetName = ruleSetName;
    }

    @Action
    public void bind(@Match(using = MatchByTypeMatchingStrategy.class) RuleContext context) {
        Runnable runnable = context.getRuleRegistry().get(getRuleSetName());

        if (runnable == null) {
            System.err.println("XXX Could not find ruleSet named [" + getRuleSetName() + "]");
            return;
        }

        runnable.run(context);
    }


    public String getRuleSetName() {
        return ruleSetName;
    }
}
