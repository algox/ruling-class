/*
 * Copyright 2002-2017 the original author or authors.
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

package org.algorithmx.rulii.lib.spring.core;

import org.algorithmx.rulii.lib.spring.core.annotation.SynthesizingMethodParameter;
import org.algorithmx.rulii.lib.spring.lang.Nullable;
import org.algorithmx.rulii.lib.spring.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Helper class that encapsulates the specification of a method parameter, i.e. a {@link Method} or
 * {@link Constructor} plus a parameter index and a nested type index for a declared generic type.
 * Useful as a specification object to pass along.
 *
 * <p>
 * As of 4.2, there is a {@link SynthesizingMethodParameter}
 * subclass available which synthesizes annotations with attribute aliases. That subclass is used
 * for web and message endpoint processing, in particular.
 *
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @author Andy Clement
 * @author Sam Brannen
 * @author Sebastien Deleuze
 * @since 2.0
 * @see SynthesizingMethodParameter
 */
public class MethodParameter {

  private final Executable executable;

  private final int parameterIndex;

  @Nullable
  private volatile Parameter parameter;

  private int nestingLevel = 1;

  /** Map from Integer level to Integer type index */
  @Nullable
  Map<Integer, Integer> typeIndexesPerLevel;

  @Nullable
  private volatile Class<?> containingClass;

  @Nullable
  private volatile Class<?> parameterType;

  @Nullable
  private volatile Type genericParameterType;

  @Nullable
  private volatile Annotation[] parameterAnnotations;

  @Nullable
  private volatile ParameterNameDiscoverer parameterNameDiscoverer;

  @Nullable
  private volatile String parameterName;

  @Nullable
  private volatile MethodParameter nestedMethodParameter;

  /**
   * Create a new {@code MethodParameter} for the given method, with nesting level 1.
   *
   * @param method the Method to specify a parameter for
   * @param parameterIndex the index of the parameter: -1 for the method return type; 0 for the
   *          first method parameter; 1 for the second method parameter, etc.
   */
  public MethodParameter(Method method, int parameterIndex) {
    this(method, parameterIndex, 1);
  }

  /**
   * Create a new {@code MethodParameter} for the given method.
   *
   * @param method the Method to specify a parameter for
   * @param parameterIndex the index of the parameter: -1 for the method return type; 0 for the
   *          first method parameter; 1 for the second method parameter, etc.
   * @param nestingLevel the nesting level of the target type (typically 1; e.g. in case of a List
   *          of Lists, 1 would indicate the nested List, whereas 2 would indicate the element of
   *          the nested List)
   */
  public MethodParameter(Method method, int parameterIndex, int nestingLevel) {
    Assert.notNull(method, "Method must not be null");
    this.executable = method;
    this.parameterIndex = validateIndex(method, parameterIndex);
    this.nestingLevel = nestingLevel;
  }

  /**
   * Create a new MethodParameter for the given constructor, with nesting level 1.
   *
   * @param constructor the Constructor to specify a parameter for
   * @param parameterIndex the index of the parameter
   */
  public MethodParameter(Constructor<?> constructor, int parameterIndex) {
    this(constructor, parameterIndex, 1);
  }

  /**
   * Create a new MethodParameter for the given constructor.
   *
   * @param constructor the Constructor to specify a parameter for
   * @param parameterIndex the index of the parameter
   * @param nestingLevel the nesting level of the target type (typically 1; e.g. in case of a List
   *          of Lists, 1 would indicate the nested List, whereas 2 would indicate the element of
   *          the nested List)
   */
  public MethodParameter(Constructor<?> constructor, int parameterIndex, int nestingLevel) {
    Assert.notNull(constructor, "Constructor must not be null");
    this.executable = constructor;
    this.parameterIndex = validateIndex(constructor, parameterIndex);
    this.nestingLevel = nestingLevel;
  }

  /**
   * Copy constructor, resulting in an independent MethodParameter object based on the same metadata
   * and cache state that the original object was in.
   *
   * @param original the original MethodParameter object to copy from
   */
  public MethodParameter(MethodParameter original) {
    Assert.notNull(original, "Original must not be null");
    this.executable = original.executable;
    this.parameterIndex = original.parameterIndex;
    this.parameter = original.parameter;
    this.nestingLevel = original.nestingLevel;
    this.typeIndexesPerLevel = original.typeIndexesPerLevel;
    this.containingClass = original.containingClass;
    this.parameterType = original.parameterType;
    this.genericParameterType = original.genericParameterType;
    this.parameterAnnotations = original.parameterAnnotations;
    this.parameterNameDiscoverer = original.parameterNameDiscoverer;
    this.parameterName = original.parameterName;
  }

