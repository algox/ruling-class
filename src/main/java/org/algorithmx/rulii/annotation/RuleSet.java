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

package org.algorithmx.rulii.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates the class with this annotation is a RuleSet.
 *
 * A RuleSet consists of the following:
 *
 * PreCondition - Condition that is to be met in order for this RuleSet to execute.
 * PreAction - Action that is executed after the PreCondition is met and before any rule is run.
 * Rules - a list of Rules for the RuleSet.
 * StopCondition - Condition that is checked after each Rule is run. If the Stop Condition is met then the execution is stopped.
 * ErrorCondition - Condition that is checked when an exception is thrown. The ErrorCondition decided when the execution will continue or stop.
 * PostAction - Executed after all the Rules are executed.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RuleSet {

    String NOT_APPLICABLE = "";

    /**
     * Name of the Rule.
     *
     * @return Name of the RuleSet.
     */
    String name() default NOT_APPLICABLE;
}
