package com.zlsk.zTool.menuCmd.menu;

public class DbFieldInfo {
    public final static String FIELD_TYPE_NUMBER = "number";
    public final static String FIELD_TYPE_STRING = "string";

    private String key;
    private String field;
    private String label;
    private String type;
    private int state = 0;

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public String getField() {
        return field;
    }
    public void setField(String field) {
        this.field = field;
    }

    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }
}
