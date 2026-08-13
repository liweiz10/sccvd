package com.zlw.stroke.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zlw.stroke.mapper.Browse;
import com.zlw.stroke.pojo.BrowseParamTotal;
import com.zlw.stroke.pojo.BrowserParam;
import com.zlw.stroke.pojo.PageTab;
import com.zlw.stroke.pojo.SampleTable;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class BrowserTable {
    @Autowired(required = false)
    private Browse browse;

    @RequestMapping("getSampleTable")
    @ResponseBody
    public PageTab getSampleTable(Integer start, Integer draw, Integer length, HttpServletRequest request) throws InterruptedException {
        PageTab tabs = new PageTab();
        String searchValue = request.getParameter("search[value]");
        PageHelper.startPage(start / length + 1, length);
        Page<SampleTable> list = browse.getSampleTable(request.getParameter("tab1Param"), request.getParameter("tab2Param"), request.getParameter("tab3Param"), request.getParameter("tab4Param"), request.getParameter("tab5Param"),searchValue,request.getParameter("order[0][column]"),request.getParameter("order[0][dir]"));
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

    @RequestMapping("tab1")
    @ResponseBody
    public Map tab1(HttpServletRequest request) throws InterruptedException {
        BrowserParam param = new BrowserParam(request.getParameter("tab1Param"), request.getParameter("tab2Param"), request.getParameter("tab3Param"), request.getParameter("tab4Param"), request.getParameter("tab5Param"));
        Map map = new HashMap();
        List<BrowseParamTotal> tab = new ArrayList<>();
        try {
            tab = browse.getTab1Param(param);
            map.put("data", tab);
            if (request.getParameter("tab1Param").equals("")){
                map.put("active", "");
            }else {
                map.put("active", "active");
            }
            return map;
        } catch (Exception e) {
            map.put("data", tab);
            System.out.println("e.getMessage=" + e.getMessage());
            return map;
        }
    }

    @RequestMapping("tab2")
    @ResponseBody
    public Map tab2(HttpServletRequest request) throws InterruptedException {
        BrowserParam param = new BrowserParam(request.getParameter("tab1Param"), request.getParameter("tab2Param"), request.getParameter("tab3Param"), request.getParameter("tab4Param"), request.getParameter("tab5Param"));
        Map map = new HashMap();
        List<BrowseParamTotal> tab = new ArrayList<>();
        try {
            tab = browse.getTab2Param(param);
            map.put("data", tab);
            if (request.getParameter("tab2Param").equals("")){
                map.put("active", "");
            }else {
                map.put("active", "active");
            }
            return map;
        } catch (Exception e) {
            map.put("data", tab);
            System.out.println("e.getMessage=" + e.getMessage());
            return map;
        }
    }

    @RequestMapping("tab3")
    @ResponseBody
    public Map tab3(HttpServletRequest request) throws InterruptedException {
        BrowserParam param = new BrowserParam(request.getParameter("tab1Param"), request.getParameter("tab2Param"), request.getParameter("tab3Param"), request.getParameter("tab4Param"), request.getParameter("tab5Param"));
        Map map = new HashMap();
        List<BrowseParamTotal> tab = new ArrayList<>();
        try {
            tab = browse.getTab3Param(param);
            map.put("data", tab);
            if (request.getParameter("tab3Param").equals("")){
                map.put("active", "");
            }else {
                map.put("active", "active");
            }
            return map;
        } catch (Exception e) {
            map.put("data", tab);
            System.out.println("e.getMessage=" + e.getMessage());
            return map;
        }
    }

    @RequestMapping("tab4")
    @ResponseBody
    public Map tab4(HttpServletRequest request) throws InterruptedException {
        BrowserParam param = new BrowserParam(request.getParameter("tab1Param"), request.getParameter("tab2Param"), request.getParameter("tab3Param"), request.getParameter("tab4Param"), request.getParameter("tab5Param"));
        Map map = new HashMap();
        List<BrowseParamTotal> tab = new ArrayList<>();
        try {
            tab = browse.getTab4Param(param);
            map.put("data", tab);
            if (request.getParameter("tab4Param").equals("")){
                map.put("active", "");
            }else {
                map.put("active", "active");
            }
            return map;
        } catch (Exception e) {
            map.put("data", tab);
            System.out.println("e.getMessage=" + e.getMessage());
            return map;
        }
    }

    @RequestMapping("tab5")
    @ResponseBody
    public Map tab5(HttpServletRequest request) throws InterruptedException {
        BrowserParam param = new BrowserParam(request.getParameter("tab1Param"), request.getParameter("tab2Param"), request.getParameter("tab3Param"), request.getParameter("tab4Param"), request.getParameter("tab5Param"));
        Map map = new HashMap();
        List<BrowseParamTotal> tab = new ArrayList<>();
        try {
            tab = browse.getTab5Param(param);
            map.put("data", tab);
            if (request.getParameter("tab5Param").equals("")){
                map.put("active", "");
            }else {
                map.put("active", "active");
            }
            return map;
        } catch (Exception e) {
            map.put("data", tab);
            System.out.println("e.getMessage=" + e.getMessage());
            return map;
        }
    }

}
