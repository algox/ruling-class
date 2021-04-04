package org.algorithmx.rulii.validation.graph;

public interface ObjectVisitor {

    TraversalCandidate[] visitObjectStart(TraversalCandidate candidate);

    default void visitObjectEnd(TraversalCandidate candidate) {}

    /*

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
