package org.algorithmx.rulii.validation.rules.positive;

import org.algorithmx.rulii.core.rule.Rule;
import org.algorithmx.rulii.core.rule.RuleBuilder;
import org.algorithmx.rulii.validation.AnnotatedRunnableBuilder;
import org.algorithmx.rulii.validation.Severity;
import org.algorithmx.rulii.annotation.ValidationRule;

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
@Inherited @Documented
@ValidationRule(PositiveOrZero.PositiveOrZeroValidationRuleBuilder.class)
public @interface PositiveOrZero {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default PositiveOrZeroValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    class PositiveOrZeroValidationRuleBuilder implements AnnotatedRunnableBuilder<PositiveOrZero> {

        public PositiveOrZeroValidationRuleBuilder() {
            super();
        }

        @Override
        public Rule[] build(PositiveOrZero positiveOrZero, String bindingName) {
            PositiveOrZeroValidationRule rule = new PositiveOrZeroValidationRule(bindingName, positiveOrZero.errorCode(),
                    positiveOrZero.severity(),
                    !NOT_APPLICABLE.equals(positiveOrZero.message()) ? positiveOrZero.message() : null);
            Rule[] result = {RuleBuilder.build(rule)};
            return result;
        }
    }
}
