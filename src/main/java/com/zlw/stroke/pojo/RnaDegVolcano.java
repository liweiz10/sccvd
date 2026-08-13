package com.zlw.stroke.pojo;

public class RnaDegVolcano {
    private String pvalue;
    private String log2fc;
    private String gene;

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
        return "RnaDegVolcano{" +
                "pvalue='" + pvalue + '\'' +
                ", log2fc='" + log2fc + '\'' +
                ", gene='" + gene + '\'' +
                '}';
    }
}
