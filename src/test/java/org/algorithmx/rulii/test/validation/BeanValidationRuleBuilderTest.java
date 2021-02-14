package org.algorithmx.rulii.test.validation;

import org.algorithmx.rulii.validation.beans.BeanValidationRuleBuilder;
import org.algorithmx.rulii.validation.beans.BeanValidationRules;
import org.algorithmx.rulii.validation.beans.ValidationRuleAnnotationTraverser;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class BeanValidationRuleBuilderTest {

    public BeanValidationRuleBuilderTest() {
        super();
    }

    @Test
    public void test1() {
        BeanValidationRules rules = BeanValidationRuleBuilder.with(Person.class).build();
        System.err.println(rules);
    }

    @Test
    public void test3() throws Exception {
        Field field = TestClass.class.getField("field1");
        ValidationRuleAnnotationTraverser traverser = new ValidationRuleAnnotationTraverser();
        Annotation[] annotations = traverser.traverse(field);

        for (Annotation annotation : annotations) {
            System.err.println(annotation);
        }
    }
}
