package org.algorithmx.rulii.test.validation;

import org.algorithmx.rulii.lib.spring.core.annotation.AnnotationUtils;
import org.algorithmx.rulii.lib.spring.core.annotation.MergedAnnotations;
import org.algorithmx.rulii.lib.spring.core.annotation.RepeatableContainers;
import org.algorithmx.rulii.validation.annotation.ValidationRule;
import org.algorithmx.rulii.validation.beans.BeanValidationRuleBuilder;
import org.algorithmx.rulii.validation.beans.BeanValidationRules;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BeanValidationRuleBuilderTest {

    public BeanValidationRuleBuilderTest() {
        super();
    }

    @Test
    public void test1() {
        BeanValidationRules rules = BeanValidationRuleBuilder.with(Person.class)
                .loadAnnotatedFields()
                .loadAnnotatedMethods()
                .build();
        System.err.println(rules);
    }

    @Test
    public void test2() throws NoSuchFieldException {
        Field field = Person.class.getDeclaredField("attributes");
        AnnotatedType at = field.getAnnotatedType();
        System.out.println(field.getName() + " : " + formatType(at));
    }

    private static String formatType(AnnotatedType type) {
        StringBuilder sb = new StringBuilder();
        Annotation[] annotations = AnnotationUtils.getAnnotations(type);
        for (Annotation a : annotations) sb.append(a).append(' ');

        if (type instanceof AnnotatedParameterizedType) {
            AnnotatedParameterizedType apt = (AnnotatedParameterizedType) type;
            sb.append(((ParameterizedType)type.getType()).getRawType().getTypeName());
            sb.append(Stream.of(apt.getAnnotatedActualTypeArguments())
                    .map(BeanValidationRuleBuilderTest::formatType).collect(Collectors.joining(",", "<", ">")));
        } else sb.append(type.getType().getTypeName());

        return sb.toString();
    }

    @Test
    public void test3() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("field4");

        MergedAnnotations.from(field, MergedAnnotations.SearchStrategy.DIRECT).stream(ValidationRule.class)
                .forEach(a -> System.err.println(a.getMetaSource().synthesize()));
    }

    @Test
    public void test4() throws NoSuchMethodException {
        Method method = Person.class.getDeclaredMethod("add", Car.class);

        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        for (int i = 0; i < parameterAnnotations.length; i++) {
            MergedAnnotations.from(method, parameterAnnotations[i], RepeatableContainers.standardRepeatables())
                    .stream(ValidationRule.class).forEach(a -> System.err.println(a.getMetaSource().synthesize()));
        }
    }
}
