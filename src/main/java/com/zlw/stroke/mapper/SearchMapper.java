package com.zlw.stroke.mapper;

import com.github.pagehelper.Page;
import com.zlw.stroke.pojo.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SearchMapper {

    @Select({
            "<script>",
            "SELECT DISTINCT sample_id AS sampleId,gse_id AS gseId,sample AS gse_sample,species,gse_age AS gse_age,gse_sex AS gse_sex,gse_treatment AS gse_treatment,genomic,technology,pmid,article,journal,year,descript,GROUP_CONCAT(gsm_id SEPARATOR '|') AS gsmId,GROUP_CONCAT(age SEPARATOR '|') AS age,GROUP_CONCAT(sex SEPARATOR '|') AS sex,GROUP_CONCAT(times SEPARATOR '|') AS times,GROUP_CONCAT(tissue SEPARATOR '|') AS tissue,GROUP_CONCAT(treatment SEPARATOR '|') AS treatment,GROUP_CONCAT(sample_name SEPARATOR '|') AS sampleName FROM sample_table WHERE 1 = 1 GROUP BY sample_id,gse_id,sample,species,gse_age,gse_sex,gse_treatment,genomic,technology,pmid,article,journal,`year`,descript",
            " <when test='searchValue !=\"\"'>",
            " AND (sample_id LIKE CONCAT('%',#{searchValue},'%') or gse_id LIKE CONCAT('%',#{searchValue},'%') or sample LIKE CONCAT('%',#{searchValue},'%') or gse_age LIKE CONCAT('%',#{searchValue},'%') or gse_treatment LIKE CONCAT('%',#{searchValue},'%') or genomic LIKE CONCAT('%',#{searchValue},'%') or article LIKE CONCAT('%',#{searchValue},'%'))",
            " </when> ",
            " <when test='order ==\"1\"'>",
            " order by sampleId ${dir}",
            " </when> ",
            " <when test='order ==\"2\"'>",
            " order by gse_sample ${dir}",
            " </when> ",
            " <when test='order ==\"3\"'>",
            " order by species ${dir}",
            " </when> ",
            " <when test='order ==\"4\"'>",
            " order by gse_age ${dir}",
            " </when> ",
            " <when test='order ==\"5\"'>",
            " order by gse_treatment ${dir}",
            " </when> ",
            " <when test='order ==\"6\"'>",
            " order by gse_sex ${dir}",
            " </when> ",
            " <when test='order ==\"7\"'>",
            " order by technology ${dir}",
            " </when> ",
            " <when test='order ==\"8\"'>",
            " order by journal ${dir}",
            " </when> ",
            "</script>"
    })
    Page<SampleTable> getSampleAna(String searchValue, String order, String dir);

    @Select({
            "<script>",
            "SELECT DISTINCT sample_id as sampleId, gse_id as gseId,gsm_id as gsmId,sample_name as sampleName,species,tissue,age,sex,treatment,disease,times,sample,genomic,technology,strain,pmid,article,journal,year,celltype FROM sample_table RIGHT JOIN celltypenumbysample ON sample_id = id WHERE 1=1",
            " <when test='param !=\"\"'> ",
            " AND celltype = #{param}",
            " </when> ",
            " <when test='searchValue !=\"\"'>",
            " AND (sample_id LIKE CONCAT('%',#{searchValue},'%') or gse_id LIKE CONCAT('%',#{searchValue},'%') or gsm_id LIKE CONCAT('%',#{searchValue},'%') or sample_name LIKE CONCAT('%',#{searchValue},'%') or species LIKE CONCAT('%',#{searchValue},'%') or tissue LIKE CONCAT('%',#{searchValue},'%') or age LIKE CONCAT('%',#{searchValue},'%'))",
            " </when> ",
            " <when test='order ==\"0\"'>",
            " order by sampleId ${dir}",
            " </when> ",
            " <when test='order ==\"1\"'>",
            " order by gseId ${dir}",
            " </when> ",
            " <when test='order ==\"2\"'>",
            " order by gsmId ${dir}",
            " </when> ",
            " <when test='order ==\"3\"'>",
            " order by sampleName ${dir}",
            " </when> ",
            " <when test='order ==\"4\"'>",
            " order by species ${dir}",
            " </when> ",
            " <when test='order ==\"5\"'>",
            " order by tissue ${dir}",
            " </when> ",
            " <when test='order ==\"6\"'>",
            " order by age ${dir}",
            " </when> ",
            " <when test='order ==\"7\"'>",
            " order by sex ${dir}",
            " </when> ",
            " <when test='order ==\"8\"'>",
            " order by treatment ${dir}",
            " </when> ",
            " <when test='order ==\"9\"'>",
            " order by disease ${dir}",
            " </when> ",
            " <when test='order ==\"10\"'>",
            " order by times ${dir}",
            " </when> ",
            " <when test='order ==\"11\"'>",
            " order by sample ${dir}",
            " </when> ",
            " <when test='order ==\"12\"'>",
            " order by genomic ${dir}",
            " </when> ",
            " <when test='order ==\"13\"'>",
            " order by technology ${dir}",
            " </when> ",
            " <when test='order ==\"14\"'>",
            " order by strain ${dir}",
            " </when> ",
            " <when test='order ==\"15\"'>",
            " order by pmid ${dir}",
            " </when> ",
            " <when test='order ==\"16\"'>",
            " order by article ${dir}",
            " </when> ",
            " <when test='order ==\"17\"'>",
            " order by journal ${dir}",
            " </when> ",
            " <when test='order ==\"18\"'>",
            " order by year ${dir}",
            " </when> ",
            " <when test='order ==\"19\"'>",
            " order by celltype ${dir}",
            " </when> ",
            "</script>"
    })
    Page<SampleTable> getSearchSampleTableByCelltype(String param, String searchValue, String order, String dir);

    @Select({
            "<script>",
            "SELECT DISTINCT sample_id as sampleId, gse_id as gseId,gsm_id as gsmId,sample_name as sampleName,species,tissue,age,sex,treatment,disease,times,sample,genomic,technology,strain,pmid,article,journal,year,celltype FROM sample_table RIGHT JOIN celltypenumbysample ON sample_id = id WHERE 1=1",
            " <when test='param !=\"\"'> ",
            " AND tissue = #{param}",
            " </when> ",
            " <when test='searchValue !=\"\"'>",
            " AND (sample_id LIKE CONCAT('%',#{searchValue},'%') or gse_id LIKE CONCAT('%',#{searchValue},'%') or gsm_id LIKE CONCAT('%',#{searchValue},'%') or sample_name LIKE CONCAT('%',#{searchValue},'%') or species LIKE CONCAT('%',#{searchValue},'%') or tissue LIKE CONCAT('%',#{searchValue},'%') or age LIKE CONCAT('%',#{searchValue},'%'))",
            " </when> ",
            " <when test='order ==\"0\"'>",
            " order by sampleId ${dir}",
            " </when> ",
            " <when test='order ==\"1\"'>",
            " order by gseId ${dir}",
            " </when> ",
            " <when test='order ==\"2\"'>",
            " order by gsmId ${dir}",
            " </when> ",
            " <when test='order ==\"3\"'>",
            " order by sampleName ${dir}",
            " </when> ",
            " <when test='order ==\"4\"'>",
            " order by species ${dir}",
            " </when> ",
            " <when test='order ==\"5\"'>",
            " order by tissue ${dir}",
            " </when> ",
            " <when test='order ==\"6\"'>",
            " order by age ${dir}",
            " </when> ",
            " <when test='order ==\"7\"'>",
            " order by sex ${dir}",
            " </when> ",
            " <when test='order ==\"8\"'>",
            " order by treatment ${dir}",
            " </when> ",
            " <when test='order ==\"9\"'>",
            " order by disease ${dir}",
            " </when> ",
            " <when test='order ==\"10\"'>",
            " order by times ${dir}",
            " </when> ",
            " <when test='order ==\"11\"'>",
            " order by sample ${dir}",
            " </when> ",
            " <when test='order ==\"12\"'>",
            " order by genomic ${dir}",
            " </when> ",
            " <when test='order ==\"13\"'>",
            " order by technology ${dir}",
            " </when> ",
            " <when test='order ==\"14\"'>",
            " order by strain ${dir}",
            " </when> ",
            " <when test='order ==\"15\"'>",
            " order by pmid ${dir}",
            " </when> ",
            " <when test='order ==\"16\"'>",
            " order by article ${dir}",
            " </when> ",
            " <when test='order ==\"17\"'>",
            " order by journal ${dir}",
            " </when> ",
            " <when test='order ==\"18\"'>",
            " order by year ${dir}",
            " </when> ",
            " <when test='order ==\"19\"'>",
            " order by celltype ${dir}",
            " </when> ",
            "</script>"
    })
    Page<SampleTable> getSearchSampleTableByTissue(String param, String searchValue, String order, String dir);

    @Select({
            "<script>",
            "SELECT DISTINCT sample_id as sampleId, gse_id as gseId,gsm_id as gsmId,sample_name as sampleName,species,tissue,age,sex,treatment,disease,times,sample,genomic,technology,strain,pmid,article,journal,year,celltype FROM sample_table RIGHT JOIN celltypenumbysample ON sample_id = id WHERE 1=1",
            " <when test='param !=\"\"'> ",
            " AND disease = #{param}",
            " </when> ",
            " <when test='searchValue !=\"\"'>",
            " AND (sample_id LIKE CONCAT('%',#{searchValue},'%') or gse_id LIKE CONCAT('%',#{searchValue},'%') or gsm_id LIKE CONCAT('%',#{searchValue},'%') or sample_name LIKE CONCAT('%',#{searchValue},'%') or species LIKE CONCAT('%',#{searchValue},'%') or tissue LIKE CONCAT('%',#{searchValue},'%') or age LIKE CONCAT('%',#{searchValue},'%'))",
            " </when> ",
            " <when test='order ==\"0\"'>",
            " order by sampleId ${dir}",
            " </when> ",
            " <when test='order ==\"1\"'>",
            " order by gseId ${dir}",
            " </when> ",
            " <when test='order ==\"2\"'>",
            " order by gsmId ${dir}",
            " </when> ",
            " <when test='order ==\"3\"'>",
            " order by sampleName ${dir}",
            " </when> ",
            " <when test='order ==\"4\"'>",
            " order by species ${dir}",
            " </when> ",
            " <when test='order ==\"5\"'>",
            " order by tissue ${dir}",
            " </when> ",
            " <when test='order ==\"6\"'>",
            " order by age ${dir}",
            " </when> ",
            " <when test='order ==\"7\"'>",
            " order by sex ${dir}",
            " </when> ",
            " <when test='order ==\"8\"'>",
            " order by treatment ${dir}",
            " </when> ",
            " <when test='order ==\"9\"'>",
            " order by disease ${dir}",
            " </when> ",
            " <when test='order ==\"10\"'>",
            " order by times ${dir}",
            " </when> ",
            " <when test='order ==\"11\"'>",
            " order by sample ${dir}",
            " </when> ",
            " <when test='order ==\"12\"'>",
            " order by genomic ${dir}",
            " </when> ",
            " <when test='order ==\"13\"'>",
            " order by technology ${dir}",
            " </when> ",
            " <when test='order ==\"14\"'>",
            " order by strain ${dir}",
            " </when> ",
            " <when test='order ==\"15\"'>",
            " order by pmid ${dir}",
            " </when> ",
            " <when test='order ==\"16\"'>",
            " order by article ${dir}",
            " </when> ",
            " <when test='order ==\"17\"'>",
            " order by journal ${dir}",
            " </when> ",
            " <when test='order ==\"18\"'>",
            " order by year ${dir}",
            " </when> ",
            " <when test='order ==\"19\"'>",
            " order by celltype ${dir}",
            " </when> ",
            "</script>"
    })
    Page<SampleTable> getSearchSampleTableByDisease(String param, String searchValue, String order, String dir);

    @Select({
            "<script>",
            "SELECT DISTINCT celltype as name,celltype as value FROM celltypenumbysample",
            "</script>"
    })
    List<NameValue> getCelltype();

    @Select({
            "<script>",
            "SELECT DISTINCT tissue as name,tissue as value FROM sample_table",
            "</script>"
    })
    List<NameValue> getTissuetype();

    @Select({
            "<script>",
            "SELECT DISTINCT disease as name,disease as value FROM sample_table",
            "</script>"
    })
    List<NameValue> getDisease();

    /*细胞类型聚类图*/
    @Select({
            "<script>",
            "SELECT barcode as name,UMAP_X as x,UMAP_Y as y,${group} as category,celltype,clusters,origident FROM ${table}",
            "</script>"
    })
    List<Nodes> getNode(String table,String group);

/*    @Select({
            "<script>",
            "SELECT DISTINCT UMAP_X as x,UMAP_Y as y, celltype, clusters, origident, sex, age, times, tissue, treatment FROM ${table} LEFT JOIN sample_table ON origident=gsm_id",
            "</script>"
    })
    List<NodeGL> getNodeGL(String table);*/
    @Select({
            "<script>",
            "SELECT DISTINCT ",
            "CAST(NULLIF(UMAP_X,'') AS DECIMAL(16,6)) AS x,",
            "CAST(NULLIF(UMAP_Y,'') AS DECIMAL(16,6)) AS y,",
            "celltype, clusters, origident, sex, age, times, tissue, treatment ",
            "FROM ${table} LEFT JOIN sample_table ON origident=gsm_id",
            "</script>"
    })
    List<NodeGL> getNodeGL(@Param("table") String table);

    @Select({
            "<script>",
            "SELECT AVG(UMAP_X) as x, AVG(UMAP_Y) as y, ${group} as category FROM ${table} group by ${group}",
            "</script>"
    })
    @MapKey("category")
    List<Nodes> getNodeText(String table, String group);


    @Select({
            "<script>",
            "SELECT distinct celltype as name FROM e_mtab_9765_metadata",
            "</script>"
    })
    List<Categories> getCategories();

    /*细胞细胞互作图*/
    @Select({
            "<script>",
            "select celltype1 AS id,celltype1 AS name,celltype1 AS category,celltype_num+15 AS symbolSize FROM ${table} GROUP BY celltype1,celltype_num",
            "</script>"
    })
    List<Nodes2> getNodeCellInteraction(String table);

    @Select({
            "<script>",
            "SELECT CAST(@row_number := @row_number + 1 AS UNSIGNED) AS id, " +
                    "tf AS name, " +
                    "'TF' AS category, " +
                    "importance AS symbolSize, importance AS value " +
                    "FROM ${table}, (SELECT @row_number := 0) AS t " +
                    "WHERE gene = #{gene}",
            "</script>"
    })
    List<Nodes2> geneTfNetwork(String table, String gene);

    @Select({
            "<script>",
            "select celltype1 AS source,celltype2 AS target, ${type} AS value FROM ${table}",
            "</script>"
    })
    List<Links> getLinkCellInteraction(String table, String type);

    @Select({
            "<script>",
            "SELECT distinct celltype1 as name FROM ${table}",
            "</script>"
    })
    List<Categories> getCategoriesCellInteraction(String table);

}
