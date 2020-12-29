package org.algorithmx.rules.script;

import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.lib.spring.util.Assert;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class DefaultScriptProcessor implements ScriptProcessor {

    private final ScriptEngine engine;

    public DefaultScriptProcessor(ScriptEngine engine) {
        super();
        Assert.notNull(engine, "engine cannot be null.");
        this.engine = engine;
    }

    @Override
    public Object evaluate(String script, Bindings bindings) throws EvaluationException {
        ScriptEngineBindings scriptEngineBindings = new ScriptEngineBindings(bindings);

        try {
            Object result = engine.eval(script, scriptEngineBindings);
            return result;
        } catch (ScriptException e) {
            throw new EvaluationException(script, "Script Error trying to evaluate [" + script + "]", e);
        }
    }

    @Override
    public ScriptEngine getEngine() {
        return engine;
    }

    @Override
    public String toString() {
        return getEngine().toString();
    }
}
