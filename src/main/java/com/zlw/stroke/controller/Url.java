package com.zlw.stroke.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class Url {
    @GetMapping("/")
    public ModelAndView home(){return new ModelAndView("messages/index");}
    @GetMapping("home")
    public ModelAndView homeenid() {
        return new ModelAndView("messages/index");
    }
    @GetMapping("index")
    public ModelAndView index() {
        return new ModelAndView("messages/index");
    }
    @GetMapping("browse")
    public ModelAndView browse() {
        return new ModelAndView("messages/browse");
    }
    @GetMapping("search")
    public ModelAndView search() {
        return new ModelAndView("messages/search");
    }
    @GetMapping("geneEnrichment")
    public ModelAndView geneEnrichment() {
        return new ModelAndView("messages/geneEnrichment");
    }
    @GetMapping("comparison")
    public ModelAndView comparison() {
        return new ModelAndView("messages/comparison");
    }
    @GetMapping("anaByGene")
    public ModelAndView anaByGene() {
        return new ModelAndView("messages/anaByGene");
    }
    @GetMapping("context")
    public ModelAndView context() {
        return new ModelAndView("messages/context");
    }
    @GetMapping("download")
    public ModelAndView download() {
        return new ModelAndView("messages/download");
    }
    @GetMapping("geneExpressed")
    public ModelAndView geneExpressed() {
        return new ModelAndView("messages/geneExpressed");
    }
    @GetMapping("help")
    public ModelAndView help() {
        return new ModelAndView("messages/help");
    }
}
