package org.algorithmx.rules.validation.rules.url;

import org.algorithmx.rules.core.rule.RuleBuilder;
import org.algorithmx.rules.core.ruleset.RuleSetBuilder;
import org.algorithmx.rules.validation.Severity;
import org.algorithmx.rules.validation.SingleValueValidationRuleBuilder;
import org.algorithmx.rules.validation.ValidationRuleBuilder;

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
@ValidationRuleBuilder(Url.UrlValidationRuleBuilder.class)
public @interface Url {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default UrlValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    String[] schemes() default {};

    String[] hostPatterns() default {};

    class UrlValidationRuleBuilder implements SingleValueValidationRuleBuilder<Url> {

        public UrlValidationRuleBuilder() {
            super();
        }

        @Override
        public RuleSetBuilder build(Url url, RuleSetBuilder builder) {
            UrlValidationRule rule = new UrlValidationRule(url.errorCode(),
                    url.severity(),
                    !NOT_APPLICABLE.equals(url.message()) ? url.message() : null, url.schemes(), url.hostPatterns());
            // TODO : Need to adapt to the annotation element
            builder.rule(RuleBuilder.build(rule));
            return builder;
        }
    }
}
