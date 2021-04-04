package org.algorithmx.rulii.test.validation;

import org.algorithmx.rulii.annotation.Validate;
import org.algorithmx.rulii.core.Identifiable;
import org.algorithmx.rulii.validation.rules.notnull.NotNull;

import java.math.BigDecimal;

public class Employment implements Identifiable {

    @NotNull
    private String id;
    private String company;
    private BigDecimal salary;
    @Validate
    private Address address;

    public Employment(String id, String company, BigDecimal salary, Address address) {
        super();
        this.id = id;
        this.company = company;
        this.salary = salary;
        this.address = address;
    }

    @Override
    public String getName() {
        return id;
    }

    public String getCompany() {
        return company;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "Employment{" +
                "id='" + id + '\'' +
                ", company='" + company + '\'' +
                ", salary=" + salary +
                ", address=" + address +
                '}';
    }
}
