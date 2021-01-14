package org.algorithmx.rules.validation.rules.digits;

import org.algorithmx.rules.core.rule.RuleBuilder;
import org.algorithmx.rules.core.ruleset.RuleSetBuilder;
import org.algorithmx.rules.validation.Severity;
import org.algorithmx.rules.validation.ValidationRuleBuilder;
import org.algorithmx.rules.validation.ValidationRuleProducer;

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
@ValidationRuleBuilder(producer = Digits.DigitsValidationRuleProducer.class)
public @interface Digits {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default DigitsValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    /**
     * @return maximum number of integral digits accepted for this number
     */
    int integer();

    /**
     * @return maximum number of fractional digits accepted for this number
     */
    int fraction();

    class DigitsValidationRuleProducer implements ValidationRuleProducer<Digits> {

        public DigitsValidationRuleProducer() {
            super();
        }

        @Override
        public void produce(Digits digits, RuleSetBuilder builder) {
            DigitsValidationRule rule = new DigitsValidationRule(digits.errorCode(),
                    digits.severity(), !NOT_APPLICABLE.equals(digits.message()) ? digits.message() : null,
                    digits.integer(), digits.fraction());
            // TODO : Need to adapt to the annotation element
            builder.rule(RuleBuilder.build(rule));
        }

        @Override
        public Class<?>[] types() {
            return DigitsValidationRule.SUPPORTED_TYPES;
        }
    }
}
