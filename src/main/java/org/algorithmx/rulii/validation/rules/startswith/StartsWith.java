package org.algorithmx.rulii.validation.rules.startswith;

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
@Inherited
@Documented
@ValidationMarker(StartsWith.StartsWithValidationRuleBuilder.class)
public @interface StartsWith {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default StartsWithValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    String[] prefixes();

    String when() default NOT_APPLICABLE;

    class StartsWithValidationRuleBuilder implements AnnotatedRunnableBuilder<StartsWith> {

        public StartsWithValidationRuleBuilder() {
            super();
        }

        @Override
        public Rule[] build(StartsWith startsWith, String bindingName, String path) {
            StartsWithValidationRule rule = new StartsWithValidationRule(bindingName, path, startsWith.errorCode(),
                    startsWith.severity(), !NOT_APPLICABLE.equals(startsWith.message()) ? startsWith.message() : null,
                    startsWith.prefixes());
            Rule[] result = {buildRule(rule, !NOT_APPLICABLE.equals(startsWith.when()) ? startsWith.when() : null)};
            return result;
        }
    }
}
