package org.algorithmx.rulii.validation.beans;

import org.algorithmx.rulii.config.RuliiSystem;
import org.algorithmx.rulii.core.rule.Rule;
import org.algorithmx.rulii.lib.spring.core.ParameterNameDiscoverer;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.lib.spring.util.ReflectionUtils;
import org.algorithmx.rulii.util.reflect.ObjectFactory;
import org.algorithmx.rulii.traverse.AnnotatedRuleBuilder;
import org.algorithmx.rulii.annotation.ValidationRule;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class BeanValidationRuleBuilder {

    private final Class<?> beanClass;
    private final Map<Object, RuleHolder> rules = new LinkedHashMap<>();

    private ObjectFactory objectFactory = RuliiSystem.getInstance().getObjectFactory();

    private BeanValidationRuleBuilder(Class<?> beanClass) {
        super();
        Assert.notNull(beanClass, "beanClass cannot be null.");
        this.beanClass = beanClass;
    }

    public static BeanValidationRuleBuilder with(Class<?> beanClass) {
        return new BeanValidationRuleBuilder(beanClass);
    }

    public BeanValidationRuleBuilder objectFactory(ObjectFactory objectFactory) {
        Assert.notNull(objectFactory, "objectFactory cannot be null.");
        this.objectFactory = objectFactory;
        return this;
    }

    public BeanValidationRuleBuilder loadAnnotatedFields() {
        ReflectionUtils.doWithFields(beanClass, new FieldHandler());
        return this;
    }

    public BeanValidationRuleBuilder loadAnnotatedMethods() {
        ReflectionUtils.doWithMethods(beanClass, new MethodHandler());
        return this;
    }

    public BeanValidationRuleBuilder loadAnnotatedConstructors() {
        CtorHandler handler = new CtorHandler();
        Arrays.stream(beanClass.getConstructors()).forEach(c -> handler.doWith(c));
        return this;
    }

    public BeanValidationRules build() {
        DefaultBeanValidationRules result = new DefaultBeanValidationRules(beanClass);
        rules.values().forEach(holder -> result.add(holder));
        return result;
    }

    public BeanValidationRuleBuilder rule(Rule<?> rule) {
        Assert.notNull(rule, "rule cannot be null.");

        ClassHolder holder = (ClassHolder) rules.get(beanClass);

        if (holder == null) {
            holder = new ClassHolder(beanClass);
            rules.put(beanClass, holder);
        }

        holder.addRule(rule);
        return this;
    }

    public BeanValidationRuleBuilder field(Field field, Rule<?> rule) {
        Assert.notNull(rule, "rule cannot be null.");
        FieldHolder holder = getFieldHolder(field);
        holder.addRule(rule);
        return this;
    }

    private FieldHolder getFieldHolder(Field field) {
        FieldHolder result = (FieldHolder) rules.get(field);
        if (result == null) {
            result = new FieldHolder(field);
            rules.put(field, result);
        }
        return result;
    }


    public BeanValidationRuleBuilder field(String name, Rule<?> rule) {
        Assert.notNull(rule, "rule cannot be null.");
        Field field = ReflectionUtils.findField(beanClass, name);
        if (field == null) throw new IllegalArgumentException("No such field [" + name + "] on class ["
                + beanClass + "] or its ancestors.");
        return field(field, rule);
    }

    public BeanValidationRuleBuilder ctor(Constructor ctor, Rule<?> rule) {
        CtorHolder holder = getCtorHolder(ctor);
        holder.addRule(rule);
        return this;
    }

    public BeanValidationRuleBuilder param(Constructor ctor, int index, Rule<?> rule) {
        CtorHolder holder = getCtorHolder(ctor);
        holder.addParameterRule(index, rule);
        return this;
    }

    private CtorHolder getCtorHolder(Constructor ctor) {
        CtorHolder result = (CtorHolder) rules.get(ctor);

        if (result == null) {
            result = new CtorHolder(ctor);
            rules.put(ctor, result);
        }

        return result;
    }

    public BeanValidationRuleBuilder method(Method method, Rule<?> rule) {
        MethodHolder holder = getMethodHolder(method);
        holder.addRule(rule);
        return this;
    }

    public BeanValidationRuleBuilder param(Method method, int index, Rule<?> rule) {
        MethodHolder holder = getMethodHolder(method);
        holder.addParameterRule(index, rule);
        return this;
    }

    private MethodHolder getMethodHolder(Method method) {
        MethodHolder result = (MethodHolder) rules.get(method);

        if (result == null) {
            result = new MethodHolder(method);
            rules.put(method, result);
        }

        return result;
    }

    private Rule[] buildRules(Annotation annotation, String bindingName) {
        ValidationRule validationRule = annotation.annotationType().getAnnotation(ValidationRule.class);
        if (validationRule == null) throw new IllegalStateException("Could not find @ValidationRule on [" + annotation + "]");
        AnnotatedRuleBuilder builder = objectFactory.create(validationRule.value(), true);
        return (Rule[]) builder.build(annotation, bindingName);
    }

    private class FieldHandler implements ReflectionUtils.FieldCallback {

        public FieldHandler() {
            super();
        }

        @Override
        public void doWith(Field field) throws IllegalArgumentException {
            ValidationRuleAnnotationTraverser traverser = new ValidationRuleAnnotationTraverser();
            Annotation[] annotations = traverser.traverse(field);

            for (Annotation annotation : annotations) {
                Rule[] rules = buildRules(annotation, field.getName());

                if (rules != null) {
                    Arrays.stream(rules).forEach(r -> field(field, r));
                }
            }
        }
    }

    private class MethodHandler implements ReflectionUtils.MethodCallback {
        private final ValidationRuleAnnotationTraverser traverser = new ValidationRuleAnnotationTraverser();

        public MethodHandler() {
            super();
        }

        @Override
        public void doWith(Method method) throws IllegalArgumentException {
            buildMethodRules(method);
            buildMethodParameterRules(method);
        }

        private void buildMethodRules(Method method) {
            Annotation[] annotations = traverser.traverse(method);

            for (Annotation annotation : annotations) {
                Rule[] rules = buildRules(annotation, method.getName());
                if (rules != null) {
                    if (rules != null) {
                        Arrays.stream(rules).forEach(r -> method(method, r));
                    }
                }
            }
        }

        private void buildMethodParameterRules(Method method) {
            if (method.getParameterCount() == 0) return;

            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            ParameterNameDiscoverer nameDiscoverer = ParameterNameDiscoverer.create();
            String[] parameterNames = nameDiscoverer.getParameterNames(method);

            for (int i = 0; i < parameterAnnotations.length; i++) {
                Annotation[] annotations = traverser.traverse(method, parameterAnnotations[i]);

                for (Annotation annotation : annotations) {
                    Rule[] rules = buildRules(annotation, parameterNames[i]);
                    if (rules != null) {
                        for (Rule rule : rules) {
                            param(method, i, rule);
                        }
                    }
                }
            }
        }
    }

    private class CtorHandler {
        private final ValidationRuleAnnotationTraverser traverser = new ValidationRuleAnnotationTraverser();

        public CtorHandler() {
            super();
        }

        public void doWith(Constructor ctor) {
            buildCtorRules(ctor);
            buildCtorParameterRules(ctor);
        }

        private void buildCtorRules(Constructor ctor) {
            Annotation[] annotations = traverser.traverse(ctor);

            for (Annotation annotation : annotations) {
                Rule[] rules = buildRules(annotation, "ctor");
                if (rules != null) {
                    if (rules != null) {
                        Arrays.stream(rules).forEach(r -> ctor(ctor, r));
                    }
                }
            }
        }

        private void buildCtorParameterRules(Constructor ctor) {
            if (ctor.getParameterCount() == 0) return;

            Annotation[][] parameterAnnotations = ctor.getParameterAnnotations();
            ParameterNameDiscoverer nameDiscoverer = ParameterNameDiscoverer.create();
            String[] parameterNames = nameDiscoverer.getParameterNames(ctor);

            for (int i = 0; i < parameterAnnotations.length; i++) {
                Annotation[] annotations = traverser.traverse(ctor, parameterAnnotations[i]);

                for (Annotation annotation : annotations) {
                    Rule[] rules = buildRules(annotation, parameterNames[i]);
                    if (rules != null) {
                        for (Rule rule : rules) {
                            param(ctor, i, rule);
                        }
                    }
                }
            }
        }
    }

}
