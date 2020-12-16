package org.algorithmx.rules.text;

import java.util.Locale;

public interface MessageFormatter {

    static MessageFormatter create() {
        return new DefaultMessageFormatter();
    }

    String format(Locale locale, String message, Object...args);

    String format(Locale locale, String message, ParameterInfo...parameters);
}
