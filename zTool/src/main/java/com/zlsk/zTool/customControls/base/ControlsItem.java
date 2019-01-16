package com.zlsk.zTool.customControls.base;

import com.zlsk.zTool.dialog.DatetimePickerDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ControlsItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean required = false;
    private boolean visible = true;
    private boolean edit;
    private ControlsItemType type;
    private String name;
    private String alias;
    private String value;
    private String defaultValue;
    /**
     * 下拉框选中监听事件ID
     */
    private int dropDownSelectEventId = 1;

    public int getDropDownSelectEventId() {
        return dropDownSelectEventId;
    }

    public void setDropDownSelectEventId(int dropDownSelectEventId) {
        this.dropDownSelectEventId = dropDownSelectEventId;
    }

    /**
     * 控制TEXT输入格式
     */
    private ControlsItemUtil.InspectTextInputType inspectTextInputType;
    /**
     * 如果是float，控制小数后多少位
     */
    private int decimalCount;

    /**
     * 图片最大限值数量
     */
    private int imageLimitCount = 9;

    /**
     * 是否添加水印
     */
    private boolean isAddWaterMark = false;
    private String  AddWaterMarkMessage = "";

    public String getAddWaterMarkMessage() {
        return AddWaterMarkMessage;
    }

    public void setAddWaterMarkMessage(String addWaterMarkMessage) {
        AddWaterMarkMessage = addWaterMarkMessage;
    }

    private String[] singleSelectValuesArray;

    private ArrayList<String> needInputItems;

    private String dbSelectValue;
    private String dbInputValue;
    private int dbSelectDeleteId;

    /**
     * 是否需要否认日期
     */
    private boolean isNullDate = false;
    private DatetimePickerDialog.EPickerType ePickerType;

    public DatetimePickerDialog.EPickerType getePickerType() {
        return ePickerType;
    }

    public void setePickerType(DatetimePickerDialog.EPickerType ePickerType) {
        this.ePickerType = ePickerType;
    }

    public boolean isNullDate() {
        return isNullDate;
    }

    public void setNullDate(boolean nullDate) {
        isNullDate = nullDate;
    }

    public int getDbSelectDeleteId() {
        return dbSelectDeleteId;
    }

    public void setDbSelectDeleteId(int dbSelectDeleteId) {
        this.dbSelectDeleteId = dbSelectDeleteId;
    }

    public String getDbSelectValue() {
        return dbSelectValue;
    }

    public void setDbSelectValue(String dbSelectValue) {
        this.dbSelectValue = dbSelectValue;
    }

    public String getDbInputValue() {
        return dbInputValue;
    }

    public void setDbInputValue(String dbInputValue) {
        this.dbInputValue = dbInputValue;
    }

    public ArrayList<String> getNeedInputItems() {
        return needInputItems;
    }

    public void setNeedInputItems(ArrayList<String> needInputItems) {
        this.needInputItems = needInputItems;
    }

    public String[] getSingleSelectValuesArray() {
        return singleSelectValuesArray;
    }

    public void setSingleSelectValuesArray(String[] singleSelectValuesArray) {
        this.singleSelectValuesArray = singleSelectValuesArray;
    }

    private List<String> singleSelectValues;

    public void setSingleSelectValues(List<String> singleSelectValues) {
        this.singleSelectValues = singleSelectValues;
    }

    public List<String> getSingleSelectValues() {
        return singleSelectValues;
    }

    public boolean isAddWaterMark() {
        return isAddWaterMark;
    }

    public void setAddWaterMark(boolean addWaterMark) {
        isAddWaterMark = addWaterMark;
    }

    public int getImageLimitCount() {
        return imageLimitCount;
    }

    public void setImageLimitCount(int imageLimitCount) {
        this.imageLimitCount = imageLimitCount;
    }

    public int getDecimalCount() {
        return decimalCount;
    }

    public void setDecimalCount(int decimalCount) {
        this.decimalCount = decimalCount;
    }

    public ControlsItemUtil.InspectTextInputType getInspectTextInputType() {
        return inspectTextInputType;
    }

    public void setInspectTextInputType(ControlsItemUtil.InspectTextInputType inspectTextInputType) {
        this.inspectTextInputType = inspectTextInputType;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public ControlsItemType getType() {
        return type;
    }

    public void setType(ControlsItemType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
