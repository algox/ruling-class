package org.algorithmx.rulii.validation.actions.rules;

import org.algorithmx.rulii.annotation.ValidationMarker;
import org.algorithmx.rulii.annotation.ValidationMarkerContainer;
import org.algorithmx.rulii.core.action.Action;
import org.algorithmx.rulii.lib.spring.core.Ordered;
import org.algorithmx.rulii.validation.AnnotatedRunnableBuilder;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.TYPE_PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, METHOD, CONSTRUCTOR, ANNOTATION_TYPE, PARAMETER, TYPE, TYPE_PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Inherited
@Documented
@ValidationMarker(Rules.RulesActionBuilder.class)
public @interface Rules {

    enum ADVICE {BEFORE, AFTER}

    String ruleSet();

    int order() default Ordered.LOWEST_PRECEDENCE;

    // TODO : Better name
    ADVICE advice() default ADVICE.AFTER;

    class RulesActionBuilder implements AnnotatedRunnableBuilder<Rules> {

        public RulesActionBuilder() {
            super();
        }

        @Override
        public Action build(Rules rules, String bindingName) {
            return buildAction(new RulesAction(rules.ruleSet()), rules.order());
        }
    }

    @Target({FIELD, METHOD, CONSTRUCTOR, ANNOTATION_TYPE, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Inherited @Documented
    @ValidationMarkerContainer(Rules.class)
    @interface RulesList {
        Rules[] value();
    }
}