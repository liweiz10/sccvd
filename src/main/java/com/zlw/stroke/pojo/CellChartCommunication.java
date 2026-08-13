package com.zlw.stroke.pojo;

import lombok.Data;

@Data
public class CellChartCommunication {
    private String source;
    private String target;
    private String ligand;
    private String receptor;
    private String prob;
    private String pval;
    private String interaction_name;
    private String interaction_name_2;
    private String pathway_name;
    private String annotation;
    private String evidence;
}
