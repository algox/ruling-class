/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.algorithmx.rules.spring.util;

import java.io.Closeable;
import java.io.Externalizable;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Miscellaneous {@code java.lang.Class} utility methods.
 * Mainly for internal use within the framework.
 *
 * @author Juergen Hoeller
 * @author Keith Donald
 * @author Rob Harrop
 * @author Sam Brannen
 * @since 1.1
 * @see TypeUtils
 */
public class ClassUtils {

    /** Suffix for array class names: {@code "[]"}. */
    public static final String ARRAY_SUFFIX = "[]";

    /** Prefix for internal array class names: {@code "["}. */
    private static final String INTERNAL_ARRAY_PREFIX = "[";

    /** Prefix for internal non-primitive array class names: {@code "[L"}. */
    private static final String NON_PRIMITIVE_ARRAY_PREFIX = "[L";

    /** The package separator character: {@code '.'}. */
    private static final char PACKAGE_SEPARATOR = '.';

    /** The inner class separator character: {@code '$'}. */
    private static final char INNER_CLASS_SEPARATOR = '$';

    /** The ".class" file suffix. */
    public static final String CLASS_FILE_SUFFIX = ".class";

    /**
     * Map with primitive wrapper type as key and corresponding primitive
     * type as value, for example: Integer.class -> int.class.
     */
    private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new IdentityHashMap<>(8);

    /**
     * Map with primitive type as key and corresponding wrapper
     * type as value, for example: int.class -> Integer.class.
     */
    private static final Map<Class<?>, Class<?>> primitiveTypeToWrapperMap = new IdentityHashMap<>(8);

    /**
     * Map with primitive type name as key and corresponding primitive
     * type as value, for example: "int" -> "int.class".
     */
    private static final Map<String, Class<?>> primitiveTypeNameMap = new HashMap<>(32);

    /**
     * Common Java language interfaces which are supposed to be ignored
     * when searching for 'primary' user-level interfaces.
     */
    private static final Set<Class<?>> javaLanguageInterfaces;

    /**
     * Map with common Java language class name as key and corresponding Class as value.
     * Primarily for efficient deserialization of remote invocations.
     */
    private static final Map<String, Class<?>> commonClassCache = new HashMap<>(64);

    private static final Map<Class<?>, Object> DEFAULT_VALUE_MAP = new HashMap<>();

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

        primitiveWrapperTypeMap.put(Boolean.class, boolean.class);
        primitiveWrapperTypeMap.put(Byte.class, byte.class);
        primitiveWrapperTypeMap.put(Character.class, char.class);
        primitiveWrapperTypeMap.put(Double.class, double.class);
        primitiveWrapperTypeMap.put(Float.class, float.class);
        primitiveWrapperTypeMap.put(Integer.class, int.class);
        primitiveWrapperTypeMap.put(Long.class, long.class);
        primitiveWrapperTypeMap.put(Short.class, short.class);

        // Map entry iteration is less expensive to initialize than forEach with lambdas
        for (Map.Entry<Class<?>, Class<?>> entry : primitiveWrapperTypeMap.entrySet()) {
            primitiveTypeToWrapperMap.put(entry.getValue(), entry.getKey());
            registerCommonClasses(entry.getKey());
        }

        Set<Class<?>> primitiveTypes = new HashSet<>(32);
        primitiveTypes.addAll(primitiveWrapperTypeMap.values());
        Collections.addAll(primitiveTypes, boolean[].class, byte[].class, char[].class,
                double[].class, float[].class, int[].class, long[].class, short[].class);
        primitiveTypes.add(void.class);
        for (Class<?> primitiveType : primitiveTypes) {
            primitiveTypeNameMap.put(primitiveType.getName(), primitiveType);
        }

        registerCommonClasses(Boolean[].class, Byte[].class, Character[].class, Double[].class,
                Float[].class, Integer[].class, Long[].class, Short[].class);
        registerCommonClasses(Number.class, Number[].class, String.class, String[].class,
                Class.class, Class[].class, Object.class, Object[].class);
        registerCommonClasses(Throwable.class, Exception.class, RuntimeException.class,
                Error.class, StackTraceElement.class, StackTraceElement[].class);
        registerCommonClasses(Enum.class, Iterable.class, Iterator.class, Enumeration.class,
                Collection.class, List.class, Set.class, Map.class, Map.Entry.class, Optional.class);

