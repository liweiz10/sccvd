package com.zlw.stroke.pojo;

import lombok.Data;

@Data
public class GeneDescription {
    private String geneid;
    private String symbol;
    private String summary;
    private String synonyms;
    private String description;
    private String dbxrefs;

    public String getGeneid() {
        return geneid;
    }

    public void setGeneid(String geneid) {
        this.geneid = geneid;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDbxrefs() {
        return dbxrefs;
    }

    public void setDbxrefs(String dbxrefs) {
        this.dbxrefs = dbxrefs;
    }
}
