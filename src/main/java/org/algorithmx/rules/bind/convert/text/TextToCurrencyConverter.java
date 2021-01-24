package org.algorithmx.rules.bind.convert.text;

import org.algorithmx.rules.bind.convert.ConversionException;
import org.algorithmx.rules.bind.convert.ConverterTemplate;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Currency;

public class TextToCurrencyConverter extends ConverterTemplate<CharSequence, Currency> {

    public TextToCurrencyConverter() {
        super();
    }

    @Override
    public Currency convert(CharSequence value, Type toType) throws ConversionException {
        if (value == null) return null;
        return Currency.getInstance(value.toString());
    }
}
