package org.algorithmx.rules.core;

import org.algorithmx.rules.core.impl.DefaultObjectFactory;

public interface ObjectFactory {

    // TODO : Handle getBean
    static ObjectFactory create() {
        return new DefaultObjectFactory();
    }

    <T> T create(Class<T> type);
}
