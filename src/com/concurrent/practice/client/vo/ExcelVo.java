package com.concurrent.practice.client.vo;

public class ExcelVo {

    private String theme;

    private int index;

    public ExcelVo(String theme, int index) {
        this.theme = theme;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}
