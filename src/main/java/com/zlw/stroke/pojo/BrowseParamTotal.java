package com.zlw.stroke.pojo;

public class BrowseParamTotal {
    private int sum;
    private String name;

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "BrowseParamTotal{" +
                "sum=" + sum +
                ", name='" + name + '\'' +
                '}';
    }
}
