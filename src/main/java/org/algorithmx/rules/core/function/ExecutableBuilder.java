package org.algorithmx.rules.core.function;

import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.core.model.MethodDefinition;
import org.algorithmx.rules.core.model.ParameterDefinition;
import org.algorithmx.rules.lib.spring.util.Assert;
import org.algorithmx.rules.util.LambdaUtils;
import org.algorithmx.rules.util.reflect.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ExecutableBuilder {

    private Object function;
    private MethodDefinition definition;

    protected ExecutableBuilder(Object function, MethodDefinition definition) {
        super();
        Assert.notNull(definition, "actionMethod cannot be null.");
        this.function = function;
        this.definition = definition;
    }

    public static MethodInfo load(Object function, Class<? extends Annotation> annotation) {
        Assert.notNull(function, "function cannot be null.");

        Method functionMethod = findFunctionMethod(function.getClass(), annotation);

        if (functionMethod == null) {
            throw new UnrulyException("Class [" + function.getClass() + "] does not implement any function methods. " +
                    "Add @Function to a method and try again.");
        }

        SerializedLambda serializedLambda = LambdaUtils.getSafeSerializedLambda(function);

        if (serializedLambda != null) {
            return withLambda(function, functionMethod, serializedLambda);
        }

        return new MethodInfo(function, MethodDefinition.load(functionMethod));
    }

    private static MethodInfo withLambda(Object function, Method functionMethod, SerializedLambda serializedLambda) {
        Assert.notNull(functionMethod, "functionMethod cannot be null.");
        Assert.notNull(serializedLambda, "serializedLambda cannot be null.");
        MethodDefinition methodDefinition = null;

        try {
            Class<?> implementationClass = LambdaUtils.getImplementationClass(serializedLambda);
            Method implementationMethod = LambdaUtils.getImplementationMethod(serializedLambda, implementationClass);
            MethodDefinition implementationMethodDefinition = MethodDefinition.load(implementationMethod);

            ParameterDefinition[] parameterDefinitions = new ParameterDefinition[functionMethod.getParameterCount()];
            int delta = implementationMethod.getParameterCount() - functionMethod.getParameterCount();

            for (int i = delta; i < implementationMethod.getParameterCount(); i++) {
                int index = i - delta;
                parameterDefinitions[index] = implementationMethodDefinition.getParameterDefinition(i);
                parameterDefinitions[index].setIndex(index);
            }

            methodDefinition = new MethodDefinition(functionMethod, implementationMethodDefinition.getOrder(),
                    implementationMethodDefinition.getDescription(), parameterDefinitions);
            methodDefinition.setReturnType(implementationMethod.getGenericReturnType());

        } catch (Exception e) {
            // Log
        }

        if (methodDefinition == null) {
            methodDefinition = MethodDefinition.load(functionMethod);
        }

        return new MethodInfo(function, methodDefinition);
    }

    protected static Method findFunctionMethod(Class<?> c, Class<? extends Annotation> annotation) {
        Method[] result = findFunctionMethods(c, annotation);

        if (result == null || result.length == 0) return null;

        // Too many Actions declared
        if (result.length > 1) {
            throw new UnrulyException("Too many actionable methods found on class [" + c + "]. Candidates ["
                    + Arrays.toString(result) + "]");
        }

        return result[0];
    }

    /**
     *
     * @param c
     * @return
     */
    protected static Method[] findFunctionMethods(Class<?> c, Class<? extends Annotation> annotation) {
        Assert.notNull(c, "c cannot be null");

        if (Modifier.isAbstract(c.getModifiers())) {
            throw new UnrulyException("Function classes cannot be abstract [" + c + "]");
        }

        Method[] candidates = ReflectionUtils.getMethodsWithAnnotation(c, annotation);

        if (candidates == null || candidates.length == 0) {
            return null;
        }

        List<Method> result = new ArrayList<>(candidates.length);

        for (Method method : candidates) {
            if (!Modifier.isPublic(method.getModifiers())) continue;
            if (method.isBridge()) continue;
            result.add(ReflectionUtils.getImplementationMethod(c, method));
        }

        return result.toArray(new Method[result.size()]);
    }

    public Object getFunction() {
        return function;
    }

    public MethodDefinition getDefinition() {
        return definition;
    }

    protected static class MethodInfo {
        private Object function;
        private MethodDefinition definition;

        public MethodInfo(Object function, MethodDefinition definition) {
            super();
            this.function = function;
            this.definition = definition;
        }

        public Object getFunction() {
            return function;
        }

        public MethodDefinition getDefinition() {
            return definition;
        }
    }
}
