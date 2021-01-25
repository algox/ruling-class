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

package org.algorithmx.rulii.bind;

import org.algorithmx.rulii.core.UnrulyException;

/**
 * Exception thrown when Bindings is asked for a bean instance for which it cannot find a definition.
 *
 * @author Max Arulananthan
 * @since 1.0
 *
 */
public class NoSuchBindingException extends UnrulyException {

    private static final long serialVersionUID = 0L;

    private String bindingName;

    public NoSuchBindingException(String name) {
        super("Binding with name [" + name + "] does not exist");
        this.bindingName = name;
    }

    public String getBindingName() {
        return bindingName;
    }
}
