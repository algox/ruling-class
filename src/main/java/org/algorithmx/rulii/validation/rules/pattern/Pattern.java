package org.algorithmx.rulii.validation.rules.pattern;

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
@Repeatable(Pattern.PatternList.class)
@ValidationMarker(Pattern.PatternValidationRuleBuilder.class)
public @interface Pattern {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default PatternValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    /**
     * @return the regular expression to match
     */
    String regex();

    boolean caseSensitive() default true;

    String when() default NOT_APPLICABLE;

    class PatternValidationRuleBuilder implements AnnotatedRunnableBuilder<Pattern> {

        public PatternValidationRuleBuilder() {
            super();
        }

        @Override
        public Rule[] build(Pattern pattern, String bindingName, String path) {
            PatternValidationRule rule = new PatternValidationRule(bindingName, path, pattern.errorCode(), pattern.severity(),
                    !NOT_APPLICABLE.equals(pattern.message()) ? pattern.message() : null,
                    pattern.caseSensitive(), pattern.regex());
            Rule[] result = {buildRule(rule, !NOT_APPLICABLE.equals(pattern.when()) ? pattern.when() : null)};
            return result;
        }
    }

    @Target({FIELD, METHOD, CONSTRUCTOR, ANNOTATION_TYPE, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Inherited @Documented
    @ValidationMarkerContainer(Pattern.class)
    @interface PatternList {
        Pattern[] value();
    }
}
