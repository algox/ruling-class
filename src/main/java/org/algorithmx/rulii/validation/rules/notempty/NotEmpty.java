package org.algorithmx.rulii.validation.rules.notempty;

import org.algorithmx.rulii.core.rule.Rule;
import org.algorithmx.rulii.core.rule.RuleBuilder;
import org.algorithmx.rulii.traverse.AnnotatedRuleBuilder;
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
@ValidationRule(NotEmpty.NotEmptyValidationRuleBuilder.class)
public @interface NotEmpty {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default NotEmptyValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    class NotEmptyValidationRuleBuilder implements AnnotatedRuleBuilder<NotEmpty> {

        public NotEmptyValidationRuleBuilder() {
            super();
        }

        @Override
        public Rule[] build(NotEmpty notEmpty, String bindingName) {
            NotEmptyValidationRule rule = new NotEmptyValidationRule(bindingName, notEmpty.errorCode(),
                    notEmpty.severity(),
                    !NOT_APPLICABLE.equals(notEmpty.message()) ? notEmpty.message() : null);
            Rule[] result = {RuleBuilder.build(rule)};
            return result;
        }
    }
}
