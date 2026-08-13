package com.zlw.stroke.pojo;

public class NodeCategory {
    private String category;
    private String count;
    private double x;
    private double y;

    public NodeCategory(String category, double x, double y, String count) {
        this.category = category;
        this.count = count;
        this.x = x;
        this.y = y;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "NodeCategory{" +
                "category='" + category + '\'' +
                ", count='" + count + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
