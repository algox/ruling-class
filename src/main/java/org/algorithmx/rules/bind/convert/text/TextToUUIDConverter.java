package org.algorithmx.rules.bind.convert.text;

import org.algorithmx.rules.bind.convert.ConversionException;
import org.algorithmx.rules.bind.convert.ConverterTemplate;
import org.algorithmx.rules.lib.apache.StringUtils;

import java.lang.reflect.Type;
import java.util.UUID;

public class TextToUUIDConverter extends ConverterTemplate<CharSequence, UUID> {

    public TextToUUIDConverter() {
        super();
    }

    @Override
    public UUID convert(CharSequence value, Type toType) throws ConversionException {
        if (value == null) return null;
        return StringUtils.isEmpty(value) ? null : UUID.fromString(value.toString().trim());
    }
}
