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
package org.algorithmx.rules.core;

import java.util.Collection;

public interface RuleSet extends Iterable<Rule> {

    String getName();

    String getDescription();

    Rule getRule(String ruleName);

    int size();

    Rule[] getRules();

    RuleSet add(Class<?> ruleClass);

    RuleSet add(Rule rule);

    RuleSet add(Collection<Rule> rules);

    RuleSet add(String name, Rule rule);
}

