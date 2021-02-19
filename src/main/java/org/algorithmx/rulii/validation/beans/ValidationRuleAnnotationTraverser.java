package org.algorithmx.rulii.validation.beans;

import org.algorithmx.rulii.lib.spring.core.annotation.MergedAnnotations;
import org.algorithmx.rulii.lib.spring.core.annotation.RepeatableContainers;
import org.algorithmx.rulii.validation.annotation.ValidationRule;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;

public class ValidationRuleAnnotationTraverser {

    public ValidationRuleAnnotationTraverser() {
        super();
    }

    public Annotation[] traverse(AnnotatedElement element) {
        List<Annotation> result = new ArrayList<>();
        MergedAnnotations.from(element, MergedAnnotations.SearchStrategy.DIRECT)
                .stream(ValidationRule.class)
                .forEach(a -> result.add(a.getMetaSource().synthesize()));
        return result.toArray(new Annotation[result.size()]);
    }

    public Annotation[] traverse(AnnotatedElement element, Annotation[] annotations) {
        List<Annotation> result = new ArrayList<>();
        MergedAnnotations.from(element, annotations, RepeatableContainers.standardRepeatables())
                .stream(ValidationRule.class)
                .forEach(a -> result.add(a.getMetaSource().synthesize()));
        return result.toArray(new Annotation[result.size()]);
    }
}
