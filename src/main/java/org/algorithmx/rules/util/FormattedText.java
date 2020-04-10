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

import org.algorithmx.rules.lib.spring.util.Assert;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Formatted Text contains named placeholders that can be replaced with formatted value using Java standard text formatting.
 *
 * ex: "this is a an example of a formatted {value|%d}"
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class FormattedText {

    private enum STATE {TEXT, PARAMETER_NAME, FORMAT}

    private static final char START_PREFIX  = '$';
    private static final char START_CHAR    = '{';
    private static final char END_CHAR      = '}';
    private static final char START_FORMAT  = '|';

    private final String text;
    private final List<Marker> markers;

    private FormattedText(String text, List<Marker> markers) {
        super();
        Assert.notNull(text, "text cannot be null.");
        this.text = text;
        this.markers = markers;
    }

    /**
     * Determines if the given text does truly requires formatting.
     *
     * @return true if formatting is required; false otherwise.
     */
    public boolean requiresFormatting() {
        return markers.size() > 0;
    }

    /**
     * Replaces the named placeholders in this formatted text with the given values.
     *
     * @param values desired values.
     * @return formatted output text.
     */
    public String format(Map<String, Object> values) {
        if (markers.size() == 0) return text;

        StringBuilder result = new StringBuilder();
        LinkedList<Marker> linkedList = new LinkedList<>(markers);
        Marker marker = linkedList.pop();

        for (int index = 0; index < text.toCharArray().length; index++) {
            char c = text.charAt(index);

            if (marker != null && index == marker.getStartIndex()) {
                result.append(marker.format(values.get(marker.getParameterName())));
                index = marker.getEndIndex();
                if (!linkedList.isEmpty()) marker = linkedList.pop();
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    /**
     * Parses the given text into a formatted text. It resolves all the place holders so that they can be formatted later.
     *
     * @param text text with optional place holders.
     * @return formatted text with resolved place holders.
     */
    public static FormattedText parse(String text) {
        List<Marker> result = new ArrayList<>();
        StringBuilder parameterName = null;
        StringBuilder format = null;
        Integer startIndex = null;
        STATE state = STATE.TEXT;

        for (int index = 0; index < text.toCharArray().length; index++) {
            char c = text.charAt(index);

            if (index > 0 && text.charAt(index - 1) == START_PREFIX && c == START_CHAR && state == STATE.TEXT) {
                startIndex = index - 1;
                state = STATE.PARAMETER_NAME;
                parameterName = new StringBuilder();
                continue;
            } else if (c == END_CHAR && (state == STATE.PARAMETER_NAME || state == STATE.FORMAT)) {
                state = STATE.TEXT;
                result.add(new Marker(parameterName.toString(), format != null ? format.toString() : null, startIndex, index));
                parameterName = null;
                format = null;
                startIndex = null;
                continue;
            } else if (c == START_FORMAT && state == STATE.PARAMETER_NAME) {
                state = STATE.FORMAT;
                format = new StringBuilder();
                continue;
            }

            if (state == STATE.FORMAT) {
                format.append(c);
            } else if (state == STATE.PARAMETER_NAME) {
                parameterName.append(c);
            }
        }

        return new FormattedText(text, result);
    }

    @Override
    public String toString() {
        return "FormattedText{" +
                "text='" + text + '\'' +
                ", markers=" + markers +
                '}';
    }
}
