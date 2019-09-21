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
import org.algorithmx.rules.model.RuleExecution;
import org.algorithmx.rules.spring.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Responsible for state management during Rule execution. This class provides access to everything that is required
 * by the Rule Engine to execute a given set of Rules.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class RuleContext implements RuleAuditor {

    // Holds on the RuleContext associated with the current Thread.
    private static final ThreadLocal<RuleContext> CTX_HOLDER = new ThreadLocal<>();

    private final Bindings bindings;
    private final BindingMatchingStrategy matchingStrategy;
    private final List<RuleExecution> audit = Collections.synchronizedList(new ArrayList<>());

    // TODO : Stop When()

    /**
     * Creates a RuleContext given a set of Bindings. The default matching strategy will be used.
     *
     * @param bindings bindings.
     * @return new RuleContext.
     */
    public static RuleContext create(Bindings bindings) {
        return new RuleContext(bindings, BindingMatchingStrategyType.getDefault().getStrategy());
    }

    /**
     * Creates a RuleContext given a set of Bindings and matching strategy.
     *
     * @param bindings bindings.
     * @param matchingStrategy binding matching strategy.
     * @return new RuleContext.
     */
    public static RuleContext create(Bindings bindings, BindingMatchingStrategy matchingStrategy) {
        return new RuleContext(bindings, matchingStrategy);
    }

    private RuleContext(Bindings bindings, BindingMatchingStrategy matchingStrategy) {
        super();
        Assert.notNull(bindings, "bindings cannot be null");
        Assert.notNull(matchingStrategy, "matchingStrategy cannot be null");
        this.bindings = bindings;
        this.matchingStrategy = matchingStrategy;
    }

    /**
     * Returns the RuleContext associated with the current Thread.
     *
     * @return RuleContext associated with the current Thread.
     */
    public static final RuleContext get() {
        return CTX_HOLDER.get();
    }

    /**
     * Associated the given RuleContext with the current Thread.
     *
     * @param ctx RuleContext
     */
    public static final void set(RuleContext ctx) {
        CTX_HOLDER.set(ctx);
    }

    /**
     * Clears any associations with the current Thread.
     */
    public static final void clear() {
        CTX_HOLDER.remove();
    }

    /**
     * Bindings to use.
     *
     * @return bindings.
     */
    public Bindings bindings() {
        return bindings;
    }

    /**
     * Matching strategy to use during the Rule execution.
     *
     * @return BindingMatchingStrategy.
     */
    public BindingMatchingStrategy matchingStrategy() {
        return matchingStrategy;
    }

    @Override
    public void audit(RuleExecution execution) {
        this.audit.add(execution);
    }

    @Override
    public RuleExecution getFirstAuditItem() {
        int size = audit.size();
        return size > 0 ? audit.get(0) : null;
    }

    @Override
    public RuleExecution getLastAuditItem() {
        int size = audit.size();
        return size > 0 ? audit.get(size - 1) : null;
    }

    @Override
    public Iterator<RuleExecution> getAuditItems() {
        return audit.iterator();
    }
}