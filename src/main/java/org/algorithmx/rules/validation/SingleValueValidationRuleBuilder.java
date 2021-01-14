package org.algorithmx.rules.validation;

import org.algorithmx.rules.core.ruleset.RuleSetBuilder;

import java.lang.annotation.Annotation;

public interface SingleValueValidationRuleBuilder<T extends Annotation> {

    RuleSetBuilder build(T type, RuleSetBuilder builder);
}
