package org.algorithmx.rulii.validation.beans;

import org.algorithmx.rulii.core.ruleset.RuleSet;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface BeanValidationRules {

    Class<?> getBeanType();

    RuleSet getBeanRules();

    RuleSet getFieldRules(Field field);

    RuleSet getPropertyRules(PropertyDescriptor property);

    RuleSet getConstructorRules(Constructor ctor);

    RuleSet getConstructorParameterRules(int index, Constructor ctor);

    RuleSet getMethodRules(Method method);

    RuleSet getMethodReturnValueRules(Method method);

    RuleSet getMethodParameterRules(int index, Method ctor);
}
