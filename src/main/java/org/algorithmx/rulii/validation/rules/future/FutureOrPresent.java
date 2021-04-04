package org.algorithmx.rulii.validation.rules.future;

import org.algorithmx.rulii.core.rule.Rule;
import org.algorithmx.rulii.core.rule.RuleBuilder;
import org.algorithmx.rulii.validation.AnnotatedRunnableBuilder;
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
@ValidationRule(FutureOrPresent.FutureOrPresentValidationRuleBuilder.class)
public @interface FutureOrPresent {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default FutureOrPresentValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    class FutureOrPresentValidationRuleBuilder implements AnnotatedRunnableBuilder<FutureOrPresent> {

        public FutureOrPresentValidationRuleBuilder() {
            super();
        }

        @Override
        public Rule[] build(FutureOrPresent futureOrPresent, String bindingName) {
            FutureOrPresentValidationRule rule = new FutureOrPresentValidationRule(bindingName, futureOrPresent.errorCode(),
                    futureOrPresent.severity(),
                    !NOT_APPLICABLE.equals(futureOrPresent.message()) ? futureOrPresent.message() : null);
            Rule[] result = {RuleBuilder.build(rule)};
            return result;
        }
    }
}
