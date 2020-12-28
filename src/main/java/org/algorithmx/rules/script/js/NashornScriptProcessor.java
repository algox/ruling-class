package org.algorithmx.rules.script.js;

import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.script.EvaluationException;
import org.algorithmx.rules.script.ScriptEngineBindings;
import org.algorithmx.rules.script.ScriptProcessor;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class NashornScriptProcessor implements ScriptProcessor {

    private final ScriptEngine engine;

    public NashornScriptProcessor() {
        super();
        this.engine = new ScriptEngineManager().getEngineByName("nashorn");
        if (engine == null) throw new UnrulyException("Javascript Engine (nashorn) unavailable.");
    }

    public static boolean isAvailable() {
        return new ScriptEngineManager().getEngineByName("nashorn") != null;
    }

    @Override
    public Object evaluate(String script, Bindings bindings) throws EvaluationException {
        ScriptEngineBindings scriptEngineBindings = new ScriptEngineBindings(bindings);

        try {
            Object result = engine.eval(script, scriptEngineBindings);
            return result;
        } catch (ScriptException e) {
            throw new EvaluationException("Script Error trying to evaluate [" + script + "]", e);
        }
    }

    @Override
    public String getName() {
        return "nashorn";
    }

    @Override
    public String getDescription() {
        return "Javascript(nashorn) Script Processor";
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
