package org.algorithmx.rulii.validation.graph;

public interface ObjectVisitor {

    void visitCandidate(TraversalCandidate candidate);

    boolean isIntrospectionRequired(TraversalCandidate candidate);

    default void traversalComplete() {}
}
