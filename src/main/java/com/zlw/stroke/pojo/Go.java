package com.zlw.stroke.pojo;

import lombok.Data;

@Data
public class Go {
    private String cluster;
    private String ontology;
    private String id;
    private String description;
    private String generatio;
    private String bgratio;
    private String pvalue;
    private String padj;
    private String qvalue;
    private String geneid;
    private String count;

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getOntology() {
        return ontology;
    }

    public void setOntology(String ontology) {
        this.ontology = ontology;
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

    @Override
    public String toString() {
        return "Go{" +
                "cluster='" + cluster + '\'' +
                ", ontology='" + ontology + '\'' +
                ", id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", generatio='" + generatio + '\'' +
                ", bgratio='" + bgratio + '\'' +
                ", pvalue='" + pvalue + '\'' +
                ", padj='" + padj + '\'' +
                ", qvalue='" + qvalue + '\'' +
                ", geneid='" + geneid + '\'' +
                ", count='" + count + '\'' +
                '}';
    }
}
