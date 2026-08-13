package com.zlw.stroke.pojo;

public class RnaSampleGroup {
    private String gse_id;
    private String case_group;
    private String ctrl_group;
    private String sample_n;
    private String gene_n;
    private String sample_name;
    private String times;

    public String getGse_id() {
        return gse_id;
    }

    public void setGse_id(String gse_id) {
        this.gse_id = gse_id;
    }

    public String getCase_group() {
        return case_group;
    }

    public void setCase_group(String case_group) {
        this.case_group = case_group;
    }

    public String getCtrl_group() {
        return ctrl_group;
    }

    public void setCtrl_group(String ctrl_group) {
        this.ctrl_group = ctrl_group;
    }

    public String getSample_n() {
        return sample_n;
    }

    public void setSample_n(String sample_n) {
        this.sample_n = sample_n;
    }

    public String getGene_n() {
        return gene_n;
    }

    public void setGene_n(String gene_n) {
        this.gene_n = gene_n;
    }

    public String getSample_name() {
        return sample_name;
    }

    public void setSample_name(String sample_name) {
        this.sample_name = sample_name;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    @Override
    public String toString() {
        return "RnaSampleGroup{" +
                "gse_id='" + gse_id + '\'' +
                ", case_group='" + case_group + '\'' +
                ", ctrl_group='" + ctrl_group + '\'' +
                ", sample_n='" + sample_n + '\'' +
                ", gene_n='" + gene_n + '\'' +
                ", sample_name='" + sample_name + '\'' +
                ", times='" + times + '\'' +
                '}';
    }
}
