package org.algorithmx.rulii.validation.rules.script;

import org.algorithmx.rulii.core.rule.Rule;
import org.algorithmx.rulii.core.rule.RuleBuilder;
import org.algorithmx.rulii.validation.AnnotatedRunnableBuilder;
import org.algorithmx.rulii.validation.Severity;
import org.algorithmx.rulii.annotation.ValidationMarker;
import org.algorithmx.rulii.annotation.ValidationMarkerContainer;
import org.algorithmx.rulii.validation.rules.size.SizeValidationRule;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, METHOD, CONSTRUCTOR, ANNOTATION_TYPE, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Inherited
@Documented
@Repeatable(ScriptRule.ScriptRuleList.class)
@ValidationMarker(ScriptRule.ScriptRuleRuleBuilder.class)
public @interface ScriptRule {

    String NOT_APPLICABLE = "N/A";

    String value();

    String errorCode() default SizeValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    class ScriptRuleRuleBuilder implements AnnotatedRunnableBuilder<ScriptRule> {

        public ScriptRuleRuleBuilder() {
            super();
        }

        @Override
        public Rule[] build(ScriptRule scriptRule, String bindingName) {
            ScriptConditionRule rule = new ScriptConditionRule(bindingName, scriptRule.value(),
                    scriptRule.errorCode(), scriptRule.severity(),
                    !NOT_APPLICABLE.equals(scriptRule.message()) ? scriptRule.message() : null);
            Rule[] result = {RuleBuilder.build(rule)};
            return result;
        }
    }

    @Target({FIELD, METHOD, CONSTRUCTOR, ANNOTATION_TYPE, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Inherited @Documented
    @ValidationMarkerContainer(ScriptRule.class)
    @interface ScriptRuleList {
        ScriptRule[] value();
    }
}
