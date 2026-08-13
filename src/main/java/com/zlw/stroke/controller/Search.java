package com.zlw.stroke.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zlw.stroke.config.Path;
import com.zlw.stroke.mapper.DetailMapper;
import com.zlw.stroke.mapper.SearchMapper;
import com.zlw.stroke.pojo.*;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class Search {
    @Autowired
    private Path path;

    @Autowired(required = false)
    private SearchMapper search;

    @RequestMapping("getSearchTab")
    @ResponseBody
    public PageTab getSearchTab(Integer start, Integer draw, Integer length, HttpServletRequest request) throws InterruptedException {
        PageTab tabs = new PageTab();
        String searchValue = request.getParameter("search[value]");
        PageHelper.startPage(start / length + 1, length);
        String type = request.getParameter("type");
        String param = request.getParameter("param");
        Page<SampleTable> list = new Page<>();
        try {
            if (type.equals("celltype")){
                list = search.getSearchSampleTableByCelltype(param,searchValue,request.getParameter("order[0][column]"),request.getParameter("order[0][dir]"));
            }else if (type.equals("tissuetype")){
                list = search.getSearchSampleTableByTissue(param,searchValue,request.getParameter("order[0][column]"),request.getParameter("order[0][dir]"));
            }else if (type.equals("disease")){
                list = search.getSearchSampleTableByDisease(param,searchValue,request.getParameter("order[0][column]"),request.getParameter("order[0][dir]"));
            }
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

    @RequestMapping("searchResult")
    @ResponseBody
    public ModelAndView searchResult(HttpServletRequest request) throws InterruptedException {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("messages/searchResult");
        try {
            String param = request.getParameter("param");
            String type = request.getParameter("type");
            mv.addObject("data", param);
            mv.addObject("type", type);
            return mv;
        }catch (Exception e){
            mv.addObject("message","Server exception!");
            return mv;
        }
    }

    @RequestMapping("getCelltype")
    @ResponseBody
    public Map getCelltype(HttpServletRequest request) throws RserveException, REXPMismatchException {
        Map map = new HashMap();
        List<NameValue> list = new ArrayList<>();
        try{
            list = search.getCelltype();
            map.put("data", list);
            return map;
        }catch (Exception e){
            NameValue name = new NameValue();
            list.add(new NameValue());
            map.put("data", list);
            return map;
        }
    }

    @RequestMapping("getTissuetype")
    @ResponseBody
    public Map getTissuetype(HttpServletRequest request) throws RserveException, REXPMismatchException {
        Map map = new HashMap();
        List<NameValue> list = new ArrayList<>();
        try{
            list = search.getTissuetype();
            map.put("data", list);
            return map;
        }catch (Exception e){
            NameValue name = new NameValue();
            list.add(new NameValue());
            map.put("data", list);
            return map;
        }
    }

    @RequestMapping("getDisease")
    @ResponseBody
    public Map getDisease(HttpServletRequest request) throws RserveException, REXPMismatchException {
        Map map = new HashMap();
        List<NameValue> list = new ArrayList<>();
        try{
            list = search.getDisease();
            map.put("data", list);
            return map;
        }catch (Exception e){
            NameValue name = new NameValue();
            list.add(new NameValue());
            map.put("data", list);
            return map;
        }
    }

}