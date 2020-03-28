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
package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.core.RuleAuditor;
import org.algorithmx.rules.model.RuleExecution;
import org.algorithmx.rules.spring.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Default implementation of the RuleAuditor.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class DefaultRuleAuditor implements RuleAuditor {

    private final List<RuleExecution> items = Collections.synchronizedList(new ArrayList<>());

    public DefaultRuleAuditor() {
        super();
    }

    @Override
    public void audit(RuleExecution execution) {
        Assert.notNull(execution, "execution cannot be null.");
        this.items.add(execution);
    }

    @Override
    public RuleExecution getFirstAuditItem() {
        int size = items.size();
        return size > 0 ? items.get(0) : null;
    }

    @Override
    public RuleExecution getLastAuditItem() {
        int size = items.size();
        return size > 0 ? items.get(size - 1) : null;
    }

    @Override
    public Iterator<RuleExecution> getAuditItems() {
        return items.iterator();
    }
}
