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

import org.algorithmx.rules.bind.ScopedBindings;
import org.algorithmx.rules.bind.convert.ConverterRegistry;
import org.algorithmx.rules.bind.match.BindingMatchingStrategy;
import org.algorithmx.rules.bind.match.ParameterMatch;
import org.algorithmx.rules.bind.match.ParameterResolver;
import org.algorithmx.rules.core.model.MethodDefinition;
import org.algorithmx.rules.lib.spring.util.Assert;
import org.algorithmx.rules.text.MessageFormatter;
import org.algorithmx.rules.text.MessageResolver;
import org.algorithmx.rules.util.reflect.ObjectFactory;

import java.util.Date;
import java.util.Locale;

/**
 * Responsible for state management during Rule execution. This class provides access to everything that is required
 * by the Rule Engine to execute a given set of Rules.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class RuleContext {

    private final Date creationTime = new Date();
    private final ScopedBindings bindings;
    private final BindingMatchingStrategy matchingStrategy;
    private final ParameterResolver parameterResolver;
    private final MessageResolver messageResolver;
    private final MessageFormatter messageFormatter;
    private final ObjectFactory objectFactory;
    private final ConverterRegistry registry;
    private Locale locale = Locale.getDefault();

    public RuleContext(ScopedBindings bindings) {
        this(bindings, BindingMatchingStrategy.create(), ParameterResolver.create(),
                MessageResolver.create("rules"), MessageFormatter.create(), ObjectFactory.create(),
                ConverterRegistry.create());
    }

    public RuleContext(ScopedBindings bindings, BindingMatchingStrategy matchingStrategy,
                       ParameterResolver parameterResolver, MessageResolver messageResolver,
                       MessageFormatter messageFormatter, ObjectFactory objectFactory,
                       ConverterRegistry registry) {
        super();
        Assert.notNull(bindings, "bindings cannot be null.");
        Assert.notNull(matchingStrategy, "matchingStrategy cannot be null.");
        Assert.notNull(parameterResolver, "parameterResolver cannot be null.");
        Assert.notNull(messageFormatter, "messageFormatter cannot be null.");
        Assert.notNull(messageResolver, "messageResolver cannot be null.");
        Assert.notNull(objectFactory, "objectFactory cannot be null.");
        Assert.notNull(registry, "registry cannot be null.");
        this.bindings = bindings;
        this.matchingStrategy = matchingStrategy;
        this.parameterResolver = parameterResolver;
        this.messageFormatter = messageFormatter;
        this.messageResolver = messageResolver;
        this.objectFactory = objectFactory;
        this.registry = registry;
    }

    public ParameterMatch[] match(MethodDefinition definition) {
        return getParameterResolver().match(definition, getBindings(), getMatchingStrategy(), getObjectFactory());
    }

    public Object[] resolve(ParameterMatch[] matches, MethodDefinition definition) {
        return getParameterResolver().resolve(matches, definition, getBindings(), matchingStrategy, getRegistry());
    }

    public String resolveMessage(String code) {
        return resolveMessage(code, null);
    }

    public String resolveMessage(String code, String defaultMessage) {
        return getMessageResolver().resolve(getLocale(), code, defaultMessage);
    }

    /**
     * Returns the Bindings.
     *
     * @return Bindings. Cannot be null.
     */
    public ScopedBindings getBindings() {
        return bindings;
    }

    /**
     * Returns the matching strategy to be used.
     *
     * @return matching strategy (cannot be null).
     */
    public BindingMatchingStrategy getMatchingStrategy() {
        return matchingStrategy;
    }

    /**
     * Returns the Parameter resolver being used.
     *
     * @return parameter resolver. Cannot be null.
     */
    protected ParameterResolver getParameterResolver() {
        return parameterResolver;
    }

    public MessageResolver getMessageResolver() {
        return messageResolver;
    }

    public MessageFormatter getMessageFormatter() {
        return messageFormatter;
    }

    /**
     * Returns the ObjectFactory being used.
     *
     * @return Object Factory. cannot be null.
     */
    public ObjectFactory getObjectFactory() {
        return objectFactory;
    }

    /**
     * Returns the ConverterRegistry being used.
     *
     * @return Converter Registry. Cannot be null.
     */
    public ConverterRegistry getRegistry() {
        return registry;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public String toString() {
        return "RuleContext created at " + creationTime;
    }
}
