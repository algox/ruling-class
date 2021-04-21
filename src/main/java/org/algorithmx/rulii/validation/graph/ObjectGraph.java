package org.algorithmx.rulii.validation.graph;

import org.algorithmx.rulii.lib.spring.util.Assert;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;

public class ObjectGraph {

    private final Deque<GraphNode> candidates = new ArrayDeque<>();

    public ObjectGraph() {
        super();
    }

    public void traverse(GraphNode root, ObjectVisitor visitor) {
        Assert.notNull(visitor, "visitor cannot be null.");
        candidates.clear();

        // Add the root object(s)
        addCandidate(root);

        while (!candidates.isEmpty()) {
            GraphNode candidate = candidates.remove();

            try {
                traverseInternal(candidate, visitor);
            } catch (Exception e) {
                throw new ObjectGraphTraversalException("Error trying to traverse [" + candidate + "]", e);
            }
        }

        visitor.traversalComplete();
    }

    protected void traverseInternal(GraphNode candidate, ObjectVisitor visitor) {
        addCandidates(visitor.visitCandidate(candidate));
    }

    protected void addCandidates(Collection<GraphNode> candidates) {
        if (candidates == null || candidates.size() == 0) return;

        candidates.stream()
                .filter(c -> c != null)
                .forEach(c -> addCandidate(c));
    }

    protected void addCandidate(GraphNode candidate) {
        getCandidates().add(candidate);
    }

    protected Deque<GraphNode> getCandidates() {
        return candidates;
    }
}
