package org.algorithmx.rulii.validation.rules.blank;

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
@ValidationRule(Blank.BlankValidationRuleBuilder.class)
public @interface Blank {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default BlankValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    class BlankValidationRuleBuilder implements BindingValidationRuleBuilder<Blank> {

        public BlankValidationRuleBuilder() {
            super();
        }

        @Override
        public Rule[] build(Blank notBlank, String bindingName) {
            BlankValidationRule rule = new BlankValidationRule(bindingName, notBlank.errorCode(),
                    notBlank.severity(),
                    !NOT_APPLICABLE.equals(notBlank.message()) ? notBlank.message() : null);
            Rule[] result = {RuleBuilder.build(rule)};
            return result;
        }
    }
}
