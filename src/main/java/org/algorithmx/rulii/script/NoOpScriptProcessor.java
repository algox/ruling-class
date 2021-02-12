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
import org.algorithmx.rulii.core.UnrulyException;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;

public class NoOpScriptProcessor implements ScriptProcessor {

    public NoOpScriptProcessor() {
        super();
    }

    @Override
    public ScriptContext createContext(Bindings bindings) {
        throw new UnrulyException("Please supply a ScriptProcessor and try again.");
    }

    @Override
    public Object evaluate(String script, Bindings bindings) throws EvaluationException {
        throw new UnrulyException("Unable to execute script [" + script + "]. Please supply a ScriptProcessor and try again.");
    }

    @Override
    public boolean evaluateCondition(String script, Bindings bindings) throws EvaluationException {
        throw new UnrulyException("Unable to execute script [" + script + "]. Please supply a ScriptProcessor and try again.");
    }

    @Override
    public Object evaluate(String script, ScriptContext context) throws EvaluationException {
        throw new UnrulyException("Unable to execute script [" + script + "]. Please supply a ScriptProcessor and try again.");
    }

    @Override
    public ScriptEngine getEngine() {
        throw new UnrulyException("Please supply a ScriptProcessor and try again.");
    }

    @Override
    public String toString() {
        return "NoOpScriptProcessor{}";
    }
}
