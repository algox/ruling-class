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

import org.algorithmx.rules.bind.BindingMatchingStrategy;
import org.algorithmx.rules.error.UnrulyException;

/**
 * Factory use to defaultObjectFactory Objects. Framework requires Object instances to be created (such as Rules), the ObjectFactory is
 * used to defaultObjectFactory those Objects.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public interface ObjectFactory {

    /**
     * Returns the default implementation of the ObjectFactory.
     *
     * @return instance of the ObjectFactory.
     */
    static ObjectFactory defaultObjectFactory() {
        return new DefaultObjectFactory();
    }

    /**
     * Creates a new instance of the desired Type.
     *
     * @param type desired type.
     * @param <T> generic Type.
     * @return new instance of Type.
     * @throws UnrulyException thrown in case we are unable to defaultObjectFactory the type at runtime.
     */
    <T> T create(Class<T> type) throws UnrulyException;

    default <T extends BindingMatchingStrategy> BindingMatchingStrategy createStrategy(Class<T> strtegyType) {
        return null;
    }
}
