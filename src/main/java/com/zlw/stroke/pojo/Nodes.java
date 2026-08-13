package com.zlw.stroke.pojo;

public class Nodes {
    private double x;
    private double y;
    private String name;
    private String celltype;
    private String category;
    private String clusters;
    private String origident;

    public String getCelltype() {
        return celltype;
    }

    public void setCelltype(String celltype) {
        this.celltype = celltype;
    }

    public String getClusters() {
        return clusters;
    }

    public void setClusters(String clusters) {
        this.clusters = clusters;
    }

    public String getOrigident() {
        return origident;
    }

    public void setOrigident(String origident) {
        this.origident = origident;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Nodes{" +
                "x=" + x +
                ", y=" + y +
                ", name='" + name + '\'' +
                ", celltype='" + celltype + '\'' +
                ", category='" + category + '\'' +
                ", clusters='" + clusters + '\'' +
                ", origident='" + origident + '\'' +
                '}';
    }
}