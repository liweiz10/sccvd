package com.zlw.stroke.pojo;

public class DiffTfMotif {
    private String feature;
    private String cluster;
    private String mean1;
    private String mean2;
    private String pvalue;
    private String padj;
    private String tfname;

    public String getTfname() {
        return tfname;
    }

    public void setTfname(String tfname) {
        this.tfname = tfname;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getMean1() {
        return mean1;
    }

    public void setMean1(String mean1) {
        this.mean1 = mean1;
    }

    public String getMean2() {
        return mean2;
    }

    public void setMean2(String mean2) {
        this.mean2 = mean2;
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

    @Override
    public String toString() {
        return "DiffTfMotif{" +
                "feature='" + feature + '\'' +
                ", cluster='" + cluster + '\'' +
                ", mean1='" + mean1 + '\'' +
                ", mean2='" + mean2 + '\'' +
                ", pvalue='" + pvalue + '\'' +
                ", padj='" + padj + '\'' +
                ", tfname='" + tfname + '\'' +
                '}';
    }
}
