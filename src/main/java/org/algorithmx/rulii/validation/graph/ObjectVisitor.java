package org.algorithmx.rulii.validation.graph;

public interface ObjectVisitor {

    boolean visitCandidate(TraversalCandidate candidate);

    default void visitObjectEnd(TraversalCandidate candidate) {}

    default void traversalComplete() {}
}
