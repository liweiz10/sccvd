package com.zlw.stroke.pojo;

public class DegTable {
    private String p_value;
    private String avg_log2fc;
    private String pct1;
    private String pct2;
    private String p_value_adj;
    private String celltype;
    private String gene;

    public String getP_value() {
        return p_value;
    }

    public void setP_value(String p_value) {
        this.p_value = p_value;
    }

    public String getAvg_log2fc() {
        return avg_log2fc;
    }

    public void setAvg_log2fc(String avg_log2fc) {
        this.avg_log2fc = avg_log2fc;
    }

    public String getPct1() {
        return pct1;
    }

    public void setPct1(String pct1) {
        this.pct1 = pct1;
    }

    public String getPct2() {
        return pct2;
    }

    public void setPct2(String pct2) {
        this.pct2 = pct2;
    }

    public String getP_value_adj() {
        return p_value_adj;
    }

    public void setP_value_adj(String p_value_adj) {
        this.p_value_adj = p_value_adj;
    }

    public String getCelltype() {
        return celltype;
    }

    public void setCelltype(String celltype) {
        this.celltype = celltype;
    }

    public String getGene() {
        return gene;
    }

    public void setGene(String gene) {
        this.gene = gene;
    }

    @Override
    public String toString() {
        return "DegTable{" +
                "p_value='" + p_value + '\'' +
                ", avg_log2fc='" + avg_log2fc + '\'' +
                ", pct1='" + pct1 + '\'' +
                ", pct2='" + pct2 + '\'' +
                ", p_value_adj='" + p_value_adj + '\'' +
                ", celltype='" + celltype + '\'' +
                ", gene='" + gene + '\'' +
                '}';
    }
}
