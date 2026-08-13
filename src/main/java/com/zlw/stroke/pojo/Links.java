package com.zlw.stroke.pojo;

public class Links {
    private String source;
    private String target;
    private String value;
    private LineStyle lineStyle;

    public LineStyle getLineStyle() {
        return lineStyle;
    }

    public void setLineStyle(LineStyle lineStyle) {
        this.lineStyle = lineStyle;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Links{" +
                "source='" + source + '\'' +
                ", target='" + target + '\'' +
                ", value='" + value + '\'' +
                ", lineStyle=" + lineStyle +
                '}';
    }
}
