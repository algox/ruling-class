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

package org.algorithmx.rulii.traverse.types;

import org.algorithmx.rulii.lib.spring.util.Assert;

import java.lang.annotation.Annotation;

public class RuleAnnotations<M extends Annotation> {

    private final M ruleMarkerAnnotation;
    private final Annotation ruleAnnotation;

    public RuleAnnotations(M ruleMarkerAnnotation, Annotation ruleAnnotation) {
        super();
        Assert.notNull(ruleMarkerAnnotation, "ruleMarkerAnnotation cannot be null.");
        Assert.notNull(ruleAnnotation, "ruleAnnotation cannot be null.");
        this.ruleMarkerAnnotation = ruleMarkerAnnotation;
        this.ruleAnnotation = ruleAnnotation;
    }

    public M getRuleMarkerAnnotation() {
        return ruleMarkerAnnotation;
    }

    public Annotation getRuleAnnotation() {
        return ruleAnnotation;
    }

    @Override
    public String toString() {
        return "RuleAnnotations{" +
                "ruleMarkerAnnotation=" + ruleMarkerAnnotation +
                ", ruleAnnotation=" + ruleAnnotation +
                '}';
    }
}
