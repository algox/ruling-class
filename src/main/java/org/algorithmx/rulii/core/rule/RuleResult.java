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

package org.algorithmx.rulii.core.rule;

import org.algorithmx.rulii.lib.spring.util.Assert;

public class RuleResult implements RuleResultExtractor {

    private final String ruleName;
    private final RuleExecutionStatus status;

    private String parentName;

    public RuleResult(String ruleName, RuleExecutionStatus status) {
        super();
        Assert.notNull(ruleName, "ruleName cannot be null.");
        Assert.notNull(status, "status cannot be null.");
        this.ruleName = ruleName;
        this.status = status;
    }

    public String getRuleName() {
        return ruleName;
    }

    public RuleExecutionStatus getStatus() {
        return status;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    @Override
    public RuleResult[] extract() {
        RuleResult[] result = new RuleResult[1];
        result[0] = this;
        return result;
    }

    @Override
    public String toString() {
        return "RuleResult{" +
                "ruleName='" + ruleName + '\'' +
                ", status=" + status +
                ", parentName='" + parentName + '\'' +
                '}';
    }
}
