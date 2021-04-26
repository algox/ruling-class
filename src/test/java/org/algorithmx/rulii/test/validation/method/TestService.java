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

package org.algorithmx.rulii.test.validation.method;

import org.algorithmx.rulii.annotation.Param;
import org.algorithmx.rulii.annotation.Validate;
import org.algorithmx.rulii.validation.rules.min.Min;
import org.algorithmx.rulii.validation.rules.notnull.NotNull;

public interface TestService {
    void addCustomer(@NotNull @Min(10) @Param("firstName") String firstName,
                     @Param("lastName") @Validate(using = "lastNameRules") String lastName,
                     @Min(100) @Param("age") Integer age);
}
