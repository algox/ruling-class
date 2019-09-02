package org.algorithmx.rules.validation;

import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.model.ValidationErrorContainer;

@Rule(name = "TestRule3")
public class TestRule3 {

    public TestRule3() {
        super();
    }

    public boolean when(Integer value) {
        return value != null && value < 30;
    }

    public void then(Integer value, ValidationErrorContainer errors) {
        errors.add("TestRule3", "Test.Error.300").param("value", value);
    }
}
