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

import org.algorithmx.rules.core.impl.DefaultRuleAuditor;
import org.algorithmx.rules.model.RuleExecution;

import java.util.Iterator;

/**
 * As the name suggests this class is responsible for auditing the execution of a Rule. Keeps an audit trail of all the
 * Rule executions during the Rule Engine "run".
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public interface RuleAuditor extends Iterable<RuleExecution> {

    /**
     * Creates the default implementation of RuleAuditor.
     *
     * @return default implementation.
     */
    static RuleAuditor defaultRuleAuditor() {
        return new DefaultRuleAuditor();
    }

    /**
     * Stores a audit item for the execution of a Rule.
     *
     * @param ruleExecution Rule Audit details.
     */
    void audit(RuleExecution ruleExecution);

    /**
     * Returns the first audit item.
     *
     * @return first audit item in the audit trail.
     */
    RuleExecution getFirstAuditItem();

    /**
     * Returns the last audit item.
     *
     * @return last audit item in the audit trail.
     */
    RuleExecution getLastAuditItem();

    /**
     * Returns the audit trail.
     * @return audit trail of the audit items.
     */
    Iterator<RuleExecution> getAuditItems();

    @Override
    default Iterator<RuleExecution> iterator() {
        return getAuditItems();
    }
}
