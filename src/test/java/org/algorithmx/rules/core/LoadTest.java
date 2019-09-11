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
package org.algorithmx.rules.core;

import org.algorithmx.rules.bind.TypeReference;
import org.algorithmx.rules.model.ActionDefinition;
import org.algorithmx.rules.model.RuleDefinition;
import org.algorithmx.rules.util.LambdaUtils;
import org.junit.Assert;
import org.junit.Test;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import static org.algorithmx.rules.core.Conditions.*;
import static org.algorithmx.rules.core.Actions.*;
/**
 * Tests for loading definitions.
 *
 * @author Max Arulananthan
 */
public class LoadTest {

    public LoadTest() {
        super();
    }

    @Test
    public void loadTest1() {
        RuleDefinition ruleDef = RuleDefinition.load(TestRule1.class);

        Assert.assertTrue("TestRule".equals(ruleDef.getName()));
        Assert.assertTrue("Test Description 1".equals(ruleDef.getDescription()));

        ActionDefinition[] actionDef = ActionDefinition.loadThenActions(TestRule1.class);
        Assert.assertTrue(actionDef != null && actionDef.length == 1);
    }

    @Test
    public void loadTest2() throws NoSuchMethodException {
        RuleDefinition def = RuleDefinition.load(TestRule1.class);

        Method m = TestRule1.class.getDeclaredMethod("when", int.class, Date.class, List.class);

        Assert.assertTrue("TestRule".equals(def.getName()));
        Assert.assertTrue(def.getCondition() != null);
        Assert.assertTrue(def.getCondition().getMethod().equals(m));
    }

    @Test
    public void loadTest3() {
        RuleDefinition def = RuleDefinition.load(TestRule1.class);

        Assert.assertTrue(def.getCondition().getParameterDefinitions().length == 3);

        Assert.assertTrue(def.getCondition().getParameterDefinitions()[0].getIndex() == 0);
        Assert.assertTrue(def.getCondition().getParameterDefinitions()[0].getName().equals("id"));
        Assert.assertTrue(def.getCondition().getParameterDefinitions()[0].getType().equals(int.class));
        Assert.assertTrue(def.getCondition().getParameterDefinitions()[0].isRequired());
        Assert.assertTrue(def.getCondition().getParameterDefinitions()[0].getAnnotations().length == 0);

        Assert.assertTrue(def.getCondition().getParameterDefinitions()[1].getIndex() == 1);
        Assert.assertTrue(def.getCondition().getParameterDefinitions()[1].getName().equals("birthDate"));
        Assert.assertTrue(def.getCondition().getParameterDefinitions()[1].getType().equals(Date.class));
        Assert.assertTrue(def.getCondition().getParameterDefinitions()[1].isRequired() == false);

        Assert.assertTrue(def.getCondition().getParameterDefinitions()[2].getIndex() == 2);
        Assert.assertTrue(def.getCondition().getParameterDefinitions()[2].getName().equals("values"));
        Assert.assertTrue(def.getCondition().getParameterDefinitions()[2].getType().equals(new TypeReference<List<String>>(){}.getType()));
        Assert.assertTrue(def.getCondition().getParameterDefinitions()[2].getAnnotations().length == 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void loadTest4() {
        RuleDefinition def = RuleDefinition.load(TestRule2.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void loadTest5() {
        RuleDefinition def = RuleDefinition.load(TestRule3.class);
    }

    @Test()
    public void loadTest6() {
        ActionDefinition[] def = ActionDefinition.loadThenActions(TestRule4.class);

        Assert.assertTrue(def.length == 1);
        Assert.assertTrue(def[0].getDescription().equals("calculate"));
        Assert.assertTrue(def[0].getAction().getParameterDefinitions()[0].getName().equals("id"));
        Assert.assertTrue(def[0].getAction().getParameterDefinitions()[0].getType().equals(int.class));
        Assert.assertTrue(def[0].getAction().getParameterDefinitions()[0].isRequired());
        Assert.assertTrue(def[0].getAction().getParameterDefinitions()[0].getAnnotations().length == 0);

        Assert.assertTrue(def[0].getAction().getParameterDefinitions()[1].getName().equals("birthDate"));
        Assert.assertTrue(def[0].getAction().getParameterDefinitions()[1].getType().equals(Date.class));
        Assert.assertTrue(def[0].getAction().getParameterDefinitions()[1].getAnnotations().length == 0);

        Assert.assertTrue(def[0].getAction().getParameterDefinitions()[2].getName().equals("values"));
        Assert.assertTrue(def[0].getAction().getParameterDefinitions()[2].getType().equals(new TypeReference<List<Integer>>(){}.getType()));
        Assert.assertTrue(def[0].getAction().getParameterDefinitions()[2].getAnnotations().length == 0);
    }

    @Test
    public void loadTest7() {
        Condition.Condition2<Integer, String> rule2 = (Integer i, String text) -> i > 100 && text != null;
        SerializedLambda lambda = LambdaUtils.getSerializedLambda(rule2);
        RuleDefinition def = RuleDefinition.load(lambda, "TestRule2", "Some rule for testing");

        Assert.assertTrue(def.getCondition().getParameterDefinitions().length == 2);

        Assert.assertTrue(def.getCondition().getParameterDefinitions()[0].getIndex() == 0);
        Assert.assertTrue(def.getCondition().getParameterDefinitions()[0].getName().equals("i"));
        Assert.assertTrue(def.getCondition().getParameterDefinitions()[0].getType().equals(Integer.class));
        Assert.assertTrue(def.getCondition().getParameterDefinitions()[0].getAnnotations().length == 0);

        Assert.assertTrue(def.getCondition().getParameterDefinitions()[1].getIndex() == 1);
        Assert.assertTrue(def.getCondition().getParameterDefinitions()[1].getName().equals("text"));
        Assert.assertTrue(def.getCondition().getParameterDefinitions()[1].getType().equals(String.class));
        Assert.assertTrue(def.getCondition().getParameterDefinitions()[1].isRequired() == false);
    }

    @Test
    public void loadTest8() {
        RuleFactory factory = RuleFactory.defaultFactory();
        Rule rule1 = factory.rule(args3((Integer a, Date date, String x) -> a != null));
        Rule rule2 = factory.rule(args4((Integer a, Date date, String x, String y) -> a != null))
                .then(args2((Integer y, String z) -> {}))
                .then(args3((Integer y, String z, Date date) -> {}));
    }
}
