package org.algorithmx.rulii.validation.rules.alphnumeric;

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
@Repeatable(AlphaNumeric.AlphaNumericList.class)
@ValidationMarker(AlphaNumeric.AlphaNumericValidationRuleBuilder.class)
public @interface AlphaNumeric {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default AlphaNumericValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    boolean allowSpace() default true;

    String when() default NOT_APPLICABLE;

    class AlphaNumericValidationRuleBuilder implements AnnotatedRunnableBuilder<AlphaNumeric> {

        public AlphaNumericValidationRuleBuilder() {
            super();
        }

        @Override
        public Rule[] build(AlphaNumeric alpha, String bindingName, String path) {
            AlphaNumericValidationRule rule = new AlphaNumericValidationRule(bindingName, path, alpha.errorCode(),
                    alpha.severity(), !NOT_APPLICABLE.equals(alpha.message()) ? alpha.message() : null,
                    alpha.allowSpace());
            Rule[] result = {buildRule(rule, !NOT_APPLICABLE.equals(alpha.when()) ? alpha.when() : null)};
            return result;
        }
    }

    @Target({FIELD, METHOD, CONSTRUCTOR, ANNOTATION_TYPE, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Inherited @Documented
    @ValidationMarkerContainer(AlphaNumeric.class)
    @interface AlphaNumericList {
        AlphaNumeric[] value();
    }
}
