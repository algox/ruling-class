package org.algorithmx.rules.bind;

import org.algorithmx.rules.lib.spring.util.Assert;

import java.lang.reflect.Type;

public class ImmutableBinding<T> implements Binding<T>  {

    private final Binding<T> target;

    public ImmutableBinding(Binding<T> target) {
        super();
        Assert.notNull(target, "target cannot be null.");
        this.target = target;
    }

    private Binding<T> getTarget() {
        return target;
    }

    @Override
    public String getName() {
        return getTarget().getName();
    }

    @Override
    public Type getType() {
        return getTarget().getType();
    }

    @Override
    public T getValue() {
        return getTarget().getValue();
    }

    @Override
    public String getDescription() {
        return getTarget().getDescription();
    }

    @Override
    public String getTypeName() {
        return getTarget().getTypeName();
    }

    @Override
    public String getTypeAndName() {
        return getTarget().getTypeAndName();
    }

    @Override
    public String getSummary() {
        return getTarget().getSummary();
    }

    @Override
    public boolean isEditable() {
        return false;
    }

    @Override
    public String getTextValue() {
        return getTarget().getTextValue();
    }

    @Override
    public boolean isPrimary() {
        return getTarget().isPrimary();
    }

    @Override
    public boolean isTypeAcceptable(Type type) {
        return getTarget().isTypeAcceptable(type);
    }

    @Override
    public boolean isAssignable(Type type) {
        return getTarget().isAssignable(type);
    }

    @Override
    public Binding<T> immutableSelf() {
        return this;
    }

    @Override
    public void setValue(T value) {
        throw new IllegalStateException("This Binding [" + getName() + "] is immutable. It cannot be edited in this context.");
    }

    @Override
    public boolean equals(Object o) {
        return getTarget().equals(o);
    }

    @Override
    public int hashCode() {
        return getTarget().hashCode();
    }

    @Override
    public String toString() {
        return "Name = " + getName() +
                ", Type = " + getTypeName() +
                ", Value = " + getTextValue() +
                ", Primary = " + isPrimary() +
                ", Editable = " + isEditable() +
                ", Description = " + getDescription();
    }
}
