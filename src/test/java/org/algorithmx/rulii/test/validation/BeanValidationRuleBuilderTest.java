package org.algorithmx.rulii.test.validation;

import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.core.context.RuleContextBuilder;
import org.algorithmx.rulii.validation.beans.BeanHolder;
import org.algorithmx.rulii.validation.beans.BeanValidator;
import org.junit.Test;

public class BeanValidationRuleBuilderTest {

    public BeanValidationRuleBuilderTest() {
        super();
    }

    @Test
    public void test1() {
        Person person = TestData.createPerson1();
        RuleContext context = RuleContextBuilder.empty();
        BeanValidator validator = new BeanValidator();
        validator.validateBean(context, person, new BeanHolder(person, null));
    }

    /*@Test
    public void test2() {
        BeanValidationRules rules = BeanValidationRuleBuilder.with(Person.class)
                .loadAnnotatedFields()
                .loadAnnotatedMethods()
                .build();
        System.err.println(rules);
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
    }*/
}
