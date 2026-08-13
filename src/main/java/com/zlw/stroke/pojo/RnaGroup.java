package com.zlw.stroke.pojo;

public class RnaGroup {
    private String customid;
    private String gse_id;
    private String gsm_id;
    private String group;
    private String group_name;
    private String species;

    public String getCustomid() {
        return customid;
    }

    public void setCustomid(String customid) {
        this.customid = customid;
    }

    public String getGse_id() {
        return gse_id;
    }

    public void setGse_id(String gse_id) {
        this.gse_id = gse_id;
    }

    public String getGsm_id() {
        return gsm_id;
    }

    public void setGsm_id(String gsm_id) {
        this.gsm_id = gsm_id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    @Override
    public String toString() {
        return "RnaGroup{" +
                "customid='" + customid + '\'' +
                ", gse_id='" + gse_id + '\'' +
                ", gsm_id='" + gsm_id + '\'' +
                ", group='" + group + '\'' +
                ", group_name='" + group_name + '\'' +
                ", species='" + species + '\'' +
                '}';
    }
}
