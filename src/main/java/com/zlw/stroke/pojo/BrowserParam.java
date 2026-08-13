package com.zlw.stroke.pojo;

public class BrowserParam {
    private String tab1Param;
    private String tab2Param;
    private String tab3Param;
    private String tab4Param;
    private String tab5Param;

    public BrowserParam(String tab1Param, String tab2Param, String tab3Param, String tab4Param, String tab5Param) {
        this.tab1Param = tab1Param;
        this.tab2Param = tab2Param;
        this.tab3Param = tab3Param;
        this.tab4Param = tab4Param;
        this.tab5Param = tab5Param;
    }

    @Override
    public String toString() {
        return "BrowserParam{" +
                "tab1Param='" + tab1Param + '\'' +
                ", tab2Param='" + tab2Param + '\'' +
                ", tab3Param='" + tab3Param + '\'' +
                ", tab4Param='" + tab4Param + '\'' +
                ", tab5Param='" + tab5Param + '\'' +
                '}';
    }
}
