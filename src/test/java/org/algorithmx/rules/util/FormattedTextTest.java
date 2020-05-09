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

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Test cases covering the Formatted Text.
 *
 * @author Max Arulananthan.
 */
public class FormattedTextTest {

    public FormattedTextTest() {
        super();
    }

    @Test
    public void test1() {
        FormattedText text = FormattedText.parse("hello there ${firstName} testing ${lastName} ${ age | %20d } DOB ${dob}");

        Date date = new Date(0);
        Map<String, Object> values = new HashMap<>();
        values.put("firstName", "Michael");
        values.put("lastName", "Jordan");
        values.put("age", 50);
        values.put("dob", date);
        String result = "hello there Michael testing Jordan                   50 DOB Wed Dec 31 19:00:00 EST 1969";
        //Assert.assertTrue(result.equals(text.format(values)));
    }
}
