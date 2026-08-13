package com.zlw.stroke.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zlw.stroke.config.Path;
import com.zlw.stroke.mapper.SearchMapper;
import com.zlw.stroke.pojo.*;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class Analysis {
    @Autowired
    private Path path;

    @Autowired(required = false)
    private SearchMapper search;

    @RequestMapping("getSampleAna")
    @ResponseBody
    public PageTab getSampleAna(Integer start, Integer draw, Integer length, HttpServletRequest request) throws InterruptedException {
        PageTab tabs = new PageTab();
        String searchValue = request.getParameter("search[value]");
        PageHelper.startPage(start / length + 1, length);
        try {
            Page<SampleTable> list = search.getSampleAna(searchValue,request.getParameter("order[0][column]"),request.getParameter("order[0][dir]"));
            tabs.setData(list.getResult());
            tabs.setRecordsTotal((int) list.getTotal());
            tabs.setRecordsFiltered((int) list.getTotal());
            tabs.setDraw(draw);
            return tabs;
        } catch (Exception e) {
            Page<SampleTable> list = new Page<>();
            tabs.setData(list.getResult());
            tabs.setRecordsTotal((int) list.getTotal());
            tabs.setRecordsFiltered((int) list.getTotal());
            tabs.setDraw(draw);
            System.out.println("e.getMessage=" + e.getMessage());
            return tabs;
        }
    }

    @RequestMapping("geneEnrichmentView")
    @ResponseBody
    public ModelAndView geneEnrichmentView(HttpServletRequest request) throws InterruptedException {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("messages/geneEnrichmentView");
        try {
            String inputgenelist = request.getParameter("genes");
            String threshold = request.getParameter("select-threshold");
            String adjust = request.getParameter("adjust");
            if (adjust == null) {
                adjust = "0";
            }
            if ("on".equalsIgnoreCase(adjust)) {
                adjust = "1";
            }
            mv.addObject("genes", inputgenelist);
            mv.addObject("threshold", threshold);
            mv.addObject("adjust", adjust);
            return mv;
        }catch (Exception e){
            mv.addObject("message","Server exception!");
            return mv;
        }
    }

    @RequestMapping("geneEnrichmentRun")
    @ResponseBody
    public Map geneEnrichmentRun(HttpServletRequest request) throws InterruptedException, REngineException, REXPMismatchException {
        Map map = new HashMap();
        String inputgenelist = request.getParameter("genes");
        String threshold = request.getParameter("threshold");
        String adjust = request.getParameter("adjust");
        inputgenelist = inputgenelist.replaceAll(", ", ",");
        try{
            RConnection c = new RConnection();
            String workPath = path.getWorkPath();
            String getExpersion = path.getExpersionCodePath();
            String bgfile = workPath+"gene_enrichment_bg.RData";
            c.assign("x", inputgenelist.split(","));
            c.assign("adjust", adjust);
            c.assign("threshold", threshold);
            c.assign("bgfile", bgfile);
            c.assign("getExpersion", getExpersion);
            c.eval("source(getExpersion)");
            RList result = c.eval("result<-enrichment(x,adjust,threshold,bgfile)").asList();
            REXPString col0 = (REXPString) result.get(0);
            REXPString col1 = (REXPString) result.get(1);
            REXPString col2 = (REXPString) result.get(2);
            REXPString col3 = (REXPString) result.get(3);
            REXPString col4 = (REXPString) result.get(4);
            REXPString col5 = (REXPString) result.get(5);
            REXPString col6 = (REXPString) result.get(6);
            REXPString col7 = (REXPString) result.get(7);
            REXPString col8 = (REXPString) result.get(8);
            REXPString col9 = (REXPString) result.get(9);
            REXPString col10 = (REXPString) result.get(10);
            REXPString col11 = (REXPString) result.get(11);
            REXPString col12 = (REXPString) result.get(12);
//            REXPString col13 = (REXPString) result.get(13);
//            REXPString col14 = (REXPString) result.get(14);
//            REXPString col15 = (REXPString) result.get(15);
            REXPString col16 = (REXPString) result.get(16);
            REXPString col17 = (REXPString) result.get(17);
            REXPString col18 = (REXPString) result.get(18);
            REXPString col19 = (REXPString) result.get(19);
            c.close();
            List<String> c0 = Arrays.asList(col0.asStrings());
            List<String> c1 = Arrays.asList(col1.asStrings());
            List<String> c2 = Arrays.asList(col2.asStrings());
            List<String> c3 = Arrays.asList(col3.asStrings());
            List<String> c4 = Arrays.asList(col4.asStrings());
            List<String> c5 = Arrays.asList(col5.asStrings());
            List<String> c6 = Arrays.asList(col6.asStrings());
            List<String> c7 = Arrays.asList(col7.asStrings());
            List<String> c8 = Arrays.asList(col8.asStrings());
            List<String> c9 = Arrays.asList(col9.asStrings());
            List<String> c10 = Arrays.asList(col10.asStrings());
            List<String> c11 = Arrays.asList(col11.asStrings());
            List<String> c12 = Arrays.asList(col12.asStrings());
//            List<String> c13 = Arrays.asList(col13.asStrings());
//            List<String> c14 = Arrays.asList(col14.asStrings());
//            List<String> c15 = Arrays.asList(col15.asStrings());
            List<String> c16 = Arrays.asList(col16.asStrings());
            List<String> c17 = Arrays.asList(col17.asStrings());
            List<String> c18 = Arrays.asList(col18.asStrings());
            List<String> c19 = Arrays.asList(col19.asStrings());
            List<EnrichmentResult> results = new ArrayList<>();
            EnrichmentResult obj = null;
            for (int i = 0; i < c0.size(); i++){
                obj = new EnrichmentResult();
                obj.setSampleid(c0.get(i));
                obj.setCluster(c1.get(i));
                obj.setGene_count(c2.get(i));
                obj.setGenes(c3.get(i));
                obj.setPvalue(c4.get(i));
                obj.setPadj(c5.get(i));
                obj.setMatchCount(c6.get(i));
                obj.setGse_id(c7.get(i));
                obj.setSpecies(c8.get(i));
                obj.setTissue(c9.get(i));
                obj.setAge(c10.get(i));
                obj.setDisease(c11.get(i));
                obj.setSample(c12.get(i));
//                obj.setGenomic(c13.get(i));
//                obj.setTechnology(c14.get(i));
//                obj.setStrain(c15.get(i));
                obj.setPmid(c16.get(i));
                obj.setArticle(c17.get(i));
                obj.setJournal(c18.get(i));
                obj.setYear(c19.get(i));
                results.add(obj);
            }
            map.put("enrichmentResult", results);
            return map;
        }catch (Exception e){
            map.put("data",new String[0]);
            List<EnrichmentResult> results = new ArrayList<>();
            map.put("enrichmentResult", results);
            return map;
        }
    }


}