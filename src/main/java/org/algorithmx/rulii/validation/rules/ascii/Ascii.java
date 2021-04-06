package org.algorithmx.rulii.validation.rules.ascii;

import org.algorithmx.rulii.core.rule.Rule;
import org.algorithmx.rulii.core.rule.RuleBuilder;
import org.algorithmx.rulii.validation.AnnotatedRunnableBuilder;
import org.algorithmx.rulii.validation.Severity;
import org.algorithmx.rulii.annotation.ValidationMarker;
import org.algorithmx.rulii.annotation.ValidationMarkerContainer;

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
@Repeatable(Ascii.AsciiList.class)
@ValidationMarker(Ascii.AsciiValidationRuleBuilder.class)
public @interface Ascii {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default AsciiValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    String when() default NOT_APPLICABLE;

    class AsciiValidationRuleBuilder implements AnnotatedRunnableBuilder<Ascii> {

        public AsciiValidationRuleBuilder() {
            super();
        }

        @Override
        public Rule[] build(Ascii ascii, String bindingName) {
            AsciiValidationRule rule = new AsciiValidationRule(bindingName, ascii.errorCode(), ascii.severity(),
                    !NOT_APPLICABLE.equals(ascii.message()) ? ascii.message() : null);
            Rule[] result = {buildRule(rule, !NOT_APPLICABLE.equals(ascii.when()) ? ascii.when() : null)};
            return result;
        }
    }

    @Target({FIELD, METHOD, CONSTRUCTOR, ANNOTATION_TYPE, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Inherited @Documented
    @ValidationMarkerContainer(Ascii.class)
    @interface AsciiList {
        Ascii[] value();
    }
}
