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
package org.algorithmx.rules.util;

import org.algorithmx.rules.lib.spring.util.Assert;
import org.algorithmx.rules.lib.spring.util.ClassUtils;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

/**
 * Lambda related utilities.
 *
 * Inspired by Benji Weber : http://benjiweber.com/blog/2015/08/04/lambda-type-references/
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public final class LambdaUtils {

    private LambdaUtils() {
        super();
    }

    /**
     * Determines if the given class is a Lambda.
     *
     * @param target target object.
     * @return true if the target class is a Lambda; false otherwise.
     */
    public static boolean isLambda(Object target) {
        Assert.notNull(target, "target cannot be null.");

        try {
            if (!target.getClass().isSynthetic()) return false;
            return (target instanceof Serializable) ? getSerializedLambda((Serializable) target) != null : false;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    /**
     * Returns the Serialized form of the Lambda.
     *
     * @param target Lambda object
     * @return Serialized form of the given lambda.
     * @throws IllegalStateException if the given target object really isn't a Lambda or if we are unable to deserialize the Lambda.
     */
    public static SerializedLambda getSerializedLambda(Serializable target) {
        Assert.notNull(target, "target cannot be null.");
        Method writeReplaceMethod = getWriteReplaceMethod(target.getClass());

        try {
            // Make sure we found the method
            if (writeReplaceMethod == null) {
                throw new IllegalStateException("Unable to find writeReplace method! Not a SerializedLambda?");
            }
            // Make it callable
            writeReplaceMethod.setAccessible(true);
            Object result = writeReplaceMethod.invoke(target);

            if (!(result instanceof SerializedLambda)) {
                throw new IllegalStateException("writeReplaceMethod did not return a SerializedLambda ["
                        + result + "]. is this Lambda?");
            }

            return (SerializedLambda) writeReplaceMethod.invoke(target);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Unable to execute writeReplace method! [" + writeReplaceMethod + "]");
        }
    }

    /**
     * Returns the Serialized form of the Lambda.
     *
     * @param target Lambda object
     * @return Serialized form of the given lambda; null in case of any error.
     */
    public static SerializedLambda getSafeSerializedLambda(Serializable target) {
        try {
            return getSerializedLambda(target);
        } catch (IllegalStateException e) {
            return null;
        }
    }

    /**
     * Return the Lambda implementation class.
     *
     * @param lambda serialized lambda form
     * @return Lambda implementation class.
     * @throws IllegalStateException if we are unable to load the implementing Class.
     */
    public static Class<?> getImplementationClass(SerializedLambda lambda) {
        Assert.notNull(lambda, "lambda cannot be null.");
        String className = null;

        try {
            className = lambda.getImplClass().replaceAll("/", ".");
            return ClassUtils.forName(className, null);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Unable to load the implementing Lambda class [" + className + "]");
        }
    }

    /**
     * Returns the Lambda implementation method.
     *
     * @param lambda serialized lambda form
     * @param implementingClass Lambda implementation class.
     * @return Lambda implementation method.
     * @throws IllegalStateException if we are unable to locate the Lambda implementing method.
     */
    public static Method getImplementationMethod(SerializedLambda lambda, Class<?> implementingClass) {
        Assert.notNull(lambda, "lambda cannot be null.");
        Assert.notNull(implementingClass, "implementingClass cannot be null.");
        Optional<Method> result = Arrays.stream(implementingClass.getDeclaredMethods())
                .filter(method -> method.getName().equals(lambda.getImplMethodName()))
                .findFirst();

        if (!result.isPresent()) {
            throw new IllegalStateException("Unable to find implementing Lambda method on class [" + implementingClass + "]");
        }

        return result.get();
    }

    /**
     * Finds the writeReplace method.
     *
     * @param c target class.
     * @return writeReplace method if found; null otherwise.
     */
    private static Method getWriteReplaceMethod(Class<?> c) {

        try {
            Method writeReplaceMethod = c.getDeclaredMethod("writeReplace");
            return writeReplaceMethod != null ? writeReplaceMethod : null;
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
