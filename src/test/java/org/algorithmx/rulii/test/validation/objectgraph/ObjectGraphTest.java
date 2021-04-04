package org.algorithmx.rulii.test.validation.objectgraph;

import org.algorithmx.rulii.validation.graph.ObjectGraph;
import org.algorithmx.rulii.validation.graph.TraversalCandidate;
import org.algorithmx.rulii.lib.spring.core.annotation.AnnotationUtils;
import org.algorithmx.rulii.lib.spring.core.annotation.MergedAnnotations;
import org.algorithmx.rulii.test.validation.Car;
import org.algorithmx.rulii.test.validation.Person;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ObjectGraphTest {

    @Test
    public void test2() {
        TestObjectVisitor visitor = new TestObjectVisitor();
        // Set to be ordered fields so we have predictable traversal
        ObjectGraph graph = new ObjectGraph();
        graph.traverse(new TraversalCandidate(create(), null), visitor);
    }

    private static TestClass create() {
        TestClass result = new TestClass();
        Map<String, Map<Person, Car[]>> map = new HashMap<>();

        Person person = new Person("Kobe", "Bryant");
        person.setId("person1");

        Car car1 = new Car("car1", "Honda", "Accord", 1991);
        Car car2 = new Car("car2", "Infiniti", "QX4", 2001);
        Car[] cars = new Car[2];
        cars[0] = car1;
        cars[1] = car2;
        Map<Person, Car[]> personMap = new HashMap<>();
        personMap.put(person, cars);
        map.put("person1", personMap);
        result.setMap(map);

        return result;
    }

    @Test
    public void test3() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("anotherMap");
        AnnotatedType at = field.getAnnotatedType();
        formatType(at);
    }

    private static String formatType(AnnotatedType type) {
        System.err.println(type.getClass() + " : " + type.getType());
        StringBuilder sb = new StringBuilder();
        Annotation[] annotations = AnnotationUtils.getAnnotations(type);
        for (Annotation a : annotations) sb.append(a).append(' ');

        if (type instanceof AnnotatedParameterizedType) {
            AnnotatedParameterizedType apt = (AnnotatedParameterizedType) type;
            sb.append(((ParameterizedType)type.getType()).getRawType().getTypeName());
            sb.append(Stream.of(apt.getAnnotatedActualTypeArguments())
                    .map(ObjectGraphTest::formatType).collect(Collectors.joining(",\n", "<", ">")));
        } else sb.append(type.getType().getTypeName());

        return sb.toString();
    }

    @Test
    public void test4() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("map");
        Annotation[] annotations = AnnotationUtils.getAnnotations(field.getAnnotatedType());
        MergedAnnotations.from(field.getAnnotatedType(), MergedAnnotations.SearchStrategy.DIRECT)
                .stream()
                .forEach(a -> System.err.println(a.synthesize()));
    }
}
