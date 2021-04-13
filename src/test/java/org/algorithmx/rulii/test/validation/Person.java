/*
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 1999-2021, Algorithmx Inc.
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

package org.algorithmx.rulii.test.validation;

import org.algorithmx.rulii.annotation.Validate;
import org.algorithmx.rulii.core.Identifiable;
import org.algorithmx.rulii.test.validation.objectgraph.Expression;
import org.algorithmx.rulii.test.validation.objectgraph.Or;
import org.algorithmx.rulii.validation.rules.min.Min;
import org.algorithmx.rulii.validation.rules.notempty.NotEmpty;
import org.algorithmx.rulii.validation.rules.notnull.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Person implements Identifiable {

    @NotNull
    private String id;
    @Annotation2
    private String firstName;
    @Annotation5
    private String lastName;
    @Expression(or = @Or) @Validate(using = "addressRules")
    private Address address;
    @Annotation3
    private List<Car> cars;
    @Annotation1(integer = 4, fraction = 5) //@Validate(using = "jobRules")
    private Employment[] jobs;
    @NotNull
    private Map<@NotNull String, Map<@NotNull String, List<@Min(5) Integer>>> attributes;

    //private Map<String, Map<String, List<Integer>>> attributes;

    public Person(String firstName, String lastName) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Person(String id, String firstName, String lastName, Address address, List<Car> cars,
                  Employment[] jobs, Map<@NotEmpty String, Map<@NotNull String, List<@Min(5) Integer>>> attributes) {
        super();
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.cars = cars;
        this.jobs = jobs;
        this.attributes = attributes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Address getAddress() {
        return address;
    }

    public List<Car> getCars() {
        return cars;
    }

    public Employment[] getJobs() {
        return jobs;
    }

    public Map<String, Map<String, List<Integer>>> getAttributes() {
        return attributes;
    }

    public void add(Car car) {
        if (this.cars == null) this.cars = new ArrayList<>();
        this.cars.add(car);
    }

    public void setAttributes(Map<String, Map<String, List<Integer>>> attributes) {
        this.attributes = attributes;
    }

    public static Person createPerson1() {
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

    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address=" + address +
                ", cars=" + cars +
                ", jobs=" + Arrays.toString(jobs) +
                ", attributes=" + attributes +
                '}';
    }
}
