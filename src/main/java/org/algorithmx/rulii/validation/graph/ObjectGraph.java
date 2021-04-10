package org.algorithmx.rulii.validation.graph;

import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.lib.spring.util.ConcurrentReferenceHashMap;
import org.algorithmx.rulii.util.reflect.ReflectionUtils;
import org.algorithmx.rulii.validation.beans.AnnotatedBeanTypeDefinition;
import org.algorithmx.rulii.validation.beans.AnnotatedBeanTypeDefinitionBuilder;
import org.algorithmx.rulii.validation.beans.SourceHolder;
import org.algorithmx.rulii.validation.extract.ExtractorRegistry;
import org.algorithmx.rulii.validation.types.AnnotatedTypeDefinition;
import org.algorithmx.rulii.validation.types.AnnotatedTypeValueExtractor;
import org.algorithmx.rulii.validation.types.ExtractedTypeValue;

import java.lang.annotation.Annotation;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Map;

public class ObjectGraph {

    private static final Map<Class<?>, AnnotatedBeanTypeDefinition> definitionMap = new ConcurrentReferenceHashMap<>();

    private final Deque<TraversalCandidate> candidates = new ArrayDeque<>();

    private final ExtractorRegistry extractorRegistry;
    private final Class<? extends Annotation> markerAnnotation;
    private final Class<? extends Annotation> introspectionAnnotation;

    public ObjectGraph(Class<? extends Annotation> markerAnnotation,
                       Class<? extends Annotation> introspectionAnnotation,
                       ExtractorRegistry extractorRegistry) {
        super();
        Assert.notNull(markerAnnotation, "markerAnnotation cannot be null.");
        Assert.notNull(introspectionAnnotation, "introspectionAnnotation cannot be null.");
        Assert.notNull(extractorRegistry, "extractorRegistry cannot be null.");
        this.markerAnnotation = markerAnnotation;
        this.introspectionAnnotation = introspectionAnnotation;
        this.extractorRegistry = extractorRegistry;
    }

    public void traverse(TraversalCandidate root, ObjectVisitor visitor) {
        Assert.notNull(visitor, "visitor cannot be null.");
        candidates.clear();

        // Add the root object(s)
        addCandidate(root);

        while (!candidates.isEmpty()) {
            TraversalCandidate candidate = candidates.remove();

            try {
                traverseInternal(candidate, visitor);
            } catch (Exception e) {
                throw new ObjectGraphTraversalException("Error trying to traverse [" + candidate + "]", e);
            }
        }

        visitor.traversalComplete();
    }

    private void traverseInternal(TraversalCandidate candidate, ObjectVisitor visitor) {
        visitor.visitCandidate(candidate);

        if (visitor.isIntrospectionRequired(candidate)) {
            addCandidates(introspectCandidate(candidate));
        }
    }

    private void addCandidates(List<TraversalCandidate> candidates) {
        if (candidates == null || candidates.size() == 0) return;

        candidates.stream()                // If it's a java core class; no need to traverse further
                .filter(c -> c != null && !ReflectionUtils.isJavaCoreClass(c.getClass()))
                .forEach(c -> addCandidate(c));
    }

    private void addCandidate(TraversalCandidate candidate) {
        this.candidates.add(candidate);
    }

    protected List<TraversalCandidate> introspectCandidate(TraversalCandidate candidate) {

        if (candidate.isNull()) return Collections.emptyList();
        if (candidate.getTypeDefinition() != null
                && !candidate.getTypeDefinition().isIntrospectionRequired()) return Collections.emptyList();

        // TODO : Handle @Validate for Collection/Arrays or any extractable type
        if (ReflectionUtils.isJavaCoreClass(candidate.getTarget().getClass())) return Collections.emptyList();

        AnnotatedBeanTypeDefinition typeDefinition = getAnnotatedBeanTypeDefinition(candidate.getTarget().getClass());
        List<TraversalCandidate> result = new ArrayList<>();

        result.addAll(findCandidates(typeDefinition.getFields(), candidate));
        result.addAll(findCandidates(typeDefinition.getProperties(), candidate));

        return result;
    }

    protected List<TraversalCandidate> findCandidates(SourceHolder[] fields, TraversalCandidate candidate) {
        List<TraversalCandidate> result = new ArrayList<>();

        for (SourceHolder holder : fields) {
            List<TraversalCandidate> candidates = extractCandidates(holder, holder.getValue(candidate.getTarget()), holder.getDefinition());
            candidates.stream()
                    .filter(c -> c != null)
                    .filter(c -> c.getTypeDefinition().requiresProcessing())
                    .filter(c -> !ReflectionUtils.isJavaCoreClass(c.getClass()))
                    .forEach(c -> {
                        c.setParent(candidate);
                        result.add(c);
                    });
        }

        return result;
    }

    protected List<TraversalCandidate> extractCandidates(SourceHolder sourceHolder, Object target,
                                                         AnnotatedTypeDefinition typeDefinition) {
        AnnotatedTypeValueExtractor valueExtractor = new AnnotatedTypeValueExtractor();
        List<ExtractedTypeValue> extractedValues = valueExtractor.extract(typeDefinition, target, extractorRegistry);
        List<TraversalCandidate> result = new ArrayList<>();

        for (ExtractedTypeValue extractedValue : extractedValues) {
            if (extractedValue.getDefinition().hasDeclaredRules() || extractedValue.getDefinition().isIntrospectionRequired())
                result.add(new TraversalCandidate(extractedValue.getObject(), sourceHolder.copy(extractedValue.getDefinition())));
        }

        return result;
    }

    protected AnnotatedBeanTypeDefinition getAnnotatedBeanTypeDefinition(Class<?> type) {
        AnnotatedBeanTypeDefinition result = definitionMap.get(type);

        if (result != null) return result;

        AnnotatedBeanTypeDefinitionBuilder builder = AnnotatedBeanTypeDefinitionBuilder
                .with(type, getMarkerAnnotation(), getIntrospectionAnnotation());

        builder.loadFields();
        builder.loadProperties();
        builder.loadMethods();

        result = builder.build();
        definitionMap.putIfAbsent(type, result);

        return result;
    }

    public ExtractorRegistry getExtractorRegistry() {
        return extractorRegistry;
    }

    public Class<? extends Annotation> getMarkerAnnotation() {
        return markerAnnotation;
    }

    public Class<? extends Annotation> getIntrospectionAnnotation() {
        return introspectionAnnotation;
    }
}
