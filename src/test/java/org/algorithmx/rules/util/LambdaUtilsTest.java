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
package org.algorithmx.rules.util;

import org.algorithmx.rules.core.condition.TriCondition;
import org.algorithmx.rules.core.condition.UnaryCondition;
import org.algorithmx.rules.lib.spring.core.DefaultParameterNameDiscoverer;
import org.algorithmx.rules.lib.spring.core.ParameterNameDiscoverer;
import org.junit.Assert;
import org.junit.Test;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.math.BigDecimal;

/**
 * Test cases related to LambdaUtils.
 *
 * @author Max Arulananthan
 */
public final class LambdaUtilsTest {

    public LambdaUtilsTest() {
        super();
    }

    @Test
    public void test1() {
        UnaryCondition<Integer> rule1 = (Integer x) -> x > 10;

        UnaryCondition<Integer> rule2 = new UnaryCondition<Integer>() {
            static final long serialVersionUID = 0L;

            @Override
            public boolean isPass(Integer value) {
                return false;
            }
        };

        Assert.assertTrue(LambdaUtils.isLambda(rule1));
        Assert.assertTrue(!LambdaUtils.isLambda(rule2));

        SerializedLambda lambda = LambdaUtils.getSerializedLambda(rule1);
        Class<?> implementationClass = LambdaUtils.getImplementationClass(lambda);
        Method implementationMethod = LambdaUtils.getImplementationMethod(lambda, implementationClass);

        Assert.assertTrue(implementationMethod.getName().equals(lambda.getImplMethodName()));
        Assert.assertTrue(implementationMethod.getReturnType().equals(boolean.class));
        Assert.assertTrue(implementationMethod.getParameterTypes()[0].equals(Integer.class));
    }

    @Test
    public void test2() {
        TriCondition<Integer, String, BigDecimal> rule3 = (Integer xxx, String value, BigDecimal salary) -> xxx > 10 && salary != null;
        SerializedLambda lambda = LambdaUtils.getSerializedLambda(rule3);
        Class<?> implementationClass = LambdaUtils.getImplementationClass(lambda);
        Method implementationMethod = LambdaUtils.getImplementationMethod(lambda, implementationClass);

        ParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
        String[] names = discoverer.getParameterNames(implementationMethod);

        Assert.assertTrue(names.length == 3);
        Assert.assertTrue(names[0].equals("xxx"));
        Assert.assertTrue(names[1].equals("value"));
        Assert.assertTrue(names[2].equals("salary"));
    }

    @Test
    public void test3() throws NoSuchMethodException {

        TriCondition<Integer, String, BigDecimal> rule3 = new TriCondition<Integer, String, BigDecimal>() {

            static final long serialVersionUID = 0L;

            @Override
            public boolean isPass(Integer xxx, String value, BigDecimal salary) {
                return false;
            }
        };

        Method implementationMethod = rule3.getClass().getDeclaredMethod("isPass", Integer.class, String.class, BigDecimal.class);
        ParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
        String[] names = discoverer.getParameterNames(implementationMethod);

        Assert.assertTrue(names.length == 3);
        Assert.assertTrue(names[0].equals("xxx"));
        Assert.assertTrue(names[1].equals("value"));
        Assert.assertTrue(names[2].equals("salary"));
    }
}
