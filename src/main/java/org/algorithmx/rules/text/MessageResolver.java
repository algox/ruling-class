package org.algorithmx.rules.text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public interface MessageResolver {

    String VALIDATORS_RESOURCE_BUNDLE_NAME = "validators";

    static MessageResolver create(String...baseNames) {
        List<String> baseNameList = baseNames != null ? new ArrayList<>(Arrays.asList(baseNames)) : new ArrayList<>();
        baseNameList.add(VALIDATORS_RESOURCE_BUNDLE_NAME);
        return new CompositeResourceBundleMessageResolver(baseNameList);
    }

    default String resolve(Locale locale, String code) {
        return resolve(locale, code, null);
    }

    String resolve(Locale locale, String code, String defaultMessage);
}
