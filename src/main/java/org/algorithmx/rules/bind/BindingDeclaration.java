package org.algorithmx.rules.bind;

import org.algorithmx.rules.UnrulyException;
import org.algorithmx.rules.spring.core.DefaultParameterNameDiscoverer;
import org.algorithmx.rules.spring.core.ParameterNameDiscoverer;
import org.algorithmx.rules.util.LambdaUtils;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.function.Function;

public interface BindingDeclaration extends Function<String, Object>, Serializable {

    long serialVersionUID = -0L;

    ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    default String name() {
        SerializedLambda lambda = LambdaUtils.getSafeSerializedLambda(this);

        if (lambda == null) {
            throw new UnrulyException("BindingDeclaration can only be used as a Lambda expression");
        }

        Class<?> implementationClass = LambdaUtils.getImplementationClass(lambda);
        Method implementationMethod = LambdaUtils.getImplementationMethod(lambda, implementationClass);
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(implementationMethod);

        return parameterNames[0];
    }

    default Object value() {
        return apply(name());
    }
}
