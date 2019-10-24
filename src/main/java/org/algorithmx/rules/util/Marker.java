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
package org.algorithmx.rules.util;

import org.algorithmx.rules.spring.util.Assert;

/**
 * Defines a placeholder in a Formatted Text.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
final class Marker {

    private String parameterName;
    private String format;
    private int startIndex;
    private int endIndex;

    Marker(String parameterName, String format, int startIndex, int endIndex) {
        super();
        Assert.notNull(parameterName, "parameter name cannot be null.");
        Assert.isTrue(endIndex > startIndex, "endIndex must be greater than startIndex.");
        this.parameterName = parameterName.trim();
        this.format = format != null ? format.trim() : null;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public String getParameterName() {
        return parameterName;
    }

    public String getFormat() {
        return format;
    }

    public boolean hasFormat() {
        return format != null;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public int getLength() {
        return endIndex - startIndex;
    }

    public String format(Object value) {
        if (value == null) return "null";
        return format != null ? String.format(format, value) : value.toString();
    }

    @Override
    public String toString() {
        return "Marker{" +
                "parameterName='" + parameterName + '\'' +
                ", format='" + format + '\'' +
                ", startIndex=" + startIndex +
                ", endIndex=" + endIndex +
                '}';
    }
}
