package com.zlw.stroke.mapper;

import com.github.pagehelper.Page;
import com.zlw.stroke.pojo.BrowseParamTotal;
import com.zlw.stroke.pojo.BrowserParam;
import com.zlw.stroke.pojo.SampleTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface Browse {

    @Select({
            "<script>",
            "SELECT sample_id as sampleId, gse_id as gseId,gsm_id as gsmId,sample_name as sampleName,species,tissue,age,sex,treatment,disease,times,sample,genomic,technology,strain,pmid,article,journal,year FROM sample_table WHERE 1=1",
            " <when test='tab1Param !=\"\"'> ",
            " AND species = #{tab1Param}",
            " </when> ",
            " <when test='tab2Param !=\"\"'> ",
            " AND tissue = #{tab2Param}",
            " </when> ",
            " <when test='tab3Param !=\"\"'>",
            " AND disease = #{tab3Param}",
            " </when> ",
            " <when test='tab4Param !=\"\"'>",
            " AND treatment = #{tab4Param}",
            " </when> ",
            " <when test='tab5Param !=\"\"'>",
            " AND technology = #{tab5Param}",
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
            "</script>"
    })
    Page<SampleTable> getSampleTable(String tab1Param, String tab2Param, String tab3Param, String tab4Param, String tab5Param, String searchValue, String order, String dir);


    @Select({
            "<script>",
            "SELECT COUNT(species) as sum , species as name FROM sample_table WHERE 1=1",
            " <when test='tab1Param !=\"\"'> ",
            " AND species = #{tab1Param}",
            " </when> ",
            " <when test='tab2Param !=\"\"'> ",
            " AND tissue = #{tab2Param}",
            " </when> ",
            " <when test='tab3Param !=\"\"'>",
            " AND disease = #{tab3Param}",
            " </when> ",
            " <when test='tab4Param !=\"\"'>",
            " AND treatment = #{tab4Param}",
            " </when> ",
            " <when test='tab5Param !=\"\"'>",
            " AND technology = #{tab5Param}",
            " </when> ",
            " GROUP BY species",
            "</script>"
    })
    List<BrowseParamTotal> getTab1Param(BrowserParam param);

    @Select({
            "<script>",
            "SELECT COUNT(tissue) as sum , tissue as name FROM sample_table WHERE 1=1",
            " <when test='tab1Param !=\"\"'> ",
            " AND species = #{tab1Param}",
            " </when> ",
            " <when test='tab2Param !=\"\"'> ",
            " AND tissue = #{tab2Param}",
            " </when> ",
            " <when test='tab3Param !=\"\"'>",
            " AND disease = #{tab3Param}",
            " </when> ",
            " <when test='tab4Param !=\"\"'>",
            " AND treatment = #{tab4Param}",
            " </when> ",
            " <when test='tab5Param !=\"\"'>",
            " AND technology = #{tab5Param}",
            " </when> ",
            " GROUP BY tissue",
            "</script>"
    })
    List<BrowseParamTotal> getTab2Param(BrowserParam param);

    @Select({
            "<script>",
            "SELECT COUNT(disease) as sum , disease as name FROM sample_table WHERE 1=1",
            " <when test='tab1Param !=\"\"'> ",
            " AND species = #{tab1Param}",
            " </when> ",
            " <when test='tab2Param !=\"\"'> ",
            " AND tissue = #{tab2Param}",
            " </when> ",
            " <when test='tab3Param !=\"\"'>",
            " AND disease = #{tab3Param}",
            " </when> ",
            " <when test='tab4Param !=\"\"'>",
            " AND treatment = #{tab4Param}",
            " </when> ",
            " <when test='tab5Param !=\"\"'>",
            " AND technology = #{tab5Param}",
            " </when> ",
            " GROUP BY disease",
            "</script>"
    })
    List<BrowseParamTotal> getTab3Param(BrowserParam param);

    @Select({
            "<script>",
            "SELECT COUNT(treatment) as sum , treatment as name FROM sample_table WHERE 1=1",
            " <when test='tab1Param !=\"\"'> ",
            " AND species = #{tab1Param}",
            " </when> ",
            " <when test='tab2Param !=\"\"'> ",
            " AND tissue = #{tab2Param}",
            " </when> ",
            " <when test='tab3Param !=\"\"'>",
            " AND disease = #{tab3Param}",
            " </when> ",
            " <when test='tab4Param !=\"\"'>",
            " AND treatment = #{tab4Param}",
            " </when> ",
            " <when test='tab5Param !=\"\"'>",
            " AND technology = #{tab5Param}",
            " </when> ",
            " GROUP BY treatment",
            "</script>"
    })
    List<BrowseParamTotal> getTab4Param(BrowserParam param);

    @Select({
            "<script>",
            "SELECT COUNT(technology) as sum , technology as name FROM sample_table WHERE 1=1",
            " <when test='tab1Param !=\"\"'> ",
            " AND species = #{tab1Param}",
            " </when> ",
            " <when test='tab2Param !=\"\"'> ",
            " AND tissue = #{tab2Param}",
            " </when> ",
            " <when test='tab3Param !=\"\"'>",
            " AND disease = #{tab3Param}",
            " </when> ",
            " <when test='tab4Param !=\"\"'>",
            " AND treatment = #{tab4Param}",
            " </when> ",
            " <when test='tab5Param !=\"\"'>",
            " AND technology = #{tab5Param}",
            " </when> ",
            " GROUP BY technology",
            "</script>"
    })
    List<BrowseParamTotal> getTab5Param(BrowserParam param);

}
