package org.algorithmx.rulii.validation.rules.positive;

import org.algorithmx.rulii.annotation.ValidationMarker;
import org.algorithmx.rulii.core.rule.Rule;
import org.algorithmx.rulii.lib.spring.core.Ordered;
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
@ValidationMarker(Positive.PositiveValidationRuleBuilder.class)
public @interface Positive {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default PositiveValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    int order() default Ordered.LOWEST_PRECEDENCE;

    String when() default NOT_APPLICABLE;

    class PositiveValidationRuleBuilder implements AnnotatedRunnableBuilder<Positive> {

        public PositiveValidationRuleBuilder() {
            super();
        }

        @Override
        public Rule build(Positive positive, String bindingName) {
            PositiveValidationRule rule = new PositiveValidationRule(bindingName, positive.errorCode(),
                    positive.severity(), !NOT_APPLICABLE.equals(positive.message()) ? positive.message() : null);
            return buildRule(rule, positive.order(), !NOT_APPLICABLE.equals(positive.when()) ? positive.when() : null);
        }
    }
}
