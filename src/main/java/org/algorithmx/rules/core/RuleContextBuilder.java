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
package org.algorithmx.rules.core;

import org.algorithmx.rules.bind.BindingMatchingStrategy;
import org.algorithmx.rules.bind.BindingMatchingStrategyType;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.impl.NoOpRuleAuditor;
import org.algorithmx.rules.spring.util.Assert;

/**
 * Builder class to properly build a RuleContext with the belss & whistles.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class RuleContextBuilder {

    private BindingMatchingStrategy matchingStrategy = BindingMatchingStrategy.getDefault();
    private Bindings bindings = Bindings.defaultBindings();
    private Condition stopWhen = null;
    private RuleAuditor auditor =  RuleAuditor.defaultRuleAuditor();
    private ParameterResolver parameterResolver = ParameterResolver.defaultParameterResolver();

    private RuleContextBuilder() {
        super();
    }

    public static RuleContextBuilder create() {
        return new RuleContextBuilder();
    }

    /**
     * Sets the matching strategy to uss.
     *
     * @param type matching strategy type.
     * @return this for fluency.
     */
    public RuleContextBuilder matchUsing(BindingMatchingStrategyType type) {
        Assert.notNull(type, "type cannot be null.");
        this.matchingStrategy = type.getStrategy();
        return this;
    }

    /**
     * Sets the Bindings to use.
     *
     * @param bindings Bindings to use.
     * @return this for fluency.
     */
    public RuleContextBuilder bindWith(Bindings bindings) {
        Assert.notNull(bindings, "bindings cannot be null.");
        this.bindings = bindings;
        return this;
    }

    /**
     * Sets the Stop Condition.
     *
     * @param condition execution stop condition.
     * @return this for fluency.
     */
    public RuleContextBuilder stopWhen(Condition condition) {
        Assert.notNull(condition, "condition cannot be null.");
        this.stopWhen = stopWhen;
        return this;
    }

    /**
     * Enables Rule Execution Auditing.
     * @return this for fluency.
     */
    public RuleContextBuilder audit() {
        this.auditor = RuleAuditor.defaultRuleAuditor();
        return this;
    }

    /**
     * Disables Rule Execution.
     * @return this for fluency.
     */
    public RuleContextBuilder doNotAudit() {
        this.auditor = new NoOpRuleAuditor();
        return this;
    }

    /**
     * Audit Rule Execution with the given auditor.
     *
     * @param auditor desired auditor.
     * @return this for fluency.
     */
    public RuleContextBuilder auditWith(RuleAuditor auditor) {
        this.auditor = auditor;
        return this;
    }



    /**
     * Builds a Rule Context with desired parameters.
     *
     * @return new Rule Context.
     */
    public RuleContext build() {
        RuleContext result  = new RuleContext();
        result.setAuditor(auditor);
        result.setBindings(bindings);
        result.setMatchingStrategy(matchingStrategy);
        result.setStopWhen(stopWhen);
        return result;
    }
}
