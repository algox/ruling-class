package org.algorithmx.rules.validation;

import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.model.ValidationErrorContainer;

@Rule(name = "TestRule2")
public class TestRule2 {

    public TestRule2() {
        super();
    }

    public boolean when(Integer value) {
        return value != null && value < 20;
    }

    public void then(Integer value, ValidationErrorContainer errors) {
        errors.add("TestRule2", "Test.Error.200").param("value", value);
    }
}
