package org.algorithmx.rules.script;

import org.algorithmx.rules.bind.Bindings;

public interface ScriptProcessor {

    String getName();

    String getDescription();

    Object evaluate( String script, Bindings bindings) throws EvaluationException;
}
