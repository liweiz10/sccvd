package com.zlw.stroke.pojo;

public class CiceroTab {
    private String peak1;
    private String peak1_gene;
    private String peak2;
    private String peak2_gene;
    private String coaccess;

    public String getPeak1() {
        return peak1;
    }

    public void setPeak1(String peak1) {
        this.peak1 = peak1;
    }

    public String getPeak1_gene() {
        return peak1_gene;
    }

    public void setPeak1_gene(String peak1_gene) {
        this.peak1_gene = peak1_gene;
    }

    public String getPeak2() {
        return peak2;
    }

    public void setPeak2(String peak2) {
        this.peak2 = peak2;
    }

    public String getPeak2_gene() {
        return peak2_gene;
    }

    public void setPeak2_gene(String peak2_gene) {
        this.peak2_gene = peak2_gene;
    }

    public String getCoaccess() {
        return coaccess;
    }

    public void setCoaccess(String coaccess) {
        this.coaccess = coaccess;
    }

    @Override
    public String toString() {
        return "CiceroTab{" +
                "peak1='" + peak1 + '\'' +
                ", peak1_gene='" + peak1_gene + '\'' +
                ", peak2='" + peak2 + '\'' +
                ", peak2_gene='" + peak2_gene + '\'' +
                ", coaccess='" + coaccess + '\'' +
                '}';
    }
}
