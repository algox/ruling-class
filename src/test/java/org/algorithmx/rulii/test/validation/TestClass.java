package org.algorithmx.rulii.test.validation;

import org.algorithmx.rulii.validation.rules.min.DecimalMin;
import org.algorithmx.rulii.validation.rules.min.Min;
import org.algorithmx.rulii.validation.rules.notempty.NotEmpty;
import org.algorithmx.rulii.validation.rules.notnull.NotNull;

import java.math.BigDecimal;

public class TestClass extends BaseClass {

    @NotNull
    @Min(20)
    public String field1;
    @NotEmpty
    public String field2;
    @DecimalMin(0.00)
    public Double field3;
    @Annotation1(integer = 5, fraction = 2)
    public BigDecimal field4;

    public TestClass() {
        super();
    }

    public String getField1() {
        return field1;
    }

    public String getField2() {
        return field2;
    }

    public Double getField3() {
        return field3;
    }

    public BigDecimal getField4() {
        return field4;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public void setField3(Double field3) {
        this.field3 = field3;
    }

    public void setField4(BigDecimal field4) {
        this.field4 = field4;
    }
}
