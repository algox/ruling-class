package org.algorithmx.rules.core;

import org.algorithmx.rules.bind.BindingDeclaration;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.context.RuleContext;
import org.algorithmx.rules.core.context.RuleContextBuilder;

public interface Runnable<T> {

    T run(RuleContext context) throws UnrulyException;

    /**
     * Derives all the arguments and executes this Rule.
     *
     * @param bindings Rule Bindings.
     * @return execution status of the rule.
     * @throws UnrulyException thrown if there are any runtime errors during the execution.
     */
    default T run(Bindings bindings) throws UnrulyException {
        return run(RuleContextBuilder.build(bindings != null ? bindings : Bindings.create()));
    }

    /**
     * Derives all the arguments and executes this Rule.
     *
     * @param params Rule Parameters.
     * @return execution status of the rule.
     * @throws UnrulyException thrown if there are any runtime errors during the execution.
     */
    default T run(BindingDeclaration...params) throws UnrulyException {
        Bindings bindings = params != null ? Bindings.create().bind(params) : Bindings.create();
        return run(RuleContextBuilder.build(bindings));
    }
}
