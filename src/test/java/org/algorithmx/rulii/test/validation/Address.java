package org.algorithmx.rulii.test.validation;

import org.algorithmx.rulii.core.Identifiable;
import org.algorithmx.rulii.validation.rules.min.Min;
import org.algorithmx.rulii.validation.rules.notnull.NotNull;

public class Address implements Identifiable {

    @NotNull
    private String id;
    @Min(50)
    private String streetNumber;
    private String streetName;
    private String city;
    private String state;
    private String country;

    public Address(String id, String streetNumber, String streetName, String city, String state, String country) {
        super();
        this.id = id;
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    @Override
    public String getName() {
        return id;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id='" + id + '\'' +
                ", streetNumber='" + streetNumber + '\'' +
                ", streetName='" + streetName + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
