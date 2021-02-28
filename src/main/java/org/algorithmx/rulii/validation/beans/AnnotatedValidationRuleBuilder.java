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

package org.algorithmx.rulii.validation.beans;

import org.algorithmx.rulii.config.RuliiSystem;
import org.algorithmx.rulii.core.UnrulyException;
import org.algorithmx.rulii.core.rule.Rule;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.lib.spring.util.StringUtils;
import org.algorithmx.rulii.util.reflect.ObjectFactory;
import org.algorithmx.rulii.validation.BindingValidationRuleBuilder;
import org.algorithmx.rulii.validation.annotation.ValidationRule;

import java.lang.annotation.Annotation;

public class AnnotatedValidationRuleBuilder {

    public static Rule[] build(String bindingName, Annotation annotation) {
        return build(bindingName, RuliiSystem.getInstance().getObjectFactory(), annotation);
    }

    public static Rule[] build(String bindingName, ObjectFactory objectFactory, Annotation annotation) {
        Assert.isTrue(StringUtils.hasText(bindingName), "bindingName cannot be null.");
        Assert.notNull(annotation, "annotation cannot be null.");
        ValidationRule validationRule = annotation.annotationType().getAnnotation(ValidationRule.class);
        if (validationRule == null) throw new UnrulyException("Could not find @ValidationRule on [" + annotation + "]");
        BindingValidationRuleBuilder builder = objectFactory.create(validationRule.value(), true);
        return builder.build(annotation, bindingName);
    }
}
