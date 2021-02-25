package org.algorithmx.rulii.util.objectgraph;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface ObjectVisitor {

    default boolean isCandidate(Class<?> field) { return true; }

    default boolean visitObjectStart(Object target) { return true; }

    default void visitObjectEnd(Object target) {}

    default boolean isCandidate(Field field) { return true; }

    default boolean visitNull(Field field, Object value, Object parent) { return false; }

    default boolean visitField(Field field, Object value, Object parent) { return true; }

    default boolean visitArray(Field field, Object values, Object parent) { return true; }

    default boolean visitCollection(Field field, Collection<?> values, Object parent) { return true; }

    default boolean visitMap(Field field, Map<?, ?> map, Object parent) { return true; }

    default boolean visitMapKeys(Field field, Map<?, ?> map, Object parent) { return true; }

    default boolean visitMapValues(Field field, Map<?, ?> map, Object parent) { return true; }

    default void traversalComplete() {}
}
