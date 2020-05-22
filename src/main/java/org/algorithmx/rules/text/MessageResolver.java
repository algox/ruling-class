package org.algorithmx.rules.text;

import java.util.Locale;
import java.util.Map;

@FunctionalInterface
public interface MessageResolver {

    String resolve(String code, Map<String, Object> args, String defaultMessage, Locale locale);
}
