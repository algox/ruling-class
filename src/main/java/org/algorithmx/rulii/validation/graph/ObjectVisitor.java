package org.algorithmx.rulii.validation.graph;

import java.util.Collection;

public interface ObjectVisitor {

    Collection<TraversalCandidate> visitCandidate(TraversalCandidate candidate);

    default void traversalComplete() {}
}
