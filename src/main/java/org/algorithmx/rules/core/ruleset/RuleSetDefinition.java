package org.algorithmx.rules.core.ruleset;

import org.algorithmx.rules.core.model.Definition;
import org.algorithmx.rules.core.model.MethodDefinition;
import org.algorithmx.rules.lib.spring.util.Assert;
import org.algorithmx.rules.util.RuleUtils;

import java.util.Arrays;

public final class RuleSetDefinition implements Definition {

    // Name of the RuleSet
    private String name;
    // Description of the RuleSet
    private final String description;

    // PreCondition method details
    private final MethodDefinition preConditionDefinition;
    // StopAction method details
    private final MethodDefinition stopActionDefinition;
    private final Definition[] definitions;

    public RuleSetDefinition(String name, String description, MethodDefinition preConditionDefinition,
                             MethodDefinition stopActionDefinition,
                             Definition...definitions) {
        super();
        setName(name);
        this.description = description;
        this.preConditionDefinition = preConditionDefinition;
        this.stopActionDefinition = stopActionDefinition;
        this.definitions = definitions;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public MethodDefinition getPreConditionDefinition() {
        return preConditionDefinition;
    }

    public MethodDefinition getStopActionDefinition() {
        return stopActionDefinition;
    }

    public Definition[] getDefinitions() {
        return definitions;
    }

    void setName(String name) {
        Assert.isTrue(RuleUtils.isValidName(name), "RuleSet name must match ["
                + RuleUtils.NAME_REGEX + "] Given [" + name + "]");
        this.name = name;
    }

    @Override
    public String toString() {
        return "RuleSetDefinition{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", preConditionDefinition=" + preConditionDefinition +
                ", stopActionDefinition=" + stopActionDefinition +
                ", definitions=" + Arrays.toString(definitions) +
                '}';
    }
}
