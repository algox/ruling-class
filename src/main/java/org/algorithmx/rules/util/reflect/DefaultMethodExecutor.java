package org.algorithmx.rules.util.reflect;

import org.algorithmx.rules.lib.spring.util.Assert;

import java.lang.reflect.Method;

public class DefaultMethodExecutor implements MethodExecutor {

    private final Method method;
    private MethodExecutor delegate;

    public DefaultMethodExecutor(Method method) {
        super();
        Assert.notNull(method, "method cannot be null.");
        this.method = method;
        ReflectionUtils.makeAccessible(method);
        try {
            this.delegate = new MethodHandleMethodExecutor(method);
        } catch (Exception e) {
            this.delegate = new ReflectiveMethodExecutor(method);
        }
    }

    @Override
    public <T> T execute(Object target, Object...userArgs) {
        return delegate.execute(target, userArgs);
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public String toString() {
        return "DefaultMethodExecutor{" +
                "method=" + method +
                ", delegate=" + delegate +
                '}';
    }
}
