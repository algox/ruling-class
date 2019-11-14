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
package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.core.ResultExtractor;
import org.algorithmx.rules.core.RuleContext;
import org.algorithmx.rules.core.RuleEngine;
import org.algorithmx.rules.core.RuleSet;
import org.algorithmx.rules.error.UnrulyException;
import org.algorithmx.rules.spring.util.Assert;

import java.util.Arrays;

/**
 * Default implementation of the Rule Engine. Execution of the Rule is delegated to the RuleCommand.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class DefaultRuleEngine implements RuleEngine {

    public DefaultRuleEngine() {
        super();
    }

    @Override
    public void run(RuleContext ctx, RuleSet... rules) throws UnrulyException {
        Assert.notNull(rules, "rules cannot be null.");
        ctx.start();
        Arrays
                .stream(rules)
                .forEach(ruleSet -> run(ruleSet, ctx));
        ctx.finish();
    }

    @Override
    public <T> T run(RuleContext ctx, ResultExtractor<T> extractor, RuleSet... rules) throws UnrulyException {
        try {
            run(ctx, rules);
            return extractor.extract(ctx.getBindings());
        } catch (UnrulyException e) {
            ctx.error();
            throw e;
        } catch (Exception e) {
            ctx.error();
            throw new UnrulyException(e);
        }
    }

    public void run(RuleSet rules, RuleContext ctx) throws UnrulyException {
        Arrays.stream(rules.getRules()).forEach(rule -> rule.run(ctx));
    }
}
