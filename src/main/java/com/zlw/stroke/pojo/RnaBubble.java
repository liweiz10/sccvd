package com.zlw.stroke.pojo;

public class RnaBubble {
    private String gene;
    private String gse_id;
    private String case_group;
    private String ctrl_group;
    private String log2fc;
    private String pvalue;
    private String padj;

    public String getGene() {
        return gene;
    }

    public void setGene(String gene) {
        this.gene = gene;
    }

    public String getGse_id() {
        return gse_id;
    }

    public void setGse_id(String gse_id) {
        this.gse_id = gse_id;
    }

    public String getCtrl_group() {
        return ctrl_group;
    }

    public void setCtrl_group(String ctrl_group) {
        this.ctrl_group = ctrl_group;
    }

    public String getCase_group() {
        return case_group;
    }

    public void setCase_group(String case_group) {
        this.case_group = case_group;
    }

    public String getLog2fc() {
        return log2fc;
    }

    public void setLog2fc(String log2fc) {
        this.log2fc = log2fc;
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
        return "RnaBubble{" +
                "gene='" + gene + '\'' +
                ", gse_id='" + gse_id + '\'' +
                ", case_group='" + case_group + '\'' +
                ", ctrl_group='" + ctrl_group + '\'' +
                ", log2fc='" + log2fc + '\'' +
                ", pvalue='" + pvalue + '\'' +
                ", padj='" + padj + '\'' +
                '}';
    }
}
