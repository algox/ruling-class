/**
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 1999-2019, Live Software & Consultants Inc (rules@algorithmx.org)
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
package org.algorithmx.rules.util.reflect;

import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.lib.spring.core.ParameterNameDiscoverer;
import org.algorithmx.rules.lib.spring.util.Assert;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Reflection related utility methods.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public final class ReflectionUtils {

    private static final ParameterNameDiscoverer parameterNameDiscoverer = ParameterNameDiscoverer.create();

    private static final Map<Type, Object> DEFAULT_VALUE_MAP = new HashMap<>();

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
        return parameterNameDiscoverer.getParameterNames(method);
    }

    /**
     * Finds the PostConstruct method(s) in a given class.
     *
     * @param c desired class
     * @return PostConstruct methods (if found); null otherwise.
     */
    public static Method getPostConstructMethods(Class<?> c) {
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
}