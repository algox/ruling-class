package org.algorithmx.rules.util.reflect;

import java.util.List;
import java.util.Map;

public class Person {

    private String firstName;
    private String lastName;
    private Address address;

    private List<Car> cars;
    private Employment[] jobs;
    private Map<String, Object> attributes;

    public Person() {
        super();
    }

    public Person(String firstName, String lastName, Address address, List<Car> cars, Employment[] jobs,
                  Map<String, Object> attributes) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.cars = cars;
        this.jobs = jobs;
        this.attributes = attributes;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public Employment[] getJobs() {
        return jobs;
    }

    public void setJobs(Employment[] jobs) {
        this.jobs = jobs;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
