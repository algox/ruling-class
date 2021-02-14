/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.algorithmx.rulii.lib.spring.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.algorithmx.rulii.lib.spring.lang.Nullable;

/**
 * Simple utility class for working with the reflection API and handling reflection exceptions.
 *
 * <p>
 * Only intended for internal use.
 *
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @author Rod Johnson
 * @author Costin Leau
 * @author Sam Brannen
 * @author Chris Beams
 * @since 1.2.2
 */
public abstract class ReflectionUtils {

  private static final Method[] NO_METHODS = {};

  /**
   * Cache for {@link Class#getDeclaredMethods()} plus equivalent default methods from Java 8 based
   * interfaces, allowing for fast iteration.
   */
  private static final Map<Class<?>, Method[]> declaredMethodsCache = new ConcurrentReferenceHashMap<>(256);

  /**
   * Attempt to find a {@link Method} on the supplied class with the supplied name and no
   * parameters. Searches all superclasses up to {@code Object}.
   * <p>
   * Returns {@code null} if no {@link Method} can be found.
   *
   * @param clazz the class to introspect
   * @param name the name of the method
   * @return the Method object, or {@code null} if none found
   */
  @Nullable
  public static Method findMethod(Class<?> clazz, String name) {
    return findMethod(clazz, name, new Class<?>[0]);
  }

  /**
   * Attempt to find a {@link Method} on the supplied class with the supplied name and parameter
   * types. Searches all superclasses up to {@code Object}.
   * <p>
   * Returns {@code null} if no {@link Method} can be found.
   *
   * @param clazz the class to introspect
   * @param name the name of the method
   * @param paramTypes the parameter types of the method (may be {@code null} to indicate any
   *          signature)
   * @return the Method object, or {@code null} if none found
   */
  @Nullable
  public static Method findMethod(Class<?> clazz, String name, @Nullable Class<?>... paramTypes) {
    Assert.notNull(clazz, "Class must not be null");
    Assert.notNull(name, "Method name must not be null");
    Class<?> searchType = clazz;
    while (searchType != null) {
      Method[] methods = (searchType.isInterface() ? searchType.getMethods() : getDeclaredMethods(searchType));
      for (Method method : methods) {
        if (name.equals(method.getName()) &&
            (paramTypes == null || Arrays.equals(paramTypes, method.getParameterTypes()))) {
          return method;
        }
      }
      searchType = searchType.getSuperclass();
    }
    return null;
  }

  /**
   * Invoke the specified {@link Method} against the supplied target object with no arguments. The
   * target object can be {@code null} when invoking a static {@link Method}.
   * <p>
   * Thrown exceptions are handled via a call to {@link #handleReflectionException}.
   *
   * @param method the method to invoke
   * @param target the target object to invoke the method on
   * @return the invocation result, if any
   * @see #invokeMethod(Method, Object, Object[])
   */
  @Nullable
  public static Object invokeMethod(Method method, @Nullable Object target) {
    return invokeMethod(method, target, new Object[0]);
  }

  /**
   * Invoke the specified {@link Method} against the supplied target object with the supplied
   * arguments. The target object can be {@code null} when invoking a static {@link Method}.
   * <p>
   * Thrown exceptions are handled via a call to {@link #handleReflectionException}.
   *
   * @param method the method to invoke
   * @param target the target object to invoke the method on
   * @param args the invocation arguments (may be {@code null})
   * @return the invocation result, if any
   */
  @Nullable
  public static Object invokeMethod(Method method, @Nullable Object target, @Nullable Object... args) {
    try {
      return method.invoke(target, args);
    } catch (Exception ex) {
      handleReflectionException(ex);
    }
    throw new IllegalStateException("Should never get here");
  }

