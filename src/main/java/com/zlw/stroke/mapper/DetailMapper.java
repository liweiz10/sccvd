package com.zlw.stroke.mapper;

import com.github.pagehelper.Page;
import com.zlw.stroke.controller.BrowserTable;
import com.zlw.stroke.pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DetailMapper {
    @Select({
            "<script>",
            "SELECT * FROM ${table} WHERE 1=1",
            " <when test='searchValue !=\"\"'>",
            " AND (source LIKE CONCAT('%',#{searchValue},'%') or target LIKE CONCAT('%',#{searchValue},'%') or weight LIKE CONCAT('%',#{searchValue},'%'))",
            " </when> ",
            " <when test='order ==\"0\"'>",
            " order by source ${dir}",
            " </when> ",
            " <when test='order ==\"1\"'>",
            " order by target ${dir}",
            " </when> ",
            " <when test='order ==\"2\"'>",
            " order by weight ${dir}",
            " </when> ",
            "</script>"
    })
    Page<WgcnaScore> getWgcnaScoreTab(String table, String searchValue, String order, String dir);

    @Select({
            "<script>",
            "SELECT * FROM ${table} WHERE 1=1",
            " <when test='searchValue !=\"\"'>",
            " AND (umap1 LIKE CONCAT('%',#{searchValue},'%') or umap2 LIKE CONCAT('%',#{searchValue},'%') or gene LIKE CONCAT('%',#{searchValue},'%') or module LIKE CONCAT('%',#{searchValue},'%') or color LIKE CONCAT('%',#{searchValue},'%') or hub LIKE CONCAT('%',#{searchValue},'%') or kme LIKE CONCAT('%',#{searchValue},'%'))",
            " </when> ",
            " <when test='order ==\"0\"'>",
            " order by umap1 ${dir}",
            " </when> ",
            " <when test='order ==\"1\"'>",
            " order by umap2 ${dir}",
            " </when> ",
            " <when test='order ==\"2\"'>",
            " order by gene ${dir}",
            " </when> ",
            " <when test='order ==\"3\"'>",
            " order by module ${dir}",
            " </when> ",
            " <when test='order ==\"4\"'>",
            " order by color ${dir}",
            " </when> ",
            " <when test='order ==\"5\"'>",
            " order by hub ${dir}",
            " </when> ",
            " <when test='order ==\"6\"'>",
            " order by kme ${dir}",
            " </when> ",
            "</script>"
    })
    Page<WgcnaUmap> getWgcnaTab(String table, String searchValue, String order, String dir);

    @Select({
            "<script>",
            "SELECT * FROM ${table} WHERE 1=1",
            " <when test='searchValue !=\"\"'>",
            " AND (source LIKE CONCAT('%',#{searchValue},'%') or target LIKE CONCAT('%',#{searchValue},'%') or ligand LIKE CONCAT('%',#{searchValue},'%') or receptor LIKE CONCAT('%',#{searchValue},'%') or interaction_name LIKE CONCAT('%',#{searchValue},'%') or interaction_name_2 LIKE CONCAT('%',#{searchValue},'%') or annotation LIKE CONCAT('%',#{searchValue},'%') or evidence LIKE CONCAT('%',#{searchValue},'%'))",
            " </when> ",
            " <when test='order ==\"0\"'>",
            " order by source ${dir}",
            " </when> ",
            " <when test='order ==\"1\"'>",
            " order by target ${dir}",
            " </when> ",
            " <when test='order ==\"2\"'>",
            " order by ligand ${dir}",
            " </when> ",
            " <when test='order ==\"3\"'>",
            " order by receptor ${dir}",
            " </when> ",
            " <when test='order ==\"4\"'>",
            " order by prob ${dir}",
            " </when> ",
            " <when test='order ==\"5\"'>",
            " order by pval ${dir}",
            " </when> ",
            " <when test='order ==\"6\"'>",
            " order by interaction_name ${dir}",
            " </when> ",
            " <when test='order ==\"7\"'>",
            " order by interaction_name_2 ${dir}",
            " </when> ",
            " <when test='order ==\"8\"'>",
            " order by pathway_name ${dir}",
            " </when> ",
            " <when test='order ==\"9\"'>",
            " order by annotation ${dir}",
            " </when> ",
            " <when test='order ==\"10\"'>",
            " order by evidence ${dir}",
            " </when> ",
            "</script>"
    })
    Page<CellChartCommunication> toCellchatTab(String table, String searchValue, String order, String dir);

    @Select({
            "<script>",
            "SELECT * FROM ${table} WHERE 1=1",
            " <when test='searchValue !=\"\"'>",
            " AND (peak1_gene LIKE CONCAT('%',#{searchValue},'%') or peak2_gene LIKE CONCAT('%',#{searchValue},'%'))",
            " </when> ",
            " <when test='order ==\"0\"'>",
            " order by peak1 ${dir}",
            " </when> ",
            " <when test='order ==\"1\"'>",
            " order by peak1_gene ${dir}",
            " </when> ",
            " <when test='order ==\"2\"'>",
            " order by peak2 ${dir}",
            " </when> ",
            " <when test='order ==\"3\"'>",
            " order by peak2_gene ${dir}",
            " </when> ",
            " <when test='order ==\"4\"'>",
            " order by coaccess ${dir}",
            " </when> ",
            "</script>"
    })
    Page<CiceroTab> toCiceroTab(String table, String searchValue, String order, String dir);

    @Select({
            "<script>",
            "SELECT * FROM ${table} WHERE 1=1",
            " <when test='searchValue !=\"\"'>",
            " AND (feature LIKE CONCAT('%',#{searchValue},'%') or cluster LIKE CONCAT('%',#{searchValue},'%') or tfname LIKE CONCAT('%',#{searchValue},'%'))",
            " </when> ",
            " <when test='order ==\"0\"'>",
            " order by feature ${dir}",
            " </when> ",
            " <when test='order ==\"1\"'>",
            " order by cluster ${dir}",
            " </when> ",
            " <when test='order ==\"2\"'>",
            " order by mean1 ${dir}",
            " </when> ",
            " <when test='order ==\"3\"'>",
            " order by mean2 ${dir}",
            " </when> ",
            " <when test='order ==\"4\"'>",
            " order by pvalue ${dir}",
            " </when> ",
            " <when test='order ==\"5\"'>",
            " order by padj ${dir}",
            " </when> ",
            " <when test='order ==\"6\"'>",
            " order by tfname ${dir}",
            " </when> ",
            "</script>"
    })
    Page<DiffTfMotif> toDiffTfMotifTab(String table, String searchValue, String order, String dir);

    @Select({
            "<script>",
            "SELECT DISTINCT celltype as name,GROUP_CONCAT(origident) as value1, GROUP_CONCAT(cell_count) as value2 FROM ${table} WHERE id=#{id} GROUP BY celltype",
            "</script>"
    })
    List<CellCount> getCellCount(String table, String id);

    @Select({
            "<script>",
            "SELECT origident as name, ${type} as value FROM ${table}",
            "</script>"
    })
    List<NameValue> getQcInfo(String table, String type);

    @Select({
            "<script>",
            "SELECT DISTINCT gene as name FROM ${table}",
            "</script>"
    })
    List<NameValue> getGeneName(String table);

    @Select({
            "<script>",
            "SELECT gene as name FROM gene_stats_per_dataset ORDER BY CAST(overall_mean_expression AS DECIMAL(20,10)) DESC;",
            "</script>"
    })
    List<Name> getGeneByAll();

    @Select({
            "<script>",
            "SELECT * FROM genecardbystroke WHERE symbol = #{gene};",
            "</script>"
    })
    List<GeneCardByStroke> getGeneCardByStroke(String gene);
    @Select({
            "<script>",
            "SELECT * FROM gene_stats_per_dataset WHERE gene = #{gene};",
            "</script>"
    })
    GeneStatsPerDataset getGeneForDatas(String gene);

    @Select({
            "<script>",
            "SELECT DISTINCT cluster as name FROM ${table}",
            "</script>"
    })
    List<NameValue> getCelltypeBySample(String table);



    @Select({
            "<script>",
            "SELECT DISTINCT description, count, padj FROM ${table} where cluster=#{param1} and ontology=#{param2} and padj &lt; ${param3} order by count desc",
            "</script>"
    })
    List<Go> getPathwayGo(String table, String param1, String param2, String param3);

    @Select({
            "<script>",
            "SELECT DISTINCT description, CAST(SUBSTRING_INDEX(generatio, '/', 1) AS DECIMAL) / CAST(SUBSTRING_INDEX(generatio, '/', -1) AS DECIMAL) AS generatio, TRIM(count) AS count, padj FROM ${table} where cluster=#{param1} and padj &lt; ${param2} order by generatio desc",
            "</script>"
    })
    List<Kegg> getPathwayKEGG(String table, String param1, String param2);

    @Select({
            "<script>",
            "SELECT DISTINCT description, count, padj FROM ${table} where cluster=#{param1} and enrich_type=#{param2} and padj &lt; ${param3} order by count desc",
            "</script>"
    })
    List<Go> getPathwayGoAtac(String table, String param1, String param2, String param3);

    @Select({
            "<script>",
            "SELECT DISTINCT description, CAST(SUBSTRING_INDEX(generatio, '/', 1) AS DECIMAL) / CAST(SUBSTRING_INDEX(generatio, '/', -1) AS DECIMAL) AS generatio, TRIM(count) AS count, padj FROM ${table} where enrich_type='KEGG' AND cluster=#{param1} and padj &lt; ${param2} order by generatio desc",
            "</script>"
    })
    List<Kegg> getPathwayKEGGAtac(String table, String param1, String param2);


    @Select({
            "<script>",
            "SELECT * FROM ${table} WHERE 1=1",
            " <when test='searchValue !=\"\"'>",
            " AND (gene LIKE CONCAT('%',#{searchValue},'%') or celltype LIKE CONCAT('%',#{searchValue},'%'))",
            " </when> ",
            " <when test='order ==\"0\"'>",
            " order by gene ${dir}",
            " </when> ",
            " <when test='order ==\"1\"'>",
            " order by celltype ${dir}",
            " </when> ",
            " <when test='order ==\"2\"'>",
            " order by p_value ${dir}",
            " </when> ",
            " <when test='order ==\"3\"'>",
            " order by avg_log2fc ${dir}",
            " </when> ",
            " <when test='order ==\"4\"'>",
            " order by p_value_adj ${dir}",
            " </when> ",
            " <when test='order ==\"5\"'>",
            " order by pct1 ${dir}",
            " </when> ",
            " <when test='order ==\"6\"'>",
            " order by pct2 ${dir}",
            " </when> ",
            "</script>"
    })
    Page<DegTable> getDegTable(String table, String searchValue, String order, String dir);

    @Select({
            "<script>",
            "SELECT pval,avglogfc,pct1,pct2,p_val_adj,distance,celltype,gene,peaks FROM ${table} WHERE 1=1",
            " <when test='searchValue !=\"\"'>",
            " AND (gene LIKE CONCAT('%',#{searchValue},'%') or celltype LIKE CONCAT('%',#{searchValue},'%'))",
            " </when> ",
            " <when test='order ==\"0\"'>",
            " order by peaks ${dir}",
            " </when> ",
            " <when test='order ==\"1\"'>",
            " order by gene ${dir}",
            " </when> ",
            " <when test='order ==\"2\"'>",
            " order by distance ${dir}",
            " </when> ",
            " <when test='order ==\"3\"'>",
            " order by pval ${dir}",
            " </when> ",
            " <when test='order ==\"4\"'>",
            " order by avglogfc ${dir}",
            " </when> ",
            " <when test='order ==\"5\"'>",
            " order by pct1 ${dir}",
            " </when> ",
            " <when test='order ==\"6\"'>",
            " order by pct2 ${dir}",
            " </when> ",
            " <when test='order ==\"7\"'>",
            " order by p_val_adj ${dir}",
            " </when> ",
            " <when test='order ==\"8\"'>",
            " order by celltype ${dir}",
            " </when> ",
            "</script>"
    })
    Page<PeakDegTable> getPeakDegTable(String table, String searchValue, String order, String dir);

    @Select({
            "<script>",
            "SELECT pval,avglogfc,pct1,pct2,p_val_adj,distance,celltype,gene,peaks FROM ${table} WHERE gene = #{gene}",
            " <when test='searchValue !=\"\"'>",
            " AND (gene LIKE CONCAT('%',#{searchValue},'%') or celltype LIKE CONCAT('%',#{searchValue},'%'))",
            " </when> ",
            " <when test='order ==\"0\"'>",
            " order by peaks ${dir}",
            " </when> ",
            " <when test='order ==\"1\"'>",
            " order by gene ${dir}",
            " </when> ",
            " <when test='order ==\"2\"'>",
            " order by distance ${dir}",
            " </when> ",
            " <when test='order ==\"3\"'>",
            " order by pval ${dir}",
            " </when> ",
            " <when test='order ==\"4\"'>",
            " order by avglogfc ${dir}",
            " </when> ",
            " <when test='order ==\"5\"'>",
            " order by pct1 ${dir}",
            " </when> ",
            " <when test='order ==\"6\"'>",
            " order by pct2 ${dir}",
            " </when> ",
            " <when test='order ==\"7\"'>",
            " order by p_val_adj ${dir}",
            " </when> ",
            " <when test='order ==\"8\"'>",
            " order by celltype ${dir}",
            " </when> ",
            "</script>"
    })
    Page<PeakDegTable> getPeakDegTableByGene(String table,String gene, String searchValue, String order, String dir);

    @Select({
            "<script>",
            "SELECT * FROM ${table} WHERE gene = #{gene}",
            " <when test='searchValue !=\"\"'>",
            " AND (gene LIKE CONCAT('%',#{searchValue},'%') or celltype LIKE CONCAT('%',#{searchValue},'%'))",
            " </when> ",
            " <when test='order ==\"0\"'>",
            " order by gene ${dir}",
            " </when> ",
            " <when test='order ==\"1\"'>",
            " order by celltype ${dir}",
            " </when> ",
            " <when test='order ==\"2\"'>",
            " order by p_value ${dir}",
            " </when> ",
            " <when test='order ==\"3\"'>",
            " order by avg_log2fc ${dir}",
            " </when> ",
            " <when test='order ==\"4\"'>",
            " order by p_value_adj ${dir}",
            " </when> ",
            " <when test='order ==\"5\"'>",
            " order by pct1 ${dir}",
            " </when> ",
            " <when test='order ==\"6\"'>",
            " order by pct2 ${dir}",
            " </when> ",
            "</script>"
    })
    Page<DegTable> getDegTableByGene(String table, String gene, String searchValue, String order, String dir);

    @Select({
            "<script>",
            "SELECT * FROM ${table} WHERE 1=1",
            " <when test='celltype !=\"\"'> ",
            " AND celltype = #{celltype}",
            " </when> ",
            " <when test='searchValue !=\"\"'>",
            " AND (gene LIKE CONCAT('%',#{searchValue},'%') or celltype LIKE CONCAT('%',#{searchValue},'%'))",
            " </when> ",
            " <when test='order ==\"0\"'>",
            " order by gene ${dir}",
            " </when> ",
            " <when test='order ==\"1\"'>",
            " order by celltype ${dir}",
            " </when> ",
            " <when test='order ==\"2\"'>",
            " order by p_value ${dir}",
            " </when> ",
            " <when test='order ==\"3\"'>",
            " order by avg_log2fc ${dir}",
            " </when> ",
            " <when test='order ==\"4\"'>",
            " order by p_value_adj ${dir}",
            " </when> ",
            " <when test='order ==\"5\"'>",
            " order by pct1 ${dir}",
            " </when> ",
            " <when test='order ==\"6\"'>",
            " order by pct2 ${dir}",
            " </when> ",
            "</script>"
    })
    Page<DegTable> getDegTableFilter(String table, String celltype, String searchValue, String order, String dir);

    @Select({
            "<script>",
            "SELECT gsm_id as gsmId,sample_name as sampleName,tissue,age,sex,treatment,disease,times,sample FROM sample_table WHERE 1=1",
            " <when test='id !=\"\"'> ",
            " AND sample_id = #{id}",
            " </when> ",
            " <when test='searchValue !=\"\"'>",
            " AND (gsm_id LIKE CONCAT('%',#{searchValue},'%') or sample_name LIKE CONCAT('%',#{searchValue},'%') or tissue LIKE CONCAT('%',#{searchValue},'%') or age LIKE CONCAT('%',#{searchValue},'%'))",
            " </when> ",
            " <when test='order ==\"0\"'>",
            " order by gsmId ${dir}",
            " </when> ",
            " <when test='order ==\"1\"'>",
            " order by sampleName ${dir}",
            " </when> ",
            " <when test='order ==\"2\"'>",
            " order by tissue ${dir}",
            " </when> ",
            " <when test='order ==\"3\"'>",
            " order by age ${dir}",
            " </when> ",
            " <when test='order ==\"4\"'>",
            " order by sex ${dir}",
            " </when> ",
            " <when test='order ==\"5\"'>",
            " order by treatment ${dir}",
            " </when> ",
            " <when test='order ==\"6\"'>",
            " order by times ${dir}",
            " </when> ",
            "</script>"
    })
    Page<SampleTable> getSampleInfo(String id, String searchValue, String order, String dir);
    @Select({
            "<script>",
            "SELECT id as sampleid, COUNT(DISTINCT origident) as origidentNum, COUNT(DISTINCT celltype) as celltypeNum, SUM(cell_count) as cellNum FROM celltypenumbysample GROUP BY id",
            " <when test='order ==\"0\"'>",
            " order by id ${dir}",
            " </when> ",
            " <when test='order ==\"1\"'>",
            " order by origidentNum ${dir}",
            " </when> ",
            " <when test='order ==\"2\"'>",
            " order by celltypeNum ${dir}",
            " </when> ",
            " <when test='order ==\"3\"'>",
            " order by cellNum ${dir}",
            " </when> ",
            "</script>"
    })
    Page<SampleTotal> getSampleTotal(String searchValue, String order, String dir);

    @Select({
            "<script>",
            "SELECT DISTINCT gse_id as gseId,species,genomic,technology,strain,pmid,article,journal,year,descript FROM sample_table WHERE sample_id = #{id}",
            "</script>"
    })
    List<SampleTable> getSampleDescript(String id);

    @Select({
            "<script>",
            "SELECT umap_x, umap_y FROM ${table}",
            "</script>"
    })
    List<UmapPosition> getUmapPosition(String table);

    @Select({
            "<script>",
            "SELECT * FROM ${table} WHERE symbol = #{gene}",
            "</script>"
    })
    GeneDescription getGeneDescription(String table, String gene);

    @Select({
            "<script>",
            "SELECT DISTINCT species FROM sample_table WHERE sample_id = #{id}",
            "</script>"
    })
    String getSpecies(String id);

    @Select("<script>" +
            "SELECT * FROM ${table} WHERE gene IN " +
            "<foreach collection='genes' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    List<GeneExpressData> getGeneExpressData(String[] genes,String table);

    @Select("<script>" +
            "SELECT DISTINCT sample_id AS sampleId,gse_id AS gseId,sample AS gse_sample,species,gse_age AS gse_age,gse_sex AS gse_sex,gse_treatment AS gse_treatment,genomic,technology,pmid,article,journal,year,descript,GROUP_CONCAT(gsm_id SEPARATOR '|') AS gsmId,GROUP_CONCAT(age SEPARATOR '|') AS age,GROUP_CONCAT(sex SEPARATOR '|') AS sex,GROUP_CONCAT(times SEPARATOR '|') AS times,GROUP_CONCAT(tissue SEPARATOR '|') AS tissue,GROUP_CONCAT(treatment SEPARATOR '|') AS treatment,GROUP_CONCAT(sample_name SEPARATOR '|') AS sampleName FROM sample_table WHERE sample_id IN " +
            "<foreach collection='datasets' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "GROUP BY sample_id,gse_id,sample,species,gse_age,gse_sex,gse_treatment,genomic,technology,pmid,article,journal,`year`,descript" +
            "</script>")
    List<SampleTable> getSampleByDataset(String[] datasets);
    @Select("<script>" +
            "WITH all_ids AS (" +
            "    SELECT DISTINCT id FROM celltypenumbysample" +
            "), " +
            "all_celltypes AS (" +
            "    SELECT DISTINCT celltype FROM celltypenumbysample" +
            "), " +
            "aggregated_data AS (" +
            "    SELECT id, celltype, SUM(cell_count) as total_cell_count " +
            "    FROM celltypenumbysample " +
            "    GROUP BY id, celltype " +
            ") " +
            "SELECT ai.id, ac.celltype, COALESCE(ad.total_cell_count, 0) as cell_count " +
            "FROM all_ids ai " +
            "CROSS JOIN all_celltypes ac " +
            "LEFT JOIN aggregated_data ad ON ai.id = ad.id AND ac.celltype = ad.celltype " +
            "ORDER BY ai.id, ac.celltype" +
            "</script>")
    List<CelltypeNumBySample> sampleCelltypeCount();

    @Select({
            "<script>",
            "SELECT * FROM marker_expression_all_samples WHERE id = #{id}",
            "</script>"
    })
    List<Marker_expression_all_samples> getDotMarker(String id);
    @Select({
            "<script>",
            "SELECT DISTINCT technology FROM sample_table WHERE sample_id = #{id}",
            "</script>"
    })
    String getTechnology(String id);
    @Select({
            "<script>",
            "SELECT customid as sampleid, gse_id, group_name,case_group,ctrl_group,sample_n,gene_n,output_file FROM rna_deg_analysis_summary WHERE customid = #{id}",
            "</script>"
    })
    List<DegTableGroup> getRnaDegGroup(String id);
    @Select({
            "<script>",
            "SELECT gene, log2FoldChange as log2fc, pvalue FROM ${id} WHERE pvalue &lt; 0.05 AND (log2FoldChange &gt; 0.5 OR log2FoldChange &lt; -0.5)",
            "</script>"
    })
    List<RnaDegVolcano> getRnaDegVolcano_sig(String id);
    @Select({
            "<script>",
            "SELECT gene, log2FoldChange as log2fc, pvalue FROM ${id} WHERE NOT (pvalue &lt; 0.05 AND (log2FoldChange &gt; 0.5 OR log2FoldChange &lt; -0.5)) ORDER BY RAND() LIMIT 3000\n",
            "</script>"
    })
    List<RnaDegVolcano> getRnaDegVolcano_nosig(String id);

    @Select({
            "<script>",
            "SELECT gene,baseMean,log2FoldChange as log2fc,lfcSE,pvalue,padj,baseMean_case,baseMean_ctrl FROM ${id} WHERE pvalue &lt; 0.05 AND (log2FoldChange &gt; 0.5 OR log2FoldChange &lt; -0.5)",
            " <when test='searchValue !=\"\"'>",
            " AND (gene LIKE CONCAT('%',#{searchValue},'%'))",
            " </when> ",
            " <when test='order ==\"0\"'>",
            " order by gene ${dir}",
            " </when> ",
            " <when test='order ==\"1\"'>",
            " order by baseMean ${dir}",
            " </when> ",
            " <when test='order ==\"2\"'>",
            " order by log2FoldChange ${dir}",
            " </when> ",
            " <when test='order ==\"3\"'>",
            " order by lfcSE ${dir}",
            " </when> ",
            " <when test='order ==\"4\"'>",
            " order by pvalue ${dir}",
            " </when> ",
            " <when test='order ==\"5\"'>",
            " order by padj ${dir}",
            " </when> ",
            " <when test='order ==\"6\"'>",
            " order by baseMean_case ${dir}",
            " </when> ",
            " <when test='order ==\"7\"'>",
            " order by baseMean_ctrl ${dir}",
            " </when> ",
            "</script>"
    })
    Page<RnaDegTab> getRnaDegTab(String id, String searchValue, String order, String dir);
    @Select({
            "<script>",
            "SELECT DISTINCT rnaseq_group.norm_name FROM sample_table LEFT JOIN rnaseq_group on sample_table.species=rnaseq_group.species WHERE sample_id=#{id}",
            "</script>"
    })
    List<String> getRnaGroup(String id);
    @Select({
            "<script>",
            "SELECT gene_symbol,gsm_id,expr_value FROM ${table} WHERE gene_symbol=#{gene}",
            "</script>"
    })
    List<RnaExp> getGeneExpr(String table,String gene);

    @Select({
            "<script>",
            "SELECT DISTINCT r.gse_id as gseId,r.gsm_id as gsmId,r.sample_name as sampleName,r.species,r.tissue,r.age,r.sex,r.treatment,r.disease,r.times,r.sample,r.genomic,r.strain,r.pmid,r.article,r.journal,r.year FROM sample_table as t LEFT JOIN sample_table_rna as r ON t.species=r.species WHERE t.sample_id=#{id}",
            " <when test='searchValue !=\"\"'>",
            " AND (r.gse_id LIKE CONCAT('%',#{searchValue},'%') or r.gsm_id LIKE CONCAT('%',#{searchValue},'%') or r.sample_name LIKE CONCAT('%',#{searchValue},'%') or r.species LIKE CONCAT('%',#{searchValue},'%') or r.tissue LIKE CONCAT('%',#{searchValue},'%') or r.age LIKE CONCAT('%',#{searchValue},'%'))",
            " </when> ",
            " <when test='order ==\"0\"'>",
            " order by r.gse_id ${dir}",
            " </when> ",
            " <when test='order ==\"1\"'>",
            " order by r.gsm_id ${dir}",
            " </when> ",
            " <when test='order ==\"2\"'>",
            " order by r.sample_name ${dir}",
            " </when> ",
            " <when test='order ==\"3\"'>",
            " order by r.species ${dir}",
            " </when> ",
            " <when test='order ==\"4\"'>",
            " order by tissue ${dir}",
            " </when> ",
            " <when test='order ==\"5\"'>",
            " order by age ${dir}",
            " </when> ",
            " <when test='order ==\"6\"'>",
            " order by sex ${dir}",
            " </when> ",
            " <when test='order ==\"7\"'>",
            " order by treatment ${dir}",
            " </when> ",
            " <when test='order ==\"8\"'>",
            " order by disease ${dir}",
            " </when> ",
            " <when test='order ==\"9\"'>",
            " order by times ${dir}",
            " </when> ",
            " <when test='order ==\"10\"'>",
            " order by sample ${dir}",
            " </when> ",
            " <when test='order ==\"11\"'>",
            " order by genomic ${dir}",
            " </when> ",
            " <when test='order ==\"12\"'>",
            " order by strain ${dir}",
            " </when> ",
            " <when test='order ==\"13\"'>",
            " order by pmid ${dir}",
            " </when> ",
            " <when test='order ==\"14\"'>",
            " order by article ${dir}",
            " </when> ",
            " <when test='order ==\"15\"'>",
            " order by journal ${dir}",
            " </when> ",
            " <when test='order ==\"16\"'>",
            " order by year ${dir}",
            " </when> ",
            "</script>"
    })
    Page<SampleTable> getRnaSampleTable(String id, String searchValue, String order, String dir);
    @Select({
            "<script>",
            "SELECT t.customid as customid,t.gse_id as gse_id,t.case_group as case_group,t.ctrl_group as ctrl_group,s.gene as gene,s.log2fc as log2fc,s.pvalue as pvalue,s.padj as padj FROM rna_deg_analysis_summary as t LEFT JOIN ${table} as s ON t.contrastid=s.contrastid WHERE t.species=#{specie} AND s.gene=#{gene}",
            "</script>"
    })
    List<RnaBubble> getRnaBubbleData(String table,String gene,String specie);
    @Select({
            "<script>",
            "SELECT DISTINCT t.gse_id as gse_id,t.case_group as case_group,t.ctrl_group as ctrl_group,t.sample_n as sample_n,s.sample_name as sample_name,s.times as times FROM rna_deg_analysis_summary as t LEFT JOIN sample_table_rna as s ON t.gse_id=s.gse_id WHERE t.species=#{specie}",
            " <when test='searchValue !=\"\"'>",
            " AND (gse_id LIKE CONCAT('%',#{searchValue},'%') or case_group LIKE CONCAT('%',#{searchValue},'%') or ctrl_group LIKE CONCAT('%',#{searchValue},'%') or sample_name LIKE CONCAT('%',#{searchValue},'%') or times LIKE CONCAT('%',#{searchValue},'%'))",
            " </when> ",
            " <when test='order ==\"0\"'>",
            " order by gse_id ${dir}",
            " </when> ",
            " <when test='order ==\"1\"'>",
            " order by case_group ${dir}",
            " </when> ",
            " <when test='order ==\"2\"'>",
            " order by ctrl_group ${dir}",
            " </when> ",
            " <when test='order ==\"3\"'>",
            " order by sample_n ${dir}",
            " </when> ",
            " <when test='order ==\"4\"'>",
            " order by sample_name ${dir}",
            " </when> ",
            " <when test='order ==\"5\"'>",
            " order by times ${dir}",
            " </when> ",
            "</script>"
    })
    Page<RnaSampleGroup> getRnaSampleGroupTable(String specie, String searchValue, String order, String dir);
}
