package org.algorithmx.rules.util.reflect;

import java.math.BigDecimal;

public class Employment {

    private String companyName;
    private BigDecimal salary;
    private Address address;

    public Employment() {
        super();
    }

    public Employment(String companyName, BigDecimal salary, Address address) {
        super();
        this.companyName = companyName;
        this.salary = salary;
        this.address = address;
    }

    public String getCompanyName() {
        return companyName;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Employment{" +
                "companyName='" + companyName + '\'' +
                ", salary=" + salary +
                ", address=" + address +
                '}';
    }
}