  /**
   * Handle the given reflection exception. Should only be called if no checked exception is
   * expected to be thrown by the target method.
   * <p>
   * Throws the underlying RuntimeException or Error in case of an InvocationTargetException with
   * such a root cause. Throws an IllegalStateException with an appropriate message or
   * UndeclaredThrowableException otherwise.
   *
   * @param ex the reflection exception to handle
   */
  public static void handleReflectionException(Exception ex) {
    if (ex instanceof NoSuchMethodException) {
      throw new IllegalStateException("Method not found: " + ex.getMessage());
    }
    if (ex instanceof IllegalAccessException) {
      throw new IllegalStateException("Could not access method: " + ex.getMessage());
    }
    if (ex instanceof InvocationTargetException) {
      handleInvocationTargetException((InvocationTargetException) ex);
    }
    if (ex instanceof RuntimeException) {
      throw (RuntimeException) ex;
    }
    throw new UndeclaredThrowableException(ex);
  }

  /**
   * Handle the given invocation target exception. Should only be called if no checked exception is
   * expected to be thrown by the target method.
   * <p>
   * Throws the underlying RuntimeException or Error in case of such a root cause. Throws an
   * UndeclaredThrowableException otherwise.
   *
   * @param ex the invocation target exception to handle
   */
  public static void handleInvocationTargetException(InvocationTargetException ex) {
    rethrowRuntimeException(ex.getTargetException());
  }

  /**
   * Rethrow the given {@link Throwable exception}, which is presumably the <em>target
   * exception</em> of an {@link InvocationTargetException}. Should only be called if no checked
   * exception is expected to be thrown by the target method.
   * <p>
   * Rethrows the underlying exception cast to a {@link RuntimeException} or {@link Error} if
   * appropriate; otherwise, throws an {@link UndeclaredThrowableException}.
   *
   * @param ex the exception to rethrow
   * @throws RuntimeException the rethrown exception
   */
  public static void rethrowRuntimeException(Throwable ex) {
    if (ex instanceof RuntimeException) {
      throw (RuntimeException) ex;
    }
    if (ex instanceof Error) {
      throw (Error) ex;
    }
    throw new UndeclaredThrowableException(ex);
  }

  /**
   * Determine whether the given method is a "hashCode" method.
   *
   * @see Object#hashCode()
   */
  public static boolean isHashCodeMethod(@Nullable Method method) {
    return (method != null && method.getName().equals("hashCode") && method.getParameterCount() == 0);
  }

  /**
   * Determine whether the given method is a "toString" method.
   *
   * @see Object#toString()
   */
  public static boolean isToStringMethod(@Nullable Method method) {
    return (method != null && method.getName().equals("toString") && method.getParameterCount() == 0);
  }

  /**
   * Perform the given callback operation on all matching methods of the given class and
   * superclasses.
   * <p>
   * The same named method occurring on subclass and superclass will appear twice, unless excluded
   * by a {@link MethodFilter}.
   *
   * @param clazz the class to introspect
   * @param mc the callback to invoke for each method
   * @throws IllegalStateException if introspection fails
   * @see #doWithMethods(Class, MethodCallback, MethodFilter)
   */
  public static void doWithMethods(Class<?> clazz, MethodCallback mc) {
    doWithMethods(clazz, mc, null);
  }

  /**
   * Perform the given callback operation on all matching methods of the given class and
   * superclasses (or given interface and super-interfaces).
   * <p>
   * The same named method occurring on subclass and superclass will appear twice, unless excluded
   * by the specified {@link MethodFilter}.
   *
   * @param clazz the class to introspect
   * @param mc the callback to invoke for each method
   * @param mf the filter that determines the methods to apply the callback to
   * @throws IllegalStateException if introspection fails
   */
  public static void doWithMethods(Class<?> clazz, MethodCallback mc, @Nullable MethodFilter mf) {
    // Keep backing up the inheritance hierarchy.
    Method[] methods = getDeclaredMethods(clazz);
    for (Method method : methods) {
      if (mf != null && !mf.matches(method)) {
        continue;
      }
      try {
        mc.doWith(method);
      } catch (IllegalAccessException ex) {
        throw new IllegalStateException("Not allowed to access method '" + method.getName() + "': " + ex);
      }
    }
    if (clazz.getSuperclass() != null) {
      doWithMethods(clazz.getSuperclass(), mc, mf);
    } else if (clazz.isInterface()) {
      for (Class<?> superIfc : clazz.getInterfaces()) {
        doWithMethods(superIfc, mc, mf);
      }
    }
  }

