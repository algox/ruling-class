package org.algorithmx.rulii.test.validation.objectgraph;

import org.algorithmx.rulii.test.validation.Person;
import org.algorithmx.rulii.test.validation.TestData;
import org.algorithmx.rulii.util.objectgraph.ObjectGraph;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ObjectGraphTest {

    @Test
    public void test1() {
        TestObjectVisitor visitor = new TestObjectVisitor();
        ObjectGraph graph = new ObjectGraph();
        Person person = TestData.createPerson1();
        graph.traverse(person, visitor);
        List<String> ids = visitor.getIdList();

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
        TestObjectVisitor visitor = new TestObjectVisitor();
        // Set to be ordered fields so we have predictable traversal
        ObjectGraph graph = new ObjectGraph();
        Person person = TestData.createPerson1();
        graph.traverse(person, visitor);
    }
}
