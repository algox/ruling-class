package org.algorithmx.rulii.validation.rules.future;

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
@Inherited @Documented
@ValidationMarker(Future.FutureValidationRuleBuilder.class)
public @interface Future {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default FutureValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    String when() default NOT_APPLICABLE;

    class FutureValidationRuleBuilder implements AnnotatedRunnableBuilder<Future> {

        public FutureValidationRuleBuilder() {
            super();
        }

        @Override
        public Rule[] build(Future future, String bindingName, String path) {
            FutureValidationRule rule = new FutureValidationRule(bindingName, path,
                    future.errorCode(), future.severity(),
                    !NOT_APPLICABLE.equals(future.message()) ? future.message() : null);
            Rule[] result = {buildRule(rule, !NOT_APPLICABLE.equals(future.when()) ? future.when() : null)};
            return result;
        }
    }
}
