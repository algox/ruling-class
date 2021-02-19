package org.algorithmx.rulii.test.validation.objectgraph;

import org.algorithmx.rulii.core.Identifiable;
import org.algorithmx.rulii.util.objectgraph.ObjectVisitorTemplate;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class TestObjectVisitor extends ObjectVisitorTemplate {

    private List<String> idList = new ArrayList<>();

    public TestObjectVisitor() {
        super();
    }

    @Override
    public boolean visitObjectStart(Object target) {
        //System.err.println(target);
        if (target instanceof Identifiable) idList.add(((Identifiable) target).getName());
        return true;
    }

    @Override
    public boolean visitField(Field field, Object value, Object parent) {
        //System.err.println("XXX Field [" + field.getName() + "] value [" + value + "]");
        return true;
    }

    @Override
    public boolean visitCollection(Field field, Collection<?> values, Object parent) {
        //System.err.println("XXX Collection [" + field.getName() + "]");
        return true;
    }

    @Override
    public boolean visitArray(Field field, Object values, Object parent) {
        //System.err.println("XXX Array [" + field.getName() + "]");
        return true;
    }

    @Override
    public boolean visitMap(Field field, Map<?, ?> values, Object parent) {
        //System.err.println("XXX Map [" + field.getName() + "]");
        return true;
    }

    public List<String> getIdList() {
        return idList;
    }
}
