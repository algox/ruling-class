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

import org.algorithmx.rules.core.Condition;
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

    public static final String RULE_NAME_REGEX         = "^[a-zA-Z][a-zA-Z0-9]*?$";
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
    public static RuleDefinition load(Condition condition, String name, String description) {
        Assert.notNull(condition, "condition cannot be null.");
        return RuleDefinition.load(LambdaUtils.getSerializedLambda(condition), name, description);
    }

    public static boolean isValidRuleName(String ruleName) {
        Assert.notNull(ruleName, "ruleName cannot be null.");
        return NAME_PATTERN.matcher(ruleName).matches();
    }
}
