package org.algorithmx.rulii.test.validation.objectgraph;

import org.algorithmx.rulii.test.validation.Address;
import org.algorithmx.rulii.test.validation.Car;
import org.algorithmx.rulii.test.validation.Employment;
import org.algorithmx.rulii.test.validation.Person;
import org.algorithmx.rulii.util.objectgraph.ObjectGraph;
import org.junit.Assert;
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
        Map<String, Map<String, List<Integer>>> attributes = new HashMap<>();
        Map<String, List<Integer>> key1 = new HashMap<>();
        key1.put("age", Arrays.asList(50));
        attributes.put("age", key1);
        Person result = new Person("person:1", "Michael", "Jordan", address1, cars, jobs, attributes);
        return result;
    }

    @Test
    public void test1() {
        TestObjectVisitor visitor = new TestObjectVisitor();
        // Set to be ordered fields so we have predictable traversal
        ObjectGraph graph = new ObjectGraph(true);
        Person person = createTestData1();
        graph.traverse(person, visitor);
        List<String> ids = visitor.getIdList();
        Assert.assertTrue(ids.size() == 8);
        Assert.assertTrue("person:1".equals(ids.get(0)));
        Assert.assertTrue("address:1".equals(ids.get(1)));
        Assert.assertTrue("car:1".equals(ids.get(2)));
        Assert.assertTrue("car:2".equals(ids.get(3)));
        Assert.assertTrue("employment:1".equals(ids.get(4)));
        Assert.assertTrue("employment:2".equals(ids.get(5)));
        Assert.assertTrue("address:2".equals(ids.get(6)));
        Assert.assertTrue("address:3".equals(ids.get(7)));
    }

    @Test
    public void test2() {
        TestObjectVisitor visitor = new TestObjectVisitor();
        // Set to be ordered fields so we have predictable traversal
        ObjectGraph graph = new ObjectGraph(false);
        Person person = createTestData1();
        graph.traverse(person, visitor);
    }
}
