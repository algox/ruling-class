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
import org.algorithmx.rulii.lib.spring.util.Assert;

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
