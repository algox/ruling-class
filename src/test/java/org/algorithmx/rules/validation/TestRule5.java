package org.algorithmx.rules.validation;

import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.model.ValidationErrorContainer;

@Rule(name = "TestRule5")
public class TestRule5 {

    public TestRule5() {
        super();
    }

    public boolean when(Integer value) {
        return value != null && value < 50;
    }

    public void then(Integer value, ValidationErrorContainer errors) {
        errors.add("TestRule5", "Test.Error.500").param("value", value);
    }
}
