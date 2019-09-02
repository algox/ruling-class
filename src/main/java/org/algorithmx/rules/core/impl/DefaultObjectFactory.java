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
package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.core.ObjectFactory;
import org.algorithmx.rules.spring.util.Assert;
import org.algorithmx.rules.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class DefaultObjectFactory implements ObjectFactory {

    private final Map<Class<?>, Method> postConstructorCache = new WeakHashMap<>();

    public DefaultObjectFactory() {
        super();
    }

    @Override
    public <T> T create(Class<T> type) {
        Assert.notNull(type, "type cannot be null");
        try {
            T result = type.newInstance();
            Method postConstructor = null;

            if (postConstructorCache.containsKey(type)) {
                postConstructor = postConstructorCache.get(type);
            } else {
                List<Method> postConstructors = ReflectionUtils.getPostConstructMethods(type);

                if (postConstructors.size() > 1) {
                    throw new UnrulyException("Invalid Number of @PostConstruct defined on class [" + type
                            + "]. Candidates [" + postConstructors + "]");
                } else if (postConstructors.size() == 1) {
                    postConstructor = postConstructors.get(0);
                    postConstructorCache.put(type, postConstructor);
                }
            }

            if (postConstructor != null) {
                ReflectionUtils.invokePostConstruct(postConstructor, result);
            }

            return result;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new UnrulyException("Unable to instantiate type [" + type + "]", e);
        }
    }
}
