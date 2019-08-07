package org.algorithmx.rules.core;

import org.algorithmx.rules.UnrulyException;
import org.algorithmx.rules.bind.BindingDeclaration;
import org.algorithmx.rules.bind.Bindings;

public interface RuleAction {

    Rule getRule();

    Action getAction();

    default void run(RuleExecutionContext ctx) throws UnrulyException {
        if (getRule().isPass(ctx)) {
            getAction().run(ctx);
        }
    }

    default void run(BindingDeclaration... bindings) {
        run(Bindings.defaultBindings().bind(bindings));
    }

    default void run(Bindings bindings) throws UnrulyException {
        run(RuleExecutionContext.create(bindings));
    }
}