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

import org.algorithmx.rules.core.ConditionConsumer;
import org.algorithmx.rules.core.Rule;
import org.algorithmx.rules.model.RuleDefinition;
import org.algorithmx.rules.spring.util.Assert;

import java.util.regex.Pattern;

/**
 * Utility class to provide convenience methods for Rules.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public final class RuleUtils {

    public static final String RULE_NAME_REGEX         = "^[a-zA-Z][a-zA-Z0-9-_]*?$";
    private static final Pattern NAME_PATTERN           = Pattern.compile(RULE_NAME_REGEX);

    private RuleUtils() {
        super();
    }

    /**
     * Successful result to a Rule execution.
     *
     * @return true
     */
    public final static boolean PASS() {
        return true;
    }

    /**
     * Unsuccessful result to a Rule execution.
     *
     * @return false
     */
    public final static boolean FAIL() {
        return false;
    }

    /**
     * Loads a new RuleDefinition from the given Condition, Rule Name and Description.
     *
     * @param condition when condition.
     * @param name Rule Name.
     * @param description Rule Description.
     * @return new Rule Definition.
     */
    public static RuleDefinition load(ConditionConsumer condition, String name, String description) {
        Assert.notNull(condition, "condition cannot be null.");
        return RuleDefinition.load(LambdaUtils.getSerializedLambda(condition), name, description);
    }

    /**
     * Determines whether the given name is "valid" Rule Name. It needs to follow the following regex ^[a-zA-Z][a-zA-Z0-9]*?$
     *
     * @param ruleName desired Rule Name.
     * @return true if the name is valid; false otherwise.
     */
    public static boolean isValidRuleName(String ruleName) {
        Assert.notNull(ruleName, "ruleName cannot be null.");
        return NAME_PATTERN.matcher(ruleName).matches();
    }

    /**
     * Merges the first rule with the rest of the Rules.
     *
     * @param rule new rule.
     * @param others other rules.
     * @return merged array of rules.
     */
    public static Rule[] merge(Rule rule, Rule[] others) {
        Assert.isTrue(others != null && others.length > 0,
                "others cannot be null and must have at least 1 element");
        Rule[] result = new Rule[others.length + 1];
        result[0] = rule;
        System.arraycopy(others, 0, result, 1, others.length);
        return result;
    }
}
