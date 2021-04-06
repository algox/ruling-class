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
            TraversalCandidate candidate = this.candidates.remove();

            try {
                traverseObject(candidate, visitor);
            } catch (Exception e) {
                throw new ObjectGraphTraversalException("Error trying to traverse [" + candidate + "]", e);
            }
        }

        visitor.traversalComplete();

    }

    private void traverseObject(TraversalCandidate candidate, ObjectVisitor visitor) {
        if (candidate.getTarget() == null) return;
        boolean requiredIntrospection = visitor.visitCandidate(candidate);

        if (requiredIntrospection) {
            List<TraversalCandidate> candidates = introspectCandidate(candidate);
            if (candidates != null) addCandidates(candidates);
        }

        visitor.visitObjectEnd(candidate);
    }

    private void addCandidates(List<TraversalCandidate> candidates) {
        if (candidates == null) return;

        candidates.stream()
                .filter(c -> c != null)
                .forEach(c -> addCandidate(c));
    }

    private void addCandidate(TraversalCandidate candidate) {
        if (candidate == null) return;
        // It's a java core class; no need to traverse further.
        if (ReflectionUtils.isJavaCoreClass(candidate.getClass())) return;
        // Looks like we have a viable candidate
        candidates.add(candidate);
    }

    protected List<TraversalCandidate> introspectCandidate(TraversalCandidate candidate) {

        /*System.err.println("XXX Object Start [" + candidate.getTarget().getClass().getSimpleName() + "] Field ["
                + candidate.getName() + "] Value [" + candidate.getTarget() + "]  Introspect ["
                + (candidate.getTypeDefinition() != null ? candidate.getTypeDefinition().requiresIntrospection() : false)
                + "] Rules [" + (candidate.getTypeDefinition() != null ? candidate.getTypeDefinition().hasDeclaredRules() : false) + "]");*/

        if (candidate.isNull()) return null;
        if (candidate.getTypeDefinition() != null && !candidate.getTypeDefinition().requiresIntrospection()) return null;
        // TODO : Handle @Validate for Collection/Arrays or any extractable type
        if (ReflectionUtils.isJavaCoreClass(candidate.getTarget().getClass())) return null;

        AnnotatedBeanTypeDefinition typeDefinition = getAnnotatedBeanTypeDefinition(candidate.getTarget().getClass());

        List<TraversalCandidate> result = new ArrayList<>();

        result.addAll(findCandidates(typeDefinition.getFields(), candidate.getTarget()));
        result.addAll(findCandidates(typeDefinition.getProperties(), candidate.getTarget()));

        return result;
    }

    protected List<TraversalCandidate> findCandidates(SourceHolder[] fields, Object target) {
        List<TraversalCandidate> result = new ArrayList<>();

        for (SourceHolder holder : fields) {
            List<TraversalCandidate> candidates = extractCandidates(holder, holder.getValue(target), holder.getDefinition());
            candidates.stream()
                    .filter(c -> c.getTypeDefinition().requiresProcessing())
                    .forEach(c -> result.add(c));
        }

        return result;
    }

    protected List<TraversalCandidate> extractCandidates(SourceHolder sourceHolder, Object target,
                                                         AnnotatedTypeDefinition typeDefinition) {
        AnnotatedTypeValueExtractor valueExtractor = new AnnotatedTypeValueExtractor();
        List<ExtractedTypeValue> extractedValues = valueExtractor.extract(typeDefinition, target, extractorRegistry);
        List<TraversalCandidate> result = new ArrayList<>();

        for (ExtractedTypeValue extractedValue : extractedValues) {
            if (extractedValue.getDefinition().hasDeclaredRules() || extractedValue.getDefinition().requiresIntrospection())
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
