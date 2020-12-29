package org.algorithmx.rules.script;

import org.algorithmx.rules.bind.Bindings;

import javax.script.ScriptEngine;

public class NoOpScriptProcessor implements ScriptProcessor {

    public NoOpScriptProcessor() {
        super();
    }

    @Override
    public Object evaluate(String script, Bindings bindings) throws EvaluationException {
        throw new IllegalStateException("ScriptProcessor was not detected. " +
                "Please configure one to RuleContext.scriptProcessor(..) and try again.");
    }

    @Override
    public ScriptEngine getEngine() {
        return null;
    }
}
