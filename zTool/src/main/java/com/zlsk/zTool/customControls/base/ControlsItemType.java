package com.zlsk.zTool.customControls.base;

public enum ControlsItemType {
    IMAGE("IMG"),
    TEXT("TEXT"),
    TEXT_NUM("TEXT_NUM"),
    LOCATION("LOCATION"),
    DATE("DATE"),
    SINGLE_SELECT("SINGLE_SELECT"),
    MUL_SELECT("MUL_SELECT"),
    VIDEO("VIDEO"),
    SINGLE_SELECT_INPUT("SINGLE_SELECT_INPUT"),
    MUL_SELECT_INPUT("MUL_SELECT_INPUT"),
    DB_SEARCH_SELECT_INPUT("DB_SEARCH_SELECT_INPUT"),;

    private final String value;

    public String getValue() {
        return value;
    }

    ControlsItemType(String value) {
        this.value = value.toUpperCase();
    }

    public static ControlsItemType getTypeByValue(String type) {
        for (ControlsItemType e : values()) {
            if (e.getValue().equalsIgnoreCase(type)) {
                return e;
            }
        }
        return null;
    }
}
