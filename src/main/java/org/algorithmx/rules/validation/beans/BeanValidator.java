package org.algorithmx.rules.validation.beans;

import org.algorithmx.rules.bind.ScopedBindings;
import org.algorithmx.rules.util.reflect.ObjectGraph;
import org.algorithmx.rules.util.reflect.ObjectVisitorTemplate;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

public class BeanValidator extends ObjectVisitorTemplate {

    private ScopedBindings bindings;

    public BeanValidator(Class<?>... ignoredClasses) {
        super(new FieldDiscriminator(), new PropertyDiscriminator(), ignoredClasses);
    }

    public BeanValidator(Predicate<Class<?>> classFilter) {
        super(classFilter, new FieldDiscriminator(), new PropertyDiscriminator());
    }

    public void validate(Object target) {
        ObjectGraph objectGraph = new ObjectGraph(false);
        this.bindings = ScopedBindings.create();
        objectGraph.traverse(target, this);
    }

    @Override
    public boolean visitObjectStart(Object target) {
        System.err.println("XXX Start [" + target + "]");
        bindings.addScope();
        return true;
    }

    @Override
    public void visitObjectEnd(Object target) {
        bindings.removeScope();
        // TODO : Validate
    }

    private void handle(Field field, Object value) {
        bindings.bind(field.getName(), value);
    }

    @Override
    public boolean visitNull(Field field, Object value, Object parent) {
        handle(field, value);
        return false;
    }

    @Override
    public boolean visitField(Field field, Object value, Object parent) {
        handle(field, value);
        return false;
    }

    @Override
    public boolean visitArray(Field field, Object values, Object parent) {
        handle(field, values);
        return false;
    }

    @Override
    public boolean visitCollection(Field field, Collection<?> values, Object parent) {
        handle(field, values);
        return false;
    }

    @Override
    public boolean visitMap(Field field, Map<?, ?> values, Object parent) {
        handle(field, values);
        return false;
    }

    private static class FieldDiscriminator implements Predicate<Field> {

        public FieldDiscriminator() {
            super();
        }

        @Override
        public boolean test(Field field) {
           return field.getDeclaredAnnotations() != null && field.getDeclaredAnnotations().length > 0;
        }
    }

    private static class PropertyDiscriminator implements Predicate<PropertyDescriptor> {

        public PropertyDiscriminator() {
            super();
        }

        @Override
        public boolean test(PropertyDescriptor property) {
            return property.getReadMethod() != null
                    && property.getReadMethod().getAnnotations() != null
                    && property.getReadMethod().getAnnotations().length > 0;
        }
    }
}
