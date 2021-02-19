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

package org.algorithmx.rulii.validation.beans;

import org.algorithmx.rulii.bind.Bindings;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.core.context.RuleContextBuilder;
import org.algorithmx.rulii.core.ruleset.RuleSet;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.util.objectgraph.ObjectGraph;
import org.algorithmx.rulii.validation.RuleViolations;

public class DefaultBeanValidator implements BeanValidator {

    public DefaultBeanValidator() {
        super();
    }

    @Override
    public RuleViolations validate(Object bean, Bindings bindings, boolean includeAnnotatedRules, RuleSet ruleSet) {
        Assert.notNull(bean, "bean cannot be null.");
        RuleContext context = createRuleContext(bindings);
        ObjectGraph graph = new ObjectGraph();
        BeanGraphValidator validator = new BeanGraphValidator(context);
        graph.traverse(bean, validator);
        return validator.getViolations();
    }

    protected RuleContext createRuleContext(Bindings bindings) {
        return bindings != null ? RuleContextBuilder.with(bindings).build() : RuleContextBuilder.empty();
    }
}
