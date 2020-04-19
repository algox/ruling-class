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
import org.algorithmx.rules.lib.spring.util.Assert;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Default Object Factory implementation. Objects are created via reflection using the default ctor.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class DefaultObjectFactory implements ObjectFactory {

    // Post Ctor cache by class.
    private final Map<Class<?>, Method> postConstructorCache = new WeakHashMap<>();

    public DefaultObjectFactory() {
        super();
    }

    @Override
    public <T> T create(Class<T> type) {
        Assert.notNull(type, "type cannot be null.");

        try {
            // Call the default ctor
            T result = type.newInstance();
            Method postConstructor = null;

            // Check if we cached the post constructor
            if (postConstructorCache.containsKey(type)) {
                postConstructor = postConstructorCache.get(type);
            } else {
                // Find the post constructor if one exists.
                postConstructor = ReflectionUtils.getPostConstructMethods(type);
                // Cache the post constructor
                postConstructorCache.put(type, postConstructor);
            }

            if (postConstructor != null) {
                // Call the Post Constructor
                ReflectionUtils.invokePostConstruct(postConstructor, result);
            }

            return result;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new UnrulyException("Unable to instantiate type [" + type + "]", e);
        }
    }
}
