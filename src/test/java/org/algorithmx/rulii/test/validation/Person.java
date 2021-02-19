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

import org.algorithmx.rulii.core.Identifiable;
import org.algorithmx.rulii.validation.annotation.Validate;
import org.algorithmx.rulii.validation.rules.min.Min;
import org.algorithmx.rulii.validation.rules.notempty.NotEmpty;
import org.algorithmx.rulii.validation.rules.notnull.NotNull;
import org.algorithmx.rulii.validation.rules.pattern.Pattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Person implements Identifiable {

    @NotNull
    private String id;
    @Annotation2
    private String firstName;
    @Annotation5
    private String lastName;
    @Validate
    private Address address;
    @Annotation3
    private List<Car> cars;
    @Annotation1(integer = 4, fraction = 5) @Validate
    private Employment[] jobs;
    private Map<String, Map<@NotNull String, List<@Min(5) Integer>>> attributes;

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

    @Override
    public String getName() {
        return id;
    }

    public @NotNull String getFirstName() {
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

    public Map<@NotEmpty String, Map<@NotNull String, List<@Min(5) Integer>>> getAttributes() {
        return attributes;
    }

    public void add(@NotNull @Annotation5 @Pattern(regex = "[*]") Car car) {
        if (this.cars == null) this.cars = new ArrayList<>();
        this.cars.add(car);
    }

    public void setAttributes(Map<@NotEmpty String, Map<@NotNull String, List<@Min(5) Integer>>> attributes) {
        this.attributes = attributes;
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
