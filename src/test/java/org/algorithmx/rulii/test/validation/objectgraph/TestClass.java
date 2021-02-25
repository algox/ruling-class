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

package org.algorithmx.rulii.test.validation.objectgraph;

import org.algorithmx.rulii.test.validation.Car;
import org.algorithmx.rulii.test.validation.Person;
import org.algorithmx.rulii.validation.rules.notempty.NotEmpty;
import org.algorithmx.rulii.validation.rules.notnull.NotNull;

import java.util.HashMap;
import java.util.Map;

public class TestClass {

    private Map<String, Person> anotherMap = new HashMap<>();
    private Map<@NotEmpty String, Map<@NotNull Person, @NotNull Car[]>> map = new HashMap<>();

    public TestClass() {
        super();
    }

    public Map<String, Map<Person, Car[]>> getMap() {
        return map;
    }

    public void setMap(Map<String, Map<Person, Car[]>> map) {
        this.map = map;
    }

    public static TestClass create() {
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
}
