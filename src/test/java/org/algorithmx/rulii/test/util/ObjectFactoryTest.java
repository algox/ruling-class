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

package org.algorithmx.rulii.test.util;

import org.algorithmx.rulii.annotation.Description;
import org.algorithmx.rulii.annotation.Given;
import org.algorithmx.rulii.annotation.Rule;
import org.algorithmx.rulii.annotation.Then;
import org.algorithmx.rulii.bind.match.MatchByNameAndTypeMatchingStrategy;
import org.algorithmx.rulii.bind.match.MatchByNameMatchingStrategy;
import org.algorithmx.rulii.bind.match.MatchByTypeMatchingStrategy;
import org.algorithmx.rulii.util.reflect.ObjectFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 * Binding ParameterDefinition tests.
 *
 * @author Max Arulananthan
 */
public class ObjectFactoryTest {

    public ObjectFactoryTest() {
        super();
    }

    @Test
    public void strategyCreationTest() {
        ObjectFactory factory = ObjectFactory.create();
        Assert.assertTrue(factory.createStrategy(MatchByNameMatchingStrategy.class) instanceof MatchByNameMatchingStrategy);
        Assert.assertTrue(factory.createStrategy(MatchByNameAndTypeMatchingStrategy.class) instanceof MatchByNameAndTypeMatchingStrategy);
        Assert.assertTrue(factory.createStrategy(MatchByTypeMatchingStrategy.class) instanceof MatchByTypeMatchingStrategy);
        Assert.assertTrue(factory.createStrategy(MatchByTypeMatchingStrategy.class) instanceof MatchByTypeMatchingStrategy);

        Assert.assertTrue(factory.createRule(TestRule.class) instanceof TestRule);
    }

    @Rule @Description("This is test rule")
    public static final class TestRule {

        public TestRule() {
            super();
        }

        @Given
        public boolean when() {
            return true;
        }

        @Then
        public void then() {

        }
    }
}
