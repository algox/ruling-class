package org.algorithmx.rules.core;

import org.algorithmx.rules.UnrulyException;
import org.algorithmx.rules.bind.BindingDeclaration;
import org.algorithmx.rules.bind.Bindings;

public interface RuleAction {

    Rule getRule();

    Action[] getActions();

    default RuleAction then(Then action) {
        return then(action, null);
    }

    RuleAction then(Then action, String description);

    default void run(RuleExecutionContext ctx) throws UnrulyException {
        if (getRule().isPass(ctx)) {
            for (Action action : getActions()) {
                action.run(ctx);
            }
        }
    }

    default void run(BindingDeclaration... bindings) {
        run(Bindings.simpleBindings().bind(bindings));
    }

    default void run(Bindings bindings) throws UnrulyException {
        run(RuleExecutionContext.create(bindings));
    }
}