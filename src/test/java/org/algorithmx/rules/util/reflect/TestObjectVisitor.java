package org.algorithmx.rules.util.reflect;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

public class TestObjectVisitor extends ObjectVisitorTemplate {

    public TestObjectVisitor() {
        super();
    }

    @Override
    public boolean visitObjectStart(Object target) {
        System.err.println("XXX visitObjectStart [" + target + "]");
        return true;
    }

    @Override
    public void visitObjectEnd(Object target) {
        System.err.println("XXX visitObjectEnd [" + target + "]");
        System.err.println();
    }

    @Override
    public boolean visitNull(Field field, Object value, Object parent) {
        System.err.println("XXX visitNullField [" + field.getName() + "]");
        return true;
    }

    @Override
    public boolean visitField(Field field, Object value, Object parent) {
        System.err.println("XXX visitField [" + field.getName() + "] value [" + value + "]");
        return true;
    }

    @Override
    public boolean visitArray(Field field, Object[] values, Object parent) {
        System.err.println("XXX visitArrayField [" + field.getName() + "]");
        return true;
    }

    @Override
    public boolean visitCollection(Field field, Collection<?> values, Object parent) {
        System.err.println("XXX visitCollectionField [" + field.getName() + "]");
        return true;
    }

    @Override
    public boolean visitMap(Field field, Map<?, ?> values, Object parent) {
        System.err.println("XXX visitMapField [" + field.getName() + "]");
        return true;
    }

    @Override
    public boolean visitNull(PropertyDescriptor property, Object value, Object parent) {
        System.err.println("XXX visitNullProperty [" + property.getName() + "]");
        return true;
    }

    @Override
    public boolean visitProperty(PropertyDescriptor property, Object value, Object parent) {
        System.err.println("XXX visitProperty [" + property.getName() + "] value [" + value + "]");
        return true;
    }

    @Override
    public boolean visitArray(PropertyDescriptor property, Object[] values, Object parent) {
        System.err.println("XXX visitArrayProperty [" + property.getName() + "]");
        return true;
    }

    @Override
    public boolean visitCollection(PropertyDescriptor property, Collection<?> values, Object parent) {
        System.err.println("XXX visitArrayCollection [" + property.getName() + "]");
        return true;
    }

    @Override
    public boolean visitMap(PropertyDescriptor property, Map<?, ?> values, Object parent) {
        System.err.println("XXX visitMapProperty [" + property.getName() + "]");
        return true;
    }
}
