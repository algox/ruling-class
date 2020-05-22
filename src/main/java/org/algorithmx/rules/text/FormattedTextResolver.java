package org.algorithmx.rules.text;

import java.util.Locale;
import java.util.Map;

public class FormattedTextResolver implements MessageResolver {

    private final FormattedText messageTemplate;

    public FormattedTextResolver(FormattedText messageTemplate) {
        super();
        this.messageTemplate = messageTemplate;
    }

    @Override
    public String resolve(String code, Map<String, Object> args, String defaultMessage, Locale locale) {
        return messageTemplate.format(args);
    }
}
