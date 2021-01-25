/**
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
package org.algorithmx.rules.core.ruleset;

import org.algorithmx.rules.core.condition.Condition;
import org.algorithmx.rules.core.condition.ConditionBuilder;
import org.algorithmx.rules.core.rule.RuleExecutionStatus;

public final class StopConditions {

    // All Rules must PASS (no SKIP or FAIL)
    public static Condition ALL_PASS = ConditionBuilder.build((RuleSetResult result)
            -> result.getCount(RuleExecutionStatus.FAIL, RuleExecutionStatus.SKIPPED) > 0);
    // All Rules must either PASS or SKIP (no FAIL)
    public static Condition ALL_PASS_OR_SKIP = ConditionBuilder.build((RuleSetResult result) -> result.isAnyFail());
    // At least one PASS
    public static Condition ANY_PASS = ConditionBuilder.build((RuleSetResult result) -> result.isAnyPass());
    // At least one PASS or SKIP
    public static Condition ANY_PASS_OR_SKIP = ConditionBuilder.build((RuleSetResult result)
            -> result.getCount(RuleExecutionStatus.PASS, RuleExecutionStatus.SKIPPED) > 0);
    // At least one SKIP
    public static Condition ANY_SKIP = ConditionBuilder.build((RuleSetResult result) -> result.isAnySkip());
    // At least one FAIL
    public static Condition ANY_FAIL = ConditionBuilder.build((RuleSetResult result) -> result.isAnyFail());

    private StopConditions() {
        super();
    }
}
