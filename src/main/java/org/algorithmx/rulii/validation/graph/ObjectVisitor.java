package org.algorithmx.rulii.validation.graph;

public interface ObjectVisitor {

    TraversalCandidate[] visitObjectStart(TraversalCandidate candidate);

    default void visitObjectEnd(TraversalCandidate candidate) {}

    default void traversalComplete() {}
}
