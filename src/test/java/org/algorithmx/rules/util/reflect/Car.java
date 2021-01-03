package org.algorithmx.rules.util.reflect;

import org.algorithmx.rules.core.Identifiable;

public class Car implements Identifiable {

    private String id;
    private String make;
    private String model;
    private int year;

    public Car(String id, String make, String model, int year) {
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

    public int getYear() {
        return year;
    }

    @Override
    public String toString() {
        return "Car{" +
                "make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                '}';
    }
}
