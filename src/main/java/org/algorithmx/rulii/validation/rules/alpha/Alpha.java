package org.algorithmx.rulii.validation.rules.alpha;

import org.algorithmx.rulii.core.rule.Rule;
import org.algorithmx.rulii.core.rule.RuleBuilder;
import org.algorithmx.rulii.validation.BindingValidationRuleBuilder;
import org.algorithmx.rulii.validation.Severity;
import org.algorithmx.rulii.validation.annotation.ValidationRule;
import org.algorithmx.rulii.validation.annotation.ValidationRuleContainer;

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
@ValidationRule(Alpha.AlphaValidationRuleBuilder.class)
public @interface Alpha {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default AlphaValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    boolean includeSpace() default true;

    class AlphaValidationRuleBuilder implements BindingValidationRuleBuilder<Alpha> {

        public AlphaValidationRuleBuilder() {
            super();
        }

        @Override
        public Rule[] build(Alpha alpha, String bindingName) {
            AlphaValidationRule rule = new AlphaValidationRule(bindingName, alpha.errorCode(), alpha.severity(),
                    alpha.message(), alpha.includeSpace());
            Rule[] result = {RuleBuilder.build(rule)};
            return result;
        }
    }

    @Target({FIELD, METHOD, CONSTRUCTOR, ANNOTATION_TYPE, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Inherited @Documented
    @ValidationRuleContainer(Alpha.class)
    @interface AlphaList {
        Alpha[] value();
    }
}
