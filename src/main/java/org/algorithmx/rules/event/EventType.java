package org.algorithmx.rules.event;

public enum EventType {

    ON_CONDITION("Condition"),
    ON_FUNCTION("Function"),
    ON_ACTION("Action"),

    RULE_START("Rule Start"),
    RULE_END("Rule End"),
    RULE_PRE_CONDITION_START("Rule Pre Condition"),
    RULE_PRE_CONDITION_END("Rule Pre Condition"),
    RULE_CONDITION_START("Rule Given Condition"),
    RULE_CONDITION_END("Rule Given Condition"),
    RULE_ACTION_START("Rule Action"),
    RULE_ACTION_END("Rule Action"),
    RULE_OTHERWISE_ACTION_START("Rule Otherwise"),
    RULE_OTHERWISE_ACTION_END("Rule Otherwise"),

    RULE_SET_START("RuleSet Start"),
    RULE_SET_END("RuleSet End"),
    RULE_SET_PRE_CONDITION_START("RuleSet Pre Condition"),
    RULE_SET_PRE_CONDITION_END("RuleSet Pre Condition"),
    RULE_SET_STOP_CONDITION_START("RuleSet Stop Condition"),
    RULE_SET_STOP_CONDITION_END("RuleSet Stop Condition");

    private String description;

    EventType() {
        this("");
    }

    EventType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
