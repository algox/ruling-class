package org.algorithmx.rulii.validation.rules.numeric;

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
@Inherited
@Documented
@ValidationMarker(Numeric.NumericValidationRuleBuilder.class)
public @interface Numeric {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default NumericValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    boolean allowSpace() default false;

    String when() default NOT_APPLICABLE;

    class NumericValidationRuleBuilder implements AnnotatedRunnableBuilder<Numeric> {

        public NumericValidationRuleBuilder() {
            super();
        }

        @Override
        public Rule[] build(Numeric numeric, String bindingName, String path) {
            NumericValidationRule rule = new NumericValidationRule(bindingName, path, numeric.errorCode(),
                    numeric.severity(), !NOT_APPLICABLE.equals(numeric.message()) ? numeric.message() : null,
                    numeric.allowSpace());
            Rule[] result = {buildRule(rule, !NOT_APPLICABLE.equals(numeric.when()) ? numeric.when() : null)};
            return result;
        }
    }
}
