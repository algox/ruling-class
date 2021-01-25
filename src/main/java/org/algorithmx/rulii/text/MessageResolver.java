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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public interface MessageResolver {

    String VALIDATORS_RESOURCE_BUNDLE_NAME = "validators";

    static MessageResolver create(String...baseNames) {
        List<String> baseNameList = baseNames != null ? new ArrayList<>(Arrays.asList(baseNames)) : new ArrayList<>();
        baseNameList.add(VALIDATORS_RESOURCE_BUNDLE_NAME);
        return new CompositeResourceBundleMessageResolver(baseNameList);
    }

    default String resolve(Locale locale, String code) {
        return resolve(locale, code, null);
    }

    String resolve(Locale locale, String code, String defaultMessage);
}
