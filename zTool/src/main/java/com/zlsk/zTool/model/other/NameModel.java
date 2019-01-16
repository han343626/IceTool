package com.zlsk.zTool.model.other;

import com.zlsk.zTool.utils.string.StringUtil;

/**
 * Created by IceWang on 2018/11/26.
 */

public class NameModel {
    public String name;
    public String firstLitter;
    public String pinyin;

    public NameModel(String name) {
        this.name = name;
        this.pinyin = StringUtil.chineseToPinyin(name);
        this.firstLitter = pinyin.substring(0,1);
    }
}
