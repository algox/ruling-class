package org.algorithmx.rules.bind;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ReservedBindings {

    RULE_CONTEXT("context");

    private String name;

    ReservedBindings(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static List<String> reservedBindings() {
        List<String> result = new ArrayList<>();
        Arrays.stream(ReservedBindings.values()).forEach(r -> result.add(r.name));
        return result;
    }

    @Override
    public String toString() {
        return "ReservedBindings{" +
                "name='" + name + '\'' +
                '}';
    }
}
