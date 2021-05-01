package org.algorithmx.rulii.validation.graph;

import org.algorithmx.rulii.annotation.ValidationMarker;
import org.algorithmx.rulii.lib.spring.util.ConcurrentReferenceHashMap;
import org.algorithmx.rulii.util.reflect.ObjectFactory;
import org.algorithmx.rulii.validation.beans.AnnotatedBeanTypeDefinition;
import org.algorithmx.rulii.validation.beans.AnnotatedBeanTypeDefinitionBuilder;
import org.algorithmx.rulii.validation.beans.SourceHolder;
import org.algorithmx.rulii.validation.extract.ExtractorRegistry;
import org.algorithmx.rulii.validation.types.AnnotatedTypeDefinition;
import org.algorithmx.rulii.validation.extract.AnnotatedTypeValueExtractor;
import org.algorithmx.rulii.validation.extract.ExtractedTypeValue;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public abstract class AbstractObjectVisitor implements ObjectVisitor {

    private static final Map<Class<?>, AnnotatedBeanTypeDefinition> definitionMap = new ConcurrentReferenceHashMap<>();

    public AbstractObjectVisitor() {
        super();
    }

    protected List<GraphNode> introspectCandidate(GraphNode candidate, ExtractorRegistry extractorRegistry, ObjectFactory objectFactory) {

        if (candidate.isNull()) return Collections.emptyList();
        if (candidate.getTypeDefinition() != null
                && !candidate.getTypeDefinition().isIntrospectionRequired()) return Collections.emptyList();

        // TODO : Handle @Validate for Collection/Arrays or any extractable type
        Predicate<Class<?>> classFilter = getClassFilter();

        if (classFilter != null && !classFilter.test(candidate.getTarget().getClass())) return Collections.emptyList();

        AnnotatedBeanTypeDefinition typeDefinition = getAnnotatedBeanTypeDefinition(candidate.getTarget().getClass());
        List<GraphNode> result = new ArrayList<>();

        result.addAll(findCandidates(typeDefinition.getFields(), candidate, extractorRegistry, objectFactory));
        result.addAll(findCandidates(typeDefinition.getProperties(), candidate, extractorRegistry, objectFactory));

        return result;
    }


    protected List<GraphNode> findCandidates(SourceHolder[] fields, GraphNode candidate,
                                             ExtractorRegistry extractorRegistry, ObjectFactory objectFactory) {
        List<GraphNode> result = new ArrayList<>();

        for (SourceHolder holder : fields) {
            List<GraphNode> candidates = extractCandidates(holder,
                    holder.getValue(candidate.getTarget()),
                    holder.getDefinition(),
                    extractorRegistry, objectFactory);

            candidates.stream()
                    .filter(c -> c != null)
                    .forEach(c -> {
                        c.setParent(candidate);
                        result.add(c);
                    });
        }

        return result;
    }

    protected List<GraphNode> extractCandidates(SourceHolder sourceHolder, Object target,
                                                AnnotatedTypeDefinition typeDefinition,
                                                ExtractorRegistry extractorRegistry,
                                                ObjectFactory objectFactory) {
        AnnotatedTypeValueExtractor valueExtractor = new AnnotatedTypeValueExtractor();
        List<ExtractedTypeValue> extractedValues = valueExtractor.extract(typeDefinition, target, extractorRegistry, objectFactory);
        List<GraphNode> result = new ArrayList<>();

        for (ExtractedTypeValue extractedValue : extractedValues) {

            if (extractedValue.getDefinition().hasDeclaredRules()) {
                result.add(new GraphNode(extractedValue.getValue(), sourceHolder.copy(extractedValue.getDefinition())));
            } else if (extractedValue.getDefinition().isIntrospectionRequired()) {
                Object value = extractedValue.getValue();
                Predicate<Class<?>> classFilter = getClassFilter();

                if (value != null && (classFilter == null || classFilter.test(value.getClass()))) {
                    result.add(new GraphNode(value, sourceHolder.copy(extractedValue.getDefinition())));
                }
            }
        }

        return result;
    }

    protected AnnotatedBeanTypeDefinition getAnnotatedBeanTypeDefinition(Class<?> type) {
        return definitionMap.computeIfAbsent(type, t -> buildBeanDefinition(t));
    }

    protected AnnotatedBeanTypeDefinition buildBeanDefinition(Class<?> type) {
        AnnotatedBeanTypeDefinitionBuilder result = AnnotatedBeanTypeDefinitionBuilder
                .with(type, getIntrospectionAnnotation(), getMarkerAnnotation());

        result.loadFields(getFieldFilter(), getFieldComparator());
        result.loadProperties(getPropertyFilter(), getPropertyComparator());
        result.loadMethods(getMethodFilter(), getMethodComparator());

        return result.build();
    }

    protected Class<? extends Annotation> getMarkerAnnotation() {
        return ValidationMarker.class;
    }

    protected abstract Class<? extends Annotation> getIntrospectionAnnotation();

    protected Predicate<Class<?>> getClassFilter() {
        return null;
    }

    protected Predicate<Field> getFieldFilter() {
        return null;
    }

    protected Predicate<PropertyDescriptor> getPropertyFilter() {
        return null;
    }

    protected Predicate<Method> getMethodFilter() {
        return null;
    }

    protected Comparator<Field> getFieldComparator() {
        return null;
    }

    protected Comparator<PropertyDescriptor> getPropertyComparator() {
        return null;
    }

    protected Comparator<Method> getMethodComparator() {
        return null;
    }
}
