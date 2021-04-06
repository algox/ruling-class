package org.algorithmx.rulii.validation.rules.in;

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
import java.util.HashSet;
import java.util.Set;

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
@Repeatable(InEnum.InList.class)
@ValidationMarker(InEnum.InEnumValidationRuleBuilder.class)
public @interface InEnum {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default InValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    Class<? extends Enum> value();

    String when() default NOT_APPLICABLE;

    class InEnumValidationRuleBuilder implements AnnotatedRunnableBuilder<InEnum> {

        public InEnumValidationRuleBuilder() {
            super();
        }

        @Override
        public Rule[] build(InEnum in, String bindingName) {
            Set<String> values = new HashSet<>();

            for (Enum e : in.value().getEnumConstants()) {
                values.add(e.name());
            }

            InValidationRule rule = new InValidationRule(bindingName, in.errorCode(),
                    in.severity(), !NOT_APPLICABLE.equals(in.message()) ? in.message() : null, values);
            Rule[] result = {buildRule(rule, !NOT_APPLICABLE.equals(in.when()) ? in.when() : null)};
            return result;
        }
    }

    @Target({FIELD, METHOD, CONSTRUCTOR, ANNOTATION_TYPE, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Inherited @Documented
    @ValidationMarkerContainer(InEnum.class)
    @interface InList {
        InEnum[] value();
    }
}
