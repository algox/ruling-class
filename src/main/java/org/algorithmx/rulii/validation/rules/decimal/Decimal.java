package org.algorithmx.rulii.validation.rules.decimal;

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
@ValidationMarker(Decimal.DecimalValidationRuleBuilder.class)
public @interface Decimal {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default DecimalValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    boolean allowSpace() default true;

    String when() default NOT_APPLICABLE;

    class DecimalValidationRuleBuilder implements AnnotatedRunnableBuilder<Decimal> {

        public DecimalValidationRuleBuilder() {
            super();
        }

        @Override
        public Rule[] build(Decimal decimal, String bindingName, String path) {
            DecimalValidationRule rule = new DecimalValidationRule(bindingName, path, decimal.errorCode(),
                    decimal.severity(), !NOT_APPLICABLE.equals(decimal.message()) ? decimal.message() : null,
                    decimal.allowSpace());
            Rule[] result = {buildRule(rule, !NOT_APPLICABLE.equals(decimal.when()) ? decimal.when() : null)};
            return result;
        }
    }
}
