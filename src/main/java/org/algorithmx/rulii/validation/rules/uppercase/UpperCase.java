package org.algorithmx.rulii.validation.rules.uppercase;

import org.algorithmx.rulii.core.rule.Rule;
import org.algorithmx.rulii.core.rule.RuleBuilder;
import org.algorithmx.rulii.lib.spring.core.Ordered;
import org.algorithmx.rulii.validation.AnnotatedRunnableBuilder;
import org.algorithmx.rulii.validation.Severity;
import org.algorithmx.rulii.annotation.ValidationMarker;
import org.algorithmx.rulii.validation.rules.lowercase.LowerCaseValidationRule;

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
@ValidationMarker(UpperCase.UpperCaseValidationRuleBuilder.class)
public @interface UpperCase {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default LowerCaseValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    int order() default Ordered.LOWEST_PRECEDENCE;

    String when() default NOT_APPLICABLE;

    class UpperCaseValidationRuleBuilder implements AnnotatedRunnableBuilder<UpperCase> {

        public UpperCaseValidationRuleBuilder() {
            super();
        }

        @Override
        public Rule build(UpperCase upperCase, String bindingName) {
            UpperCaseValidationRule rule = new UpperCaseValidationRule(bindingName, upperCase.errorCode(),
                    upperCase.severity(), !NOT_APPLICABLE.equals(upperCase.message()) ? upperCase.message() : null);
            return buildRule(rule, upperCase.order(), !NOT_APPLICABLE.equals(upperCase.when()) ? upperCase.when() : null);
        }
    }
}
