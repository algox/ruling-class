package org.algorithmx.rulii.validation.rules.script;

import org.algorithmx.rulii.annotation.ValidationMarker;
import org.algorithmx.rulii.annotation.ValidationMarkerContainer;
import org.algorithmx.rulii.core.rule.Rule;
import org.algorithmx.rulii.lib.spring.core.Ordered;
import org.algorithmx.rulii.validation.AnnotatedRunnableBuilder;
import org.algorithmx.rulii.validation.Severity;
import org.algorithmx.rulii.validation.rules.size.SizeValidationRule;

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
@Repeatable(ScriptAssert.ScriptRuleList.class)
@ValidationMarker(ScriptAssert.ScriptRuleRuleBuilder.class)
public @interface ScriptAssert {

    String NOT_APPLICABLE = "N/A";

    String value();

    String errorCode() default SizeValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    int order() default Ordered.LOWEST_PRECEDENCE;

    String when() default NOT_APPLICABLE;

    class ScriptRuleRuleBuilder implements AnnotatedRunnableBuilder<ScriptAssert> {

        public ScriptRuleRuleBuilder() {
            super();
        }

        @Override
        public Rule build(ScriptAssert scriptRule, String bindingName) {
            ScriptAssertRule rule = new ScriptAssertRule(bindingName, scriptRule.value(),
                    scriptRule.errorCode(), scriptRule.severity(),
                    !NOT_APPLICABLE.equals(scriptRule.message()) ? scriptRule.message() : null);
            return buildRule(rule, scriptRule.order(), !NOT_APPLICABLE.equals(scriptRule.when()) ? scriptRule.when() : null);
        }
    }

    @Target({FIELD, METHOD, CONSTRUCTOR, ANNOTATION_TYPE, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Inherited @Documented
    @ValidationMarkerContainer(ScriptAssert.class)
    @interface ScriptRuleList {
        ScriptAssert[] value();
    }
}
