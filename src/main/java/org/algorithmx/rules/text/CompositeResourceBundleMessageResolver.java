package org.algorithmx.rules.text;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

public class CompositeResourceBundleMessageResolver implements MessageResolver {

    private final Set<String> baseNames = new LinkedHashSet<>();

    public CompositeResourceBundleMessageResolver(List<String> baseNames) {
        super();
        if (baseNames != null) this.baseNames.addAll(baseNames);
    }

    public CompositeResourceBundleMessageResolver(String...baseNames) {
        this(baseNames != null ? Arrays.asList(baseNames) : null);
    }

    @Override
    public String resolve(Locale locale, String code, String defaultMessage) {
        if (code == null) return defaultMessage;

        String result = null;

        for (String baseName : baseNames) {
            try {
                ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale != null ? locale : Locale.getDefault());
                result = bundle.getString(code);
                break;
            } catch (MissingResourceException e) {}
        }

        if (result == null) result = defaultMessage;

        return result;
    }
}