  /**
   * Get all declared methods on the leaf class and all superclasses. Leaf class methods are
   * included first.
   *
   * @param leafClass the class to introspect
   * @throws IllegalStateException if introspection fails
   */
  public static Method[] getAllDeclaredMethods(Class<?> leafClass) {
    final List<Method> methods = new ArrayList<>(32);
    doWithMethods(leafClass, methods::add);
    return methods.toArray(new Method[0]);
  }

  /**
   * This variant retrieves {@link Class#getDeclaredMethods()} from a local cache in order to avoid
   * the JVM's SecurityManager check and defensive array copying. In addition, it also includes Java
   * 8 default methods from locally implemented interfaces, since those are effectively to be
   * treated just like declared methods.
   *
   * @param clazz the class to introspect
   * @return the cached array of methods
   * @throws IllegalStateException if introspection fails
   * @see Class#getDeclaredMethods()
   */
  private static Method[] getDeclaredMethods(Class<?> clazz) {
    Assert.notNull(clazz, "Class must not be null");
    Method[] result = declaredMethodsCache.get(clazz);
    if (result == null) {
      try {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        List<Method> defaultMethods = findConcreteMethodsOnInterfaces(clazz);
        if (defaultMethods != null) {
          result = new Method[declaredMethods.length + defaultMethods.size()];
          System.arraycopy(declaredMethods, 0, result, 0, declaredMethods.length);
          int index = declaredMethods.length;
          for (Method defaultMethod : defaultMethods) {
            result[index] = defaultMethod;
            index++;
          }
        } else {
          result = declaredMethods;
        }
        declaredMethodsCache.put(clazz, (result.length == 0 ? NO_METHODS : result));
      } catch (Throwable ex) {
        throw new IllegalStateException("Failed to introspect Class [" + clazz.getName() +
            "] from ClassLoader [" + clazz.getClassLoader() + "]", ex);
      }
    }
    return result;
  }

  @Nullable
  private static List<Method> findConcreteMethodsOnInterfaces(Class<?> clazz) {
    List<Method> result = null;
    for (Class<?> ifc : clazz.getInterfaces()) {
      for (Method ifcMethod : ifc.getMethods()) {
        if (!Modifier.isAbstract(ifcMethod.getModifiers())) {
          if (result == null) {
            result = new LinkedList<>();
          }
          result.add(ifcMethod);
        }
      }
    }
    return result;
  }

  /**
   * Action to take on each method.
   */
  @FunctionalInterface
  public interface MethodCallback {

    /**
     * Perform an operation using the given method.
     *
     * @param method the method to operate on
     */
    void doWith(Method method) throws IllegalArgumentException, IllegalAccessException;
  }

  /**
   * Callback optionally used to filter methods to be operated on by a method callback.
   */
  @FunctionalInterface
  public interface MethodFilter {

    /**
     * Determine whether the given method matches.
     *
     * @param method the method to check
     */
    boolean matches(Method method);
  }

  /**
   * Make the given method accessible, explicitly setting it accessible if necessary. The
   * {@code setAccessible(true)} method is only called when actually necessary, to avoid unnecessary
   * conflicts with a JVM SecurityManager (if active).
   *
   * @param method the method to make accessible
   * @see Method#setAccessible
   */
  public static void makeAccessible(Method method) {
    if ((!Modifier.isPublic(method.getModifiers()) ||
        !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
      method.setAccessible(true);
    }
  }

  /**
   * Determine whether the given method is an "equals" method.
   *
   * @see Object#equals(Object)
   */
  public static boolean isEqualsMethod(@Nullable Method method) {
    if (method == null || !method.getName().equals("equals")) {
      return false;
    }
    Class<?>[] paramTypes = method.getParameterTypes();
    return (paramTypes.length == 1 && paramTypes[0] == Object.class);
  }

  /****************************** For Test ****************************/

}
