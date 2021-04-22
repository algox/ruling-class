package org.algorithmx.rulii.validation.rules.alpha;

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
@Repeatable(Alpha.AlphaList.class)
@ValidationMarker(Alpha.AlphaValidationRuleBuilder.class)
public @interface Alpha {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default AlphaValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    boolean allowSpace() default true;

    String when() default NOT_APPLICABLE;

    class AlphaValidationRuleBuilder implements AnnotatedRunnableBuilder<Alpha> {

        public AlphaValidationRuleBuilder() {
            super();
        }

        @Override
        public Rule build(Alpha alpha, String bindingName) {
            AlphaValidationRule rule = new AlphaValidationRule(bindingName, alpha.errorCode(), alpha.severity(),
                    !NOT_APPLICABLE.equals(alpha.message()) ? alpha.message() : null, alpha.allowSpace());
            return buildRule(rule, !NOT_APPLICABLE.equals(alpha.when()) ? alpha.when() : null);
        }
    }

    @Target({FIELD, METHOD, CONSTRUCTOR, ANNOTATION_TYPE, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Inherited @Documented
    @ValidationMarkerContainer(Alpha.class)
    @interface AlphaList {
        Alpha[] value();
    }
}
