package org.algorithmx.rules.validation;

import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.model.ValidationErrorContainer;

@Rule(name = "TestRule4")
public class TestRule4 {

    public TestRule4() {
        super();
    }

    public boolean when(Integer value) {
        return value != null && value < 40;
    }

    public void then(Integer value, ValidationErrorContainer errors) {
        errors.add("TestRule4", "Test.Error.400").param("value", value);
    }
}
