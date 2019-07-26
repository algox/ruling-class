package org.algorithmx.rules.core;

import org.algorithmx.rules.UnrulyException;
import org.algorithmx.rules.bind.Bindings;

import java.util.Objects;
import java.util.function.Predicate;

@FunctionalInterface
public interface BaseRule extends Predicate<RuleExecutionContext> {

    default boolean test(Bindings bindings) throws UnrulyException {
        return run(RuleExecutionContext.create(bindings));
    }

    default boolean test(RuleExecutionContext ctx) throws UnrulyException {
        return run(ctx);
    }

    default BaseRule and(Rule other) {
        Objects.requireNonNull(other);
        return (t) -> run(t) && other.test(t);
    }

    default BaseRule negate() {
        return (t) -> !test(t);
    }

    default BaseRule or(Rule other) {
        Objects.requireNonNull(other);
        return (t) -> test(t) || other.test(t);
    }

    default boolean run(Bindings bindings) throws UnrulyException {
        return run(RuleExecutionContext.create(bindings));
    }

    boolean run(RuleExecutionContext ctx) throws UnrulyException;

}