  /**
   * Return the wrapped Method, if any.
   * <p>
   * Note: Either Method or Constructor is available.
   *
   * @return the Method, or {@code null} if none
   */
  @Nullable
  public Method getMethod() {
    return (this.executable instanceof Method ? (Method) this.executable : null);
  }

  /**
   * Return the wrapped Constructor, if any.
   * <p>
   * Note: Either Method or Constructor is available.
   *
   * @return the Constructor, or {@code null} if none
   */
  @Nullable
  public Constructor<?> getConstructor() {
    return (this.executable instanceof Constructor ? (Constructor<?>) this.executable : null);
  }

  /**
   * Return the class that declares the underlying Method or Constructor.
   */
  public Class<?> getDeclaringClass() {
    return this.executable.getDeclaringClass();
  }

  /**
   * Return the wrapped member.
   *
   * @return the Method or Constructor as Member
   */
  public Member getMember() {
    return this.executable;
  }

  /**
   * Return the wrapped annotated element.
   * <p>
   * Note: This method exposes the annotations declared on the method/constructor itself (i.e. at
   * the method/constructor level, not at the parameter level).
   *
   * @return the Method or Constructor as AnnotatedElement
   */
  public AnnotatedElement getAnnotatedElement() {
    return this.executable;
  }

  /**
   * Return the wrapped executable.
   *
   * @return the Method or Constructor as Executable
   * @since 5.0
   */
  public Executable getExecutable() {
    return this.executable;
  }

  /**
   * Return the {@link Parameter} descriptor for method/constructor parameter.
   *
   * @since 5.0
   */
  public Parameter getParameter() {
    Parameter parameter = this.parameter;
    if (parameter == null) {
      parameter = getExecutable().getParameters()[this.parameterIndex];
      this.parameter = parameter;
    }
    return parameter;
  }

  /**
   * Return the index of the method/constructor parameter.
   *
   * @return the parameter index (-1 in case of the return type)
   */
  public int getParameterIndex() {
    return this.parameterIndex;
  }

  /**
   * Return the nesting level of the target type (typically 1; e.g. in case of a List of Lists, 1
   * would indicate the nested List, whereas 2 would indicate the element of the nested List).
   */
  public int getNestingLevel() {
    return this.nestingLevel;
  }

  /**
   * Set the type index for the current nesting level.
   *
   * @param typeIndex the corresponding type index (or {@code null} for the default type index)
   * @see #getNestingLevel()
   */
  public void setTypeIndexForCurrentLevel(int typeIndex) {
    getTypeIndexesPerLevel().put(this.nestingLevel, typeIndex);
  }

  /**
   * Return the type index for the current nesting level.
   *
   * @return the corresponding type index, or {@code null} if none specified (indicating the default
   *         type index)
   * @see #getNestingLevel()
   */
  @Nullable
  public Integer getTypeIndexForCurrentLevel() {
    return getTypeIndexForLevel(this.nestingLevel);
  }

  /**
   * Return the type index for the specified nesting level.
   *
   * @param nestingLevel the nesting level to check
   * @return the corresponding type index, or {@code null} if none specified (indicating the default
   *         type index)
   */
  @Nullable
  public Integer getTypeIndexForLevel(int nestingLevel) {
    return getTypeIndexesPerLevel().get(nestingLevel);
  }

  /**
   * Obtain the (lazily constructed) type-indexes-per-level Map.
   */
  private Map<Integer, Integer> getTypeIndexesPerLevel() {
    if (this.typeIndexesPerLevel == null) {
      this.typeIndexesPerLevel = new HashMap<>(4);
    }
    return this.typeIndexesPerLevel;
  }

  /**
   * Return whether this method indicates a parameter which is not required: either in the form of
   * Java 8's {@link Optional}, any variant of a parameter-level {@code Nullable}
   * annotation (such as from JSR-305 or the FindBugs set of annotations), or a language-level
   * nullable type declaration in Kotlin.
   *
   * @since 4.3
   */
  public boolean isOptional() {
    return (getParameterType() == Optional.class || hasNullableAnnotation());
  }

