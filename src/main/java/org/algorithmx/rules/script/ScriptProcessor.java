package org.algorithmx.rules.script;

import org.algorithmx.rules.bind.Bindings;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import java.util.stream.Collectors;

public interface ScriptProcessor {

    String JAVASCRIPT = "ECMAScript";

    static ScriptProcessor create() {
        return create(JAVASCRIPT);
    }

    static ScriptProcessor create(String language) {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine engine = scriptEngineManager.getEngineByName(language);

        if (engine == null) {
            throw new IllegalArgumentException("Unsupported Scripting Language [" + language + "] Available ["
                    + scriptEngineManager.getEngineFactories().stream().map(ScriptEngineFactory::getLanguageName)
                    .collect(Collectors.joining(", ")) + "]");
        }

        return new DefaultScriptProcessor(engine);
    }

    static ScriptProcessor create(ScriptEngine engine) {
        return new DefaultScriptProcessor(engine);
    }

    Object evaluate( String script, Bindings bindings) throws EvaluationException;

    ScriptEngine getEngine();
}
