package com.zlw.stroke.pojo;

public class Nodes2 {
    private String id;
    private String name;
    private String symbolSize;
    private String value;
    private String category;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbolSize() {
        return symbolSize;
    }

    public void setSymbolSize(String symbolSize) {
        this.symbolSize = symbolSize;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Nodes2{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", symbolSize='" + symbolSize + '\'' +
                ", value='" + value + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
