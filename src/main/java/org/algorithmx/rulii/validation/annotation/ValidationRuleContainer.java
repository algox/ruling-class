package org.algorithmx.rulii.validation.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target({ ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface ValidationRuleContainer {

    Class<? extends Annotation> value();
}
