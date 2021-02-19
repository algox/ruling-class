package org.algorithmx.rulii.test.validation;

import org.algorithmx.rulii.annotation.AliasFor;
import org.algorithmx.rulii.validation.rules.digits.Digits;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, METHOD, CONSTRUCTOR, ANNOTATION_TYPE, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Inherited @Documented
@Digits(integer = 2, fraction = 3)
public @interface Annotation1 {

    @AliasFor(value = "integer", annotation = Digits.class)
    int integer();

    @AliasFor(value = "fraction", annotation = Digits.class)
    int fraction();
}
