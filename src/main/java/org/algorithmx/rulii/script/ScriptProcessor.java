/*
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 1999-2021, Algorithmx Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.algorithmx.rulii.script;

import org.algorithmx.rulii.bind.Bindings;

import javax.script.ScriptContext;
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

    ScriptContext createContext(Bindings bindings);

    Object evaluate(String script, Bindings bindings) throws EvaluationException;

    Object evaluate(String script, ScriptContext context) throws EvaluationException;

    ScriptEngine getEngine();
}
