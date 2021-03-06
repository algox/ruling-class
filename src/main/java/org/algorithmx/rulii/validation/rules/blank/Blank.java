package org.algorithmx.rulii.validation.rules.blank;

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
@ValidationMarker(Blank.BlankValidationRuleBuilder.class)
public @interface Blank {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default BlankValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    int order() default Ordered.LOWEST_PRECEDENCE;

    String when() default NOT_APPLICABLE;

    class BlankValidationRuleBuilder implements AnnotatedRunnableBuilder<Blank> {

        public BlankValidationRuleBuilder() {
            super();
        }

        @Override
        public Rule build(Blank blank, String bindingName) {
            BlankValidationRule rule = new BlankValidationRule(bindingName, blank.errorCode(),
                    blank.severity(), !NOT_APPLICABLE.equals(blank.message()) ? blank.message() : null);
            return buildRule(rule, blank.order(), !NOT_APPLICABLE.equals(blank.when()) ? blank.when() : null);
        }
    }
}
