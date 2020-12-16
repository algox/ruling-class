package org.algorithmx.rules.text;

import java.text.MessageFormat;
import java.util.Locale;

public class DefaultMessageFormatter implements MessageFormatter {

    public DefaultMessageFormatter() {
        super();
    }

    public String format(Locale locale, String message, Object...args) {
        MessageFormat format = new MessageFormat(message, locale);
        return format.format(args);
    }

    @Override
    public String format(Locale locale, String message, ParameterInfo...parameters) {
        FormattedText formattedText = FormattedTextParser.parse(message);
        String template = formattedText.hasPlaceholders() ? formattedText.replaceWithIndex(parameters) : message;
        return format(locale, template, createArguments(parameters));
    }

    private Object[] createArguments(ParameterInfo...parameters) {
        Object[] result = new Object[parameters != null ? parameters.length : 0];

        if (parameters == null || parameters.length == 0) return result;

        for (int i = 0; i < parameters.length; i++) {
            result[i] = parameters[i].getValue();
        }

        return result;
    }
}
