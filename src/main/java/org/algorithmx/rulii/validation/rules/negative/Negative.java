package org.algorithmx.rulii.validation.rules.negative;

import org.algorithmx.rulii.core.rule.Rule;
import org.algorithmx.rulii.core.rule.RuleBuilder;
import org.algorithmx.rulii.validation.BindingValidationRuleBuilder;
import org.algorithmx.rulii.validation.Severity;
import org.algorithmx.rulii.validation.annotation.ValidationRule;

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
@ValidationRule(Negative.NegativeValidationRuleBuilder.class)
public @interface Negative {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default NegativeValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    class NegativeValidationRuleBuilder implements BindingValidationRuleBuilder<Negative> {

        public NegativeValidationRuleBuilder() {
            super();
        }

        @Override
        public Rule[] build(Negative negative, String bindingName) {
            NegativeValidationRule rule = new NegativeValidationRule(bindingName, negative.errorCode(),
                    negative.severity(),
                    !NOT_APPLICABLE.equals(negative.message()) ? negative.message() : null);
            Rule[] result = {RuleBuilder.build(rule)};
            return result;
        }
    }
}
