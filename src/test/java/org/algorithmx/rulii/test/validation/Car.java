package org.algorithmx.rulii.test.validation;

import org.algorithmx.rulii.core.Identifiable;

public class Car implements Identifiable {

    private String id;
    private String make;
    private String model;
    private Integer year;

    public Car(String id, String make, String model, Integer year) {
        super();
        this.id = id;
        this.make = make;
        this.model = model;
        this.year = year;
    }

    @Override
    public String getName() {
        return id;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public Integer getYear() {
        return year;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id='" + id + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                '}';
    }
}
