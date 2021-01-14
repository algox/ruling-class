package org.algorithmx.rules.validation;

import org.algorithmx.rules.core.ruleset.RuleSetBuilder;

import java.lang.annotation.Annotation;

public interface ValidationRuleProducer<T extends Annotation> {

    void produce(T type, RuleSetBuilder builder);

    Class<?>[] types();
}
