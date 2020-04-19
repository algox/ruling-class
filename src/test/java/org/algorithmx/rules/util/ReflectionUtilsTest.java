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

import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.core.condition.TriCondition;
import org.algorithmx.rules.util.reflect.ReflectionUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.PostConstruct;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Test cases related to ReflectionUtils.
 *
 * @author Max Arulananthan
 */
public class ReflectionUtilsTest {

    public ReflectionUtilsTest() {
        super();
    }

    @Test
    public void parameterNamesTest1() throws NoSuchMethodException {
        Method m = SomeClass.class.getDeclaredMethod("testMethod", String.class, Integer.class, List.class);
        Assert.assertTrue(m != null);
        String[] parameterNames = ReflectionUtils.getParameterNames(m);
        Assert.assertTrue(parameterNames.length == 3);
        Assert.assertTrue("a".equals(parameterNames[0]));
        Assert.assertTrue("b".equals(parameterNames[1]));
        Assert.assertTrue("c".equals(parameterNames[2]));
    }

    @Test
    public void parameterNamesTest2() throws NoSuchMethodException {
        TriCondition<Integer, String, List<Float>> lambda = (Integer a, String b, List<Float> c) -> a > 100;
        SerializedLambda serializedLambda = LambdaUtils.getSafeSerializedLambda(lambda);
        Assert.assertTrue(serializedLambda != null);
        Class<?> c = LambdaUtils.getImplementationClass(serializedLambda);
        Assert.assertTrue(c != null);
        Method m = LambdaUtils.getImplementationMethod(serializedLambda, c);
        String[] parameterNames = ReflectionUtils.getParameterNames(m);
        Assert.assertTrue(parameterNames.length == 3);
        Assert.assertTrue("a".equals(parameterNames[0]));
        Assert.assertTrue("b".equals(parameterNames[1]));
        Assert.assertTrue("c".equals(parameterNames[2]));
    }

    @Test
    public void postConstructorTest1() {
        Method postConstructor = ReflectionUtils.getPostConstructMethods(SomeClass.class);
        Assert.assertTrue(postConstructor != null);
        ReflectionUtils.invokePostConstruct(postConstructor, new SomeClass());
    }

    @Test(expected = UnrulyException.class)
    public void postConstructorTest2() {
        // 2 PostConstructors
        ReflectionUtils.getPostConstructMethods(OtherClass.class);
    }

    @Test
    public void postConstructorTest3() {
        Method postConstructor = ReflectionUtils.getPostConstructMethods(ErrorClass.class);
        Assert.assertTrue(postConstructor == null);
    }

    private static class SomeClass {

        public SomeClass() {
            super();
        }

        @PostConstruct
        private void init() {
            //
        }

        public void testMethod(String a, Integer b, List<Float> c) {
            // test
        }
    }

    private static class OtherClass {

        public OtherClass() {
            super();
        }

        @PostConstruct
        private void init1() {
            //
        }

        @PostConstruct
        private void init2() {
            //
        }
    }

    private static class ErrorClass {

        public ErrorClass() {
            super();
        }

        @PostConstruct
        private int init1() {
            return 0;
        }

        @PostConstruct
        private void init2(int x) {
            //
        }

        @PostConstruct
        private void init3() throws Exception {
            //
        }
    }
}
