package org.algorithmx.rules.text;

import org.algorithmx.rules.lib.spring.util.Assert;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ResourceBundleMessageResolver implements MessageResolver {

    private final String baseName;

    public ResourceBundleMessageResolver(String baseName) {
        super();
        Assert.notNull(baseName, "baseName cannot be null.");
        this.baseName = baseName;
    }

    @Override
    public String resolve(Locale locale, String code, String defaultMessage) {
        if (code == null) return defaultMessage;

        String result = defaultMessage;

        try {
            ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale != null ? locale : Locale.getDefault());
            result = bundle.getString(code);
        } catch (MissingResourceException e) {}

        return result;
    }
}
