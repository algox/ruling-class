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
@ValidationRuleBuilder(Negative.NegativeValidationRuleBuilder.class)
public @interface Negative {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default NegativeValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    class NegativeValidationRuleBuilder implements SingleValueValidationRuleBuilder<Negative> {

        public NegativeValidationRuleBuilder() {
            super();
        }

        @Override
        public RuleSetBuilder build(Negative negative, RuleSetBuilder builder) {
            NegativeValidationRule rule = new NegativeValidationRule(negative.errorCode(),
                    negative.severity(),
                    !NOT_APPLICABLE.equals(negative.message()) ? negative.message() : null);
            // TODO : Need to adapt to the annotation element
            builder.rule(RuleBuilder.build(rule));
            return builder;
        }
    }
}
