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
package org.algorithmx.rules.validation;

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Given;
import org.algorithmx.rules.annotation.Match;
import org.algorithmx.rules.annotation.Otherwise;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.bind.match.MatchByTypeMatchingStrategy;
import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.core.context.RuleContext;
import org.algorithmx.rules.lib.spring.util.Assert;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Validation Rule to make sure the the value is less the desired Max.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value is less than the desired Max.")
public class MaxRule extends ValidationRule {

    private static final String ERROR_CODE        = "validators.maxRule";
    private static final String DEFAULT_MESSAGE   = "Value must be less than/equal to {1}. Given {0}.";

    private final Number max;
    private final Supplier<Object> supplier;

    public MaxRule(Number max, Object value) {
        this(max, () -> value);
    }

    public MaxRule(Number max, Supplier<Object> supplier) {
        super(ERROR_CODE, Severity.ERROR, DEFAULT_MESSAGE);
        Assert.notNull(max, "max cannot be null.");
        Assert.notNull(supplier, "supplier cannot be null.");
        this.max = max;
        this.supplier = supplier;
    }

    @Given
    public boolean isValid() {
        Object value = supplier.get();
        if (value == null) return false;
        return isLessThanMax(value, max);
    }

    @Otherwise
    public void otherwise(@Match(using = MatchByTypeMatchingStrategy.class) RuleContext context,
                          @Match(using = MatchByTypeMatchingStrategy.class) RuleViolations errors) {

        RuleViolationBuilder builder = createRuleViolationBuilder()
                .param("value", supplier.get())
                .param("max", max);

        errors.add(builder.build(context));
    }

    /**
     * Determines if the given object (size/length) is less than or equal to the Max value.
     *
     * @param value given Object.
     * @param max Maximum size.
     * @return given object (size/length) less than the size of the Object.
     */
    public static boolean isLessThanMax(Object value, Number max) {
        if (value == null) return false;

        if (value instanceof Number) {
            Number number = (Number) value;
            return number.doubleValue() <= max.doubleValue();
        } else if (value instanceof CharSequence) {
            CharSequence text = (CharSequence) value;
            return text.length() <= max.intValue();
        } else if (value instanceof Collection) {
            Collection collection = (Collection) value;
            return collection.size() <= max.intValue();
        } else if (value instanceof Map) {
            Map map = (Map) value;
            return map.size() <= max.intValue();
        } else if (value.getClass().isArray()) {
            return Array.getLength(value) <= max.intValue();
        }

        throw new UnrulyException("MaxRule is not supported on type [" + value.getClass()
                + "] only supported on numbers, string, collections, maps and arrays");
    }

    public Number getMax() {
        return max;
    }

    public Supplier<Object> getSupplier() {
        return supplier;
    }

    @Override
    public String toString() {
        return "MaxRule{" +
                "max=" + max +
                ", value=" + supplier.get() +
                '}';
    }
}
