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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TestData {

    public static Person createPerson1() {
        Address address1 = new Address("address:1","1", "Jordan way", "Chicago", "IL", "USA");
        Car car1 = new Car("car:1","Ferrari ", "275 GTB", 1964);
        Car car2 = new Car("car:2","Tesla ", "Model S", 2020);
        List<Car> cars = Arrays.asList(car1, car2);
        Address address2 = new Address("address:2", "3431", "Somewhere in Charlotte", "Charlotte", "NC",
                "USA");
        Address address3 = new Address("address:3", "1", "Oregon Way", "Oregon", "OR",
                "USA");
        Employment[] jobs = new Employment[3];
        jobs[0] = new Employment("employment:1", "Charlotte Hornets", new BigDecimal("50000000"), address2);
        jobs[1] = new Employment("employment:2", "Jordan Brand", new BigDecimal("250000000"), address3);
        jobs[2] = new Employment("employment:3", "Nascar", new BigDecimal("21000000"), address1);
        Map<String, Map<String, List<Integer>>> attributes = new HashMap<>();
        Map<String, List<Integer>> key1 = new HashMap<>();
        key1.put("age", Arrays.asList(50));
        attributes.put("value", key1);
        Person result = new Person("person:1", "Michael", "Jordan", address1, cars, jobs, attributes);
        return result;
    }

}
