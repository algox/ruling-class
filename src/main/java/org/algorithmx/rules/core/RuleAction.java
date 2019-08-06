package org.algorithmx.rules.core;

import org.algorithmx.rules.UnrulyException;
import org.algorithmx.rules.bind.BindingDeclaration;
import org.algorithmx.rules.bind.Bindings;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

@FunctionalInterface
public interface RuleAction extends Consumer<RuleExecutionContext> {

    void run(RuleExecutionContext ctx) throws UnrulyException;

    default void run(BindingDeclaration... bindings) {
        run(Bindings.create(bindings));
    }

    default void run(Bindings bindings) throws UnrulyException {
        run(RuleExecutionContext.create(bindings));
    }

    @Override
    default void accept(RuleExecutionContext ctx) {
        run(ctx);
    }

    /*default RuleAction andThen(RuleAction after) {
        return (RuleExecutionContext t) -> { accept(t); after.accept(t); };
    }*/
}
