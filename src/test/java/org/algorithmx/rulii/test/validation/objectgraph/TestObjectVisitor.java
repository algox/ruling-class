package org.algorithmx.rulii.test.validation.objectgraph;

import org.algorithmx.rulii.lib.spring.core.annotation.AnnotationUtils;
import org.algorithmx.rulii.traverse.objectgraph.ObjectVisitorTemplate;
import org.algorithmx.rulii.traverse.objectgraph.TraversalCandidate;
import org.algorithmx.rulii.validation.annotation.Validate;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

public class TestObjectVisitor extends ObjectVisitorTemplate {

    private final Predicate<Field> validateCheck = field -> AnnotationUtils.getAnnotation(field, Validate.class) != null;

    public TestObjectVisitor() {
        super();
    }

    @Override
    public boolean visitObjectStart(TraversalCandidate candidate) {
        System.err.println("XXX Object Start [" + candidate.getTarget() + "]");
        return false;
    }

    @Override
    public void visitObjectEnd(TraversalCandidate candidate) {
        System.err.println("XXX Object End [" + candidate.getTarget() + "]");
    }

    /*@Override
    public boolean visitObjectStart(Object target) {
        System.err.println("Object Start : [" + target + "]");
        return true;
    }

    @Override
    public void visitObjectEnd(Object target) {
        System.err.println("Object End : [" + target + "]");
    }

    @Override
    public boolean visitField(Field field, Object value, Object parent) {
        System.err.println("XXX Field [" + field.getName() + "] value [" + value + "]");
        return validateCheck.test(field);
    }

    @Override
    public boolean visitCollection(Field field, Collection<?> values, Object parent) {
        System.err.println("XXX Collection [" + field.getName() + "]");
        return validateCheck.test(field);
    }

    @Override
    public boolean visitArray(Field field, Object values, Object parent) {
        System.err.println("XXX Array [" + field.getName() + "]");
        return validateCheck.test(field);
    }

    @Override
    public boolean visitMap(Field field, Map<?, ?> values, Object parent) {
        System.err.println("XXX Map [" + field.getName() + "]");
        return true;
    }

    @Override
    public boolean visitMapKeys(Field field, Map<?, ?> map, Object parent) {
        return validateCheck.test(field);
    }

    @Override
    public boolean visitMapValues(Field field, Map<?, ?> map, Object parent) {
        return validateCheck.test(field);
    }*/
}
