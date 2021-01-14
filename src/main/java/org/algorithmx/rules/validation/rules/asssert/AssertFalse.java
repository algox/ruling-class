package org.algorithmx.rules.validation.rules.asssert;

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
@ValidationRuleBuilder(producer = AssertFalse.AssertFalseValidationRuleProducer.class)
public @interface AssertFalse {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default AssertFalseValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    class AssertFalseValidationRuleProducer implements ValidationRuleProducer<AssertFalse> {

        public AssertFalseValidationRuleProducer() {
            super();
        }

        @Override
        public void produce(AssertFalse assertFalse, RuleSetBuilder builder) {
            AssertFalseValidationRule rule = new AssertFalseValidationRule(assertFalse.errorCode(),
                    assertFalse.severity(), !NOT_APPLICABLE.equals(assertFalse.message()) ? assertFalse.message() : null);
            // TODO : Need to adapt to the annotation element
            builder.rule(RuleBuilder.build(rule));
        }

        @Override
        public Class<?>[] types() {
            return AssertFalseValidationRule.SUPPORTED_TYPES;
        }
    }
}
