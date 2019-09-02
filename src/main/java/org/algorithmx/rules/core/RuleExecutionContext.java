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

public class RuleExecutionContext implements RuleExecutionAuditor {

    private static final ThreadLocal<RuleExecutionContext> CTX_HOLDER = new ThreadLocal<>();

    private final Bindings bindings;
    private final BindingMatchingStrategy matchingStrategy;
    private final List<RuleExecution> audit = Collections.synchronizedList(new ArrayList<>());

    public static RuleExecutionContext create(Bindings bindings) {
        return new RuleExecutionContext(bindings, BindingMatchingStrategyType.getDefault().getStrategy());
    }

    public static RuleExecutionContext create(Bindings bindings, BindingMatchingStrategy matchingStrategy) {
        return new RuleExecutionContext(bindings, matchingStrategy);
    }

    private RuleExecutionContext(Bindings bindings, BindingMatchingStrategy matchingStrategy) {
        super();
        Assert.notNull(bindings, "bindings cannot be null");
        Assert.notNull(matchingStrategy, "matchingStrategy cannot be null");
        this.bindings = bindings;
        this.matchingStrategy = matchingStrategy;
    }

    public static final RuleExecutionContext get() {
        return CTX_HOLDER.get();
    }

    public static final void set(RuleExecutionContext ctx) {
        CTX_HOLDER.set(ctx);
    }

    public static final void clear() {
        CTX_HOLDER.remove();
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

    public Bindings bindings() {
        return bindings;
    }

    public BindingMatchingStrategy matchingStrategy() {
        return matchingStrategy;
    }
}