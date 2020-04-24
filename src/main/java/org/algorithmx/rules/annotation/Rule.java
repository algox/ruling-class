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
package org.algorithmx.rules.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates the class with this annotation is Rule and it will follow the "rules" of a being a Rule.
 *
 * The only requirement for a class to be considered a Rule is to have a public "when" method (aka Condition in standard Rule terms).
 * The when method can take arbitrary number of arguments but must return a boolean value. The boolean is the result of
 * of the rule. The Rule class can also have optionally one or more "then" methods (aka Then action in standard Rule terms).
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Rule {

    String NOT_APPLICABLE = "";

    /**
     * Name of the Rule.
     *
     * @return Name of the Rule.
     */
    String name() default NOT_APPLICABLE;
}
