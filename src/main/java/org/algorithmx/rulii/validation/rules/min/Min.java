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
@ValidationMarker(Min.MinValidationRuleBuilder.class)
public @interface Min {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default MinValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    long value();

    String when() default NOT_APPLICABLE;

    class MinValidationRuleBuilder implements AnnotatedRunnableBuilder<Min> {

        public MinValidationRuleBuilder() {
            super();
        }

        @Override
        public Rule build(Min min, String bindingName) {
            MinValidationRule rule = new MinValidationRule(bindingName, min.errorCode(), min.severity(),
                    !NOT_APPLICABLE.equals(min.message()) ? min.message() : null, min.value());
            return buildRule(rule, !NOT_APPLICABLE.equals(min.when()) ? min.when() : null);
        }
    }
}
