package com.zlw.stroke.pojo;

public class PeakDegTable {
    private String chr;
    private String starts;
    private String ends;
    private String pval;
    private String avglogfc;
    private String pct1;
    private String pct2;
    private String p_val_adj;
    private String distance;
    private String celltype;
    private String gene;
    private String peaks;

    public String getChr() {
        return chr;
    }

    public void setChr(String chr) {
        this.chr = chr;
    }

    public String getStarts() {
        return starts;
    }

    public void setStarts(String starts) {
        this.starts = starts;
    }

    public String getEnds() {
        return ends;
    }

    public void setEnds(String ends) {
        this.ends = ends;
    }

    public String getPval() {
        return pval;
    }

    public void setPval(String pval) {
        this.pval = pval;
    }

    public String getAvglogfc() {
        return avglogfc;
    }

    public void setAvglogfc(String avglogfc) {
        this.avglogfc = avglogfc;
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

    public String getP_val_adj() {
        return p_val_adj;
    }

    public void setP_val_adj(String p_val_adj) {
        this.p_val_adj = p_val_adj;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
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

    public String getPeaks() {
        return peaks;
    }

    public void setPeaks(String peaks) {
        this.peaks = peaks;
    }

    @Override
    public String toString() {
        return "PeakDegTable{" +
                "chr='" + chr + '\'' +
                ", starts='" + starts + '\'' +
                ", ends='" + ends + '\'' +
                ", pval='" + pval + '\'' +
                ", avglogfc='" + avglogfc + '\'' +
                ", pct1='" + pct1 + '\'' +
                ", pct2='" + pct2 + '\'' +
                ", p_val_adj='" + p_val_adj + '\'' +
                ", distance='" + distance + '\'' +
                ", celltype='" + celltype + '\'' +
                ", gene='" + gene + '\'' +
                ", peaks='" + peaks + '\'' +
                '}';
    }
}
