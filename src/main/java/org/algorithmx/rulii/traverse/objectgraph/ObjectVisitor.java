package org.algorithmx.rulii.traverse.objectgraph;

import org.algorithmx.rulii.traverse.types.AnnotatedTypeDefinition;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

public interface ObjectVisitor {

    default boolean isCandidate(Class<?> field) { return true; }

    default boolean visitObjectStart(TraversalCandidate candidate) { return true; }
    default void visitObjectEnd(TraversalCandidate candidate) {}

    /*default TraversalCandidate[] extractAnnotatedTypes(Object target, AnnotatedTypeDefinition typeDefinition) { return null; }

    default boolean isCandidate(Field field) { return true; }

    default boolean visitNull(Field field, Object value, Object parent) { return false; }

    default boolean visitField(Field field, Object value, Object parent) { return true; }

    default boolean visitArray(Field field, Object values, Object parent) { return true; }

    default boolean visitCollection(Field field, Collection<?> values, Object parent) { return true; }

    default boolean visitMap(Field field, Map<?, ?> map, Object parent) { return true; }

    default boolean visitMapKeys(Field field, Map<?, ?> map, Object parent) { return true; }

    default boolean visitMapValues(Field field, Map<?, ?> map, Object parent) { return true; }
*/
    default void traversalComplete() {}
}
