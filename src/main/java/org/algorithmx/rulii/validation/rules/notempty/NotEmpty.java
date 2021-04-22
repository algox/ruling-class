package org.algorithmx.rulii.validation.rules.notempty;

import org.algorithmx.rulii.annotation.ValidationMarker;
import org.algorithmx.rulii.core.rule.Rule;
import org.algorithmx.rulii.validation.AnnotatedRunnableBuilder;
import org.algorithmx.rulii.validation.Severity;

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
@ValidationMarker(NotEmpty.NotEmptyValidationRuleBuilder.class)
public @interface NotEmpty {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default NotEmptyValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    String when() default NOT_APPLICABLE;

    class NotEmptyValidationRuleBuilder implements AnnotatedRunnableBuilder<NotEmpty> {

        public NotEmptyValidationRuleBuilder() {
            super();
        }

        @Override
        public Rule build(NotEmpty notEmpty, String bindingName) {
            NotEmptyValidationRule rule = new NotEmptyValidationRule(bindingName, notEmpty.errorCode(),
                    notEmpty.severity(), !NOT_APPLICABLE.equals(notEmpty.message()) ? notEmpty.message() : null);
            return buildRule(rule, !NOT_APPLICABLE.equals(notEmpty.when()) ? notEmpty.when() : null);
        }
    }
}
