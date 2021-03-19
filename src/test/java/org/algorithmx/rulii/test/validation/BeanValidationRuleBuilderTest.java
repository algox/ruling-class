package org.algorithmx.rulii.test.validation;

import org.algorithmx.rulii.core.Identifiable;
import org.algorithmx.rulii.lib.spring.core.annotation.MergedAnnotations;
import org.algorithmx.rulii.lib.spring.core.annotation.RepeatableContainers;
import org.algorithmx.rulii.traverse.objectgraph.ObjectGraph;
import org.algorithmx.rulii.traverse.objectgraph.ObjectVisitor;
import org.algorithmx.rulii.annotation.ValidationRule;
import org.algorithmx.rulii.traverse.objectgraph.TraversalCandidate;
import org.algorithmx.rulii.validation.beans.BeanValidationRuleBuilder;
import org.algorithmx.rulii.validation.beans.BeanValidationRules;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class BeanValidationRuleBuilderTest {

    public BeanValidationRuleBuilderTest() {
        super();
    }

    //@Test
    public void test1() {
        List<String> ids = new ArrayList<>();

        ObjectVisitor visitor = new ObjectVisitor() {
            @Override
            public boolean visitObjectStart(TraversalCandidate candidate) {
                if (candidate.getTarget() instanceof Identifiable) ids.add(((Identifiable) candidate.getTarget()).getName());
                return true;
            }

        };

        ObjectGraph graph = new ObjectGraph();
        Person person = TestData.createPerson1();
        graph.traverse(person, null, visitor);

        Assert.assertTrue(ids.size() == 8);
        Assert.assertTrue(ids.contains("person:1"));
        Assert.assertTrue(ids.contains("address:1"));
        Assert.assertTrue(ids.contains("car:1"));
        Assert.assertTrue(ids.contains("car:2"));
        Assert.assertTrue(ids.contains("employment:1"));
        Assert.assertTrue(ids.contains("employment:2"));
        Assert.assertTrue(ids.contains("address:2"));
        Assert.assertTrue(ids.contains("address:3"));
    }

    @Test
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
    }
}
