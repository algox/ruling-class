package org.algorithmx.rules.validation.rules.min;

import org.algorithmx.rules.core.rule.RuleBuilder;
import org.algorithmx.rules.core.ruleset.RuleSetBuilder;
import org.algorithmx.rules.validation.Severity;
import org.algorithmx.rules.validation.ValidationRuleBuilder;
import org.algorithmx.rules.validation.ValidationRuleProducer;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.math.BigDecimal;

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
@ValidationRuleBuilder(producer = DecimalMin.DecimalMinValidationRuleProducer.class)
public @interface DecimalMin {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default DecimalMinValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    double value();

    boolean inclusive() default true;

    class DecimalMinValidationRuleProducer implements ValidationRuleProducer<DecimalMin> {

        public DecimalMinValidationRuleProducer() {
            super();
        }

        @Override
        public void produce(DecimalMin min, RuleSetBuilder builder) {
            DecimalMinValidationRule rule = new DecimalMinValidationRule(min.errorCode(), min.severity(),
                    !NOT_APPLICABLE.equals(min.message()) ? min.message() : null,
                    BigDecimal.valueOf(min.value()), min.inclusive());
            builder.rule(RuleBuilder.build(rule));
        }

        @Override
        public Class<?>[] types() {
            return DecimalMinValidationRule.SUPPORTED_TYPES;
        }
    }
}
