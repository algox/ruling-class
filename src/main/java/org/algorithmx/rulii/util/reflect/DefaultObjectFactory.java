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

import org.algorithmx.rulii.bind.match.BindingMatchingStrategy;
import org.algorithmx.rulii.core.UnrulyException;
import org.algorithmx.rulii.lib.spring.util.Assert;

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
    private static final Map<Class<?>, Method> postConstructorCache = new WeakHashMap<>();
    private static final Map<Class<?>, Object> objectCache          = new WeakHashMap<>();

    private final boolean useCache;

    public DefaultObjectFactory() {
        this(true);
    }

    public DefaultObjectFactory(boolean useCache) {
        super();
        this.useCache = useCache;
    }

    @Override
    public <T extends BindingMatchingStrategy> T createBindingMatchingStrategy(Class<T> type) throws UnrulyException {
        return create(type);
    }

    @Override
    public <T> T createAction(Class<T> type) throws UnrulyException {
        return create(type);
    }

    @Override
    public <T> T createCondition(Class<T> type) throws UnrulyException {
        return create(type);
    }

    @Override
    public <T> T createFunction(Class<T> type) throws UnrulyException {
        return create(type);
    }

    @Override
    public <T> T createRule(Class<T> type) throws UnrulyException {
        return create(type);
    }

    @Override
    public <T> T createRuleSet(Class<T> type) throws UnrulyException {
        return create(type);
    }

    protected <T> T create(Class<T> type) {
        Assert.notNull(type, "type cannot be null.");

        if (isUseCache() && objectCache.containsKey(type)) return (T) objectCache.get(type);

        // Create the object
        T result = createInternal(type);

        Method postConstructor;

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

        // Cache it
        if (isUseCache()) objectCache.put(type, result);

        return result;
    }

    protected boolean isUseCache() {
        return useCache;
    }

    protected <T> T createInternal(Class<T> type) throws UnrulyException {
        try {
            return type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new UnrulyException("Unable to instantiate type [" + type + "]. Does it have a default ctor ?", e);
        }
    }
}
