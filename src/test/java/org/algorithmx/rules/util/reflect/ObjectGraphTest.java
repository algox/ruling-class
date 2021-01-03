package org.algorithmx.rules.util.reflect;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectGraphTest {

    private static Person createTestData1() {
        Address address1 = new Address("address:1","1", "Jordan way", "Chicago", "IL", "USA");
        Car car1 = new Car("car:1","Ferrari ", "275 GTB", 1964);
        Car car2 = new Car("car:2","Tesla ", "Model S", 2020);
        List<Car> cars = Arrays.asList(car1, car2);
        Address address2 = new Address("address:2", "3431", "Somewhere in Charlotte", "Charlotte", "NC",
                "USA");
        Address address3 = new Address("address:3", "1", "Oregon Way", "Oregon", "OR",
                "USA");
        Employment[] jobs = new Employment[2];
        jobs[0] = new Employment("employment:1", "Charlotte Hornets", new BigDecimal("50000000"), address2);
        jobs[1] = new Employment("employment:2", "Jordan Brand", new BigDecimal("250000000"), address3);
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("age", 50);
        attributes.put("title", "GOAT");
        attributes.put("jobs", jobs);
        Person result = new Person("person:1", "Michael", "Jordan", address1, cars, jobs, attributes);
        return result;
    }

    //@Test
    public void test1() {
        TestObjectVisitor visitor = new TestObjectVisitor();
        ObjectGraph graph = new ObjectGraph();
        Person person = createTestData1();

        graph.traverse(person, visitor);
    }
}
