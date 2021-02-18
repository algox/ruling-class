package org.algorithmx.rulii.validation.beans;


import org.algorithmx.rulii.lib.spring.core.annotation.AnnotatedElementUtils;
import org.algorithmx.rulii.lib.spring.core.annotation.AnnotationUtils;
import org.algorithmx.rulii.lib.spring.util.ClassUtils;
import org.algorithmx.rulii.validation.annotation.ValidationRule;
import org.algorithmx.rulii.validation.annotation.ValidationRuleContainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.HashSet;
import java.util.Set;

public class ValidationRuleAnnotationTraverser {

    public ValidationRuleAnnotationTraverser() {
        super();
    }

    public Annotation[] traverse(AnnotatedElement element) {
        return traverse(element, AnnotationUtils.getAnnotations(element));
    }

    protected Annotation[] traverse(AnnotatedElement element, Annotation[] annotations) {
        try {
            return traverseInternal(element, annotations);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    protected Annotation[] traverseInternal(AnnotatedElement element, Annotation[] annotations) throws ClassNotFoundException {
        Set<String> annotationNames = new HashSet<>();
        Set<Class<? extends Annotation>> candidates = new HashSet<>();
        Set<Annotation> result = new HashSet<>();

        for (Annotation annotation : annotations) {
            if (isAnnotated(annotation.annotationType(), ValidationRule.class)) result.add(annotation);
            Set<String> names = AnnotatedElementUtils.getMetaAnnotationTypes(element, annotation.annotationType());
            if (names != null) annotationNames.addAll(names);
        }

        for (String annotationName : annotationNames) {
            if (annotationName.equals(ValidationRule.class.getName())) continue;
            Class<? extends Annotation> annotationType = (Class<? extends Annotation>) ClassUtils.forName(annotationName, null);
            ValidationRuleContainer container = annotationType.getAnnotation(ValidationRuleContainer.class);

            if (container != null) {
                for (Annotation annotation : AnnotatedElementUtils.findMergedRepeatableAnnotations(element, container.value())) {
                    if (isAnnotated(annotation.annotationType(), ValidationRule.class)) {
                        result.add(annotation);
                    }
                }
            } else {
                for (Annotation annotation : AnnotatedElementUtils.findAllMergedAnnotations(element, annotationType)) {
                    if (isAnnotated(annotation.annotationType(), ValidationRule.class)) {
                        result.add(annotation);
                    }
                }
            }

        }

        return result.toArray(new Annotation[candidates.size()]);
    }

    private boolean isAnnotated(Class<? extends Annotation> annotationType, Class<? extends Annotation> metaAnnotationType) {
        return annotationType.getAnnotation(metaAnnotationType) != null;
    }
}
