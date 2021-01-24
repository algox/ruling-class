/**
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
