package org.algorithmx.rules.text;

import java.util.Objects;

public class ParameterInfo implements Comparable<ParameterInfo> {

    private final Integer index;
    private final String name;
    private final Object value;

    public ParameterInfo(int index, String name, Object value) {
        super();
        this.index = index;
        this.name = name;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public int compareTo(ParameterInfo o) {
        return index.compareTo(o.index);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParameterInfo parameter = (ParameterInfo) o;
        return index == parameter.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "index=" + index +
                ", name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
