package com.zlw.stroke.pojo;

public class RnaDegTab {
    private String pvalue;
    private String padj;
    private String log2fc;
    private String gene;
    private String lfcSE;
    private String baseMean;
    private String baseMean_case;
    private String baseMean_ctrl;

    public String getPadj() {
        return padj;
    }

    public void setPadj(String padj) {
        this.padj = padj;
    }

    public String getLfcSE() {
        return lfcSE;
    }

    public void setLfcSE(String lfcSE) {
        this.lfcSE = lfcSE;
    }

    public String getBaseMean() {
        return baseMean;
    }

    public void setBaseMean(String baseMean) {
        this.baseMean = baseMean;
    }

    public String getBaseMean_case() {
        return baseMean_case;
    }

    public void setBaseMean_case(String baseMean_case) {
        this.baseMean_case = baseMean_case;
    }

    public String getBaseMean_ctrl() {
        return baseMean_ctrl;
    }

    public void setBaseMean_ctrl(String baseMean_ctrl) {
        this.baseMean_ctrl = baseMean_ctrl;
    }

    public String getPvalue() {
        return pvalue;
    }

    public void setPvalue(String pvalue) {
        this.pvalue = pvalue;
    }

    public String getLog2fc() {
        return log2fc;
    }

    public void setLog2fc(String log2fc) {
        this.log2fc = log2fc;
    }

    public String getGene() {
        return gene;
    }

    public void setGene(String gene) {
        this.gene = gene;
    }

    @Override
    public String toString() {
        return "RnaDegTab{" +
                "pvalue='" + pvalue + '\'' +
                ", padj='" + padj + '\'' +
                ", log2fc='" + log2fc + '\'' +
                ", gene='" + gene + '\'' +
                ", lfcSE='" + lfcSE + '\'' +
                ", baseMean='" + baseMean + '\'' +
                ", baseMean_case='" + baseMean_case + '\'' +
                ", baseMean_ctrl='" + baseMean_ctrl + '\'' +
                '}';
    }
}
