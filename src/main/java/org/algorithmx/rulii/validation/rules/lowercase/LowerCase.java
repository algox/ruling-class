package org.algorithmx.rulii.validation.rules.lowercase;

import org.algorithmx.rulii.core.rule.Rule;
import org.algorithmx.rulii.core.rule.RuleBuilder;
import org.algorithmx.rulii.validation.AnnotatedRunnableBuilder;
import org.algorithmx.rulii.validation.Severity;
import org.algorithmx.rulii.annotation.ValidationMarker;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
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
@ValidationMarker(LowerCase.LowerCaseValidationRuleBuilder.class)
public @interface LowerCase {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default LowerCaseValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    String when() default NOT_APPLICABLE;

    class LowerCaseValidationRuleBuilder implements AnnotatedRunnableBuilder<LowerCase> {

        public LowerCaseValidationRuleBuilder() {
            super();
        }

        @Override
        public Rule[] build(LowerCase lowerCase, String bindingName) {
            LowerCaseValidationRule rule = new LowerCaseValidationRule(bindingName, lowerCase.errorCode(),
                    lowerCase.severity(), !NOT_APPLICABLE.equals(lowerCase.message()) ? lowerCase.message() : null);
            Rule[] result = {buildRule(rule, !NOT_APPLICABLE.equals(lowerCase.when()) ? lowerCase.when() : null)};
            return result;
        }
    }
}
