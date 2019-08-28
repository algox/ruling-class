package org.algorithmx.rules.core;

import org.algorithmx.rules.model.ActionDefinition;

public interface Action {

    void execute(Object...args);

    ActionDefinition getActionDefinition();

    Object getTarget();
}
