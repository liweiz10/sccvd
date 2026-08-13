package com.zlw.stroke.pojo;

public class RnaExp {
    private String gse;
    private String gene_symbol;
    private String gsm_id;
    private String expr_value;

    public String getGse() {
        return gse;
    }

    public void setGse(String gse) {
        this.gse = gse;
    }

    public String getGene_symbol() {
        return gene_symbol;
    }

    public void setGene_symbol(String gene_symbol) {
        this.gene_symbol = gene_symbol;
    }

    public String getGsm_id() {
        return gsm_id;
    }

    public void setGsm_id(String gsm_id) {
        this.gsm_id = gsm_id;
    }

    public String getExpr_value() {
        return expr_value;
    }

    public void setExpr_value(String expr_value) {
        this.expr_value = expr_value;
    }

    @Override
    public String toString() {
        return "RnaExp{" +
                "gse='" + gse + '\'' +
                ", gene_symbol='" + gene_symbol + '\'' +
                ", gsm_id='" + gsm_id + '\'' +
                ", expr_value='" + expr_value + '\'' +
                '}';
    }
}
