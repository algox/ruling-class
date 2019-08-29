package org.algorithmx.rules.core;

import org.algorithmx.rules.model.RuleExecution;

import java.util.Iterator;

public interface RuleExecutionAuditor {

    void audit(RuleExecution auditItem);

    RuleExecution getFirstAuditItem();

    RuleExecution getLastAuditItem();

    Iterator<RuleExecution> getAuditItems();
}
