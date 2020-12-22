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

import org.algorithmx.rules.bind.match.ParameterMatch;
import org.algorithmx.rules.core.model.MethodDefinition;
import org.algorithmx.rules.core.rule.Rule;
import org.algorithmx.rules.core.rule.RuleDefinition;
import org.algorithmx.rules.core.ruleset.RuleSet;
import org.algorithmx.rules.lib.spring.util.Assert;

import java.util.regex.Pattern;

/**
 * Utility class to provide convenience methods for Rules.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public final class RuleUtils {

    public static final String TAB              = "\t";
    public static final String NAME_REGEX       = "^[a-zA-Z][a-zA-Z0-9-_]*?$";
    private static final Pattern NAME_PATTERN   = Pattern.compile(NAME_REGEX);

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
     * Determines whether the given name is "valid" Name. It needs to follow the following regex ^[a-zA-Z][a-zA-Z0-9]*?$
     *
     * @param name desired Rule Name.
     * @return true if the name is valid; false otherwise.
     */
    public static boolean isValidName(String name) {
        return name != null && name.trim().length() > 0 && NAME_PATTERN.matcher(name).matches();
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

    public static String getRuleSetDescription(RuleSet ruleSet, String prefix) {
        StringBuilder result = new StringBuilder();
        result.append(prefix + "RuleSet Name    : " + ruleSet.getName() + System.lineSeparator());
        result.append(prefix + "Description     : " + ruleSet.getDescription() + System.lineSeparator());
        result.append(prefix + "PreCondition    : " + (ruleSet.getPreCondition() != null) + System.lineSeparator());
        result.append(prefix + "PreAction       : " + (ruleSet.getPreAction() != null) + System.lineSeparator());
        result.append(prefix + "PostAction      : " + (ruleSet.getPostAction() != null) + System.lineSeparator());
        result.append(prefix + "ErrorHandler    : " + (ruleSet.getErrorHandler() != null) + System.lineSeparator());
        result.append(prefix + "Number of Rules : " + ruleSet.size() + System.lineSeparator());
        return result.toString();
    }

    public static String getRuleDescription(RuleDefinition ruleDefinition, MethodDefinition methodDefinition,
                                            String prefix) {
        StringBuilder result = new StringBuilder();
        result.append(prefix + "Rule Name   : " + ruleDefinition.getName() + System.lineSeparator());
        result.append(prefix + "Rule Class  : " + ruleDefinition.getRuleClass().getName() + System.lineSeparator());
        result.append(prefix + "Description : " + ruleDefinition.getDescription() + System.lineSeparator());
        result.append(prefix + "Method      : " + methodDefinition.getSignature() + System.lineSeparator());
        return result.toString();
    }

    public static String getMethodDescription(MethodDefinition methodDefinition, ParameterMatch[] matches,
                                              Object[] values, String prefix) {
        StringBuilder result = new StringBuilder();
        result.append(prefix + "Method      : " + methodDefinition.getSignature() + System.lineSeparator());
        result.append(prefix + "Class       : " + methodDefinition.getMethod().getDeclaringClass() + System.lineSeparator());
        result.append(prefix + "Parameter Matches :");
        result.append(System.lineSeparator());
        result.append(getArgumentDescriptions(methodDefinition, matches, values, prefix + prefix));

        return result.toString();
    }

    public static String getArgumentDescriptions(MethodDefinition methodDefinition, ParameterMatch[] matches,
                                                 Object[] values, String prefix) {
        if (matches == null && values == null) return "";

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < methodDefinition.getParameterDefinitions().length; i++) {
            result.append(prefix + prefix + "Parameter(index = " + i + ") : ");

            result.append(" (" + methodDefinition.getParameterDefinition(i).getTypeName()
                    + " " + methodDefinition.getParameterDefinition(i).getName()
                    + " = ");

            if (values != null && i < values.length && values[i] != null) {
                result.append(values[i].toString() + ")");
            } else {
                result.append("null)");
            }

            if (matches != null && i < matches.length && matches[i] != null) {
                if (matches[i].getBinding() != null) {
                    result.append(" using Binding (" + matches[i].getBinding().getTypeAndName() + ")");

                    boolean mismatch = matches[i].getBinding().isTypeAcceptable(methodDefinition.getParameterDefinition(i).getType());

                    if (!mismatch) result.append(" ! Types Incompatible Expected ["
                            + methodDefinition.getParameterDefinition(i).getTypeName() + "]"
                            + " Given [" + matches[i].getBinding().getTypeName() + "]");
                }
            }

            result.append(System.lineSeparator());
        }

        return result.toString();
    }
}
