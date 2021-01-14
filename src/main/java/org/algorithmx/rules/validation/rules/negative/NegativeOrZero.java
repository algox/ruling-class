package org.algorithmx.rules.validation.rules.negative;

import org.algorithmx.rules.core.rule.RuleBuilder;
import org.algorithmx.rules.core.ruleset.RuleSetBuilder;
import org.algorithmx.rules.validation.Severity;
import org.algorithmx.rules.validation.SingleValueValidationRuleBuilder;
import org.algorithmx.rules.validation.ValidationRuleBuilder;

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
@ValidationRuleBuilder(NegativeOrZero.NegativeOrZeroValidationRuleBuilder.class)
public @interface NegativeOrZero {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default NegativeOrZeroValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    class NegativeOrZeroValidationRuleBuilder implements SingleValueValidationRuleBuilder<NegativeOrZero> {

        public NegativeOrZeroValidationRuleBuilder() {
            super();
        }

        @Override
        public RuleSetBuilder build(NegativeOrZero negativeOrZero, RuleSetBuilder builder) {
            NegativeOrZeroValidationRule rule = new NegativeOrZeroValidationRule(negativeOrZero.errorCode(),
                    negativeOrZero.severity(),
                    !NOT_APPLICABLE.equals(negativeOrZero.message()) ? negativeOrZero.message() : null);
            // TODO : Need to adapt to the annotation element
            builder.rule(RuleBuilder.build(rule));
            return builder;
        }
    }
}