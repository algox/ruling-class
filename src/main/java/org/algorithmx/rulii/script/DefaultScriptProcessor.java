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

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

public class DefaultScriptProcessor implements ScriptProcessor {

    private final ScriptEngine engine;

    public DefaultScriptProcessor(ScriptEngine engine) {
        super();
        Assert.notNull(engine, "engine cannot be null.");
        this.engine = engine;
    }

    @Override
    public ScriptContext createContext(Bindings bindings) {
        Assert.notNull(bindings, "bindings cannot be null.");
        javax.script.Bindings scriptEngineBindings = translate(bindings);
        return createContext(scriptEngineBindings);
    }

    @Override
    public Object evaluate(String script, Bindings bindings) throws EvaluationException {
        Assert.notNull(script, "script cannot be null.");
        Assert.notNull(bindings, "bindings cannot be null.");
        ScriptContext context = createContext(bindings);
        return evaluate(script, context);
    }

    @Override
    public boolean evaluateCondition(String script, Bindings bindings) throws EvaluationException {
        Object result = evaluate(script, bindings);

        if (!(result instanceof Boolean)) {
            throw new EvaluationException(script, "Invalid Condition Script. ["
                    + script + "] Condition must return a boolean. Result [" + result + "]");
        }

        return (Boolean) result;
    }

    @Override
    public Object evaluate(String script, ScriptContext context) throws EvaluationException {
        Assert.notNull(script, "script cannot be null.");
        Assert.notNull(context, "context cannot be null.");

        try {
            return engine.eval(script, context);
        } catch (ScriptException e) {
            throw new EvaluationException(script, "Script Error trying to evaluate [" + script + "]", e);
        }
    }

    protected javax.script.Bindings translate(Bindings bindings) {
        return new ScriptEngineBindings(bindings);
    }

    protected ScriptContext createContext(javax.script.Bindings bindings) {
        ScriptContext result = new SimpleScriptContext();
        result.setBindings(bindings, ScriptContext.GLOBAL_SCOPE);
        return result;
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
