package org.algorithmx.rules.core;

import org.algorithmx.rules.UnrulyException;
import org.algorithmx.rules.bind.BindingDeclaration;
import org.algorithmx.rules.bind.Bindings;

import java.util.Objects;
import java.util.function.Predicate;

@FunctionalInterface
public interface Rule extends Predicate<RuleExecutionContext> {

    boolean run(RuleExecutionContext ctx) throws UnrulyException;

    default boolean run(BindingDeclaration... bindings) {
        return run(Bindings.create(bindings));
    }

    default boolean run(Bindings bindings) throws UnrulyException {
        return run(RuleExecutionContext.create(bindings));
    }

    default boolean test(Bindings bindings) throws UnrulyException {
        return run(RuleExecutionContext.create(bindings));
    }

    default boolean test(RuleExecutionContext ctx) throws UnrulyException {
        return run(ctx);
    }

    default Rule and(Rule other) {
        Objects.requireNonNull(other);
        return (t) -> run(t) && other.test(t);
    }

    default Rule negate() {
        return (t) -> !test(t);
    }

    default Rule or(Rule other) {
        Objects.requireNonNull(other);
        return (t) -> test(t) || other.test(t);
    }
}
