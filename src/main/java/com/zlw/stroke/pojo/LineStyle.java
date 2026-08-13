package com.zlw.stroke.pojo;

public class LineStyle {
    private String width;

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    @Override
    public String toString() {
        return "LineStyle{" +
                "width='" + width + '\'' +
                '}';
    }
}
