/*
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 1999-2021, Algorithmx Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.algorithmx.rulii.util.reflect;

import org.algorithmx.rulii.bind.Binding;
import org.algorithmx.rulii.core.UnrulyException;
import org.algorithmx.rulii.lib.apache.ClassUtils;
import org.algorithmx.rulii.lib.spring.core.ParameterNameDiscoverer;
import org.algorithmx.rulii.lib.spring.core.annotation.AnnotationAttributes;
import org.algorithmx.rulii.lib.spring.core.annotation.AnnotationUtils;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.lib.spring.util.StringUtils;

import javax.annotation.PostConstruct;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Reflection related utility methods.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public final class ReflectionUtils {

    private static final ParameterNameDiscoverer parameterNameDiscoverer = ParameterNameDiscoverer.create();

    private static final Predicate<Class<?>> JAVA_CORE_CLASSES = (clazz) ->
            clazz == null
                    || clazz.isPrimitive()
                    || clazz.isArray()
                    || clazz.getPackage() == null
                    || clazz.getClassLoader() == null
                    || clazz.getPackage().getName().startsWith("java.")
                    || clazz.getPackage().getName().startsWith("javax.");

    private static final Map<Type, Object> DEFAULT_VALUE_MAP = new HashMap<>();
    private static final Map<Class<?>, MethodHandles.Lookup> METHOD_HANDLE_CACHE = new HashMap<>();

    private static boolean DEFAULT_BOOLEAN;
    private static byte DEFAULT_BYTE;
    private static char DEFAULT_CHAR;
    private static double DEFAULT_DOUBLE;
    private static float DEFAULT_FLOAT;
    private static int DEFAULT_INTEGER;
    private static long DEFAULT_LONG;
    private static short DEFAULT_SHORT;

    static {
        DEFAULT_VALUE_MAP.put(boolean.class, DEFAULT_BOOLEAN);
        DEFAULT_VALUE_MAP.put(byte.class, DEFAULT_BYTE);
        DEFAULT_VALUE_MAP.put(char.class, DEFAULT_CHAR);
        DEFAULT_VALUE_MAP.put(double.class, DEFAULT_DOUBLE);
        DEFAULT_VALUE_MAP.put(float.class, DEFAULT_FLOAT);
        DEFAULT_VALUE_MAP.put(int.class, DEFAULT_INTEGER);
        DEFAULT_VALUE_MAP.put(long.class, DEFAULT_LONG);
        DEFAULT_VALUE_MAP.put(short.class, DEFAULT_SHORT);
        DEFAULT_VALUE_MAP.put(void.class, null);
    }

    private ReflectionUtils() {
        super();
    }

    /**
     * Retrieves the method parameter names. First it looks for @Param to get the parameter information.
     * If that failed then we use standard Java Reflection techniques to retrieve the information and If that fails
     * we use ASM to load the bytecode to find the method information.
     *
     * @param method target method
     * @return method parameter names.
     */
    public static String[] getParameterNames(Method method) {
        Assert.notNull(method, "method cannot be null");
        return parameterNameDiscoverer.getParameterNames(method);
    }

    /**
     * Finds the PostConstruct method(s) in a given class.
     *
     * @param c desired class
     * @return PostConstruct methods (if found); null otherwise.
     */
    public static Method getPostConstructMethods(Class<?> c) {
        Assert.notNull(c, "c cannot be null");
        List<Method> postConstructors = Arrays.stream(c.getDeclaredMethods())
                .filter(method -> void.class.equals(method.getReturnType()) &&
                        method.getParameterCount() == 0 && method.getExceptionTypes().length == 0 &&
                        method.getAnnotation(PostConstruct.class) != null).collect(Collectors.toList());

        // More than one post constructor
        if (postConstructors.size() > 1) {
            throw new UnrulyException("Invalid Number of Post Constructors(@PostConstruct) defined on class [" + c
                    + "]. Candidates [" + postConstructors + "]");
        }

        return postConstructors.size() > 0 ? postConstructors.get(0) : null;
    }

    /**
     * Returns a default value for the given Type. For example: Primitive int default value is 0;
     *
     * @param type desired type;
     * @return default value if one is found; null otherwise.
     */
    public static Object getDefaultValue(Type type) {
        return DEFAULT_VALUE_MAP.get(type);
    }

    /**
     * Invokes the PostConstruct method on the given target.
     *
     * @param postConstructMethod post constructor method.
     * @param target target object.
     */
    public static void invokePostConstruct(Method postConstructMethod, Object target) {
        Assert.notNull(postConstructMethod, "postConstructMethod cannot be null");

        postConstructMethod.setAccessible(true);
        try {
            postConstructMethod.invoke(target);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Error occurred trying to call @PostConstruct ["
                    + postConstructMethod + "]", e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException("Error occurred trying to call @PostConstruct ["
                    + postConstructMethod + "]", e);
        }
    }

    /**
     * Determines whether the given method is annotated with the given annotation class.
     *
     * @param method method to check.
     * @param annotationClass annotation to check.
     * @return true if the given method is annotated with the annotationClass or if any annotation exists that is
     * annotated with annotationClass.
     */
    public static boolean isAnnotated(Method method, Class<? extends Annotation> annotationClass) {
        Assert.notNull(method, "method cannot be null");
        Assert.notNull(annotationClass, "annotationClass cannot be null");
        return AnnotationUtils.getAnnotation(method, annotationClass) != null;
    }

    /**
     * Finds all the methods that are annotated with annotationClass.
     *
     * @param clazz class to look up.
     * @param annotationClass annotation to look for.
     * @return all methods that are annotated with annotationClass or any other annotation that has annotationClass on it.
     */
    public static Method[] getMethodsWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        return getMethods(clazz, m -> isAnnotated(m, annotationClass));
    }

    /**
     * Finds all the declared methods that match the filter criteria.
     *
     * @param clazz class to look up.
     * @param filter annotation to look for.
     * @return all methods that match the filter.
     */
    public static Method[] getMethods(Class<?> clazz, Predicate<Method> filter) {
        Assert.notNull(clazz, "clazz cannot be null");
        Assert.notNull(filter, "filter cannot be null");

        List<Method> result = new ArrayList<>();
        List<Class<?>> candidateClasses = new ArrayList<>();

        candidateClasses.add(clazz);
        // For Functional classes
        candidateClasses.addAll(ClassUtils.getAllInterfaces(clazz));

        for (Class<?> c : candidateClasses) {
            result.addAll(findMethods(c, filter));
        }

        return result.toArray(new Method[result.size()]);
    }

    /**
     * Finds all the declared methods that have meet the given matcher.
     *
     * @param clazz working class.
     * @param filter function to determine whether the given method matches the desired criteria.
     * @return all the matching methods.
     */
    private static List<Method> findMethods(Class<?> clazz, Predicate<Method> filter) {
        Assert.notNull(clazz, "clazz cannot be null.");
        Assert.notNull(clazz, "annotationClazz cannot be null.");

        List<Method> result = new ArrayList<>();

        org.algorithmx.rulii.lib.spring.util.ReflectionUtils.doWithMethods(clazz, m -> {
            if (filter.test(m)) result.add(m);
        });

        return result;
    }

    /**
     * Retrives the method handles for the given class.
     *
     * @param c requesting class.
     * @return method handles for the given class.
     */
    public static MethodHandles.Lookup getMethodLookup(Class<?> c) {
        Assert.notNull(c, "c cannot be null.");

        MethodHandles.Lookup result = METHOD_HANDLE_CACHE.get(c);

        if (result == null) {
            result = MethodHandles.lookup().in(c);
            METHOD_HANDLE_CACHE.put(c, result);
        }

        return result;
    }

    /**
     * Unreflects the method and returns it's MethodHandle.
     *
     * @param method method to unreflect.
     * @return MethodHandle
     * @throws IllegalAccessException unable to get the method handle due to security violation.
     */
    public static MethodHandle getMethodHandle(Method method) throws IllegalAccessException {
        Assert.notNull(method, "method cannot be null.");
        makeAccessible(method);
        MethodHandles.Lookup lookup = getMethodLookup(method.getDeclaringClass());
        return lookup.unreflect(method);
    }

    public static BeanInfo loadBeanInfo(Class<?> type) {
        BeanInfo result;

        try {
            result = Introspector.getBeanInfo(type);
        } catch (IntrospectionException e) {
            throw new UnrulyException("Unable to load BeanInfo for class [" + type + "]", e);
        }

        return result;
    }

    /**
     * Makes the given executable accessible via reflection.
     *
     * @param executable method/field etc
     */
    public static void makeAccessible(Executable executable) {
        Assert.notNull(executable, "executable cannot be null.");
        // TODO : Java 9 check
        executable.setAccessible(true);
    }

    public static boolean isBinding(Type type) {
        return isWrapped(type, Binding.class);
    }

    public static Type getUnderlyingBindingType(Type type) {
        return getUnderlyingType(type, Binding.class);
    }

    private static boolean isWrapped(Type type, Class<?> wrapperClass) {
        if (wrapperClass.equals(type)) return true;
        if (!(type instanceof ParameterizedType)) return false;
        ParameterizedType parameterizedType = (ParameterizedType) type;
        return wrapperClass.equals(parameterizedType.getRawType());
    }

    private static Type getUnderlyingType(Type type, Class<?> wrapperClass) {
        if (wrapperClass.equals(type)) return Object.class;
        if (!(type instanceof ParameterizedType)) throw new UnrulyException("Not a " + wrapperClass.getSimpleName()
                + " Type [" + type + "]");
        ParameterizedType parameterizedType = (ParameterizedType) type;

        if (!wrapperClass.equals(parameterizedType.getRawType())) {
            throw new UnrulyException("Not a " + wrapperClass.getSimpleName() + " Type [" + type + "]");
        }

        return parameterizedType.getActualTypeArguments()[0];
    }

    public static boolean isJavaCoreClass(Class<?> clazz) {
        Assert.notNull(clazz, "clazz cannot be null.");
        return JAVA_CORE_CLASSES.test(clazz);
    }

    public static String getAnnotationText(Annotation annotation) {
        Assert.notNull(annotation, "annotation cannot be null.");
        StringBuilder result = new StringBuilder(annotation.annotationType().getSimpleName() + "(");

        AnnotationAttributes attributes = AnnotationUtils.getAnnotationAttributes(annotation, true, false);

        boolean written = false;

        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            Object defaultValue = AnnotationUtils.getDefaultValue(annotation, entry.getKey());

            if (!Objects.equals(defaultValue, entry.getValue())) {
                result.append(written ? ", " : "");
                result.append(entry.getKey());
                result.append('=');
                result.append(valueToString(entry.getValue()));
                written = true;

            }
        }

        result.append(")");

        return result.toString();
    }

    private static String valueToString(Object value) {
        if (value instanceof Object[]) {
            return "[" + StringUtils.arrayToDelimitedString((Object[]) value, ", ") + "]";
        }
        return String.valueOf(value);
    }
}
