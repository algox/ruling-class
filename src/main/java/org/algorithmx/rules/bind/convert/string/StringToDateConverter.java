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
package org.algorithmx.rules.bind.convert.string;

import org.algorithmx.rules.bind.convert.ConversionException;
import org.algorithmx.rules.bind.convert.ConverterTemplate;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Converts a String value to a LocalDate.
 *
 * @author Max Arulananthan.
 * @since 1.0
 */
public class StringToDateConverter extends ConverterTemplate<String, Date> {

    private static final String DATE_ONLY_FORMAT        = "yyyy-MM-dd";
    private static final String DATE_TIME_FORMAT        = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String DATE_TIME_ZONE_FORMAT   = "yyyy-MM-dd'T'HH:mm:ssZ";

    private static final DateTimeFormatter DATE_FORMAT =  DateTimeFormatter.ISO_DATE;

    public StringToDateConverter() {
        super();
    }

    @Override
    public Date convert(String value, Type toType) throws ConversionException {
        if (value == null) return null;

        try {
            int timeIndex = value.indexOf('T');

            if (timeIndex > 0) {
                int zoneIndex = value.indexOf('-', timeIndex);
                return zoneIndex > timeIndex ? parseDateTimeZone(value) : parseDateTime(value);
            } else {
                return parseDate(value);
            }
        } catch (Exception e) {
            throw new ConversionException(e, value, getSourceType(), getTargetType());
        }
    }

    private Date parseDate(String value) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_ONLY_FORMAT);
        return dateFormat.parse(value);
    }

    private Date parseDateTime(String value) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        return dateFormat.parse(value);
    }

    private Date parseDateTimeZone(String value) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_ZONE_FORMAT);
        return dateFormat.parse(value);
    }
}
