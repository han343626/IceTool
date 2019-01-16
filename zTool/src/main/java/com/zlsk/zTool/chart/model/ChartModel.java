package com.zlsk.zTool.chart.model;

import java.io.Serializable;

/**
 * Created by IceWang on 2018/9/12.
 */

public class ChartModel implements Serializable{
    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ChartModel(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
