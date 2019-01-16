package com.zlsk.zTool.customControls.base;

/**
 * Created by IceWang on 2018/7/6.
 */

public class SelectModel {
    private String name;
    private String alias;
    private boolean isSelect;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public SelectModel(String name, boolean isSelect) {
        this.name = name;
        this.isSelect = isSelect;
    }
}
