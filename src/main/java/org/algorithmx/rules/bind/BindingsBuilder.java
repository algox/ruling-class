package org.algorithmx.rules.bind;

import org.algorithmx.rules.spring.util.Assert;

public class BindingsBuilder<S extends Bindings> {

    public static final String SELF_BIND_NAME = "bindings";

    private final S target;
    private boolean selfAware = true;
    private String selfBindingName = SELF_BIND_NAME;

    private BindingsBuilder(S target) {
        super();
        this.target = target;
    }

    public static BindingsBuilder<Bindings> withNoScopes() {
        return new BindingsBuilder<>(new DefaultBindings());
    }

    public static BindingsBuilder<ScopedBindings> withScopes() {
        return new BindingsBuilder<>(new DefaultScopedBindings());
    }

    protected S getTarget() {
        return target;
    }

    public boolean isSelfAware() {
        return selfAware;
    }

    public BindingsBuilder<S> selfAware(boolean selfAware) {
        this.selfAware = selfAware;
        return this;
    }

    public BindingsBuilder<S> selfBindingName(String selfBindingName) {
        Assert.notNull(selfBindingName, "selfBindingName cannot be null.");
        this.selfBindingName = selfBindingName;
        return this;
    }

    public String getSelfBindingName() {
        return selfBindingName;
    }

    protected void registerSelf() {
        if (isSelfAware()) {
            getTarget().bind(getSelfBindingName(), TypeReference.with(Bindings.class), getTarget(), false);
        }
    }

    public S build() {
        registerSelf();
        return getTarget();
    }
}
