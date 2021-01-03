package org.algorithmx.rules.util.reflect;

import org.algorithmx.rules.core.Identifiable;

public class Address implements Identifiable {

    private String id;
    private String number;
    private String streetName;
    private String city;
    private String state;
    private String country;

    public Address(String id, String number, String streetName, String city, String state, String country) {
        super();
        this.id = id;
        this.number = number;
        this.streetName = streetName;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    @Override
    public String getName() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Address{" +
                "number='" + number + '\'' +
                ", streetName='" + streetName + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
