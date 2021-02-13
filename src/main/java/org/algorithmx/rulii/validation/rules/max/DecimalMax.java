package org.algorithmx.rulii.validation.rules.max;

import org.algorithmx.rulii.core.rule.Rule;
import org.algorithmx.rulii.core.rule.RuleBuilder;
import org.algorithmx.rulii.validation.BindingValidationRuleBuilder;
import org.algorithmx.rulii.validation.Severity;
import org.algorithmx.rulii.validation.annotation.ValidationRule;

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
@ValidationRule(DecimalMax.DecimalMaxValidationRuleBuilder.class)
public @interface DecimalMax {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default DecimalMaxValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    double value();

    boolean inclusive() default true;

    class DecimalMaxValidationRuleBuilder implements BindingValidationRuleBuilder<DecimalMax> {

        public DecimalMaxValidationRuleBuilder() {
            super();
        }

        @Override
        public Rule[] build(DecimalMax max, String bindingName) {
            DecimalMaxValidationRule rule = new DecimalMaxValidationRule(bindingName, max.errorCode(), max.severity(),
                    !NOT_APPLICABLE.equals(max.message()) ? max.message() : null,
                    BigDecimal.valueOf(max.value()), max.inclusive());
            Rule[] result = {RuleBuilder.build(rule)};
            return result;
        }
    }
}
