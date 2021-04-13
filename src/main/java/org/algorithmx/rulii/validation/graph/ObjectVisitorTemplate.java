package org.algorithmx.rulii.validation.graph;

import org.algorithmx.rulii.annotation.ValidationMarker;
import org.algorithmx.rulii.lib.spring.util.ConcurrentReferenceHashMap;
import org.algorithmx.rulii.validation.beans.AnnotatedBeanTypeDefinition;
import org.algorithmx.rulii.validation.beans.AnnotatedBeanTypeDefinitionBuilder;
import org.algorithmx.rulii.validation.beans.SourceHolder;
import org.algorithmx.rulii.validation.extract.ExtractorRegistry;
import org.algorithmx.rulii.validation.types.AnnotatedTypeDefinition;
import org.algorithmx.rulii.validation.types.AnnotatedTypeValueExtractor;
import org.algorithmx.rulii.validation.types.ExtractedTypeValue;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public abstract class ObjectVisitorTemplate implements ObjectVisitor {

    private static final Map<Class<?>, AnnotatedBeanTypeDefinition> definitionMap = new ConcurrentReferenceHashMap<>();

    public ObjectVisitorTemplate() {
        super();
    }

    /*protected ObjectVisitorTemplate(Predicate<Field> fieldFilter, Class<?>...ignoredClasses) {
        this(c -> {
            Set<Class<?>> ignoredClassSet = new HashSet<>(ignoredClasses != null
                    ? Arrays.asList(ignoredClasses)
                    : new ArrayList<>());
            return ignoredClassSet.contains(c);
        }, fieldFilter);
    }

    protected ObjectVisitorTemplate(Predicate<Class<?>> classFilter, Predicate<Field> fieldFilter) {
        super();
        this.classFilter = classFilter;
        this.fieldFilter = fieldFilter;
    }*/

    protected List<TraversalCandidate> introspectCandidate(TraversalCandidate candidate, ExtractorRegistry extractorRegistry) {

        if (candidate.isNull()) return Collections.emptyList();
        if (candidate.getTypeDefinition() != null
                && !candidate.getTypeDefinition().isIntrospectionRequired()) return Collections.emptyList();

        // TODO : Handle @Validate for Collection/Arrays or any extractable type
        Predicate<Class<?>> classFilter = getClassFilter();

        if (classFilter != null && !classFilter.test(candidate.getTarget().getClass())) return Collections.emptyList();

        AnnotatedBeanTypeDefinition typeDefinition = getAnnotatedBeanTypeDefinition(candidate.getTarget().getClass());
        List<TraversalCandidate> result = new ArrayList<>();

        result.addAll(findCandidates(typeDefinition.getFields(), candidate, extractorRegistry));
        result.addAll(findCandidates(typeDefinition.getProperties(), candidate, extractorRegistry));

        return result;
    }


    protected List<TraversalCandidate> findCandidates(SourceHolder[] fields, TraversalCandidate candidate,
                                                      ExtractorRegistry extractorRegistry) {
        List<TraversalCandidate> result = new ArrayList<>();

        for (SourceHolder holder : fields) {
            List<TraversalCandidate> candidates = extractCandidates(holder,
                    holder.getValue(candidate.getTarget()),
                    holder.getDefinition(),
                    extractorRegistry);

            candidates.stream()
                    .filter(c -> c != null)
                    .forEach(c -> {
                        c.setParent(candidate);
                        result.add(c);
                    });
        }

        return result;
    }

    protected List<TraversalCandidate> extractCandidates(SourceHolder sourceHolder, Object target,
                                                         AnnotatedTypeDefinition typeDefinition,
                                                         ExtractorRegistry extractorRegistry) {
        AnnotatedTypeValueExtractor valueExtractor = new AnnotatedTypeValueExtractor();
        List<ExtractedTypeValue> extractedValues = valueExtractor.extract(typeDefinition, target, extractorRegistry);
        List<TraversalCandidate> result = new ArrayList<>();

        for (ExtractedTypeValue extractedValue : extractedValues) {

            if (extractedValue.getDefinition().hasDeclaredRules()) {
                result.add(new TraversalCandidate(extractedValue.getValue(), sourceHolder.copy(extractedValue.getDefinition())));
            } else if (extractedValue.getDefinition().isIntrospectionRequired()) {
                Object value = extractedValue.getValue();
                Predicate<Class<?>> classFilter = getClassFilter();

                if (value != null && (classFilter == null || !classFilter.test(value.getClass()))) {
                    result.add(new TraversalCandidate(value, sourceHolder.copy(extractedValue.getDefinition())));
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
                .with(type, getMarkerAnnotation(), getIntrospectionAnnotation());

        result.loadFields(getFieldFilter());
        result.loadProperties(getPropertyFilter());
        result.loadMethods(getMethodFilter());

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
}
