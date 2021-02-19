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

import org.algorithmx.rulii.validation.RuleViolations;
import org.algorithmx.rulii.validation.beans.BeanValidator;
import org.junit.Test;

import java.math.BigDecimal;

public class BeanValidatorTest {

    public BeanValidatorTest() {
        super();
    }

    @Test
    public void test1() {
        BeanValidator validator = BeanValidator.create();
        TestClass testClass = new TestClass();
        testClass.setField1("Hello World!");
        testClass.setField2("");
        testClass.setField3(-100.00);
        testClass.setField4(new BigDecimal("100000.00"));
        RuleViolations violations = validator.validate(testClass);
        System.err.println(violations);
    }
}
