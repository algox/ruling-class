package org.algorithmx.rules.bind.convert.text;

import org.algorithmx.rules.bind.convert.ConversionException;
import org.algorithmx.rules.bind.convert.ConverterTemplate;

import java.lang.reflect.Type;
import java.nio.charset.Charset;

public class TextToCharsetConverter extends ConverterTemplate<CharSequence, Charset> {

    public TextToCharsetConverter() {
        super();
    }

    @Override
    public Charset convert(CharSequence value, Type toType) throws ConversionException {
        if (value == null) return null;
        return Charset.forName(value.toString());
    }
}
