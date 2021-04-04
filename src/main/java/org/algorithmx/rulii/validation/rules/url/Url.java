package org.algorithmx.rulii.validation.rules.url;

import org.algorithmx.rulii.core.rule.Rule;
import org.algorithmx.rulii.core.rule.RuleBuilder;
import org.algorithmx.rulii.validation.AnnotatedRunnableBuilder;
import org.algorithmx.rulii.validation.Severity;
import org.algorithmx.rulii.annotation.ValidationRule;
import org.algorithmx.rulii.annotation.ValidationRuleContainer;

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
@Repeatable(Url.UrlList.class)
@ValidationRule(Url.UrlValidationRuleBuilder.class)
public @interface Url {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default UrlValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    String[] schemes() default {};

    String[] hostPatterns() default {};

    class UrlValidationRuleBuilder implements AnnotatedRunnableBuilder<Url> {

        public UrlValidationRuleBuilder() {
            super();
        }

        @Override
        public Rule[] build(Url url, String bindingName) {
            UrlValidationRule rule = new UrlValidationRule(bindingName, url.errorCode(), url.severity(),
                    !NOT_APPLICABLE.equals(url.message()) ? url.message() : null, url.schemes(), url.hostPatterns());
            Rule[] result = {RuleBuilder.build(rule)};
            return result;
        }
    }

    @Target({FIELD, METHOD, CONSTRUCTOR, ANNOTATION_TYPE, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Inherited @Documented
    @ValidationRuleContainer(Url.class)
    @interface UrlList {
        Url[] value();
    }
}
