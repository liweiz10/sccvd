package com.zlw.stroke.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zlw.stroke.config.Path;
import com.zlw.stroke.getexpression.GeneExpressionReader2;
import com.zlw.stroke.mapper.DetailMapper;
import com.zlw.stroke.pojo.*;
import com.zlw.stroke.getexpression.GeneExpressionReader;
import com.zlw.stroke.tfactivate.TfActivityReader;
import org.rosuda.REngine.*;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class Detail {
    @Autowired
    private Path path;

    @Autowired(required = false)
    private DetailMapper detail;
    /*
    * 获取质控结果
    * */
    @RequestMapping("getQcInfo")
    @ResponseBody
    public Map getQcInfo(HttpServletRequest request) {
        Map map = new HashMap();
        List<NameValue> data = new ArrayList<>();
        try{
            String table = request.getParameter("id").toLowerCase() + "_metadata";
            String type = request.getParameter("type");
            data = detail.getQcInfo(table,type);
            List<String> names = data.stream()
                    .map(NameValue::getName)
                    .collect(Collectors.toList());
            List<String> values = data.stream()
                    .map(NameValue::getValue)
                    .collect(Collectors.toList());
            String[] name = names.toArray(new String[0]);
            String[] value = values.toArray(new String[0]);
            List<String> groups = data.stream()
                    .map(NameValue::getName)
                    .distinct()
                    .collect(Collectors.toList());
            map.put("name", name);
            map.put("value", value);
            map.put("groups", groups);
            return map;
        }catch (Exception e){
            String[] name = null;
            String[] value = null;
            List<String> groups = new ArrayList<>();
            map.put("name", name);
            map.put("value", value);
            map.put("groups", groups);
            return map;
        }
    }

    @RequestMapping("getCellCount")
    @ResponseBody
    public Map getCellCount(HttpServletRequest request) throws RserveException, REXPMismatchException {
        Map map = new HashMap();
        List<CellCount> list = new ArrayList<>();
        try{
            String table = request.getParameter("type") + "numbysample";
            String id = request.getParameter("id");
            list = detail.getCellCount(table, id);
            map.put("data", list);
            return map;
        }catch (Exception e){
            list.add(new CellCount());
            System.out.println("----------？");
            map.put("data", list);
            return map;
        }
    }
    @RequestMapping("getCellCountCluster")
    @ResponseBody
    public Map getCellCountCluster(HttpServletRequest request) throws RserveException, REXPMismatchException {
        Map map = new HashMap();
        List<CellCount> list = new ArrayList<>();
        try{
            String table = request.getParameter("type") + "numbysample";
            String id = request.getParameter("id");
            list = detail.getCellCount(table, id);
            map.put("data", list);
            return map;
        }catch (Exception e){
            list.add(new CellCount());
            map.put("data", list);
            return map;
        }
    }
    @RequestMapping("toCellchatTab")
    @ResponseBody
    public PageTab toCellchatTab(Integer start, Integer draw, Integer length, HttpServletRequest request) throws InterruptedException {
        PageTab tabs = new PageTab();
        try {
            PageHelper.startPage(start / length + 1, length);
            String table = request.getParameter("id").toLowerCase() + "_cellchat_communication";
            Page<CellChartCommunication> list = detail.toCellchatTab(table,request.getParameter("search[value]"),request.getParameter("order[0][column]"),request.getParameter("order[0][dir]"));
            tabs.setData(list.getResult());
            tabs.setRecordsTotal((int) list.getTotal());
            tabs.setRecordsFiltered((int) list.getTotal());
            tabs.setDraw(draw);
            return tabs;
        } catch (Exception e) {
            Page<CellChartCommunication> list = new Page<>();
            tabs.setData(list.getResult());
            tabs.setRecordsTotal((int) list.getTotal());
            tabs.setRecordsFiltered((int) list.getTotal());
            tabs.setDraw(draw);
            System.out.println("e.getMessage=" + e.getMessage());
            return tabs;
        }
    }
    @RequestMapping("toCiceroTab")
    @ResponseBody
    public PageTab toCiceroTab(Integer start, Integer draw, Integer length, HttpServletRequest request) throws InterruptedException {
        PageTab tabs = new PageTab();
        try {
            PageHelper.startPage(start / length + 1, length);
            String table = request.getParameter("id").toLowerCase() + "_cicero_interactions";
            Page<CiceroTab> list = detail.toCiceroTab(table,request.getParameter("search[value]"),request.getParameter("order[0][column]"),request.getParameter("order[0][dir]"));
            tabs.setData(list.getResult());
            tabs.setRecordsTotal((int) list.getTotal());
            tabs.setRecordsFiltered((int) list.getTotal());
            tabs.setDraw(draw);
            return tabs;
        } catch (Exception e) {
            Page<CiceroTab> list = new Page<>();
            tabs.setData(list.getResult());
            tabs.setRecordsTotal((int) list.getTotal());
            tabs.setRecordsFiltered((int) list.getTotal());
            tabs.setDraw(draw);
            System.out.println("e.getMessage=" + e.getMessage());
            return tabs;
        }
    }
    @RequestMapping("toDiffTfMotifTab")
    @ResponseBody
    public PageTab toDiffTfMotifTab(Integer start, Integer draw, Integer length, HttpServletRequest request) throws InterruptedException {
        PageTab tabs = new PageTab();
        try {
            PageHelper.startPage(start / length + 1, length);
            String table = request.getParameter("id").toLowerCase() + "_differential_tf_motif_enriched_in_clusters";
            Page<DiffTfMotif> list = detail.toDiffTfMotifTab(table,request.getParameter("search[value]"),request.getParameter("order[0][column]"),request.getParameter("order[0][dir]"));
            tabs.setData(list.getResult());
            tabs.setRecordsTotal((int) list.getTotal());
            tabs.setRecordsFiltered((int) list.getTotal());
            tabs.setDraw(draw);
            return tabs;
        } catch (Exception e) {
            Page<DiffTfMotif> list = new Page<>();
            tabs.setData(list.getResult());
            tabs.setRecordsTotal((int) list.getTotal());
            tabs.setRecordsFiltered((int) list.getTotal());
            tabs.setDraw(draw);
            System.out.println("e.getMessage=" + e.getMessage());
            return tabs;
        }
    }

    @RequestMapping("getWgcnaTab")
    @ResponseBody
    public PageTab getWgcnaTab(Integer start, Integer draw, Integer length, HttpServletRequest request) throws InterruptedException {
        PageTab tabs = new PageTab();
        try {
            PageHelper.startPage(start / length + 1, length);
            String table = request.getParameter("id").toLowerCase() + "_wgcna_module_umap";
            Page<WgcnaUmap> list = detail.getWgcnaTab(table,request.getParameter("search[value]"),request.getParameter("order[0][column]"),request.getParameter("order[0][dir]"));
            tabs.setData(list.getResult());
            tabs.setRecordsTotal((int) list.getTotal());
            tabs.setRecordsFiltered((int) list.getTotal());
            tabs.setDraw(draw);
            return tabs;
        } catch (Exception e) {
            Page<WgcnaUmap> list = new Page<>();
            tabs.setData(list.getResult());
            tabs.setRecordsTotal((int) list.getTotal());
            tabs.setRecordsFiltered((int) list.getTotal());
            tabs.setDraw(draw);
            System.out.println("e.getMessage=" + e.getMessage());
            return tabs;
        }
    }

    @RequestMapping("getWgcnaScoreTab")
    @ResponseBody
    public PageTab getWgcnaScoreTab(Integer start, Integer draw, Integer length, HttpServletRequest request) throws InterruptedException {
        PageTab tabs = new PageTab();
        try {
            PageHelper.startPage(start / length + 1, length);
            String table = request.getParameter("id").toLowerCase() + "_wgcna_edges";
            Page<WgcnaScore> list = detail.getWgcnaScoreTab(table,request.getParameter("search[value]"),request.getParameter("order[0][column]"),request.getParameter("order[0][dir]"));
            tabs.setData(list.getResult());
            tabs.setRecordsTotal((int) list.getTotal());
            tabs.setRecordsFiltered((int) list.getTotal());
            tabs.setDraw(draw);
            return tabs;
        } catch (Exception e) {
            Page<WgcnaScore> list = new Page<>();
            tabs.setData(list.getResult());
            tabs.setRecordsTotal((int) list.getTotal());
            tabs.setRecordsFiltered((int) list.getTotal());
            tabs.setDraw(draw);
            System.out.println("e.getMessage=" + e.getMessage());
            return tabs;
        }
    }

    /*
     * 获取富集结果
     * */
    @RequestMapping("getCelltypeBySample")
    @ResponseBody
    public Map getCelltypeBySample(HttpServletRequest request) throws RserveException, REXPMismatchException {
        Map map = new HashMap();
        List<NameValue> list = new ArrayList<>();
        try{
            String type = request.getParameter("type");
            String table = request.getParameter("id").toLowerCase() + "_go_"+type;
            list = detail.getCelltypeBySample(table);
            map.put("data", list);
            return map;
        }catch (Exception e){
            NameValue name = new NameValue();
            list.add(new NameValue());
            map.put("data", list);
            return map;
        }
    }
    @RequestMapping("getCelltypeBySampleAtac")
    @ResponseBody
    public Map getCelltypeBySampleAtac(HttpServletRequest request) throws RserveException, REXPMismatchException {
        Map map = new HashMap();
        List<NameValue> list = new ArrayList<>();
        try{
            String type = request.getParameter("type");
            String table = request.getParameter("id").toLowerCase() + "_enriched";
            list = detail.getCelltypeBySample(table);
            map.put("data", list);
            return map;
        }catch (Exception e){
            NameValue name = new NameValue();
            list.add(new NameValue());
            map.put("data", list);
            return map;
        }
    }
    @RequestMapping("getPathwayGo")
    @ResponseBody
    public Map getPathwayGo(HttpServletRequest request) {
        Map map = new HashMap();
        try{
            String id = request.getParameter("id");
            String param1 = request.getParameter("param1");
            String param2 = request.getParameter("param2");
            String param3 = request.getParameter("param3");
            String type = request.getParameter("type");
            String table = id.toLowerCase() + "_go_" + type;
            PageHelper.clearPage();
            List<Go> data = detail.getPathwayGo(table,param1,param2,param3);
            List<List<String>> result = new ArrayList<>();
            List<String> header = new ArrayList<>();
            header.add("score");
            header.add("amount");
            header.add("product");
            result.add(header);
            for (int i=0;i<data.size();i++){
                List<String> body = new ArrayList<>();
                Double padjust = -Math.log10(Double.parseDouble(data.get(i).getPadj()));
                body.add(padjust.toString());
                body.add(data.get(i).getCount());
                body.add(data.get(i).getDescription());
                result.add(body);
            }
            map.put("data",result);
            return map;
        }catch (Exception e){
            map.put("error","Server exception, please try again later!");
            map.put("data",new ArrayList[0]);
            return map;
        }
    }
    @RequestMapping("getPathwayGoAtac")
    @ResponseBody
    public Map getPathwayGoAtac(HttpServletRequest request) {
        Map map = new HashMap();
        try{
            String id = request.getParameter("id");
            String param1 = request.getParameter("param1");
            String param2 = request.getParameter("param2");
            param2 = "GO_" + param2;
            String param3 = request.getParameter("param3");
            String type = request.getParameter("type");
            String table = id.toLowerCase() + "_enriched";
            PageHelper.clearPage();
            List<Go> data = detail.getPathwayGoAtac(table,param1,param2,param3);
            List<List<String>> result = new ArrayList<>();
            List<String> header = new ArrayList<>();
            header.add("score");
            header.add("amount");
            header.add("product");
            result.add(header);
            for (int i=0;i<data.size();i++){
                List<String> body = new ArrayList<>();
                Double padjust = -Math.log10(Double.parseDouble(data.get(i).getPadj()));
                body.add(padjust.toString());
                body.add(data.get(i).getCount());
                body.add(data.get(i).getDescription());
                result.add(body);
            }
            map.put("data",result);
            return map;
        }catch (Exception e){
            map.put("error","Server exception, please try again later!");
            map.put("data",new ArrayList[0]);
            return map;
        }
    }
    @RequestMapping("getPathwayKEGGAtac")
    @ResponseBody
    public Map getPathwayKEGGAtac(HttpServletRequest request) {
        Map map = new HashMap();
        try{
            String id = request.getParameter("id");
            String param1 = request.getParameter("param1");
            String param2 = request.getParameter("param2");
            String type = request.getParameter("type");
            String table = id.toLowerCase() + "_enriched";
            PageHelper.clearPage();
            List<Kegg> data = detail.getPathwayKEGGAtac(table,param1,param2);
            List<List<String>> result = new ArrayList<>();
            int max = 0;
            int min = 10000;
            Double padjMax = 0.0;
            Double padjMin = 100.0;
            for (int i=0;i<data.size();i++){
                List<String> body = new ArrayList<>();
                Double padj = -Math.log10(Double.parseDouble(data.get(i).getPadj()));
                body.add(data.get(i).getGeneratio());
                body.add(data.get(i).getDescription());
                body.add(padj.toString());
                body.add(data.get(i).getCount());
                result.add(body);
                int countt = Integer.parseInt(data.get(i).getCount());
                if (countt>max){
                    max = countt;
                }
                if (countt<min){
                    min = countt;
                }
                if (padj>padjMax){
                    padjMax = padj;
                }
                if (padj<padjMin){
                    padjMin = padj;
                }
            }
            map.put("data",result);
            map.put("countMin",min);
            map.put("countMax",max);
            map.put("padjMin",padjMin);
            map.put("padjMax",padjMax);
            return map;
        }catch (Exception e){
            map.put("error","Server exception, please try again later!");
            return map;
        }
    }
    @RequestMapping("getPathwayKEGG")
    @ResponseBody
    public Map getPathwayKEGG(HttpServletRequest request) {
        Map map = new HashMap();
        try{
            String id = request.getParameter("id");
            String param1 = request.getParameter("param1");
            String param2 = request.getParameter("param2");
            String type = request.getParameter("type");
            String table = id.toLowerCase() + "_kegg_" + type;
            PageHelper.clearPage();
            List<Kegg> data = detail.getPathwayKEGG(table,param1,param2);
            List<List<String>> result = new ArrayList<>();
            int max = 0;
            int min = 10000;
            Double padjMax = 0.0;
            Double padjMin = 100.0;
            for (int i=0;i<data.size();i++){
                List<String> body = new ArrayList<>();
                Double padj = -Math.log10(Double.parseDouble(data.get(i).getPadj()));
                body.add(data.get(i).getGeneratio());
                body.add(data.get(i).getDescription());
                body.add(padj.toString());
                body.add(data.get(i).getCount());
                result.add(body);
                int countt = Integer.parseInt(data.get(i).getCount());
                if (countt>max){
                    max = countt;
                }
                if (countt<min){
                    min = countt;
                }
                if (padj>padjMax){
                    padjMax = padj;
                }
                if (padj<padjMin){
                    padjMin = padj;
                }
            }
            map.put("data",result);
            map.put("countMin",min);
            map.put("countMax",max);
            map.put("padjMin",padjMin);
            map.put("padjMax",padjMax);
            return map;
        }catch (Exception e){
            map.put("error","Server exception, please try again later!");
            return map;
        }
    }

    @RequestMapping("getSampleInfo")
    @ResponseBody
    public PageTab getSampleInfo(Integer start, Integer draw, Integer length, HttpServletRequest request) throws InterruptedException {
        PageTab tabs = new PageTab();
        String searchValue = request.getParameter("search[value]");
        PageHelper.startPage(start / length + 1, length);
        Page<SampleTable> list = detail.getSampleInfo(request.getParameter("id"),searchValue,request.getParameter("order[0][column]"),request.getParameter("order[0][dir]"));
        try {
            tabs.setData(list.getResult());
            tabs.setRecordsTotal((int) list.getTotal());
            tabs.setRecordsFiltered((int) list.getTotal());
            tabs.setDraw(draw);
            return tabs;
        } catch (Exception e) {
            tabs.setData(list.getResult());
            tabs.setRecordsTotal((int) list.getTotal());
            tabs.setRecordsFiltered((int) list.getTotal());
            tabs.setDraw(draw);
            System.out.println("e.getMessage=" + e.getMessage());
            return tabs;
        }
    }

    @RequestMapping("getSampleTotal")
    @ResponseBody
    public PageTab getSampleTotal(Integer start, Integer draw, Integer length, HttpServletRequest request) throws InterruptedException {
        PageTab tabs = new PageTab();
        String searchValue = request.getParameter("search[value]");
        PageHelper.startPage(start / length + 1, length);
        Page<SampleTotal> list = detail.getSampleTotal(searchValue,request.getParameter("order[0][column]"),request.getParameter("order[0][dir]"));
        try {
            tabs.setData(list.getResult());
            tabs.setRecordsTotal((int) list.getTotal());
            tabs.setRecordsFiltered((int) list.getTotal());
            tabs.setDraw(draw);
            return tabs;
        } catch (Exception e) {
            tabs.setData(list.getResult());
            tabs.setRecordsTotal((int) list.getTotal());
            tabs.setRecordsFiltered((int) list.getTotal());
            tabs.setDraw(draw);
            System.out.println("e.getMessage=" + e.getMessage());
            return tabs;
        }
    }

    @RequestMapping("getSampleDescript")
    @ResponseBody
    public Map getSampleDescript(HttpServletRequest request) throws InterruptedException {
        Map map = new HashMap();
        List<SampleTable> data = new ArrayList<>();
        try {
            String id = request.getParameter("id");
            data = detail.getSampleDescript(id);
            map.put("data", data);
            return map;
        } catch (Exception e) {
            map.put("data", data);
            return map;
        }
    }

    /*
     * 获取基因表达数据
     * 有时候出现报错，看看是不是电脑内存不足导致的，重启一下idea试试。
     * */
    @RequestMapping("getUmapPosition")
    @ResponseBody
    public Map getUmapPosition(HttpServletRequest request) throws InterruptedException {
        Map map = new HashMap();
        List<UmapPosition> data = new ArrayList<>();
        try {
            String id = request.getParameter("id");
            data = detail.getUmapPosition(id.toLowerCase() + "_metadata");
            map.put("data", data);
            return map;
        } catch (Exception e) {
            map.put("data", data);
            return map;
        }
    }

    @RequestMapping("getGeneExpression")
    @ResponseBody
    public Map getGeneExpression(HttpServletRequest request) throws RserveException, REXPMismatchException {
        RConnection c = new RConnection();
        Map map = new HashMap();
        try{
            String workPath = path.getWorkPath();
            String getData = path.getExpersionCodePath();
            String file = workPath+request.getParameter("id")+"_expression_matrix.h5";
            String targetGene = request.getParameter("geneName");  // 目标基因
            if (targetGene==null||targetGene==""){
                targetGene = "error";
            }
            c.assign("file", file);
            c.assign("gene", targetGene);
            c.assign("getData", getData);
            c.eval("source(getData)");
            RList result = c.eval("result<-getGeneExp(file,gene)").asList();
            REXPDouble exp = (REXPDouble) result.elementAt(0);
            double[][] expResult = exp.asDoubleMatrix();
            REXPDouble max = (REXPDouble) result.elementAt(1);
            double mResult = max.asDouble();
            map.put("data",expResult);
            map.put("max",mResult);
            c.close();
            return map;
        }catch (Exception e){
            System.out.println(e.getMessage());
            c.close();
            map.put("data",new double[0][]);
            map.put("max",new double[0]);
            return map;
        }
    }

    @RequestMapping("getGeneExpressionValue")
    @ResponseBody
    public Map getGeneExpressionValue(HttpServletRequest request) throws RserveException, REXPMismatchException, IOException {
        Map map = new HashMap();
        String workPath = path.getWorkPath();
        String geneName = request.getParameter("geneName");  // 目标基因
        String binaryFile = workPath+request.getParameter("id") + "_expression.bin";
        String indexFile =  workPath+request.getParameter("id") + "_gene_index.idx";
        System.out.println(request.getParameter("id"));
        System.out.println(geneName);
        System.out.println(binaryFile);
        System.out.println(indexFile);
        // 因为Sample_026这个样本矩阵文件太大了，Java无法进行转换，所以只能使用另一种方式进行写读了。
        if (!request.getParameter("id").equals("Sample_026")){
            GeneExpressionReader reader = new GeneExpressionReader();
            try {
                reader.initialize(binaryFile, indexFile);
                float[] expression = reader.getGeneExpression(geneName);
                float maxValue = 0;
                for (float value : expression) {
                    if (value > maxValue) maxValue = value;
                }
                map.put("data",expression);
                map.put("max",maxValue);
                return map;
            } catch (Exception e) {
                System.out.println("read error: " + e.getMessage());
                e.printStackTrace();
                map.put("data",new double[0]);
                map.put("max",0);
                return map;
            } finally {
                reader.close();
            }
        }else {
            GeneExpressionReader2 reader = new GeneExpressionReader2();
            reader.initialize(binaryFile, indexFile);
            reader.debugFileStructure();// 先调试文件结构
            try {
                if (reader.containsGene(geneName)) {
                    float[] expression = reader.getGeneExpression(geneName);
                    float maxValue = reader.getGeneMaxValue(geneName);
                    map.put("data", expression);
                    map.put("max", maxValue);
                    return map;
                } else {
                    System.out.println("Gene not found");
                    map.put("data", new double[0]);
                    map.put("max", 0);
                    return map;
                }
            } catch (Exception e) {
                System.out.println("read error: " + e.getMessage());
                e.printStackTrace();
                map.put("data",new double[0]);
                map.put("max",0);
                return map;
            } finally {
                reader.close();
            }
        }
    }
    @RequestMapping("getSignacExpressionValue")
    @ResponseBody
    public Map getSignacExpressionValue(HttpServletRequest request) throws RserveException, REXPMismatchException, IOException {
        Map map = new HashMap();
        String workPath = path.getWorkPath();
        String geneName = request.getParameter("geneName");  // 目标基因
        String binaryFile = workPath+request.getParameter("id") + "_signac_expression.bin";
        String indexFile =  workPath+request.getParameter("id") + "_signac_gene_index.idx";
        // 因为Sample_026这个样本矩阵文件太大了，Java无法进行转换，所以只能使用另一种方式进行写读了。
        if (!request.getParameter("id").equals("Sample_026")){
            GeneExpressionReader reader = new GeneExpressionReader();
            try {
                reader.initialize(binaryFile, indexFile);
                float[] expression = reader.getGeneExpression(geneName);
                float maxValue = 0;
                for (float value : expression) {
                    if (value > maxValue) maxValue = value;
                }
                map.put("data",expression);
                map.put("max",maxValue);
                return map;
            } catch (Exception e) {
                System.out.println("read error: " + e.getMessage());
                e.printStackTrace();
                map.put("data",new double[0]);
                map.put("max",0);
                return map;
            } finally {
                reader.close();
            }
        }else {
            GeneExpressionReader2 reader = new GeneExpressionReader2();
            reader.initialize(binaryFile, indexFile);
            reader.debugFileStructure();// 先调试文件结构
            try {
                if (reader.containsGene(geneName)) {
                    float[] expression = reader.getGeneExpression(geneName);
                    float maxValue = reader.getGeneMaxValue(geneName);
                    map.put("data", expression);
                    map.put("max", maxValue);
                    return map;
                } else {
                    System.out.println("Gene not found");
                    map.put("data", new double[0]);
                    map.put("max", 0);
                    return map;
                }
            } catch (Exception e) {
                System.out.println("read error: " + e.getMessage());
                e.printStackTrace();
                map.put("data",new double[0]);
                map.put("max",0);
                return map;
            } finally {
                reader.close();
            }
        }
    }
    @RequestMapping("getGeneExpressionValueIgnor")
    @ResponseBody
    public Map getGeneExpressionValueIgnor(HttpServletRequest request) throws RserveException, REXPMismatchException, IOException {
        Map map = new HashMap();
        String workPath = path.getWorkPath();
        String geneName = request.getParameter("geneName");  // 目标基因
        String binaryFile = workPath+request.getParameter("id") + "_expression.bin";
        String indexFile =  workPath+request.getParameter("id") + "_gene_index.idx";
        // 因为Stroke_026这个样本矩阵文件太大了，Java无法进行转换，所以只能使用另一种方式进行写读了。
        if (!request.getParameter("id").equals("Stroke_026")){
            GeneExpressionReader reader = new GeneExpressionReader();
            try {
                reader.initialize(binaryFile, indexFile);
                float[] expression = reader.getGeneExpressionIgnor(geneName);
                System.out.println(expression.length+"---------------------");
                float maxValue = 0;
                for (float value : expression) {
                    if (value > maxValue) maxValue = value;
                }
                map.put("data",expression);
                map.put("max",maxValue);
                return map;
            } catch (Exception e) {
                System.out.println("read error: " + e.getMessage());
                e.printStackTrace();
                map.put("data",new double[0]);
                map.put("max",0);
                return map;
            } finally {
                reader.close();
            }
        }else {
            GeneExpressionReader2 reader = new GeneExpressionReader2();
            reader.initialize(binaryFile, indexFile);
            reader.debugFileStructure();// 先调试文件结构
            try {
                if (reader.containsGene(geneName)) {
                    float[] expression = reader.getGeneExpressionIgnor(geneName);
                    float maxValue = reader.getGeneMaxValue(geneName);
                    map.put("data", expression);
                    map.put("max", maxValue);
                    return map;
                } else {
                    System.out.println("Gene not found");
                    map.put("data", new double[0]);
                    map.put("max", 0);
                    return map;
                }
            } catch (Exception e) {
                System.out.println("read error: " + e.getMessage());
                e.printStackTrace();
                map.put("data",new double[0]);
                map.put("max",0);
                return map;
            } finally {
                reader.close();
            }
        }
    }

    @RequestMapping("getGeneDescription")
    @ResponseBody
    public Map getGeneDescription(HttpServletRequest request) {
        Map map = new HashMap();
        GeneDescription result = new GeneDescription();
        try{
            String id = request.getParameter("id");
            String gene = request.getParameter("gene");
            String species = detail.getSpecies(id);
            if ("Homo sapiens".equals(species)) {
                result = detail.getGeneDescription("homo_sapiens_gene", gene);
            } else if ("Mus musculus".equals(species)){
                result = detail.getGeneDescription("mus_musculus_gene", gene);
            } else if ("Callithrix jacchus".equals(species)){
                result = detail.getGeneDescription("callithrix_jacchus_gene", gene);
            } else {
                result = detail.getGeneDescription("rattus_norvegicus_gene", gene);
            }
            map.put("data", result);
            return map;
        }catch (Exception e){
            map.put("data", result);
            return map;
        }
    }

    @RequestMapping("getGeneName")
    @ResponseBody
    public Map getGeneName(HttpServletRequest request) throws RserveException, REXPMismatchException {
        Map map = new HashMap();
        List<NameValue> geneList = new ArrayList<>();
        try{
            RConnection c = new RConnection();
            String workPath = path.getWorkPath();
            String getData = path.getExpersionCodePath();
            String file = workPath+request.getParameter("id")+"_expression_matrix.h5";
            c.assign("file", file);
            c.assign("getData", getData);
            c.eval("source(getData)");
            String[] strs = c.eval("result<-getGeneName(file)").asStrings();
            for (String geneName : strs) {
                geneList.add(new NameValue(geneName, ""));
            }
            map.put("data", geneList);
            c.close();
            return map;
        }catch (Exception e){
            geneList.add(new NameValue());
            map.put("data", geneList);
            return map;
        }

    }

    @RequestMapping("getGeneNameBinary")
    @ResponseBody
    public Map getGeneNameBinary(HttpServletRequest request) throws RserveException, REXPMismatchException, IOException {
        Map map = new HashMap();
        String workPath = path.getWorkPath();
        String sampleid = request.getParameter("id");
        String binaryFile = workPath+ sampleid + "_expression.bin";
        String indexFile =  workPath+ sampleid + "_gene_index.idx";
        List<NameValue> geneList = new ArrayList<>();
        // 因为Stroke_026这个样本矩阵文件太大了，Java无法进行转换，所以只能使用另一种方式进行写读了。
        if (!request.getParameter("id").equals("Stroke_026")){
            GeneExpressionReader reader = new GeneExpressionReader();
            try{
                reader.initialize(binaryFile, indexFile);
                String[] strs = reader.getAllGeneNames();
                for (String geneName : strs) {
                    geneList.add(new NameValue(geneName, ""));
                }
                map.put("data", geneList);
                return map;
            }catch (Exception e){
                geneList.add(new NameValue());
                map.put("data", geneList);
                return map;
            } finally {
                reader.close();
            }
        }else {
            GeneExpressionReader2 reader = new GeneExpressionReader2();
            try{
                reader.initialize(binaryFile, indexFile);
                String[] strs = reader.getAllGeneNames();
                for (String geneName : strs) {
                    geneList.add(new NameValue(geneName, ""));
                }
                map.put("data", geneList);
                return map;
            }catch (Exception e){
                geneList.add(new NameValue());
                map.put("data", geneList);
                return map;
            } finally {
                reader.close();
            }
        }
    }
    @RequestMapping("getSignacNameBinary")
    @ResponseBody
    public Map getSignacNameBinary(HttpServletRequest request) throws RserveException, REXPMismatchException, IOException {
        Map map = new HashMap();
        String workPath = path.getWorkPath();
        String binaryFile = workPath+request.getParameter("id") + "_signac_expression.bin";
        String indexFile =  workPath+request.getParameter("id") + "_signac_gene_index.idx";
        List<NameValue> geneList = new ArrayList<>();
        // 因为Stroke_026这个样本矩阵文件太大了，Java无法进行转换，所以只能使用另一种方式进行写读了。
        if (!request.getParameter("id").equals("Stroke_026")){
            GeneExpressionReader reader = new GeneExpressionReader();
            try{
                reader.initialize(binaryFile, indexFile);
                String[] strs = reader.getAllGeneNames();
                for (String geneName : strs) {
                    geneList.add(new NameValue(geneName, ""));
                }
                map.put("data", geneList);
                return map;
            }catch (Exception e){
                geneList.add(new NameValue());
                map.put("data", geneList);
                return map;
            } finally {
                reader.close();
            }
        }else {
            GeneExpressionReader2 reader = new GeneExpressionReader2();
            try{
                reader.initialize(binaryFile, indexFile);
                String[] strs = reader.getAllGeneNames();
                for (String geneName : strs) {
                    geneList.add(new NameValue(geneName, ""));
                }
                map.put("data", geneList);
                return map;
            }catch (Exception e){
                geneList.add(new NameValue());
                map.put("data", geneList);
                return map;
            } finally {
                reader.close();
            }
        }
    }

    /*
     * 获取转录因子表达数据
     * */
    @RequestMapping("getTFName")
    @ResponseBody
    public Map getTFName(HttpServletRequest request) throws RserveException, REXPMismatchException {
        Map map = new HashMap();
        List<NameValue> geneList = new ArrayList<>();
        try{
            RConnection c = new RConnection();
            String workPath = path.getWorkPath();
            String getData = path.getExpersionCodePath();
            String file = workPath+request.getParameter("id")+"_tf_auc.h5";
            c.assign("file", file);
            c.assign("getData", getData);
            c.eval("source(getData)");
            String[] strs = c.eval("result<-getTFName(file)").asStrings();
            for (String geneName : strs) {
                geneList.add(new NameValue(geneName, ""));
            }
            map.put("data", geneList);
            c.close();
            return map;
        }catch (Exception e){
            geneList.add(new NameValue());
            map.put("data", geneList);
            return map;
        }
    }
    @RequestMapping("getTFExpression")
    @ResponseBody
    public Map getTFExpression(HttpServletRequest request) throws RserveException, REXPMismatchException {
        RConnection c = new RConnection();
        Map map = new HashMap();
        try{
            String workPath = path.getWorkPath();
            String getData = path.getExpersionCodePath();
            String file = workPath+request.getParameter("id")+"_tf_auc.h5";
            String targetTF = request.getParameter("tfName");  // 目标基因
            if (targetTF==null||targetTF==""){
                targetTF = "error";
            }
            c.assign("file", file);
            c.assign("tf_name", targetTF);
            c.assign("getData", getData);
            c.eval("source(getData)");
            RList result = c.eval("result<-extract_tf_from_h5(file,tf_name)").asList();
            REXPDouble exp = (REXPDouble) result.elementAt(0);
            double[][] expResult = exp.asDoubleMatrix();
            REXPDouble max = (REXPDouble) result.elementAt(1);
            double mResult = max.asDouble();
            map.put("data",expResult);
            map.put("max",mResult);
            c.close();
            return map;
        }catch (Exception e){
            System.out.println(e.getMessage());
            c.close();
            map.put("data",new double[0][]);
            map.put("max",new double[0]);
            return map;
        }
    }
    @RequestMapping("getTFNameBinary")
    @ResponseBody
    public Map getTFNameBinary(HttpServletRequest request) throws RserveException, REXPMismatchException {
        Map map = new HashMap();
        List<NameValue> geneList = new ArrayList<>();
        String workPath = path.getWorkPath();
        String binaryFile = workPath+request.getParameter("id")+"_tf_activity.bin";
        String indexFile = workPath+request.getParameter("id")+"_tf_index.idx";
        TfActivityReader reader = new TfActivityReader();
        try{
            reader.initialize(binaryFile, indexFile);
            String[] strs = reader.getAllTfNames();
            for (String geneName : strs) {
                geneList.add(new NameValue(geneName, ""));
            }
            map.put("data", geneList);
            return map;
        }catch (Exception e){
            geneList.add(new NameValue());
            map.put("data", geneList);
            return map;
        } finally {
            reader.close();
        }
    }

    @RequestMapping("getTFExpressionValue")
    @ResponseBody
    public Map getTFExpressionValue(HttpServletRequest request) throws RserveException, REXPMismatchException {
        Map map = new HashMap();
        String workPath = path.getWorkPath();
        String binaryFile = workPath+request.getParameter("id")+"_tf_activity.bin";
        String indexFile = workPath+request.getParameter("id")+"_tf_index.idx";
        String tfName = request.getParameter("tfName");  // 目标基因
        TfActivityReader reader = new TfActivityReader();
        try {
            reader.initialize(binaryFile, indexFile);
            TfActivityReader.TfActivityResult result = reader.getTfActivityWithStats(tfName);
            float[] activity = result.getActivity();
            map.put("data",activity);
            map.put("max",result.getMaxValue());
            return map;
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
            e.printStackTrace();
            map.put("data",new double[0][]);
            map.put("max",new double[0]);
            return map;
        } finally {
            reader.close();
        }
    }

    @Value("${app.stroke-names}")
    private List<String> strokeList;
    @RequestMapping("getTfUmapPosition")
    @ResponseBody
    public Map getTfUmapPosition(HttpServletRequest request) throws InterruptedException {
        Map map = new HashMap();
        List<UmapPosition> data = new ArrayList<>();
        String suffix = "_metadata";
        try {
            String id = request.getParameter("id");
            for (int i=0; i<strokeList.size(); i++){
                if (id.equals(strokeList.get(i))){
                    suffix = "_metadata_downsample";
                }
            }
            data = detail.getUmapPosition(id.toLowerCase() + suffix);
            map.put("data", data);
            return map;
        } catch (Exception e) {
            map.put("data", data);
            return map;
        }
    }


    @RequestMapping("getDetailView")
    @ResponseBody
    public ModelAndView getDetailView(HttpServletRequest request) throws InterruptedException {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("messages/detail");
        try {
            String id = request.getParameter("id");
            mv.addObject("id", id);
            return mv;
        }catch (Exception e){
            mv.addObject("message","Server exception!");
            return mv;
        }
    }

    @RequestMapping("getDetailViewGL")
    @ResponseBody
    public ModelAndView getDetailViewGL(HttpServletRequest request) throws InterruptedException {
        ModelAndView mv = new ModelAndView();
        try {
            String id = request.getParameter("id");
            String technology = detail.getTechnology(id);
            if (technology.equals("RNA-seq")){
                mv.setViewName("messages/detailRna");
            }else if (technology.equals("scATAC-seq")||technology.equals("snATAC-seq")||technology.equals("sci-ATAC-seq")||technology.equals("sciMAP-ATAC")) {
                mv.setViewName("messages/detailDeckGLscAtac");
            }else {
                mv.setViewName("messages/detailDeckGL");
            }
            mv.addObject("id", id);
            return mv;
        }catch (Exception e){
            mv.addObject("message","Server exception!");
            return mv;
        }
    }

    @RequestMapping("getDegTable")
    @ResponseBody
    public PageTab getDegTable(Integer start, Integer draw, Integer length, HttpServletRequest request) throws InterruptedException {
        PageTab tabs = new PageTab();
        try {
            PageHelper.startPage(start / length + 1, length);
            String table = request.getParameter("id").toLowerCase() + "_degs_celltype";
            Page<DegTable> list = detail.getDegTable(table,request.getParameter("search[value]"),request.getParameter("order[0][column]"),request.getParameter("order[0][dir]"));
            tabs.setData(list.getResult());
            tabs.setRecordsTotal((int) list.getTotal());
            tabs.setRecordsFiltered((int) list.getTotal());
            tabs.setDraw(draw);
            return tabs;
        } catch (Exception e) {
            Page<DegTable> list = new Page<>();
            tabs.setData(list.getResult());
            tabs.setRecordsTotal((int) list.getTotal());
            tabs.setRecordsFiltered((int) list.getTotal());
            tabs.setDraw(draw);
            System.out.println("e.getMessage=" + e.getMessage());
            return tabs;
        }
    }
    @RequestMapping("getPeakDegTable")
    @ResponseBody
    public PageTab getPeakDegTable(Integer start, Integer draw, Integer length, HttpServletRequest request) throws InterruptedException {
        PageTab tabs = new PageTab();
        try {
            PageHelper.startPage(start / length + 1, length);
            String table = request.getParameter("id").toLowerCase() + "_degs_celltype";
            Page<PeakDegTable> list = detail.getPeakDegTable(table,request.getParameter("search[value]"),request.getParameter("order[0][column]"),request.getParameter("order[0][dir]"));
            tabs.setData(list.getResult());
            tabs.setRecordsTotal((int) list.getTotal());
            tabs.setRecordsFiltered((int) list.getTotal());
            tabs.setDraw(draw);
            return tabs;
        } catch (Exception e) {
            Page<PeakDegTable> list = new Page<>();
            tabs.setData(list.getResult());
            tabs.setRecordsTotal((int) list.getTotal());
            tabs.setRecordsFiltered((int) list.getTotal());
            tabs.setDraw(draw);
            System.out.println("e.getMessage=" + e.getMessage());
            return tabs;
        }
    }
    @RequestMapping("getDegTableByGene")
    @ResponseBody
    public PageTab getDegTableByGene(Integer start, Integer draw, Integer length, HttpServletRequest request) throws InterruptedException {
        PageTab tabs = new PageTab();
        try {
            PageHelper.startPage(start / length + 1, length);
            String table = request.getParameter("id").toLowerCase() + "_degs_celltype";
            Page<DegTable> list = detail.getDegTableByGene(table,request.getParameter("gene"),request.getParameter("search[value]"),request.getParameter("order[0][column]"),request.getParameter("order[0][dir]"));
            tabs.setData(list.getResult());
            tabs.setRecordsTotal((int) list.getTotal());
            tabs.setRecordsFiltered((int) list.getTotal());
            tabs.setDraw(draw);
            return tabs;
        } catch (Exception e) {
            Page<DegTable> list = new Page<>();
            tabs.setData(list.getResult());
            tabs.setRecordsTotal((int) list.getTotal());
            tabs.setRecordsFiltered((int) list.getTotal());
            tabs.setDraw(draw);
            System.out.println("e.getMessage=" + e.getMessage());
            return tabs;
        }
    }

    @RequestMapping("getDegAtacTableByGene")
    @ResponseBody
    public PageTab getDegAtacTableByGene(Integer start, Integer draw, Integer length, HttpServletRequest request) throws InterruptedException {
        PageTab tabs = new PageTab();
        try {
            PageHelper.startPage(start / length + 1, length);
            String table = request.getParameter("id").toLowerCase() + "_degs_celltype";
            Page<PeakDegTable> list = detail.getPeakDegTableByGene(table,request.getParameter("gene"),request.getParameter("search[value]"),request.getParameter("order[0][column]"),request.getParameter("order[0][dir]"));
            tabs.setData(list.getResult());
            tabs.setRecordsTotal((int) list.getTotal());
            tabs.setRecordsFiltered((int) list.getTotal());
            tabs.setDraw(draw);
            return tabs;
        } catch (Exception e) {
            Page<DegTable> list = new Page<>();
            tabs.setData(list.getResult());
            tabs.setRecordsTotal((int) list.getTotal());
            tabs.setRecordsFiltered((int) list.getTotal());
            tabs.setDraw(draw);
            System.out.println("e.getMessage=" + e.getMessage());
            return tabs;
        }
    }
    @RequestMapping("getDegTableFilter")
    @ResponseBody
    public PageTab getDegTableFilter(Integer start, Integer draw, Integer length, HttpServletRequest request) throws InterruptedException {
        PageTab tabs = new PageTab();
        try {
            PageHelper.startPage(start / length + 1, length);
            String table = request.getParameter("id").toLowerCase() + "_degs_celltype";
            String celltype = request.getParameter("celltype");
            Page<DegTable> list = detail.getDegTableFilter(table,celltype,request.getParameter("search[value]"),request.getParameter("order[0][column]"),request.getParameter("order[0][dir]"));
            tabs.setData(list.getResult());
            tabs.setRecordsTotal((int) list.getTotal());
            tabs.setRecordsFiltered((int) list.getTotal());
            tabs.setDraw(draw);
            return tabs;
        } catch (Exception e) {
            Page<DegTable> list = new Page<>();
            tabs.setData(list.getResult());
            tabs.setRecordsTotal((int) list.getTotal());
            tabs.setRecordsFiltered((int) list.getTotal());
            tabs.setDraw(draw);
            System.out.println("e.getMessage=" + e.getMessage());
            return tabs;
        }
    }

    @RequestMapping("getDegClusterTable")
    @ResponseBody
    public PageTab getDegClusterTable(Integer start, Integer draw, Integer length, HttpServletRequest request) throws InterruptedException {
        PageTab tabs = new PageTab();
        try {
            PageHelper.startPage(start / length + 1, length);
            String table = request.getParameter("id").toLowerCase() + "_degs_cluster";
            Page<DegTable> list = detail.getDegTable(table,request.getParameter("search[value]"),request.getParameter("order[0][column]"),request.getParameter("order[0][dir]"));
            tabs.setData(list.getResult());
            tabs.setRecordsTotal((int) list.getTotal());
            tabs.setRecordsFiltered((int) list.getTotal());
            tabs.setDraw(draw);
            return tabs;
        } catch (Exception e) {
            Page<DegTable> list = new Page<>();
            tabs.setData(list.getResult());
            tabs.setRecordsTotal((int) list.getTotal());
            tabs.setRecordsFiltered((int) list.getTotal());
            tabs.setDraw(draw);
            System.out.println("e.getMessage=" + e.getMessage());
            return tabs;
        }
    }

    /* single gene analysis*/
    @RequestMapping("getGeneByAll")
    @ResponseBody
    public Map getGeneByAll(HttpServletRequest request) throws IOException {
        Map map = new HashMap();
        List<Name> geneList = new ArrayList<>();
        try{
            geneList = detail.getGeneByAll();
            map.put("data", geneList);
            return map;
        }catch (Exception e){
            geneList.add(new Name());
            map.put("data", geneList);
            return map;
        }
    }
    @RequestMapping("getGeneCardByStroke")
    @ResponseBody
    public Map getGeneCardByStroke(HttpServletRequest request) throws IOException {
        Map map = new HashMap();
        List<GeneCardByStroke> data = new ArrayList<>();
        try{
            String gene = request.getParameter("gene");
            data = detail.getGeneCardByStroke(gene);
            map.put("data", data);
            return map;
        }catch (Exception e){
            data.add(new GeneCardByStroke());
            map.put("data", data);
            return map;
        }
    }

    @RequestMapping("getGeneForDatas")
    @ResponseBody
    public Map getGeneForDatas(HttpServletRequest request) throws IOException {
        Map map = new HashMap();
        List<SampleTable> data = new ArrayList<>();
        try{
            String gene = request.getParameter("gene");
            GeneStatsPerDataset geneForDatas = detail.getGeneForDatas(gene);
            String[] datasets = geneForDatas.getDatasets().split(";");
            String[] expr_cell_ratios = geneForDatas.getExpr_cell_ratios().split(";");
            String[] mean_expressions = geneForDatas.getMean_expressions().split(";");
            String[] percentiles = geneForDatas.getPercentiles().split(";");
            data = detail.getSampleByDataset(datasets);
            Map<String, String[]> statsMap = new HashMap<>();
            for (int i = 0; i < datasets.length; i++) {
                statsMap.put(datasets[i], new String[]{expr_cell_ratios[i], mean_expressions[i], percentiles[i]});
            }
            for (SampleTable item : data) {
                String id = item.getSampleId();
                String[] stats = statsMap.get(id);
                if (stats != null) {
                    item.setExpr_cell_ratios(stats[0]);
                    item.setMean_expressions(stats[1]);
                    item.setPercentiles(stats[2]);
                }
            }
            map.put("data", data);
            return map;
        }catch (Exception e){
            map.put("data", data);
            return map;
        }
    }

    @RequestMapping("sampleCelltypeCount")
    @ResponseBody
    public Map sampleCelltypeCount(HttpServletRequest request) throws IOException {
        Map map = new HashMap();
        List<CelltypeNumBySample> list = new ArrayList<>();
        try{
            list = detail.sampleCelltypeCount();
            map.put("data", list);
            return map;
        }catch (Exception e){
            list.add(new CelltypeNumBySample());
            map.put("data", list);
            return map;
        }
    }

    @RequestMapping("getDotMarker")
    @ResponseBody
    public Map getDotMarker(HttpServletRequest request) throws InterruptedException {
        String sampleid = request.getParameter("id");
        Map map = new HashMap();
        List<Marker_expression_all_samples> dataList = new ArrayList<>();
        try {
            dataList = detail.getDotMarker(sampleid);
            String[][] result = dataList.stream()
                    .map(data -> new String[]{
                            data.getCelltype(),
                            data.getGenename(),
                            data.getTotal_cells(),
                            data.getExpressed_cells(),
                            data.getGeneexp_avg(),
                            data.getExpincell()
                    })
                    .toArray(String[][]::new);
            // 使用LinkedHashSet保持插入顺序
            Set<String> geneSet = new LinkedHashSet<>();
            // 使用LinkedHashMap保持插入顺序，并记录每个celltype第一次出现的n_cells
            Map<String, String> celltypeMap = new LinkedHashMap<>();

            double maxAvgExp = Double.MIN_VALUE;
            double maxPctExp = Double.MIN_VALUE;
            // 按原始顺序遍历，保证第一次出现的顺序被保留
            for (Marker_expression_all_samples data : dataList) {
                // gene去重：只有第一次出现的gene会被加入
                geneSet.add(data.getGenename());
                // celltype去重：只有第一次出现的celltype会被记录
                celltypeMap.putIfAbsent(data.getCelltype(), data.getTotal_cells());
                maxAvgExp = Math.max(maxAvgExp, Double.parseDouble(data.getGeneexp_avg()));
                maxPctExp = Math.max(maxPctExp, Double.parseDouble(data.getExpincell()));
            }

            map.put("data", result);
            map.put("maxAvgExp", maxAvgExp);
            map.put("maxPctExp", maxPctExp);
            return map;
        } catch (Exception e) {
            String[][] result = new String[0][0];
            map.put("data", result);
            return map;
        }
    }

    @RequestMapping("getRnaDegGroup")
    @ResponseBody
    public Map getRnaDegGroup(HttpServletRequest request) throws InterruptedException {
        String sampleid = request.getParameter("id");
        Map map = new HashMap();
        List<DegTableGroup> dataList = new ArrayList<>();
        try {
            dataList = detail.getRnaDegGroup(sampleid);
            map.put("data", dataList);
            return map;
        } catch (Exception e) {
            DegTableGroup data = new DegTableGroup();
            dataList.add(data);
            map.put("data", dataList);
            return map;
        }
    }

    @RequestMapping("getRnaDegVolcano")
    @ResponseBody
    public Map getRnaDegVolcano(HttpServletRequest request) throws InterruptedException {
        String sampleid = request.getParameter("id");
         Map map = new HashMap();
        List<RnaDegVolcano> dataList1 = new ArrayList<>();
        List<RnaDegVolcano> dataList2 = new ArrayList<>();
        try {
            sampleid = sampleid.toLowerCase();
            dataList1 = detail.getRnaDegVolcano_sig(sampleid);
            dataList2 = detail.getRnaDegVolcano_nosig(sampleid);
            dataList1.addAll(dataList2);
            map.put("data", dataList1);
            return map;
        } catch (Exception e) {
            RnaDegVolcano data = new RnaDegVolcano();
            dataList1.add(data);
            map.put("data", dataList1);
            return map;
        }
    }

    @RequestMapping("getRnaDegTab")
    @ResponseBody
    public PageTab getRnaDegTab(Integer start, Integer draw, Integer length, HttpServletRequest request) throws InterruptedException {
        PageTab tabs = new PageTab();
        String searchValue = request.getParameter("search[value]");
        PageHelper.startPage(start / length + 1, length);
        String sampleid = request.getParameter("id");
        sampleid = sampleid.toLowerCase();
        Page<RnaDegTab> list = detail.getRnaDegTab(sampleid,searchValue,request.getParameter("order[0][column]"),request.getParameter("order[0][dir]"));
        try {
            tabs.setData(list.getResult());
            tabs.setRecordsTotal((int) list.getTotal());
            tabs.setRecordsFiltered((int) list.getTotal());
            tabs.setDraw(draw);
            return tabs;
        } catch (Exception e) {
            tabs.setData(list.getResult());
            tabs.setRecordsTotal((int) list.getTotal());
            tabs.setRecordsFiltered((int) list.getTotal());
            tabs.setDraw(draw);
            System.out.println("e.getMessage=" + e.getMessage());
            return tabs;
        }
    }
    @RequestMapping("getRnaGroup")
    @ResponseBody
    public Map getRnaGroup(HttpServletRequest request) throws InterruptedException {
        String sampleid = request.getParameter("id");
        String gene = request.getParameter("gene");
        Map map = new HashMap();
        List<String> tablename = new ArrayList<>();
        List<RnaExp> result = new ArrayList<>();
        try {
            tablename = detail.getRnaGroup(sampleid);
            for (String table : tablename) {
                String gse = table.replace("_long", "");
                List<RnaExp> data = detail.getGeneExpr(table, gene);
                for (RnaExp rnaExp : data) {
                    rnaExp.setGse(gse.toUpperCase());
                }
                result.addAll(data);
            }
            map.put("data", result);
            return map;
        } catch (Exception e) {
            map.put("data", result);
            return map;
        }
    }
    @RequestMapping("getRnaSampleTable")
    @ResponseBody
    public PageTab getRnaSampleTable(Integer start, Integer draw, Integer length, HttpServletRequest request) throws InterruptedException {
        PageTab tabs = new PageTab();
        String searchValue = request.getParameter("search[value]");
        PageHelper.startPage(start / length + 1, length);
        Page<SampleTable> list = detail.getRnaSampleTable(request.getParameter("id"),searchValue,request.getParameter("order[0][column]"),request.getParameter("order[0][dir]"));
        try {
            tabs.setData(list.getResult());
            tabs.setRecordsTotal((int) list.getTotal());
            tabs.setRecordsFiltered((int) list.getTotal());
            tabs.setDraw(draw);
            return tabs;
        } catch (Exception e) {
            tabs.setData(list.getResult());
            tabs.setRecordsTotal((int) list.getTotal());
            tabs.setRecordsFiltered((int) list.getTotal());
            tabs.setDraw(draw);
            System.out.println("e.getMessage=" + e.getMessage());
            return tabs;
        }
    }
    @RequestMapping("getRnaSampleGroupTable")
    @ResponseBody
    public PageTab getRnaSampleGroupTable(Integer start, Integer draw, Integer length, HttpServletRequest request) throws InterruptedException {
        String sampleid = request.getParameter("id");
        String specie = detail.getSpecies(sampleid);
        PageTab tabs = new PageTab();
        String searchValue = request.getParameter("search[value]");
        PageHelper.startPage(start / length + 1, length);
        Page<RnaSampleGroup> list = detail.getRnaSampleGroupTable(specie,searchValue,request.getParameter("order[0][column]"),request.getParameter("order[0][dir]"));
        try {
            tabs.setData(list.getResult());
            tabs.setRecordsTotal((int) list.getTotal());
            tabs.setRecordsFiltered((int) list.getTotal());
            tabs.setDraw(draw);
            return tabs;
        } catch (Exception e) {
            tabs.setData(list.getResult());
            tabs.setRecordsTotal((int) list.getTotal());
            tabs.setRecordsFiltered((int) list.getTotal());
            tabs.setDraw(draw);
            System.out.println("e.getMessage=" + e.getMessage());
            return tabs;
        }
    }
    @RequestMapping("getRnaBubbleData")
    @ResponseBody
    public Map getRnaBubbleData(HttpServletRequest request) throws InterruptedException {
        String sampleid = request.getParameter("id");
        String gene = request.getParameter("gene");
        Map map = new HashMap();
        List<RnaBubble> result = new ArrayList<>();
        try {
            String specie = detail.getSpecies(sampleid);
            String table = specie.replace(" ", "_").toLowerCase()+"_deg_long";
            result = detail.getRnaBubbleData(table,gene,specie);
            map.put("data", result);
            return map;
        } catch (Exception e) {
            RnaBubble rnadata = new RnaBubble();
            result.add(rnadata);
            map.put("data", result);
            return map;
        }
    }

    @RequestMapping("canvasheat")
    @ResponseBody
    public ModelAndView canvasheat(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        String sample = request.getParameter("sample");
        System.out.println("sample");
        System.out.println(sample);
        System.out.println("sample");
        mv.setViewName("messages/canvasheat");
        mv.addObject("sample",sample);
        return mv;
    }
}
