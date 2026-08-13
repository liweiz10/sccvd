package com.zlw.stroke.pojo;

import lombok.Data;

@Data
public class Kegg {
    private String category;
    private String subcategory;
    private String id;
    private String description;
    private String generatio;
    private String bgratio;
    private String pvalue;
    private String padj;
    private String qvalue;
    private String geneid;
    private String count;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGeneratio() {
        return generatio;
    }

    public void setGeneratio(String generatio) {
        this.generatio = generatio;
    }

    public String getBgratio() {
        return bgratio;
    }

    public void setBgratio(String bgratio) {
        this.bgratio = bgratio;
    }

    public String getPvalue() {
        return pvalue;
    }

    public void setPvalue(String pvalue) {
        this.pvalue = pvalue;
    }

    public String getPadj() {
        return padj;
    }

    public void setPadj(String padj) {
        this.padj = padj;
    }

    public String getQvalue() {
        return qvalue;
    }

    public void setQvalue(String qvalue) {
        this.qvalue = qvalue;
    }

    public String getGeneid() {
        return geneid;
    }

    public void setGeneid(String geneid) {
        this.geneid = geneid;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
