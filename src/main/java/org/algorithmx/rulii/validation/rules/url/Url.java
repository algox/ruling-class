package org.algorithmx.rulii.validation.rules.url;

import org.algorithmx.rulii.annotation.ValidationMarker;
import org.algorithmx.rulii.annotation.ValidationMarkerContainer;
import org.algorithmx.rulii.core.rule.Rule;
import org.algorithmx.rulii.lib.spring.core.Ordered;
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
@Repeatable(Url.UrlList.class)
@ValidationMarker(Url.UrlValidationRuleBuilder.class)
public @interface Url {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default UrlValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    String[] schemes() default {};

    String[] hostPatterns() default {};

    int order() default Ordered.LOWEST_PRECEDENCE;

    String when() default NOT_APPLICABLE;

    class UrlValidationRuleBuilder implements AnnotatedRunnableBuilder<Url> {

        public UrlValidationRuleBuilder() {
            super();
        }

        @Override
        public Rule build(Url url, String bindingName) {
            UrlValidationRule rule = new UrlValidationRule(bindingName, url.errorCode(), url.severity(),
                    !NOT_APPLICABLE.equals(url.message()) ? url.message() : null, url.schemes(), url.hostPatterns());
            return buildRule(rule, url.order(), !NOT_APPLICABLE.equals(url.when()) ? url.when() : null);
        }
    }

    @Target({FIELD, METHOD, CONSTRUCTOR, ANNOTATION_TYPE, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Inherited @Documented
    @ValidationMarkerContainer(Url.class)
    @interface UrlList {
        Url[] value();
    }
}
