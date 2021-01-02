package org.algorithmx.rules.util.reflect;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public abstract class ObjectVisitorTemplate implements ObjectVisitor {

    private final Predicate<Class<?>> classFilter;
    private final Predicate<Field> fieldFilter;
    private final Predicate<PropertyDescriptor> propertyFilter;

    public ObjectVisitorTemplate() {
        this.classFilter = null;
        this.fieldFilter = null;
        this.propertyFilter = null;
    }

    protected ObjectVisitorTemplate(Predicate<Field> fieldFilter, Predicate<PropertyDescriptor> propertyFilter,
                                    Class<?>...ignoredClasses) {
        this(c -> {
            Set<Class<?>> ignoredClassSet = new HashSet<>(ignoredClasses != null
                    ? Arrays.asList(ignoredClasses)
                    : new ArrayList<>());
            return ignoredClassSet.contains(c);
        }, fieldFilter, propertyFilter);
    }

    protected ObjectVisitorTemplate(Predicate<Class<?>> classFilter, Predicate<Field> fieldFilter,
                                    Predicate<PropertyDescriptor> propertyFilter) {
        super();
        this.classFilter = classFilter;
        this.fieldFilter = fieldFilter;
        this.propertyFilter = propertyFilter;
    }

    @Override
    public boolean shouldVisitClass(Class<?> clazz) {
        return classFilter != null ? classFilter.test(clazz) : true;
    }

    @Override
    public boolean shouldVisitField(Field field) {
        return fieldFilter != null ? fieldFilter.test(field) : true;
    }

    public Predicate<Class<?>> getClassFilter() {
        return classFilter;
    }

    public Predicate<Field> getFieldFilter() {
        return fieldFilter;
    }

    public Predicate<PropertyDescriptor> getPropertyFilter() {
        return propertyFilter;
    }
}
