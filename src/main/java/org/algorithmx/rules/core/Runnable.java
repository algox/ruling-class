package org.algorithmx.rules.core;

import org.algorithmx.rules.bind.BindingDeclaration;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.context.RuleContext;

public interface Runnable<T> {

    T run(RuleContext context) throws UnrulyException;

    /**
     * Derives all the arguments and executes this Rule.
     *
     * @param bindings Rule Bindings.
     * @return execution status of the rule.
     * @throws UnrulyException thrown if there are any runtime errors during the execution.
     */
    T run(Bindings bindings) throws UnrulyException;

    /**
     * Derives all the arguments and executes this Rule.
     *
     * @param params Rule Parameters.
     * @return execution status of the rule.
     * @throws UnrulyException thrown if there are any runtime errors during the execution.
     */
    T run(BindingDeclaration...params) throws UnrulyException;
}
