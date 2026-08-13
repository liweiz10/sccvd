package com.zlw.stroke.pojo;

public class DegTableGroup {
    private String sampleid;
    private String gse_id;
    private String group_name;
    private String case_group;
    private String ctrl_group;
    private String sample_n;
    private String gene_n;
    private String output_file;

    public String getSampleid() {
        return sampleid;
    }

    public void setSampleid(String sampleid) {
        this.sampleid = sampleid;
    }

    public String getGse_id() {
        return gse_id;
    }

    public void setGse_id(String gse_id) {
        this.gse_id = gse_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
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

    public String getOutput_file() {
        return output_file;
    }

    public void setOutput_file(String output_file) {
        this.output_file = output_file;
    }

    @Override
    public String toString() {
        return "DegTableGroup{" +
                "sampleid='" + sampleid + '\'' +
                ", gse_id='" + gse_id + '\'' +
                ", group_name='" + group_name + '\'' +
                ", case_group='" + case_group + '\'' +
                ", ctrl_group='" + ctrl_group + '\'' +
                ", sample_n='" + sample_n + '\'' +
                ", gene_n='" + gene_n + '\'' +
                ", output_file='" + output_file + '\'' +
                '}';
    }
}
