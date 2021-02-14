package org.algorithmx.rulii.test.validation;

import org.algorithmx.rulii.validation.rules.min.Min;
import org.algorithmx.rulii.validation.rules.notnull.NotNull;

import java.math.BigDecimal;

public class TestClass extends BaseClass {

    @Annotation1(integer = 5, fraction = 2)
    @NotNull
    @Min(20)
    public String field1;
    @Annotation2 @Annotation3
    public String field2;
    @Annotation4
    public Double field3;
    @Annotation5
    public BigDecimal field4;

    public TestClass() {
        super();
    }

    @Annotation1(integer = 7, fraction = 3)
    public String getField1() {
        return field1;
    }

    @Annotation2 @Annotation3
    public String getField2() {
        return field2;
    }

    @Annotation4
    public Double getField3() {
        return field3;
    }

    @Annotation5
    public BigDecimal getField4() {
        return field4;
    }
}
