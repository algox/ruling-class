package org.algorithmx.rules.script;

import org.algorithmx.rules.bind.Bindings;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public interface ScriptProcessor {

    String JAVASCRIPT = "ECMAScript";

    static ScriptProcessor create() {
        // Create Javascript Script processor (if possible)
        ScriptProcessor result = create(JAVASCRIPT);

        // Javascript not found see any other scripting languages are avail
        if (result == null) {
            ScriptEngineManager scriptEngineManager = new ScriptEngineManager();

            if (scriptEngineManager.getEngineFactories().size() > 0) {
                result = create(scriptEngineManager.getEngineFactories().get(0).getScriptEngine());
            }
        }

        return result;
    }

    static ScriptProcessor create(String language) {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine engine = scriptEngineManager.getEngineByName(language);
        return engine != null ? new DefaultScriptProcessor(engine) : null;
    }

    static ScriptProcessor create(ScriptEngine engine) {
        return new DefaultScriptProcessor(engine);
    }

    Object evaluate(String script, Bindings bindings) throws EvaluationException;

    ScriptEngine getEngine();
}
