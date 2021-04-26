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

package org.algorithmx.rulii.test.validation.method;

import org.algorithmx.rulii.core.action.ActionBuilder;
import org.algorithmx.rulii.core.condition.ConditionBuilder;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.core.context.RuleContextBuilder;
import org.algorithmx.rulii.core.rule.RuleBuilder;
import org.algorithmx.rulii.core.ruleset.RuleSet;
import org.algorithmx.rulii.core.ruleset.RuleSetBuilder;
import org.algorithmx.rulii.validation.RuleViolationBuilder;
import org.algorithmx.rulii.validation.RuleViolations;
import org.algorithmx.rulii.validation.beans.MethodValidator;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

public class MethodValidationTest {

    public MethodValidationTest() {
        super();
    }

    @Test
    public void test1() throws NoSuchMethodException {
        Method method = TestService.class.getMethod("addCustomer", String.class, String.class, Integer.class);
        RuleContext context = RuleContextBuilder.empty();
        context.getRuleRegistry().register(createLastNameRuleSet());

        MethodValidator validator = new MethodValidator();

        RuleViolations violations = validator.validateBefore(context, method, "Michael", "Jordan", 50);
        //System.err.println(violations);
        Assert.assertTrue(violations.size() == 2);
    }

    private RuleSet createLastNameRuleSet() {
        RuleSet result = RuleSetBuilder.with("lastNameRules")
                .rule(RuleBuilder
                        .name("rule1")
                        .given(ConditionBuilder.build((String lastName) -> lastName.startsWith("Jor")))
                        .otherwise(ActionBuilder.build((String lastName, RuleViolations violations) -> {
                            RuleViolationBuilder builder = RuleViolationBuilder
                                    .with("LastNamePrefixRule")
                                    .errorCode("error.101")
                                    .param("lastName", lastName);
                            violations.add(builder.build());
                        }))
                        .build())
                .build();
        return result;
    }
}
