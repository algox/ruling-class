package org.algorithmx.rulii.validation.rules.notnull;

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
import static java.lang.annotation.ElementType.TYPE_PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, METHOD, CONSTRUCTOR, ANNOTATION_TYPE, PARAMETER, TYPE_PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Inherited @Documented
@ValidationRule(NotNull.NotNullValidationRuleBuilder.class)
public @interface NotNull {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default NotNullValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    class NotNullValidationRuleBuilder implements AnnotatedRuleBuilder<NotNull> {

        public NotNullValidationRuleBuilder() {
            super();
        }

        @Override
        public Rule[] build(NotNull notNull, String bindingName) {
            NotNullValidationRule rule = new NotNullValidationRule(bindingName, notNull.errorCode(),
                    notNull.severity(),
                    !NOT_APPLICABLE.equals(notNull.message()) ? notNull.message() : null);
            Rule[] result = {RuleBuilder.build(rule)};
            return result;
        }
    }
}