        Class<?>[] javaLanguageInterfaceArray = {Serializable.class, Externalizable.class,
                Closeable.class, AutoCloseable.class, Cloneable.class, Comparable.class};
        registerCommonClasses(javaLanguageInterfaceArray);
        javaLanguageInterfaces = new HashSet<>(Arrays.asList(javaLanguageInterfaceArray));
    }

    /**
     * Determine whether the given interface is a common Java language interface:
     * {@link Serializable}, {@link Externalizable}, {@link Closeable}, {@link AutoCloseable},
     * {@link Cloneable}, {@link Comparable} - all of which can be ignored when looking
     * for 'primary' user-level interfaces. Common characteristics: no service-level
     * operations, no bean property methods, no default methods.
     * @param ifc the interface to check
     * @return true if the given interface is a Java defined interface.
     * @since 5.0.3
     */
    public static boolean isJavaLanguageInterface(Class<?> ifc) {
        return javaLanguageInterfaces.contains(ifc);
    }

    /**
     * Determine if the supplied class is an <em>inner class</em>,
     * i.e. a non-static member of an enclosing class.
     * @param clazz class in question.
     * @return {@code true} if the supplied class is an inner class
     * @since 5.0.
     * @see Class#isMemberClass()
     */
    public static boolean isInnerClass(Class<?> clazz) {
        return (clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers()));
    }

    /**
     * Register the given common classes with the ClassUtils cache.
     */
    private static void registerCommonClasses(Class<?>... commonClasses) {
        for (Class<?> clazz : commonClasses) {
            commonClassCache.put(clazz.getName(), clazz);
        }
    }

    /**
     * Determine the name of the class file, relative to the containing
     * package: e.g. "String.class"
     * @param clazz the class
     * @return the file name of the ".class" file
     */
    public static String getClassFileName(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        String className = clazz.getName();
        int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR);
        return className.substring(lastDotIndex + 1) + CLASS_FILE_SUFFIX;
    }

    /**
     * Resolve the given class name into a Class instance. Supports
     * primitives (like "int") and array class names (like "String[]").
     * <p>This is effectively equivalent to the {@code forName}
     * method with the same arguments, with the only difference being
     * the exceptions thrown in case of class loading failure.
     * @param className the name of the Class
     * @param classLoader the class loader to use
     * (may be {@code null}, which indicates the default class loader)
     * @return a class instance for the supplied name
     * @throws IllegalArgumentException if the class name was not resolvable
     * (that is, the class could not be found or the class file could not be loaded)
     * @throws IllegalStateException if the corresponding class is resolvable but
     * there was a readability mismatch in the inheritance hierarchy of the class
     * (typically a missing dependency declaration in a Jigsaw module definition
     * for a superclass or interface implemented by the class to be loaded here)
     * @see #forName(String, ClassLoader)
     */
    public static Class<?> resolveClassName(String className, ClassLoader classLoader)
            throws IllegalArgumentException {

        try {
            return forName(className, classLoader);
        }
        catch (IllegalAccessError err) {
            throw new IllegalStateException("Readability mismatch in inheritance hierarchy of class [" +
                    className + "]: " + err.getMessage(), err);
        }
        catch (LinkageError err) {
            throw new IllegalArgumentException("Unresolvable class definition for class [" + className + "]", err);
        }
        catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException("Could not find class [" + className + "]", ex);
        }
    }

    /**
     * Replacement for {@code Class.forName()} that also returns Class instances
     * for primitives (e.g. "int") and array class names (e.g. "String[]").
     * Furthermore, it is also capable of resolving inner class names in Java source
     * style (e.g. "java.lang.Thread.State" instead of "java.lang.Thread$State").
     * @param name the name of the Class
     * @param classLoader the class loader to use
     * (may be {@code null}, which indicates the default class loader)
     * @return a class instance for the supplied name
     * @throws ClassNotFoundException if the class was not found
     * @throws LinkageError if the class file could not be loaded
     * @see Class#forName(String, boolean, ClassLoader)
     */
    public static Class<?> forName(String name, ClassLoader classLoader)
            throws ClassNotFoundException, LinkageError {

        Assert.notNull(name, "Name must not be null");

        Class<?> clazz = resolvePrimitiveClassName(name);
        if (clazz == null) {
            clazz = commonClassCache.get(name);
        }
        if (clazz != null) {
            return clazz;
        }

        // "java.lang.String[]" style arrays
        if (name.endsWith(ARRAY_SUFFIX)) {
            String elementClassName = name.substring(0, name.length() - ARRAY_SUFFIX.length());
            Class<?> elementClass = forName(elementClassName, classLoader);
            return Array.newInstance(elementClass, 0).getClass();
        }

        // "[Ljava.lang.String;" style arrays
        if (name.startsWith(NON_PRIMITIVE_ARRAY_PREFIX) && name.endsWith(";")) {
            String elementName = name.substring(NON_PRIMITIVE_ARRAY_PREFIX.length(), name.length() - 1);
            Class<?> elementClass = forName(elementName, classLoader);
            return Array.newInstance(elementClass, 0).getClass();
        }

        // "[[I" or "[[Ljava.lang.String;" style arrays
        if (name.startsWith(INTERNAL_ARRAY_PREFIX)) {
            String elementName = name.substring(INTERNAL_ARRAY_PREFIX.length());
            Class<?> elementClass = forName(elementName, classLoader);
            return Array.newInstance(elementClass, 0).getClass();
        }

        ClassLoader clToUse = classLoader;
        if (clToUse == null) {
            clToUse = getDefaultClassLoader();
        }
        try {
            return Class.forName(name, false, clToUse);
        }
        catch (ClassNotFoundException ex) {
            int lastDotIndex = name.lastIndexOf(PACKAGE_SEPARATOR);
            if (lastDotIndex != -1) {
                String innerClassName =
                        name.substring(0, lastDotIndex) + INNER_CLASS_SEPARATOR + name.substring(lastDotIndex + 1);
                try {
                    return Class.forName(innerClassName, false, clToUse);
                }
                catch (ClassNotFoundException ex2) {
                    // Swallow - let original exception getBinding through
                }
            }
            throw ex;
        }
    }

    /**
     * Resolve the given class name as primitive class, if appropriate,
     * according to the JVM's naming rules for primitive classes.
     * <p>Also supports the JVM's internal class names for primitive arrays.
     * Does <i>not</i> support the "[]" suffix notation for primitive arrays;
     * this is only supported by {@link #forName(String, ClassLoader)}.
     * @param name the name of the potentially primitive class
     * @return the primitive class, or {@code null} if the name does not denote
     * a primitive class or primitive array class
     */
    public static Class<?> resolvePrimitiveClassName(String name) {
        Class<?> result = null;
        // Most class names will be quite long, considering that they
        // SHOULD sit in a package, so a length check is worthwhile.
        if (name != null && name.length() <= 8) {
            // Could be a primitive - likely.
            result = primitiveTypeNameMap.get(name);
        }
        return result;
    }

    /**
     * Return the default ClassLoader to use: typically the thread context
     * ClassLoader, if available; the ClassLoader that loaded the ClassUtils
     * class will be used as fallback.
     * <p>Call this method if you intend to use the thread context ClassLoader
     * in a scenario where you clearly prefer a non-null ClassLoader reference:
     * for example, for class path resource loading (but not necessarily for
     * {@code Class.forName}, which accepts a {@code null} ClassLoader
     * reference as well).
     * @return the default ClassLoader (only {@code null} if even the system
     * ClassLoader isn't accessible)
     * @see Thread#getContextClassLoader()
     * @see ClassLoader#getSystemClassLoader()
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        }
        catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ClassUtils.class.getClassLoader();
            if (cl == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                }
                catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return cl;
    }

    public static Class<?> getWrapperClass(Class<?> primitiveType) {
        Assert.notNull(primitiveType, "primitiveType cannot be null");

        if (!primitiveType.isPrimitive()) return primitiveType;

        return primitiveTypeToWrapperMap.get(primitiveType);
    }

    /**
     * Returns a default value for the given type. Useful for getting defaults for primitive values.
     *
     * @param c desired type
     * @return default value for primitives or null.
     */
    public static Object getDefaultValue(Class<?> c) {
        return c.isPrimitive() ? DEFAULT_VALUE_MAP.get(c) : null;
    }
}
