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

package org.algorithmx.rulii.util;

import org.algorithmx.rulii.bind.match.ParameterMatch;
import org.algorithmx.rulii.core.model.MethodDefinition;
import org.algorithmx.rulii.core.rule.Rule;
import org.algorithmx.rulii.core.rule.RuleDefinition;
import org.algorithmx.rulii.core.ruleset.RuleSet;
import org.algorithmx.rulii.lib.spring.util.Assert;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Utility class to provide convenience methods for Rules.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public final class RuleUtils {

    public static final String TAB              = "\t";
    public static final String NAME_REGEX       = "[a-zA-Z$_][a-zA-Z0-9$_]*";
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

    public static Predicate<Rule> createPackageRuleFilter(String...packageNames) {
        return r -> {
            if (packageNames == null || packageNames.length == 0) return true;
            Set<String> names = new HashSet<>(Arrays.asList(packageNames));
            if (r.getTarget() == null) return false;
            return names.contains(r.getTarget().getClass().getPackage().getName());
        };
    }

    public static String getRuleSetDescription(RuleSet ruleSet, String prefix) {
        StringBuilder result = new StringBuilder();
        result.append(prefix + "RuleSet Name    : " + ruleSet.getName() + System.lineSeparator());
        result.append(prefix + "Description     : " + ruleSet.getDescription() + System.lineSeparator());
        result.append(prefix + "PreCondition    : " + (ruleSet.getPreCondition() != null) + System.lineSeparator());
        result.append(prefix + "StopCondition    : " + (ruleSet.getStopCondition() != null) + System.lineSeparator());
        result.append(prefix + "Number of Rules : " + ruleSet.size() + System.lineSeparator());
        return result.toString();
    }

    public static String getRuleDescription(RuleDefinition ruleDefinition, MethodDefinition methodDefinition,
                                            String prefix) {
        StringBuilder result = new StringBuilder();
        result.append(prefix + "Rule Name   : " + ruleDefinition.getName() + System.lineSeparator());
        result.append(prefix + "Rule Class  : " + ruleDefinition.getRuleClass().getSimpleName() + System.lineSeparator());

        if (ruleDefinition.getDescription() != null) {
            result.append(prefix + "Description : " + ruleDefinition.getDescription() + System.lineSeparator());
        }

        result.append(prefix + "Method      : " + methodDefinition.getSignature() + System.lineSeparator());
        return result.toString();
    }

    public static String getMethodDescription(MethodDefinition methodDefinition, ParameterMatch[] matches,
                                              Object[] values, String prefix) {
        StringBuilder result = new StringBuilder();
        result.append(prefix + "Method : " + methodDefinition.getSignature() + System.lineSeparator());

        if (methodDefinition.getParameterDefinitions().length > 0) {
            result.append(prefix + "Parameter Matches :");
            result.append(System.lineSeparator());
            result.append(getArgumentDescriptions(methodDefinition, matches, values, (prefix + TAB)));
        }

        return result.toString();
    }

    public static String getArgumentDescriptions(MethodDefinition methodDefinition, ParameterMatch[] matches,
                                                 Object[] values, String prefix) {
        if (matches == null && values == null) return "";

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < methodDefinition.getParameterDefinitions().length; i++) {
            result.append(prefix + "Parameter(index = " + i + ") :");

            result.append(" (" + methodDefinition.getParameterDefinition(i).getTypeName()
                    + " " + methodDefinition.getParameterDefinition(i).getName()
                    + " = ");

            if (values != null && i < values.length && values[i] != null) {
                result.append(getTextValue(values[i], 80) + ")");
            } else {
                result.append("null)");
            }

            if (matches != null && i < matches.length && matches[i] != null) {
                if (matches[i].getBinding() != null) {
                    result.append(System.lineSeparator());
                    result.append(prefix);
                    result.append("Matched Binding      : (" + matches[i].getBinding().getTypeAndName() + ")");

                    boolean mismatch = matches[i].getBinding().isTypeAcceptable(methodDefinition.getParameterDefinition(i).getType());

                    if (!mismatch) result.append(" ! Types Incompatible Expected ["
                            + methodDefinition.getParameterDefinition(i).getTypeName() + "]"
                            + " Given [" + matches[i].getBinding().getTypeName() + "]");
                }
            }

            if (i < methodDefinition.getParameterDefinitions().length - 1) result.append(System.lineSeparator());
        }

        return result.toString();
    }

    public static String getTabs(int count) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < count; i++) {
            result.append(TAB);
        }

        return result.toString();
    }

    public static String getTextValue(Object value, int max) {
        if (value == null) return "null";
        String result = value.toString();
        if (result == null) return "null";
        return result.length() > max ? (result.substring(0, max) + "...+") : result;
    }

    public static ParameterMatch[] immutable(ParameterMatch[] matches) {
        if (matches == null) return null;

        ParameterMatch[] result = new ParameterMatch[matches.length];

        for (int i = 0; i < matches.length; i++) {
            ParameterMatch match = matches[i];
            ParameterMatch immutable = new ParameterMatch(match.getDefinition(),
                    match.getBinding() != null ? match.getBinding().immutableSelf() : null);
            result[i] = immutable;
        }

        return result;
    }
}
