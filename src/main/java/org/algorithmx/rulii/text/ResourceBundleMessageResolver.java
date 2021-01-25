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

package org.algorithmx.rulii.text;

import org.algorithmx.rulii.lib.spring.util.Assert;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ResourceBundleMessageResolver implements MessageResolver {

    private final String baseName;

    public ResourceBundleMessageResolver(String baseName) {
        super();
        Assert.notNull(baseName, "baseName cannot be null.");
        this.baseName = baseName;
    }

    @Override
    public String resolve(Locale locale, String code, String defaultMessage) {
        if (code == null) return defaultMessage;

        String result = defaultMessage;

        try {
            ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale != null ? locale : Locale.getDefault());
            result = bundle.getString(code);
        } catch (MissingResourceException e) {}

        return result;
    }
}
