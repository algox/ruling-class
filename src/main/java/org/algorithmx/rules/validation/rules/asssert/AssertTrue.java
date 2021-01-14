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
@ValidationRuleBuilder(producer = AssertTrue.AssertTrueValidationRuleProducer.class)
public @interface AssertTrue {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default AssertTrueValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    class AssertTrueValidationRuleProducer implements ValidationRuleProducer<AssertTrue> {

        public AssertTrueValidationRuleProducer() {
            super();
        }

        @Override
        public void produce(AssertTrue assertTrue, RuleSetBuilder builder) {
            AssertTrueValidationRule rule = new AssertTrueValidationRule(assertTrue.errorCode(),
                    assertTrue.severity(), !NOT_APPLICABLE.equals(assertTrue.message()) ? assertTrue.message() : null);
            // TODO : Need to adapt to the annotation element
            builder.rule(RuleBuilder.build(rule));
        }

        @Override
        public Class<?>[] types() {
            return AssertTrueValidationRule.SUPPORTED_TYPES;
        }
    }
}
