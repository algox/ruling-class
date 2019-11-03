package org.algorithmx.rules.validation;

import org.algorithmx.rules.annotation.Bind;
import org.algorithmx.rules.annotation.Given;
import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.bind.BindingMatchingStrategyType;
import org.algorithmx.rules.core.RuleContext;
import org.algorithmx.rules.model.Severity;
import org.algorithmx.rules.spring.util.Assert;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;

public class BindingValidationRule<T> extends ValidationRule {

    private static final AtomicInteger COUNT = new AtomicInteger();

    private String ruleName;
    private final String bindingName;
    private final Supplier<Binding<T>> supplier;
    private final Function<T, Boolean> condition;

    public BindingValidationRule(String errorCode, Severity severity, String errorMessage,
                                 Function<T, Boolean> condition, String bindingName) {
        super(errorCode, severity, errorMessage);
        Assert.notNull(condition, "condition cannot be null.");
        Assert.notNull(bindingName, "bindingName cannot be null.");
        this.condition = condition;
        this.bindingName = bindingName;
        this.supplier = null;
    }

    public BindingValidationRule(String errorCode, Severity severity, String errorMessage,
                                 Function<T, Boolean> condition, Supplier<Binding<T>> supplier) {
        super(errorCode, severity, errorMessage);
        Assert.notNull(condition, "condition cannot be null.");
        Assert.notNull(supplier, "supplier cannot be null.");
        this.condition = condition;
        this.supplier = supplier;
        this.bindingName = null;
    }

    /**
     * Rule condition : Check if the Binding is present and value is not null.
     *
     * @param ruleContext current rule context.
     * @return true if the validation condition is met; false otherwise.
     */
    @Given
    public boolean when(@Bind(using = BindingMatchingStrategyType.MATCH_BY_TYPE) RuleContext ruleContext) {
        Binding<T> binding = supplier != null ? supplier.get() : ruleContext.getBindings().getBinding(bindingName);
        return when(binding);
    }

    protected boolean when(Binding<T> binding) {
        return binding != null ? when(binding.get()) : false;
    }

    /**
     * Delegating method to do the actual condition check.
     *
     * @param value Binding value.
     * @return true if condition is satisfied; false otherwise.
     */
    protected boolean when(T value) {
        return condition.apply(value);
    }

    protected Map<String, Binding> resolveParameters(RuleContext ctx) {
        Map<String, Binding> result = new LinkedHashMap<>();

        if (bindingName != null) {
            result.put(bindingName, ctx.getBindings().getBinding(bindingName));
        } else {
            Binding binding = supplier.get();
            if (binding != null) result.put(binding.getName(), binding);
        }

        return result;
    }

    @Override
    public String getName() {
        return ruleName != null ? ruleName : super.getName() + "_" + COUNT.addAndGet(1);
    }

    protected String getBindingName() {
        if (bindingName != null) return bindingName;
        Binding binding = supplier.get();
        return binding != null ? binding.getName() : "";
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }
}
