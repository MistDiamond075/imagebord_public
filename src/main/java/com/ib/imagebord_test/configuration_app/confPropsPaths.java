package com.ib.imagebord_test.configuration_app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class confPropsPaths {
    @Value("${path.threadfiles}")
    private String threadfiles;

    @Value(value = "${path.cssfiles}")
    private String cssfiles;

    @Value(value = "${path.jsfiles}")
    private String jsfiles;

    @Value(value="${path.resources}")
    private String resources;

    @Value(value = "${path.templatefiles}")
    private String templates;

    @Value(value = "${path.indexpage}")
    private String indexpage;

    @Value(value = "${path.samplebord}")
    private String samplebord;

    @Value(value = "${path.samplepage}")
    private String samplepage;

    @Value(value = "${path.files.textautoformatlist}")
    private String textautoformat_list;

    public String getThreadfilesPath() {
        return threadfiles;
    }

    public String getThreadfilesName(){
        return "threadFiles";
    }

    public String getCssfilesPath() {
        return cssfiles;
    }

    public String getCssFilesName(){
        return "cssfiles";
    }

    public String getJsfilesPath() {
        return jsfiles;
    }

    public String getJsfilesName() {
        return "jsfiles";
    }

    public String getResources() {
        return resources;
    }

    public String getTemplates() {
        return templates;
    }

    public String getIndexpage() {
        return indexpage;
    }

    public String getSamplebord() {
        return samplebord;
    }

    public String getSamplepage() {
        return samplepage;
    }

    public String getTextautoformat_list() {
        return textautoformat_list;
    }
}
