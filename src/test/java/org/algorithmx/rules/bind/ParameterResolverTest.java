/**
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 2019, algorithmx.org (dev@algorithmx.org)
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
package org.algorithmx.rules.bind;

import org.algorithmx.rules.core.Condition;
import org.algorithmx.rules.core.Conditions;
import org.algorithmx.rules.model.ParameterDefinition;
import org.algorithmx.rules.util.LambdaUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Binding Parameter tests.
 *
 * @author Max Arulananthan
 */
public class ParameterResolverTest {

    public ParameterResolverTest() {
        super();
    }

    @Test @Ignore
    public void testBindableParameter1() throws NoSuchMethodException {
        Method m = TestClass.class.getDeclaredMethod("testMethod1", int.class, Map.class);
        ParameterDefinition[] parameters = ParameterDefinition.load(m);
        Assert.assertTrue(!parameters[0].isBinding() && parameters[0].getType().equals(int.class));
        Assert.assertTrue(!parameters[1].isBinding());
    }

    @Test @Ignore
    public void testBindableParameter2() throws NoSuchMethodException {
        Method m = TestClass.class.getDeclaredMethod("testMethod2", Binding.class);
        ParameterDefinition[] parameters = ParameterDefinition.load(m);
        Assert.assertTrue(parameters[0].isBinding() && parameters[0].getType().equals(Integer.class));
    }

    @Test @Ignore
    public void testBindableParameter3() throws NoSuchMethodException {
        Method m = TestClass.class.getDeclaredMethod("testMethod3", String.class, Integer.class, Binding.class);
        ParameterDefinition[] parameters = ParameterDefinition.load(m);
        Assert.assertTrue(!parameters[0].isBinding() && parameters[0].getType().equals(String.class));
        Assert.assertTrue(!parameters[1].isBinding() && parameters[1].getType().equals(Integer.class));
        Assert.assertTrue(parameters[2].isBinding() && parameters[2].getType().equals(
                new TypeReference<List<Integer>>() {}.getType()));
    }

    @Test
    public void testBindableParameter4() {
        Condition.Condition2<Integer, List<String>> x = Conditions.cond2((Integer a, List<String> b) -> a > 10);
        SerializedLambda lambda = LambdaUtils.getSerializedLambda(x);
        Class c = LambdaUtils.getImplementationClass(lambda);
        Method m = LambdaUtils.getImplementationMethod(lambda, c);
        ParameterDefinition.load(m);
    }

    static class TestClass {

        public void testMethod1(int a, Map<String, Integer> values) {}

        public boolean testMethod2(Binding<Integer> x) {
            return true;
        }

        public boolean testMethod3(String a, Integer b, Binding<List<Integer>> x) {
            return true;
        }
    }
}
