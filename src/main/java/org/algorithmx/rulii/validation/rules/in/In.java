package org.algorithmx.rulii.validation.rules.in;

import org.algorithmx.rulii.annotation.ValidationMarker;
import org.algorithmx.rulii.annotation.ValidationMarkerContainer;
import org.algorithmx.rulii.config.RuliiSystem;
import org.algorithmx.rulii.convert.Converter;
import org.algorithmx.rulii.convert.ConverterRegistry;
import org.algorithmx.rulii.core.rule.Rule;
import org.algorithmx.rulii.validation.AnnotatedRunnableBuilder;
import org.algorithmx.rulii.validation.Severity;
import org.algorithmx.rulii.validation.ValidationRuleException;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
@Repeatable(In.InList.class)
@ValidationMarker(In.InValidationRuleBuilder.class)
public @interface In {

    String NOT_APPLICABLE = "N/A";

    String errorCode() default InValidationRule.ERROR_CODE;

    String message() default NOT_APPLICABLE;

    Severity severity() default Severity.ERROR;

    String[] values();

    Class<?> type() default void.class;

    String when() default NOT_APPLICABLE;

    class InValidationRuleBuilder implements AnnotatedRunnableBuilder<In> {

        public InValidationRuleBuilder() {
            super();
        }

        @Override
        public Rule build(In in, String bindingName) {
            Set<Object> values = void.class.equals(in.type()) ?
                    new HashSet<>(Arrays.asList(in.values()))
                    : convertValues(in.values(), in.type(), RuliiSystem.getInstance().getConverterRegistry());
            InValidationRule rule = new InValidationRule(bindingName, in.errorCode(),
                    in.severity(), !NOT_APPLICABLE.equals(in.message()) ? in.message() : null, values);
            return buildRule(rule, !NOT_APPLICABLE.equals(in.when()) ? in.when() : null);
        }

        protected Set<Object> convertValues(String[] values, Class<?> type, ConverterRegistry registry) {
            Set<Object> result = new HashSet<>();

            Converter converter = registry.find(String.class, type);

            if (converter == null)
                throw new ValidationRuleException("Could not convert [" + Arrays.toString(values) + "] to an array of ["
                        + type + "]. ConverterRegistry does not have a converter that can convert from String to ["
                        + type + "]. Register a converter and try again.");

            for (String value : values) {
                result.add(converter.convert(value, type));
            }

            return result;
        }
    }

    @Target({FIELD, METHOD, CONSTRUCTOR, ANNOTATION_TYPE, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Inherited @Documented
    @ValidationMarkerContainer(In.class)
    @interface InList {
        In[] value();
    }
}
