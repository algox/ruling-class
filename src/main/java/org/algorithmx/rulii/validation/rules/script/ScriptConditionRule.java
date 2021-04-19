package org.algorithmx.rulii.validation.rules.script;

import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.validation.BindingValidationRule;
import org.algorithmx.rulii.validation.Severity;

public class ScriptConditionRule extends BindingValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {Object.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.ScriptRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "Given Condition {2} on {0} not met. Given value {1}.";

    private String scriptCondition;

    public ScriptConditionRule(String bindingName, String scriptCondition) {
        this(bindingName, bindingName, scriptCondition);
    }

    public ScriptConditionRule(String bindingName, String path, String scriptCondition) {
        this(bindingName, path, scriptCondition, ERROR_CODE, Severity.ERROR, null);
    }

    public ScriptConditionRule(String bindingName, String path, String scriptCondition, String errorCode,
                               Severity severity, String errorMessage) {
        super(bindingName, path, errorCode, severity, errorMessage, DEFAULT_MESSAGE);
        Assert.notNull(scriptCondition, "scriptCondition cannot be null.");
        this.scriptCondition = scriptCondition;
    }

    @Override
    protected boolean isValid(RuleContext context, Object value) {
        return context.getScriptProcessor().evaluateCondition(scriptCondition, context.getBindings());
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
