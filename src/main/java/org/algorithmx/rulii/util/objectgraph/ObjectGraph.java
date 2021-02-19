package org.algorithmx.rulii.util.objectgraph;

import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.lib.spring.util.ReflectionUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ObjectGraph {

    private static final Predicate<Class<?>> JAVA_CORE_CLASSES = (clazz) ->
            clazz == null || clazz.isPrimitive() || clazz.getPackage() == null
                    || clazz.getClassLoader() == null
                    || clazz.getPackage().getName().startsWith("java.")
                    || clazz.getPackage().getName().startsWith("javax.");


    private final static Map<Class<?>, ClassFields> classCache = new HashMap<>();

    private final Map<Object, Class<?>> breadCrumbs = new IdentityHashMap<>();
    private final Deque<Object> candidates = new ArrayDeque<>();

    private final boolean ordered;

    public ObjectGraph() {
        this(false);
    }

    public ObjectGraph(boolean ordered) {
        super();
        this.ordered = ordered;
    }

    public void traverse(Object target, ObjectVisitor visitor) throws ObjectGraphTraversalException {
        Assert.notNull(visitor, "visitor cannot be null.");
        candidates.clear();
        breadCrumbs.clear();
        // Add the root object
        addCandidate(target, visitor);

        while (!candidates.isEmpty()) {
            Object candidate = candidates.remove();

            try {
                traverseObject(candidate, visitor);
            } catch (Exception e) {
                throw new ObjectGraphTraversalException("Error trying to traverse [" + candidate + "]", e);
            }
        }

        visitor.traversalComplete();
    }

    private void traverseObject(Object target, ObjectVisitor visitor) throws IllegalAccessException {
        if (target == null) return;

        try {
            boolean introspectObject = visitor.visitObjectStart(target);

            if (introspectObject) {
                processFields(target, visitor);
            }
        } finally {
            visitor.visitObjectEnd(target);
        }
    }

    private void addCandidate(Object candidate, ObjectVisitor visitor) {
        if (candidate == null) return;

        try {
            if (candidate.getClass().isArray()) {
                addArray(candidate, visitor);
            } else if (candidate instanceof Collection) {
                addCollection(candidate, visitor);
            } else if (candidate instanceof Map) {
                addMap(candidate, visitor);
            } else {
                // Nothing to traverse
                if (candidate == null) return;
                // It's a java core class; no need to traverse further.
                if (JAVA_CORE_CLASSES.test(candidate.getClass())) return;
                // Check to see if the visitor is interested in this Class
                if (!visitor.isCandidate(candidate.getClass())) return;
                // Have have already visited this object?
                if (breadCrumbs.containsKey(candidate)) return;
                // Looks like we have a viable candidate
                candidates.add(candidate);
            }
        } finally {
            breadCrumbs.put(candidate, candidate.getClass());
        }
    }

    private void addArray(Object target, ObjectVisitor visitor) {
        // Nothing to traverse
        if (target == null) return;
        // It's not an array
        if (!target.getClass().isArray())
            throw new IllegalStateException("Invalid type. It should be an Array. " +
                    "Given [" + target.getClass() + "]");
        // Looks like a primitive array. No need to traverse any further.
        if (target.getClass().getComponentType().isPrimitive()) return;

        for (int i = 0; i < Array.getLength(target); i++) {
            Object element = Array.get(target, i);
            addCandidate(element, visitor);
        }
    }

    private void addCollection(Object target, ObjectVisitor visitor) {
        // Nothing to traverse
        if (target == null) return;
        // It's not an Collection
        if (!(target instanceof Collection))
            throw new IllegalStateException("Invalid type. It should be an Collection. " +
                    "Given [" + target.getClass() + "]");

        Collection collection = (Collection) target;

        for (Object element : collection) {
            addCandidate(element, visitor);
        }
    }

    private void addMap(Object target, ObjectVisitor visitor) {
        // Nothing to traverse
        if (target == null) return;
        // It's not an Map
        if (!(target instanceof Map))
            throw new IllegalStateException("Invalid type. It should be an Map. " +
                    "Given [" + target.getClass() + "]");

        Map<?, ?> map = (Map) target;

        for (Map.Entry<?, ?> e : map.entrySet()) {
            // First visit the Key
            addCandidate(e.getKey(), visitor);
            // First visit the Value
            addCandidate(e.getValue(), visitor);
        }
    }

    private void processFields(Object target, ObjectVisitor visitor) throws IllegalAccessException {

        ClassFields classFields = classCache.get(target);

        if (classFields == null) {
            List<Field> candidateFields = new ArrayList<>();

            ReflectionUtils.doWithFields(target.getClass(), field -> {
                if (visitor.isCandidate(field)) {
                    candidateFields.add(field);
                }
            });

            classFields = new ClassFields(candidateFields.toArray(new Field[candidateFields.size()]), isOrdered());
            classCache.put(target.getClass(), classFields);
        }

        for (Field field : classFields.fields) {
            //field.setAccessible(true);
            ReflectionUtils.makeAccessible(field);
            Object value = field.get(target);
            boolean introspectField = false;

            if (value == null) {
                visitor.visitNull(field, null, target);
            } else if (value instanceof Collection) {
                introspectField = visitor.visitCollection(field, (Collection) value, target);
            } else if (value instanceof Map) {
                introspectField = visitor.visitMap(field, (Map) value, target);
            } else if (value.getClass().isArray()) {
                introspectField = visitor.visitArray(field, value, target);
            } else {
                introspectField = visitor.visitField(field, value, target);
            }

            if (introspectField) addCandidate(value, visitor);
        }
    }

    public boolean isOrdered() {
        return ordered;
    }

    private static class ClassFields {
        private Field[] fields;

        private ClassFields(Field[] fields, boolean sorted) {
            super();
            if (fields != null && sorted) Arrays.sort(fields, Comparator.comparing(Field::getName));
            this.fields = fields;
        }
    }
}
