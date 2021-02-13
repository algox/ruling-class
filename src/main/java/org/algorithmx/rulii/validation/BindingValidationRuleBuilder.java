package org.algorithmx.rulii.validation;

import org.algorithmx.rulii.core.rule.Rule;

import java.lang.annotation.Annotation;

public interface BindingValidationRuleBuilder<T extends Annotation> {

    Rule[] build(T type, String bindingName);
}
