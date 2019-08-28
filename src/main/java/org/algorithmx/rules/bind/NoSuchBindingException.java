package org.algorithmx.rules.bind;

import org.algorithmx.rules.core.UnrulyException;

/**
 * Exception thrown when Bindings is asked for a bean instance for which it cannot find a definition.
 *
 * @author Max Arulananthan
 * @since 1.0
 *
 */
public class NoSuchBindingException extends UnrulyException {

    static final long serialVersionUID = 0L;

    public NoSuchBindingException(String name) {
        super("Binding with name [" + name + "] does not exist");
    }
}
