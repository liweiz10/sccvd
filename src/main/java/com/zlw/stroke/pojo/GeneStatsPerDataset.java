package com.zlw.stroke.pojo;

import lombok.Data;

@Data
public class GeneStatsPerDataset {
    private String gene;
    private String datasets;
    private String species;
    private String expr_cell_ratios;
    private String mean_expressions;
    private String percentiles;
    private String overall_expr_cell_ratio;
    private String overall_mean_expression;
    private String overall_percentile;
    private String max_expr_dataset;
    private String max_percentile_dataset;
    private String max_prevalence_dataset;
}
