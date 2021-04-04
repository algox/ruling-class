package org.algorithmx.rulii.validation.graph;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public abstract class ObjectVisitorTemplate implements ObjectVisitor {

    private final Predicate<Class<?>> classFilter;
    private final Predicate<Field> fieldFilter;

    public ObjectVisitorTemplate() {
        this.classFilter = null;
        this.fieldFilter = null;
    }

    protected ObjectVisitorTemplate(Predicate<Field> fieldFilter, Class<?>...ignoredClasses) {
        this(c -> {
            Set<Class<?>> ignoredClassSet = new HashSet<>(ignoredClasses != null
                    ? Arrays.asList(ignoredClasses)
                    : new ArrayList<>());
            return ignoredClassSet.contains(c);
        }, fieldFilter);
    }

    protected ObjectVisitorTemplate(Predicate<Class<?>> classFilter, Predicate<Field> fieldFilter) {
        super();
        this.classFilter = classFilter;
        this.fieldFilter = fieldFilter;
    }

    public Predicate<Class<?>> getClassFilter() {
        return classFilter;
    }

    public Predicate<Field> getFieldFilter() {
        return fieldFilter;
    }
}
