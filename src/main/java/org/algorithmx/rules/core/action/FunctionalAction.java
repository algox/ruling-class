package org.algorithmx.rules.core.action;

import org.algorithmx.rules.util.ActionUtils;

import java.io.Serializable;

public interface FunctionalAction extends Action, Serializable {

    @Override
    default ActionDefinition getActionDefinition() {
        return ActionUtils.load(this, "");
    }

    @Override
    default Object getTarget() {
        return null;
    }

}
