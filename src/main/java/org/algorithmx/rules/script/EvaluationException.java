package org.algorithmx.rules.script;

import org.algorithmx.rules.core.UnrulyException;

public class EvaluationException extends UnrulyException {

    private String script;

    public EvaluationException(String script, String message, Throwable cause) {
        super(message, cause);
        this.script = script;
    }

    public String getScript() {
        return script;
    }
}
