package com.zlw.stroke.controller;

import com.zlw.stroke.mapper.SearchMapper;
import com.zlw.stroke.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class NodeGraph {
    @Autowired(required = false)
    private SearchMapper search;


    /*
    * 以力导向图的方式绘制细胞类型umap图
    * */
    @RequestMapping("getNode")
    @ResponseBody
    public Map getNode(HttpServletRequest request){
        Map map = new HashMap();
        String group = request.getParameter("group");
        List<Nodes> node = search.getNode(request.getParameter("id").toLowerCase() + "_metadata", group);
        List<Categories> categories = search.getCategories();
        map.put("node",node);
        map.put("categories",categories);
        return map;
    }


    /*
    * 以散点图方式绘制细胞类型umap图
    * */
    @RequestMapping("getNodeData")
    @ResponseBody
    public Map getNodeData(HttpServletRequest request){
        Map<String, String[][]> result = new HashMap<>();
        try{
            String table = request.getParameter("id").toLowerCase() + "_metadata";
            String group = request.getParameter("group");
            List<Nodes> node = search.getNode(table,group);
            List<String> celltypes = new ArrayList<String>();
            // 根据细胞类型分组
            Map<String, List<Nodes>> groupedNodes = node.stream()
                    .collect(Collectors.groupingBy(Nodes::getCategory));
            // 输出结果，针对细胞类型设置横纵坐标等信息
            for (Map.Entry<String, List<Nodes>> entry : groupedNodes.entrySet()) {
                celltypes.add(entry.getKey());
                String[][] arr = new String[entry.getValue().size()][];
                for (int i = 0; i < entry.getValue().size(); i++) {
                    // 查看每个细胞类型中细胞数量
                    // System.out.println(entry.getValue().toArray(new Nodes[entry.getValue().size()]).length);
                    String[] data = new String[4];
                    data[0] = String.valueOf(entry.getValue().get(i).getX());
                    data[1] = String.valueOf(entry.getValue().get(i).getY());
                    data[2] = entry.getValue().get(i).getCategory();
                    data[3] = entry.getValue().get(i).getName();
                    arr[i] = data;
                }
                result.put(entry.getKey(),arr);
            }
            return result;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return result;
        }
    }

    /*
     * 以deckGL绘制细胞类型umap图
     * */
    @RequestMapping("getNodeDataGL")
    @ResponseBody
    public Map getNodeDataGL(HttpServletRequest request){
        Map result = new HashMap<>();
//        try{
            String table = request.getParameter("id").toLowerCase() + "_metadata";
            System.out.println(":::"+table+":::");
            List<NodeGL> nodes = search.getNodeGL(table);
            List<NodeCategory> celltypeMeans = nodes.parallelStream()
                    .filter(node -> node.getCelltype() != null) // 过滤掉 celltype 为 null 的数据
                    .collect(Collectors.groupingBy(NodeGL::getCelltype))
                    .entrySet()
                    .parallelStream()
                    .map(entry -> {
                        String category = entry.getKey();
                        List<NodeGL> groupNodes = entry.getValue();
                        double meanX = entry.getValue().stream().collect(Collectors.averagingDouble(NodeGL::getX));
                        double meanY = entry.getValue().stream().collect(Collectors.averagingDouble(NodeGL::getY));
                        int count = groupNodes.size();
                        return new NodeCategory(category, meanX, meanY, String.valueOf(count));
                    })
                    .collect(Collectors.toList());
            List<NodeCategory> clusterMeans = nodes.parallelStream()
                    .filter(node -> node.getClusters() != null) // 过滤掉 celltype 为 null 的数据
                    .collect(Collectors.groupingBy(NodeGL::getClusters))
                    .entrySet()
                    .parallelStream()
                    .map(entry -> {
                        String category = entry.getKey();
                        List<NodeGL> groupNodes = entry.getValue();
                        double meanX = entry.getValue().stream().collect(Collectors.averagingDouble(NodeGL::getX));
                        double meanY = entry.getValue().stream().collect(Collectors.averagingDouble(NodeGL::getY));
                        int count = groupNodes.size();
                        return new NodeCategory(category, meanX, meanY, String.valueOf(count));
                    })
                    .collect(Collectors.toList());
            List<NodeCategory> origMeans = nodes.parallelStream()
                    .filter(node -> node.getOrigident() != null) // 过滤掉 celltype 为 null 的数据
                    .collect(Collectors.groupingBy(NodeGL::getOrigident))
                    .entrySet()
                    .parallelStream()
                    .map(entry -> {
                        String category = entry.getKey();
                        List<NodeGL> groupNodes = entry.getValue();
                        double meanX = entry.getValue().stream().collect(Collectors.averagingDouble(NodeGL::getX));
                        double meanY = entry.getValue().stream().collect(Collectors.averagingDouble(NodeGL::getY));
                        int count = groupNodes.size();
                        return new NodeCategory(category, meanX, meanY, String.valueOf(count));
                    })
                    .collect(Collectors.toList());
            List<NodeCategory> sexMeans = nodes.parallelStream()
                    .filter(node -> node.getSex() != null) // 过滤掉 celltype 为 null 的数据
                    .collect(Collectors.groupingBy(NodeGL::getSex))
                    .entrySet()
                    .parallelStream()
                    .map(entry -> {
                        String category = entry.getKey();
                        List<NodeGL> groupNodes = entry.getValue();
                        double meanX = entry.getValue().stream().collect(Collectors.averagingDouble(NodeGL::getX));
                        double meanY = entry.getValue().stream().collect(Collectors.averagingDouble(NodeGL::getY));
                        int count = groupNodes.size();
                        return new NodeCategory(category, meanX, meanY, String.valueOf(count));
                    })
                    .collect(Collectors.toList());
            List<NodeCategory> ageMeans = nodes.parallelStream()
                    .filter(node -> node.getAge() != null) // 过滤掉 celltype 为 null 的数据
                    .collect(Collectors.groupingBy(NodeGL::getAge))
                    .entrySet()
                    .parallelStream()
                    .map(entry -> {
                        String category = entry.getKey();
                        List<NodeGL> groupNodes = entry.getValue();
                        double meanX = entry.getValue().stream().collect(Collectors.averagingDouble(NodeGL::getX));
                        double meanY = entry.getValue().stream().collect(Collectors.averagingDouble(NodeGL::getY));
                        int count = groupNodes.size();
                        return new NodeCategory(category, meanX, meanY, String.valueOf(count));
                    })
                    .collect(Collectors.toList());
            List<NodeCategory> timesMeans = nodes.parallelStream()
                    .filter(node -> node.getTimes() != null) // 过滤掉 celltype 为 null 的数据
                    .collect(Collectors.groupingBy(NodeGL::getTimes))
                    .entrySet()
                    .parallelStream()
                    .map(entry -> {
                        String category = entry.getKey();
                        List<NodeGL> groupNodes = entry.getValue();
                        double meanX = entry.getValue().stream().collect(Collectors.averagingDouble(NodeGL::getX));
                        double meanY = entry.getValue().stream().collect(Collectors.averagingDouble(NodeGL::getY));
                        int count = groupNodes.size();
                        return new NodeCategory(category, meanX, meanY, String.valueOf(count));
                    })
                    .collect(Collectors.toList());
            List<NodeCategory> tissueMeans = nodes.parallelStream()
                    .filter(node -> node.getTissue() != null) // 过滤掉 celltype 为 null 的数据
                    .collect(Collectors.groupingBy(NodeGL::getTissue))
                    .entrySet()
                    .parallelStream()
                    .map(entry -> {
                        String category = entry.getKey();
                        List<NodeGL> groupNodes = entry.getValue();
                        double meanX = entry.getValue().stream().collect(Collectors.averagingDouble(NodeGL::getX));
                        double meanY = entry.getValue().stream().collect(Collectors.averagingDouble(NodeGL::getY));
                        int count = groupNodes.size();
                        return new NodeCategory(category, meanX, meanY, String.valueOf(count));
                    })
                    .collect(Collectors.toList());
            List<NodeCategory> treatmentMeans = nodes.parallelStream()
                    .filter(node -> node.getTreatment() != null) // 过滤掉 celltype 为 null 的数据
                    .collect(Collectors.groupingBy(NodeGL::getTreatment))
                    .entrySet()
                    .parallelStream()
                    .map(entry -> {
                        String category = entry.getKey();
                        List<NodeGL> groupNodes = entry.getValue();
                        double meanX = entry.getValue().stream().collect(Collectors.averagingDouble(NodeGL::getX));
                        double meanY = entry.getValue().stream().collect(Collectors.averagingDouble(NodeGL::getY));
                        int count = groupNodes.size();
                        return new NodeCategory(category, meanX, meanY, String.valueOf(count));
                    })
                    .collect(Collectors.toList());
            result.put("data", nodes);
            result.put("celltypeText", celltypeMeans);
            result.put("clusterText", clusterMeans);
            result.put("origText", origMeans);
            result.put("sexText", sexMeans);
            result.put("ageText", ageMeans);
            result.put("timesText", timesMeans);
            result.put("tissueText", tissueMeans);
            result.put("treatmentText", treatmentMeans);
            return result;
        /*}catch (Exception e){
            List<Nodes> nodes = new ArrayList<>();
            result.put("data", nodes);
            System.out.println(e.getMessage());
            return result;
        }*/
    }


    /*
    * 细胞细胞互作图
    * */
    @RequestMapping("getNodeCellInteraction")
    @ResponseBody
    public Map getNodeCellInteraction(HttpServletRequest request){
        Map map = new HashMap();
        try{
            String table = request.getParameter("id").toLowerCase() + "_cellchat";
            String type = request.getParameter("type");
            List<Nodes2> nodes = search.getNodeCellInteraction(table);
            List<Links> links = search.getLinkCellInteraction(table,type);
            List<Categories> categories = search.getCategoriesCellInteraction(table);
            LineStyle lineStyle = null;
            for (int i=0;i<links.size();i++){
                lineStyle = new LineStyle();
                lineStyle.setWidth(links.get(i).getValue());
                links.get(i).setLineStyle(lineStyle);
            }
            map.put("nodes",nodes);
            map.put("links",links);
            map.put("categories",categories);
            return map;
        }catch (Exception e){
            List<Nodes2> nodes = new ArrayList<>();
            List<Links> links = new ArrayList<>();
            List<Categories> categories = new ArrayList<>();
            map.put("nodes",nodes);
            map.put("links",links);
            map.put("categories",categories);
            return map;
        }
    }

    @RequestMapping("getNodeDataGLsplit")
    @ResponseBody
    public Map getNodeDataGLsplit(HttpServletRequest request) {
        Map result = new HashMap<>();
        try {
            String table = request.getParameter("id").toLowerCase() + "_metadata";
            List<NodeGL> nodes = search.getNodeGL(table);
            result.put("data", nodes);
            return result;
        }catch (Exception e){
            List<NodeGL> nodes = new ArrayList<>();
            result.put("data", nodes);
            return result;
        }
    }

    @RequestMapping("geneTfNetwork")
    @ResponseBody
    public Map geneTfNetwork(HttpServletRequest request){
        Map map = new HashMap();
        String table = request.getParameter("id").toLowerCase()+"_scenic_adj";
        String gene = request.getParameter("gene");
        List<Nodes2> nodes = search.geneTfNetwork(table,gene);
        Nodes2 geneNode = new Nodes2();
        geneNode.setId("0");
        geneNode.setCategory("Gene");
        geneNode.setName(gene);
        geneNode.setSymbolSize("10");
        geneNode.setValue("-");
        List<Links> links = new ArrayList<>();
        Links link = null;
        for (int i=0;i<nodes.size();i++){
            link = new Links();
            link.setSource(nodes.get(i).getId());
            link.setTarget("0");
            links.add(link);
        }
        nodes.add(geneNode);
        List<Categories> categories = new ArrayList<>();
        Categories categories1 = new Categories();
        categories1.setName("TF");
        Categories categories2 = new Categories();
        categories2.setName("Gene");
        categories.add(categories1);
        categories.add(categories2);
        map.put("nodes",nodes);
        map.put("links",links);
        map.put("categories",categories);
        return map;
    }

}
