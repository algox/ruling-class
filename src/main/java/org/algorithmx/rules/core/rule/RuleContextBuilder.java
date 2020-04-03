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
package org.algorithmx.rules.core.rule;

import org.algorithmx.rules.bind.BindingMatchingStrategy;
import org.algorithmx.rules.bind.BindingMatchingStrategyType;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.bind.ParameterResolver;
import org.algorithmx.rules.bind.convert.string.ConverterRegistry;
import org.algorithmx.rules.spring.util.Assert;
import org.algorithmx.rules.util.reflect.BindableMethodExecutor;
import org.algorithmx.rules.util.reflect.ObjectFactory;

/**
 * Builder class to properly build a RuleContext with the bells and whistles.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class RuleContextBuilder {

    private final Bindings bindings;
    private BindingMatchingStrategy matchingStrategy = BindingMatchingStrategy.defaultBindingMatchingStrategy();
    private ParameterResolver parameterResolver = ParameterResolver.defaultParameterResolver();
    private BindableMethodExecutor methodExecutor = BindableMethodExecutor.defaultBindableMethodExecutor();
    private ObjectFactory objectFactory = ObjectFactory.defaultObjectFactory();
    private ConverterRegistry registry = ConverterRegistry.defaultConverterRegistry();

    private RuleContextBuilder(Bindings bindings) {
        super();
        this.bindings = bindings;
    }

    /**
     * Sets the Bindings to use.
     *
     * @param bindings Bindings to use.
     * @return this for fluency.
     */
    public static RuleContextBuilder with(Bindings bindings) {
        Assert.notNull(bindings, "bindings cannot be null.");
        return new RuleContextBuilder(bindings);
    }

    /*public static RuleContextBuilder with(Map<String, Object> values) {
        Bindings bindings = Bindings.defaultBindings();

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            bindings.bind(entry.getKey(), entry.getValue());
        }

        return new RuleContextBuilder(bindings);
    }

    public <T> RuleContextBuilder bind(Binding<T> binding) {
        bindings.bind(binding);
        return this;
    }*/

    /**
     * Sets the matching strategy to uss.
     *
     * @param type matching strategy type.
     * @return this for fluency.
     */
    public RuleContextBuilder matchUsing(BindingMatchingStrategyType type) {
        Assert.notNull(type, "type cannot be null.");
        this.matchingStrategy = type.getStrategy();
        return this;
    }

    public RuleContextBuilder paramResolver(ParameterResolver parameterResolver) {
        Assert.notNull(objectFactory, "parameterResolver cannot be null.");
        this.parameterResolver = parameterResolver;
        return this;
    }

    public RuleContextBuilder methodExecutor(BindableMethodExecutor methodExecutor) {
        Assert.notNull(objectFactory, "methodExecutor cannot be null.");
        this.methodExecutor = methodExecutor;
        return this;
    }

    public RuleContextBuilder objectFactory(ObjectFactory objectFactory) {
        Assert.notNull(objectFactory, "objectFactory cannot be null.");
        this.objectFactory = objectFactory;
        return this;
    }

    public RuleContextBuilder converterRegitry(ConverterRegistry registry) {
        Assert.notNull(registry, "registry cannot be null.");
        this.registry = registry;
        return this;
    }

    /**
     * Builds a Rule Context with desired parameters.
     *
     * @return new Rule Context.
     */
    public RuleContext build() {
        RuleContext result  = new RuleContext(bindings, matchingStrategy, parameterResolver, methodExecutor,
                objectFactory, registry);
        bindings.bind("ruleContext", RuleContext.class, result);
        return result;
    }
}
