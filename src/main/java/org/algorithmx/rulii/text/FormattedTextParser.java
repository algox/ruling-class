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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class FormattedTextParser {

    public static final Pattern PATTERN = Pattern.compile("\\$\\{(.*?)}");

    private FormattedTextParser() {
        super();
    }

    public static FormattedText parse(String template) {
        Assert.notNull(template, "template cannot be null.");
        List<Placeholder> placeholders = new ArrayList<>();
        Matcher matcher = PATTERN.matcher(template);

        while (matcher.find()) {
            String value = matcher.group(1);
            String[] splitValues = value.split(",");
            String placeHolderName = splitValues[0].trim();
            String[] options = new String[splitValues.length - 1];

            if (options.length > 0) {
                System.arraycopy(splitValues, 1, options, 0, options.length);
            }

            placeholders.add(new Placeholder(placeHolderName, matcher.start(), matcher.end(), options));
        }

        return new FormattedText(template, placeholders);
    }
}
