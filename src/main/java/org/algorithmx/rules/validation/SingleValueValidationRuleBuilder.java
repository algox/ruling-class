package org.algorithmx.rules.validation;

import org.algorithmx.rules.core.rule.Rule;
import org.algorithmx.rules.core.ruleset.RuleSetBuilder;

import java.lang.annotation.Annotation;

public interface SingleValueValidationRuleBuilder<T extends Annotation> {

    default Rule[] build(T type) { return null;}
    
    RuleSetBuilder build(T type, RuleSetBuilder builder);
}
