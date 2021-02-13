package org.algorithmx.rulii.validation.annotation;

import org.algorithmx.rulii.validation.BindingValidationRuleBuilder;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target({ ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface ValidationRule {

    Class<? extends BindingValidationRuleBuilder> value();
}
