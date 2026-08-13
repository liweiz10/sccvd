package com.zlw.stroke.pojo;

import lombok.Data;

@Data
public class GeneExpressData {
    private String gene;
    private String cell_type;
    private String avg_exp;
    private String pct_exp;
    private String n_cells;
    private String avg_exp_scale;
}
