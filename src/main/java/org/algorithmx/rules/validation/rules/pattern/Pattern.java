package org.algorithmx.rules.validation.rules.pattern;

import org.algorithmx.rules.core.rule.RuleBuilder;
import org.algorithmx.rules.core.ruleset.RuleSetBuilder;
import org.algorithmx.rules.validation.Severity;
import org.algorithmx.rules.validation.ValidationRuleBuilder;
import org.algorithmx.rules.validation.ValidationRuleProducer;

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
@ValidationRuleBuilder(producer = Pattern.PatternValidationRuleProducer.class)
public @interface Pattern {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default PatternValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    /**
     * @return the regular expression to match
     */
    String[] patterns();

    boolean caseSensitive() default true;

    class PatternValidationRuleProducer implements ValidationRuleProducer<Pattern> {

        public PatternValidationRuleProducer() {
            super();
        }

        @Override
        public void produce(Pattern pattern, RuleSetBuilder builder) {
            PatternValidationRule rule = new PatternValidationRule(pattern.errorCode(), pattern.severity(),
                    !NOT_APPLICABLE.equals(pattern.message()) ? pattern.message() : null,
                    pattern.caseSensitive(), pattern.patterns());
            // TODO : Need to adapt to the annotation element
            builder.rule(RuleBuilder.build(rule));
        }

        @Override
        public Class<?>[] types() {
            return PatternValidationRule.SUPPORTED_TYPES;
        }
    }
}
