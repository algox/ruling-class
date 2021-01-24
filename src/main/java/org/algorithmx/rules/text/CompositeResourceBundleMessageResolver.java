/**
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
package org.algorithmx.rules.text;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

public class CompositeResourceBundleMessageResolver implements MessageResolver {

    private final Set<String> baseNames = new LinkedHashSet<>();

    public CompositeResourceBundleMessageResolver(List<String> baseNames) {
        super();
        if (baseNames != null) this.baseNames.addAll(baseNames);
    }

    public CompositeResourceBundleMessageResolver(String...baseNames) {
        this(baseNames != null ? Arrays.asList(baseNames) : null);
    }

    @Override
    public String resolve(Locale locale, String code, String defaultMessage) {
        if (code == null) return defaultMessage;

        String result = null;

        for (String baseName : baseNames) {
            try {
                ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale != null ? locale : Locale.getDefault());
                result = bundle.getString(code);
                break;
            } catch (MissingResourceException e) {}
        }

        if (result == null) result = defaultMessage;

        return result;
    }
}
