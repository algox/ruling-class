package org.algorithmx.rulii.validation.rules.script;

import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.validation.BindingValidationRule;
import org.algorithmx.rulii.validation.Severity;

public class ScriptAssertRule extends BindingValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {Object.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.ScriptAssertRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "Script Condition not met. Script {0}.";

    private final String scriptCondition;

    public ScriptAssertRule(String bindingName, String scriptCondition) {
        this(bindingName, scriptCondition, ERROR_CODE, Severity.ERROR, null);
    }

    public ScriptAssertRule(String bindingName, String scriptCondition, String errorCode,
                            Severity severity, String errorMessage) {
        super(bindingName, errorCode, severity, errorMessage, DEFAULT_MESSAGE);
        Assert.notNull(scriptCondition, "scriptCondition cannot be null.");
        this.scriptCondition = scriptCondition;
    }

    @Override
    protected boolean isValid(RuleContext context, Object value) {
        return context.getScriptProcessor().evaluateCondition(scriptCondition, context.getBindings());
    }

    @Override
    public Object getBindingValue(RuleContext context) {
        return scriptCondition;
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
