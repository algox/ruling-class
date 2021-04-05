package org.algorithmx.rulii.validation.rules.min;

import org.algorithmx.rulii.core.rule.Rule;
import org.algorithmx.rulii.core.rule.RuleBuilder;
import org.algorithmx.rulii.validation.AnnotatedRunnableBuilder;
import org.algorithmx.rulii.validation.Severity;
import org.algorithmx.rulii.annotation.ValidationMarker;

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
@ValidationMarker(DecimalMin.DecimalMinValidationRuleBuilder.class)
public @interface DecimalMin {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default DecimalMinValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    double value();

    boolean inclusive() default true;

    class DecimalMinValidationRuleBuilder implements AnnotatedRunnableBuilder<DecimalMin> {

        public DecimalMinValidationRuleBuilder() {
            super();
        }

        @Override
        public Rule[] build(DecimalMin min, String bindingName) {
            DecimalMinValidationRule rule = new DecimalMinValidationRule(bindingName, min.errorCode(), min.severity(),
                    !NOT_APPLICABLE.equals(min.message()) ? min.message() : null,
                    BigDecimal.valueOf(min.value()), min.inclusive());
            Rule[] result = {RuleBuilder.build(rule)};
            return result;
        }
    }
}
