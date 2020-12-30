package org.algorithmx.rules.core.ruleset;

import org.algorithmx.rules.core.model.MethodDefinition;
import org.algorithmx.rules.core.rule.RuleDefinition;
import org.algorithmx.rules.lib.spring.util.Assert;
import org.algorithmx.rules.util.RuleUtils;

import java.util.Arrays;

public class RuleSetDefinition {

    // Name of the RuleSet
    private String name;
    // Description of the RuleSet
    private final String description;

    // PreCondition method details
    private final MethodDefinition preConditionDefinition;
    // PreAction method details
    private final MethodDefinition preActionDefinition;
    // PostAction method details
    private final MethodDefinition postActionDefinition;
    // StopAction method details
    private final MethodDefinition stopActionDefinition;
    // ErrorCondition method details
    private final MethodDefinition errorConditionDefinition;
    private final RuleDefinition[] ruleDefinitions;

    public RuleSetDefinition(String name, String description, MethodDefinition preConditionDefinition,
                             MethodDefinition preActionDefinition,
                             MethodDefinition postActionDefinition,
                             MethodDefinition stopActionDefinition,
                             MethodDefinition errorConditionDefinition,
                             RuleDefinition...ruleDefinitions) {
        super();
        setName(name);
        this.description = description;
        this.preConditionDefinition = preConditionDefinition;
        this.preActionDefinition = preActionDefinition;
        this.postActionDefinition = postActionDefinition;
        this.stopActionDefinition = stopActionDefinition;
        this.errorConditionDefinition = errorConditionDefinition;
        this.ruleDefinitions = ruleDefinitions;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public MethodDefinition getPreConditionDefinition() {
        return preConditionDefinition;
    }

    public MethodDefinition getPreActionDefinition() {
        return preActionDefinition;
    }

    public MethodDefinition getPostActionDefinition() {
        return postActionDefinition;
    }

    public MethodDefinition getStopActionDefinition() {
        return stopActionDefinition;
    }

    public MethodDefinition getErrorConditionDefinition() {
        return errorConditionDefinition;
    }

    public RuleDefinition[] getRuleDefinitions() {
        return ruleDefinitions;
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
                ", preActionDefinition=" + preActionDefinition +
                ", postActionDefinition=" + postActionDefinition +
                ", stopActionDefinition=" + stopActionDefinition +
                ", errorConditionDefinition=" + errorConditionDefinition +
                ", ruleDefinitions=" + Arrays.toString(ruleDefinitions) +
                '}';
    }
}
