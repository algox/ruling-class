package org.algorithmx.rules.util.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

public class AnnotationTraverser {

    private final static Map<CacheKey, Annotation[]> annotationCache = new HashMap<>();

    private final Deque<Annotation> candidates = new ArrayDeque<>();
    private final List<Annotation> result = new ArrayList<>();

    public AnnotationTraverser() {
        super();
    }

    public <T extends Annotation> Annotation[] traverse(AnnotatedElement root, Class<T> annotationClass) {
        CacheKey key = new CacheKey(root, annotationClass);

        if (annotationCache.containsKey(root)) return annotationCache.get(key);

        Predicate<Annotation> check = a -> a.annotationType().getAnnotation(annotationClass) != null;

        result.clear();
        candidates.clear();
        candidates.addAll(Arrays.asList(root.getDeclaredAnnotations()));

        while (!candidates.isEmpty()) {
            Annotation candidate = candidates.remove();
            traverseAnnotation(candidate, check);
        }

        Annotation[] annotations = result.toArray(new Annotation[result.size()]);
        annotationCache.put(key, annotations);
        return annotations;
    }

    private void traverseAnnotation(Annotation target, Predicate<Annotation> check) {
        if (check.test(target)) result.add(target);
        candidates.addAll(Arrays.asList(target.annotationType().getDeclaredAnnotations()));
    }

    private static class CacheKey {
        private AnnotatedElement element;
        private Class<? extends Annotation> annotationClass;

        public CacheKey(AnnotatedElement element, Class<? extends Annotation> annotationClass) {
            super();
            this.element = element;
            this.annotationClass = annotationClass;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CacheKey cacheKey = (CacheKey) o;
            return element.equals(cacheKey.element) &&
                    annotationClass.equals(cacheKey.annotationClass);
        }

        @Override
        public int hashCode() {
            return Objects.hash(element, annotationClass);
        }
    }
}
