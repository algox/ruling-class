package org.algorithmx.rulii.validation.rules.script;

import org.algorithmx.rulii.annotation.Match;
import org.algorithmx.rulii.annotation.Otherwise;
import org.algorithmx.rulii.bind.match.MatchByTypeMatchingStrategy;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.validation.BindingValidationRule;
import org.algorithmx.rulii.validation.RuleViolationBuilder;
import org.algorithmx.rulii.validation.RuleViolations;
import org.algorithmx.rulii.validation.Severity;

public class ScriptConditionRule extends BindingValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {Object.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.ScriptRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "Given Condition {2} on {0} not met. Given value {1}.";

    private String scriptCondition;

    public ScriptConditionRule(String bindingName, String scriptCondition) {
        this(bindingName, scriptCondition, ERROR_CODE, Severity.ERROR, null);
    }

    public ScriptConditionRule(String bindingName, String scriptCondition, String errorCode, Severity severity, String errorMessage) {
        super(bindingName, errorCode, severity, errorMessage, DEFAULT_MESSAGE);
        Assert.notNull(scriptCondition, "scriptCondition cannot be null.");
        this.scriptCondition = scriptCondition;
    }

    @Override
    protected boolean isValid(RuleContext context, Object value) {
        return context.getScriptingProcessor().evaluateCondition(scriptCondition, context.getBindings());
    }

    @Otherwise
    public void otherwise(RuleContext context, Object value,
                          @Match(using = MatchByTypeMatchingStrategy.class) RuleViolations errors) {
        RuleViolationBuilder builder = createRuleViolationBuilder()
                .param("bindingName", getBindingName())
                .param(getBindingName(), value);
        errors.add(builder.build(context));
    }

    @Override
    public Class<?>[] getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    @Override
    public String toString() {
        return "ScriptRule{" +
                "scriptCondition='" + scriptCondition + '\'' +
                ", bindingName=" + getBindingName() +
                '}';
    }
}
