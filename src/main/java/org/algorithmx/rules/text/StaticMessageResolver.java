package org.algorithmx.rules.text;

import java.util.Locale;
import java.util.Map;

public class StaticMessageResolver implements MessageResolver {

    private final String message;

    public StaticMessageResolver(String message) {
        this.message = message;
    }

    @Override
    public String resolve(String code, Map<String, Object> args, String defaultMessage, Locale locale) {
        return message;
    }
}
