package org.algorithmx.rules.script;

import org.algorithmx.rules.core.UnrulyException;

public class EvaluationException extends UnrulyException {

    public EvaluationException(String message) {
        super(message);
    }

    public EvaluationException(Throwable cause) {
        super(cause);
    }

    public EvaluationException(String message, Throwable cause) {
        super(message, cause);
    }
}
