package org.algorithmx.rulii.validation.graph;

import java.util.Collection;

public interface ObjectVisitor {

    Collection<GraphNode> visitCandidate(GraphNode candidate);

    default void traversalComplete() {}
}
