package org.algorithmx.rulii.validation.actions.bind;

import org.algorithmx.rulii.annotation.ValidationMarker;
import org.algorithmx.rulii.annotation.ValidationMarkerContainer;
import org.algorithmx.rulii.config.RuliiSystem;
import org.algorithmx.rulii.convert.Converter;
import org.algorithmx.rulii.convert.ConverterRegistry;
import org.algorithmx.rulii.core.action.Action;
import org.algorithmx.rulii.lib.spring.core.Ordered;
import org.algorithmx.rulii.validation.AnnotatedRunnableBuilder;
import org.algorithmx.rulii.validation.ValidationRuleException;

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
@Repeatable(Bind.BindList.class)
@ValidationMarker(Bind.BindActionBuilder.class)
public @interface Bind {

    String NOT_APPLICABLE = "N/A";

    String name();

    String value();

    Class<?> type() default void.class;

    int order() default Ordered.LOWEST_PRECEDENCE;

    class BindActionBuilder implements AnnotatedRunnableBuilder<Bind> {

        public BindActionBuilder() {
            super();
        }

        @Override
        public Action build(Bind bind, String bindingName) {
            Object value = void.class.equals(bind.type()) ?
                    bind.value() :
                    convertValues(bind.value(), bind.type(), RuliiSystem.getInstance().getConverterRegistry());
            return buildAction(new BindAction(bind.name(), value), bind.order());
        }

        protected Object convertValues(String value, Class<?> type, ConverterRegistry registry) {
            Object result;

            Converter converter = registry.find(String.class, type);

            if (converter == null)
                throw new ValidationRuleException("Could not convert [" + value + "] to ["
                        + type + "]. ConverterRegistry does not have a converter that can convert from String to ["
                        + type + "]. Register a converter and try again.");

            result = converter.convert(value, type);

            return result;
        }
    }

    @Target({FIELD, METHOD, CONSTRUCTOR, ANNOTATION_TYPE, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Inherited @Documented
    @ValidationMarkerContainer(Bind.class)
    @interface BindList {
        Bind[] value();
    }
}
