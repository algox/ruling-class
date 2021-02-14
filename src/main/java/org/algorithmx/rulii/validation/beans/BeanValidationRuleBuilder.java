package org.algorithmx.rulii.validation.beans;

import org.algorithmx.rulii.config.RuliiSystem;
import org.algorithmx.rulii.core.rule.Rule;
import org.algorithmx.rulii.core.ruleset.RuleSet;
import org.algorithmx.rulii.core.ruleset.RuleSetBuilder;
import org.algorithmx.rulii.lib.spring.core.ParameterNameDiscoverer;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.lib.spring.util.ReflectionUtils;
import org.algorithmx.rulii.util.reflect.ObjectFactory;
import org.algorithmx.rulii.validation.BindingValidationRuleBuilder;
import org.algorithmx.rulii.validation.annotation.ValidationRule;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BeanValidationRuleBuilder {

    private final Class<?> beanClass;
    private final Map<Object, RuleHolder> rules = new HashMap<>();

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
        this.objectFactory = objectFactory;
        return this;
    }

    private void load() {
        ReflectionUtils.doWithFields(beanClass, new FieldHandler());
        ReflectionUtils.doWithMethods(beanClass, new MethodHandler());
    }

    private class FieldHandler implements ReflectionUtils.FieldCallback {

        public FieldHandler() {
            super();
        }

        @Override
        public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
            ValidationRuleAnnotationTraverser traverser = new ValidationRuleAnnotationTraverser();
            Annotation[] annotations = traverser.traverse(field);

            for (Annotation annotation : annotations) {
                Rule[] rules = buildRules(annotation, field.getName());
                if (rules != null) {
                    for (Rule rule : rules) {
                        field(field, rule);
                    }
                }
            }
        }
    }

    private Rule[] buildRules(Annotation annotation, String bindingName) {
        ValidationRule validationRule = annotation.annotationType().getAnnotation(ValidationRule.class);
        if (validationRule == null) throw new IllegalStateException("Could not find @ValidationRule on [" + annotation + "]");
        BindingValidationRuleBuilder builder = objectFactory.create(validationRule.value(), true);
        return builder.build(annotation, bindingName);
    }

    private class MethodHandler implements ReflectionUtils.MethodCallback {

        public MethodHandler() {
            super();
        }

        @Override
        public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
            ValidationRuleAnnotationTraverser traverser = new ValidationRuleAnnotationTraverser();
            Annotation[] annotations = traverser.traverse(method);
            ParameterNameDiscoverer nameDiscoverer = ParameterNameDiscoverer.create();

            String[] parameterNames = nameDiscoverer.getParameterNames(method);

            for (Annotation annotation : annotations) {
                Rule[] rules = buildRules(annotation, method.getName());
                if (rules != null) {
                    for (Rule rule : rules) {
                        method(method, rule);
                    }
                }
            }

            annotations = traverser.traverse(method, method.getAnnotatedReturnType().getDeclaredAnnotations());

            for (Annotation annotation : annotations) {
                Rule[] rules = buildRules(annotation, "returnValue");
                if (rules != null) {
                    for (Rule rule : rules) {
                        returnValue(method, rule);
                    }
                }
            }

            Annotation[][] parameterAnnotations = method.getParameterAnnotations();

            for (int i = 0; i < parameterAnnotations.length; i++) {
                annotations = traverser.traverse(method, parameterAnnotations[i]);

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

    public BeanValidationRules build() {
        load();

        DefaultBeanValidationRules result = new DefaultBeanValidationRules(beanClass);

        for (RuleHolder holder : rules.values()) {
            result.add(holder);
        }

        return result;
    }

    public BeanValidationRuleBuilder rule(Rule<?> rule) {
        ClassHolder holder = (ClassHolder) rules.get(beanClass);

        if (holder == null) {
            holder = new ClassHolder(beanClass);
            rules.put(beanClass, holder);
        }

        holder.addRule(rule);
        return this;
    }

    public BeanValidationRuleBuilder field(Field field, Rule<?> rule) {
        System.err.println("XXX Field Rule [" + rule + "]");
        FieldHolder holder = (FieldHolder) rules.get(field);

        if (holder == null) {
            holder = new FieldHolder(field);
            rules.put(field, holder);
        }

        holder.addRule(rule);
        return this;
    }

    public BeanValidationRuleBuilder field(String name, Rule<?> rule) {
        Field field = ReflectionUtils.findField(beanClass, name);

        if (field == null) {
            throw new IllegalArgumentException("No such field [" + name + "] on class [" + beanClass + "] or its ancestors.");
        }

        return field(field, rule);
    }

    public BeanValidationRuleBuilder property(PropertyDescriptor property, Rule<?> rule) {
        PropertyHolder holder = (PropertyHolder) rules.get(property);

        if (holder == null) {
            holder = new PropertyHolder(property);
            rules.put(property, holder);
        }

        holder.addRule(rule);
        return this;
    }

    public BeanValidationRuleBuilder property(String name, Rule<?> rule) throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
        // TODO : Fix
        //PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(beanClass, name);
        PropertyDescriptor propertyDescriptor = null;

        if (propertyDescriptor == null || propertyDescriptor.getReadMethod() == null) {
            throw new IllegalArgumentException("No such property [" + name + "] on class [" + beanClass + "] or its ancestors.");
        }

        return this;
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

    public CtorHolder getCtorHolder(Constructor ctor) {
        CtorHolder result = (CtorHolder) rules.get(ctor);

        if (result == null) {
            result = new CtorHolder(ctor);
            rules.put(ctor, result);
        }

        return result;
    }

    public BeanValidationRuleBuilder method(Method method, Rule<?> rule) {
        System.err.println("XXX Method Rule [" + rule + "]");
        MethodHolder holder = getMethodHolder(method);
        holder.addRule(rule);
        return this;
    }

    public BeanValidationRuleBuilder param(Method method, int index, Rule<?> rule) {
        System.err.println("XXX Method Param [" + rule + "]");
        MethodHolder holder = getMethodHolder(method);
        holder.addParameterRule(index, rule);
        return this;
    }

    public BeanValidationRuleBuilder returnValue(Method method, Rule<?> rule) {
        System.err.println("XXX Method Return [" + rule + "]");
        MethodHolder holder = getMethodHolder(method);
        holder.addReturnValueRule(rule);
        return this;
    }

    public MethodHolder getMethodHolder(Method method) {
        MethodHolder result = (MethodHolder) rules.get(method);

        if (result == null) {
            result = new MethodHolder(method);
            rules.put(method, result);
        }

        return result;
    }

    public static class DefaultBeanValidationRules implements BeanValidationRules {
        private final Class<?> beanClass;
        private final Map<Object, RuleHolder> ruleMap = new LinkedHashMap<>();

        public DefaultBeanValidationRules(Class<?> beanClass) {
            super();
            this.beanClass = beanClass;
        }

        public void add(RuleHolder holder) {
            Assert.notNull(holder, "holder cannot be null.");
            holder.build();
            this.ruleMap.put(holder.getType(), holder);
        }

        @Override
        public Class<?> getBeanType() {
            return beanClass;
        }

        @Override
        public RuleSet getBeanRules() {
            RuleHolder result = ruleMap.get(beanClass);
            return result != null ? result.getRuleSet() : null;
        }

        @Override
        public RuleSet getFieldRules(Field field) {
            RuleHolder result = ruleMap.get(field);
            return result != null ? result.getRuleSet() : null;
        }

        @Override
        public RuleSet getPropertyRules(PropertyDescriptor property) {
            RuleHolder result = ruleMap.get(property);
            return result != null ? result.getRuleSet() : null;
        }

        @Override
        public RuleSet getConstructorRules(Constructor ctor) {
            RuleHolder result = ruleMap.get(ctor);
            return result != null ? result.getRuleSet() : null;
        }

        @Override
        public RuleSet getConstructorParameterRules(int index, Constructor ctor) {
            CtorHolder result = (CtorHolder) ruleMap.get(ctor);
            return result != null ? result.getParameterRules(index) : null;
        }

        @Override
        public RuleSet getMethodRules(Method method) {
            RuleHolder result = ruleMap.get(method);
            return result != null ? result.getRuleSet() : null;
        }

        @Override
        public RuleSet getMethodReturnValueRules(Method method) {
            MethodHolder result = (MethodHolder) ruleMap.get(method);
            return result != null ? result.getMethodReturnValueRules() : null;
        }

        @Override
        public RuleSet getMethodParameterRules(int index, Method method) {
            MethodHolder result = (MethodHolder) ruleMap.get(method);
            return result != null ? result.getParameterRules(index) : null;
        }

        public String prettyPrint() {
            StringBuilder result = new StringBuilder();

            result.append("Bean Rules for : " + beanClass.getName());
            result.append(System.lineSeparator());
            for (RuleHolder ruleHolder : ruleMap.values()) {

                if (ruleHolder instanceof ClassHolder) {
                    result.append("Class Rules " + ruleHolder.getName());
                    result.append(System.lineSeparator());
                    result.append(ruleHolder.getRuleSet());
                    result.append(System.lineSeparator());
                }

                if (ruleHolder instanceof FieldHolder) {
                    result.append("Field Rules " + ruleHolder.getName());
                    result.append(System.lineSeparator());
                    result.append(ruleHolder.getRuleSet());
                    result.append(System.lineSeparator());
                }

                if (ruleHolder instanceof PropertyHolder) {
                    result.append("Property Rules " + ruleHolder.getName());
                    result.append(System.lineSeparator());
                    result.append(ruleHolder.getRuleSet());
                    result.append(System.lineSeparator());
                }

                if (ruleHolder instanceof MethodHolder) {
                    MethodHolder methodHolder = (MethodHolder) ruleHolder;
                    result.append("Method Rules " + methodHolder.getName());
                    result.append(System.lineSeparator());
                    result.append(methodHolder.getRuleSet());
                    result.append(System.lineSeparator());
                    result.append("Method Return Value Rules");
                    result.append(System.lineSeparator());
                    result.append(methodHolder.getMethodReturnValueRules());
                    result.append(System.lineSeparator());

                    for (int i = 0; i < methodHolder.getParameterCount(); i++) {
                        result.append("Method Parameter : " + i);
                        result.append(System.lineSeparator());
                        result.append(methodHolder.getParameterRules(i));
                        result.append(System.lineSeparator());
                    }
                }
            }

            return result.toString();
        }

        @Override
        public String toString() {
            return prettyPrint();
        }
    }

    private static class RuleHolder<T> {
        private final T type;
        private final String name;
        private final RuleSetBuilder builder;
        private RuleSet ruleSet;

        public RuleHolder(T type, String name) {
            super();
            this.type = type;
            this.name = name;
            this.builder = RuleSetBuilder.with(name +"-Rules");
        }

        public void addRule(Rule<?> rule) {
            Assert.notNull(rule, "rule cannot be null.");
            builder.rule(rule);
        }

        public T getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public RuleSetBuilder getBuilder() {
            return builder;
        }

        public RuleSet getRuleSet() {
            return ruleSet;
        }

        public void build() {
            this.ruleSet = builder.build();
        }
    }

    private static class ClassHolder extends RuleHolder<Class> {

        public ClassHolder(Class clazz) {
            super(clazz, clazz.getName());
        }
    }

    private static class FieldHolder extends RuleHolder<Field> {

        public FieldHolder(Field field) {
            super(field, field.getName());
        }
    }

    private static class PropertyHolder extends RuleHolder<PropertyDescriptor> {

        public PropertyHolder(PropertyDescriptor property) {
            super(property, property.getName());
        }
    }

    private static class CtorHolder extends RuleHolder<Constructor> {

        private final List<RuleSetBuilder> ctorParameterRuleBuilders = new ArrayList<>();
        private final List<RuleSet> ctorParameterRules = new ArrayList<>();

        public CtorHolder(Constructor ctor) {
            super(ctor, "ctorRules");
            for (int i = 0; i < ctor.getParameterCount(); i++) {
                ctorParameterRuleBuilders.add(RuleSetBuilder.with("ctor-" + i + "-Rules"));
            }
        }

        public void addParameterRule(int index, Rule<?> rule) {
            Assert.notNull(rule, "rule cannot be null.");
            Assert.isTrue(index < ctorParameterRuleBuilders.size() - 1, "Invalid index [" + index + "]");
            this.ctorParameterRuleBuilders.get(index).rule(rule);
        }

        public RuleSet getParameterRules(int index) {
            return ctorParameterRules.get(index);
        }

        @Override
        public void build() {
            super.build();

            for (RuleSetBuilder builder : ctorParameterRuleBuilders) {
                this.ctorParameterRules.add(builder.build());
            }
        }
    }

    private static class MethodHolder extends RuleHolder<Method> {

        private final RuleSetBuilder methodReturnValueRuleBuilder;
        private RuleSet methodReturnValueRules;
        private final List<RuleSetBuilder> methodParameterRuleBuilders = new ArrayList<>();
        private final List<RuleSet> methodParameterRules = new ArrayList<>();

        public MethodHolder(Method method) {
            super(method, method.getName());
            Assert.notNull(method, "method cannot be null.");
            this.methodReturnValueRuleBuilder = RuleSetBuilder.with(method.getName() + "-returnValueRules");
            for (int i = 0; i < method.getParameterCount(); i++) {
                methodParameterRuleBuilders.add(RuleSetBuilder.with(method.getName() + "-" + i + "-Rules"));
            }
        }

        public void addParameterRule(int index, Rule<?> rule) {
            Assert.notNull(rule, "rule cannot be null.");
            Assert.isTrue(index < methodParameterRuleBuilders.size(), "Invalid index [" + index + "]");
            this.methodParameterRuleBuilders.get(index).rule(rule);
        }

        public void addReturnValueRule(Rule<?> rule) {
            Assert.notNull(rule, "rule cannot be null.");
            this.methodReturnValueRuleBuilder.rule(rule);
        }

        public RuleSet getMethodReturnValueRules() {
            return methodReturnValueRules;
        }

        public int getParameterCount() {
            return methodParameterRules.size();
        }

        public RuleSet getParameterRules(int index) {
            return methodParameterRules.get(index);
        }

        @Override
        public void build() {
            super.build();
            this.methodReturnValueRules = methodReturnValueRuleBuilder.build();
            for (RuleSetBuilder builder : methodParameterRuleBuilders) {
                methodParameterRules.add(builder.build());
            }
        }
    }

}
