package com.zlw.stroke.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "com.path")
@ToString
@Data
public class Path {

    /**
     * 项目的工作路径
     */
    private String workPath;
    private String expersionCodePath;



}