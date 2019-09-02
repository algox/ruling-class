/**
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 2019, algorithmx.org (dev@algorithmx.org)
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
package org.algorithmx.rules.bind;

import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.util.LambdaUtils;
import org.algorithmx.rules.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.function.Function;

public interface BindingDeclaration extends Function<String, Object>, Serializable {

    long serialVersionUID = -0L;

    default String name() {
        SerializedLambda lambda = LambdaUtils.getSafeSerializedLambda(this);

        if (lambda == null) {
            throw new UnrulyException("BindingDeclaration can only be used as a Lambda expression");
        }

        Class<?> implementationClass = LambdaUtils.getImplementationClass(lambda);
        Method implementationMethod = LambdaUtils.getImplementationMethod(lambda, implementationClass);
        String[] parameterNames = ReflectionUtils.getParameterNames(implementationMethod);

        return parameterNames[0];
    }

    default Object value() {
        return apply(name());
    }
}
