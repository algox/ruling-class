package org.algorithmx.rulii.validation.rules.past;

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
@ValidationMarker(PastOrPresent.PastOrPresentValidationRuleBuilder.class)
public @interface PastOrPresent {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default PastOrPresentValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    int order() default Ordered.LOWEST_PRECEDENCE;

    String when() default NOT_APPLICABLE;

    class PastOrPresentValidationRuleBuilder implements AnnotatedRunnableBuilder<PastOrPresent> {

        public PastOrPresentValidationRuleBuilder() {
            super();
        }

        @Override
        public Rule build(PastOrPresent pastOrPresent, String bindingName) {
            PastOrPresentValidationRule rule = new PastOrPresentValidationRule(bindingName, pastOrPresent.errorCode(),
                    pastOrPresent.severity(), !NOT_APPLICABLE.equals(pastOrPresent.message()) ? pastOrPresent.message() : null);
            return buildRule(rule, pastOrPresent.order(), !NOT_APPLICABLE.equals(pastOrPresent.when()) ? pastOrPresent.when() : null);
        }
    }
}