  /**
   * Check whether this method parameter is annotated with any variant of a {@code Nullable}
   * annotation, e.g. {@code javax.annotation.Nullable} or
   * {@code edu.umd.cs.findbugs.annotations.Nullable}.
   */
  private boolean hasNullableAnnotation() {
    for (Annotation ann : getParameterAnnotations()) {
      if ("Nullable".equals(ann.annotationType().getSimpleName())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Set a containing class to resolve the parameter type against.
   */
  void setContainingClass(Class<?> containingClass) {
    this.containingClass = containingClass;
  }

  public Class<?> getContainingClass() {
    Class<?> containingClass = this.containingClass;
    return (containingClass != null ? containingClass : getDeclaringClass());
  }

  /**
   * Return the type of the method/constructor parameter.
   *
   * @return the parameter type (never {@code null})
   */
  public Class<?> getParameterType() {
    Class<?> paramType = this.parameterType;
    if (paramType == null) {
      if (this.parameterIndex < 0) {
        Method method = getMethod();
        paramType = (method != null ? method.getReturnType() : void.class);
      } else {
        paramType = this.executable.getParameterTypes()[this.parameterIndex];
      }
      this.parameterType = paramType;
    }
    return paramType;
  }

  /**
   * Return the generic type of the method/constructor parameter.
   *
   * @return the parameter type (never {@code null})
   * @since 3.0
   */
  public Type getGenericParameterType() {
    Type paramType = this.genericParameterType;
    if (paramType == null) {
      if (this.parameterIndex < 0) {
        Method method = getMethod();
        paramType = (method != null ? method.getGenericReturnType() : void.class);
      } else {
        paramType = this.executable.getGenericParameterTypes()[this.parameterIndex];
      }
      this.genericParameterType = paramType;
    }
    return paramType;
  }

  /**
   * Return the nested type of the method/constructor parameter.
   *
   * @return the parameter type (never {@code null})
   * @since 3.1
   * @see #getNestingLevel()
   */
  public Class<?> getNestedParameterType() {
    if (this.nestingLevel > 1) {
      Type type = getGenericParameterType();
      for (int i = 2; i <= this.nestingLevel; i++) {
        if (type instanceof ParameterizedType) {
          Type[] args = ((ParameterizedType) type).getActualTypeArguments();
          Integer index = getTypeIndexForLevel(i);
          type = args[index != null ? index : args.length - 1];
        }
        // TODO: Object.class if unresolvable
      }
      if (type instanceof Class) {
        return (Class<?>) type;
      } else if (type instanceof ParameterizedType) {
        Type arg = ((ParameterizedType) type).getRawType();
        if (arg instanceof Class) {
          return (Class<?>) arg;
        }
      }
      return Object.class;
    } else {
      return getParameterType();
    }
  }

  /**
   * Return the nested generic type of the method/constructor parameter.
   *
   * @return the parameter type (never {@code null})
   * @since 4.2
   * @see #getNestingLevel()
   */
  public Type getNestedGenericParameterType() {
    if (this.nestingLevel > 1) {
      Type type = getGenericParameterType();
      for (int i = 2; i <= this.nestingLevel; i++) {
        if (type instanceof ParameterizedType) {
          Type[] args = ((ParameterizedType) type).getActualTypeArguments();
          Integer index = getTypeIndexForLevel(i);
          type = args[index != null ? index : args.length - 1];
        }
      }
      return type;
    } else {
      return getGenericParameterType();
    }
  }

  /**
   * Return the annotations associated with the target method/constructor itself.
   */
  public Annotation[] getMethodAnnotations() {
    return adaptAnnotationArray(getAnnotatedElement().getAnnotations());
  }

  /**
   * Return the annotations associated with the specific method/constructor parameter.
   */
  public Annotation[] getParameterAnnotations() {
    Annotation[] paramAnns = this.parameterAnnotations;
    if (paramAnns == null) {
      Annotation[][] annotationArray = this.executable.getParameterAnnotations();
      if (this.parameterIndex >= 0 && this.parameterIndex < annotationArray.length) {
        paramAnns = adaptAnnotationArray(annotationArray[this.parameterIndex]);
      } else {
        paramAnns = new Annotation[0];
      }
      this.parameterAnnotations = paramAnns;
    }
    return paramAnns;
  }

  /**
   * Return the name of the method/constructor parameter.
   *
   * @return the parameter name (may be {@code null} if no parameter name metadata is contained in
   *         the class file or no {@link #initParameterNameDiscovery ParameterNameDiscoverer} has
   *         been set to begin with)
   */
  @Nullable
  public String getParameterName() {
    ParameterNameDiscoverer discoverer = this.parameterNameDiscoverer;
    if (discoverer != null) {
      String[] parameterNames = null;
      if (this.executable instanceof Method) {
        parameterNames = discoverer.getParameterNames((Method) this.executable);
      } else if (this.executable instanceof Constructor) {
        parameterNames = discoverer.getParameterNames((Constructor<?>) this.executable);
      }
      if (parameterNames != null) {
        this.parameterName = parameterNames[this.parameterIndex];
      }
      this.parameterNameDiscoverer = null;
    }
    return this.parameterName;
  }

  /**
   * A template method to post-process a given annotation instance before returning it to the
   * caller.
   * <p>
   * The default implementation simply returns the given annotation as-is.
   *
   * @param annotation the annotation about to be returned
   * @return the post-processed annotation (or simply the original one)
   * @since 4.2
   */
  protected <A extends Annotation> A adaptAnnotation(A annotation) {
    return annotation;
  }

  /**
   * A template method to post-process a given annotation array before returning it to the caller.
   * <p>
   * The default implementation simply returns the given annotation array as-is.
   *
   * @param annotations the annotation array about to be returned
   * @return the post-processed annotation array (or simply the original one)
   * @since 4.2
   */
  protected Annotation[] adaptAnnotationArray(Annotation[] annotations) {
    return annotations;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof MethodParameter)) {
      return false;
    }
    MethodParameter otherParam = (MethodParameter) other;
    return (this.parameterIndex == otherParam.parameterIndex && getExecutable().equals(otherParam.getExecutable()));
  }

  @Override
  public int hashCode() {
    return (getExecutable().hashCode() * 31 + this.parameterIndex);
  }

  @Override
  public String toString() {
    Method method = getMethod();
    return (method != null ? "method '" + method.getName() + "'" : "constructor") +
        " parameter " + this.parameterIndex;
  }

  @Override
  public MethodParameter clone() {
    return new MethodParameter(this);
  }

  protected static int findParameterIndex(Parameter parameter) {
    Executable executable = parameter.getDeclaringExecutable();
    Parameter[] allParams = executable.getParameters();
    for (int i = 0; i < allParams.length; i++) {
      if (parameter == allParams[i]) {
        return i;
      }
    }
    throw new IllegalArgumentException("Given parameter [" + parameter +
        "] does not match any parameter in the declaring executable");
  }

  private static int validateIndex(Executable executable, int parameterIndex) {
    int count = executable.getParameterCount();
    Assert.isTrue(parameterIndex < count, () -> "Parameter index needs to be between -1 and " + (count - 1));
    return parameterIndex;
  }

  /**
   * Inner class to avoid a hard dependency on Kotlin at runtime.
   */
  /*private static class KotlinDelegate {

    public static boolean isOptional(MethodParameter param) {
      Method method = param.getMethod();
      Constructor<?> ctor = param.getConstructor();
      int index = param.getParameterIndex();
      if (method != null && index == -1) {
        KFunction<?> function = ReflectJvmMapping.getKotlinFunction(method);
        return (function != null && function.getReturnType().isMarkedNullable());
      } else {
        KFunction<?> function = null;
        if (method != null) {
          function = ReflectJvmMapping.getKotlinFunction(method);
        } else if (ctor != null) {
          function = ReflectJvmMapping.getKotlinFunction(ctor);
        }
        if (function != null) {
          List<KParameter> parameters = function.getParameters();
          KParameter parameter = parameters
              .stream()
              .filter(p -> KParameter.Kind.VALUE.equals(p.getKind()))
              .collect(Collectors.toList())
              .get(index);
          return (parameter.getType().isMarkedNullable() || parameter.isOptional());
        }
      }
      return false;
    }

  }*/

}
