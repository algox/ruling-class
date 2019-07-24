package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.UnrulyException;
import org.algorithmx.rules.core.ObjectFactory;
import org.algorithmx.rules.spring.util.Assert;

public class DefaultObjectFactory implements ObjectFactory {

    public DefaultObjectFactory() {
        super();
    }

    @Override
    public <T> T create(Class<T> type) {
        Assert.notNull(type, "type cannot be null");
        try {
            return type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new UnrulyException("Unable to instantiate type [" + type + "]", e);
        }
    }
}
