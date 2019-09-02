package org.algorithmx.rules.validation;

import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.model.ValidationErrorContainer;

@Rule(name = "TestRule1")
public class TestRule1 {

    public TestRule1() {
        super();
    }

    public boolean when(Integer value) {
        return value != null && value < 10;
    }

    public void then(Integer value, ValidationErrorContainer errors) {
        errors.add("TestRule1", "Test.Error.100").param("value", value);
    }
}
