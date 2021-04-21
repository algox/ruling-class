package org.algorithmx.rulii.validation.rules.digits;

import org.algorithmx.rulii.annotation.ValidationMarker;
import org.algorithmx.rulii.annotation.ValidationMarkerContainer;
import org.algorithmx.rulii.core.rule.Rule;
import org.algorithmx.rulii.validation.AnnotatedRunnableBuilder;
import org.algorithmx.rulii.validation.Severity;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
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
@Repeatable(Digits.DigitsList.class)
@ValidationMarker(Digits.DigitsValidationRuleBuilder.class)
public @interface Digits {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default DigitsValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    /**
     * @return maximum number of integral digits accepted for this number
     */
    int integer();

    /**
     * @return maximum number of fractional digits accepted for this number
     */
    int fraction();

    String when() default NOT_APPLICABLE;

    class DigitsValidationRuleBuilder implements AnnotatedRunnableBuilder<Digits> {

        public DigitsValidationRuleBuilder() {
            super();
        }

        @Override
        public Rule[] build(Digits digits, String bindingName) {
            DigitsValidationRule rule = new DigitsValidationRule(bindingName, digits.errorCode(),
                    digits.severity(), !NOT_APPLICABLE.equals(digits.message()) ? digits.message() : null,
                    digits.integer(), digits.fraction());
            Rule[] result = {buildRule(rule, !NOT_APPLICABLE.equals(digits.when()) ? digits.when() : null)};
            return result;
        }
    }

    @Target({FIELD, METHOD, CONSTRUCTOR, ANNOTATION_TYPE, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Inherited @Documented
    @ValidationMarkerContainer(Digits.class)
    @interface DigitsList {
        Digits[] value();
    }
}
