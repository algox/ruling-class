/**
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 1999-2019, Live Software & Consultants Inc (rules@algorithmx.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.algorithmx.rules.bind.load;

import org.algorithmx.rules.bind.Bindings;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests for BindingLoader.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class BindingLoaderTest {

    public BindingLoaderTest() {
        super();
    }

    @Test
    public void propertyLoaderTest() {
        Athlete jordan = new Athlete("Michael", "Jordan", 23, new BigDecimal("100000000"));
        Bindings bindings = Bindings.create();
        bindings.bindUsing(new PropertyBindingLoader<>(), jordan);
        Assert.assertTrue(bindings.getValue("firstName").equals("Michael"));
        Assert.assertTrue(bindings.getValue("lastName").equals("Jordan"));
        Assert.assertTrue(bindings.getValue("age").equals(23));
        Assert.assertTrue(bindings.getValue("salary").equals(new BigDecimal("100000000")));
    }

    @Test
    public void fieldLoaderTest() {
        Athlete jordan = new Athlete("Michael", "Jordan", 23, new BigDecimal("100000000"));
        Bindings bindings = Bindings.create();
        bindings.bindUsing(new FieldBindingLoader<>(), jordan);
        Assert.assertTrue(bindings.getValue("firstName").equals("Michael"));
        Assert.assertTrue(bindings.getValue("lastName").equals("Jordan"));
        Assert.assertTrue(bindings.getValue("age").equals(23));
        Assert.assertTrue(bindings.getValue("salary").equals(new BigDecimal("100000000")));
    }

    @Test
    public void mapLoaderTest() {
        Map<String, Object> map = new HashMap<>();

        map.put("firstName", "Michael");
        map.put("lastName", "Jordan");
        map.put("age", 23);
        map.put("salary", new BigDecimal("100000000"));

        Bindings bindings = Bindings.create();
        bindings.bindUsing(new MapBindingLoader(), map);

        Assert.assertTrue(bindings.getValue("firstName").equals("Michael"));
        Assert.assertTrue(bindings.getValue("lastName").equals("Jordan"));
        Assert.assertTrue(bindings.getValue("age").equals(23));
        Assert.assertTrue(bindings.getValue("salary").equals(new BigDecimal("100000000")));
    }

    private static class Person {
        private String firstName;
        private String lastName;
        private int age;

        public Person(String firstName, String lastName, int age) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    private static class Athlete extends Person {
        private BigDecimal salary;

        public Athlete(String firstName, String lastName, int age, BigDecimal salary) {
            super(firstName, lastName, age);
            this.salary = salary;
        }

        public BigDecimal getSalary() {
            return salary;
        }

        public void setSalary(BigDecimal salary) {
            this.salary = salary;
        }

    }
}
