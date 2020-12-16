package org.algorithmx.rules.text;

import org.algorithmx.rules.lib.apache.StringUtils;
import org.algorithmx.rules.lib.spring.util.Assert;

import java.util.Arrays;

public class Placeholder implements Comparable<Placeholder> {

    private String name;
    private final Integer startPosition;
    private final int endPosition;
    private final String[] options;

    Placeholder(String name, int startPosition, int endPosition, String...options) {
        super();
        Assert.isTrue(!StringUtils.isBlank(name), "PlaceHolder name cannot be null/empty. " +
                "Follow : ${BindingName,option(s)}. Given [" + name + "]");
        Assert.isTrue(endPosition > startPosition, "endIndex must be greater than startIndex. " +
                "[" + startPosition + "] [" + endPosition + "]");
        Assert.isTrue(options == null || options.length < 3, "Placeholder must follow either " +
                "${ParameterName,FormatType,FormatStyle} or ${ParameterName,FormatType} or ${ParameterName} given " + name + "]");
        this.name = name;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.options =  options;
    }

    public String getName() {
        return name;
    }

    public String[] getOptions() {
        return options;
    }

    public Integer getStartPosition() {
        return startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public boolean hasOptions() {
        return options.length > 0;
    }

    public String getMessageFormatText(int index) {
        return "{" + index + (options.length > 0 ? "," + String.join(",", options) : "") + "}";
    }

    @Override
    public int compareTo(Placeholder o) {
        return startPosition.compareTo(o.startPosition);
    }

    @Override
    public String toString() {
        return "Placeholder{" +
                ", name='" + name + '\'' +
                ", startPosition=" + startPosition +
                ", endPosition=" + endPosition +
                ", options=" + Arrays.toString(options) +
                '}';
    }
}
