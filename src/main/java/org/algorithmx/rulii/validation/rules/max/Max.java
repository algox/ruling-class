package org.algorithmx.rulii.validation.rules.max;

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
@ValidationMarker(Max.MaxValidationRuleBuilder.class)
public @interface Max {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default MaxValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    long value();

    String when() default NOT_APPLICABLE;

    class MaxValidationRuleBuilder implements AnnotatedRunnableBuilder<Max> {

        public MaxValidationRuleBuilder() {
            super();
        }

        @Override
        public Rule[] build(Max max, String bindingName) {
            MaxValidationRule rule = new MaxValidationRule(bindingName, max.errorCode(), max.severity(),
                    !NOT_APPLICABLE.equals(max.message()) ? max.message() : null, max.value());
            Rule[] result = {buildRule(rule, !NOT_APPLICABLE.equals(max.when()) ? max.when() : null)};
            return result;
        }
    }
}
