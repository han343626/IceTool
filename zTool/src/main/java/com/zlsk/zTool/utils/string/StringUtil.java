package com.zlsk.zTool.utils.string;

import android.support.annotation.Nullable;
import android.util.Patterns;
import android.webkit.URLUtil;

import com.github.promeg.pinyinhelper.Pinyin;
import com.zlsk.zTool.model.other.NameModel;
import com.zlsk.zTool.utils.list.ListUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IceWang on 2018/5/28.
 */

public class StringUtil {
    /**
     * 判断字符串是否为空
     *
     */
    public static boolean isNullString(@Nullable String str) {
        return str == null || str.length() == 0 || "null".equals(str) || str.trim().isEmpty() || str.isEmpty();
    }

    public static boolean isNullOrWhiteSpace(String str){
        return str == null || str.trim().isEmpty();
    }

    public static boolean isBlank(String var0) {
        return var0 == null || var0.trim().length() == 0 || var0.equals("");
    }

    public static boolean isEmpty(String var0) {
        return var0 == null || var0.length() == 0;
    }

    /**
     * 避免显示 null
     */
    public static String nullStrToEmpty(String var0) {
        return var0 == null ? "" : var0;
    }

    /**
     * 首字母大写
     */
    public static String capitalizeFirstLetter(String var0) {
        char var1;
        return isEmpty(var0) ? var0 : (Character.isLetter(var1 = var0.charAt(0)) && !Character.isUpperCase(var1)?(new StringBuilder(var0.length())).append(Character.toUpperCase(var1)).append(var0.substring(1)).toString():var0);
    }

    /**
     * 字符串转UTF-8
     */
    public static String utf8Encode(String var0) {
        if(!isEmpty(var0) && var0.getBytes().length != var0.length()) {
            try {
                return URLEncoder.encode(var0, "UTF-8");
            } catch (UnsupportedEncodingException var1) {
                throw new RuntimeException("UnsupportedEncodingException occurred. ", var1);
            }
        } else {
            return var0;
        }
    }

    /**
     * 全角转半角
     */
    public static String fullWidthToHalfWidth(String var0) {
        if(isEmpty(var0)) {
            return var0;
        } else {
            char[] var2 = var0.toCharArray();

            for(int var1 = 0; var1 < var2.length; ++var1) {
                if(var2[var1] == 12288) {
                    var2[var1] = 32;
                } else if(var2[var1] >= '！' && var2[var1] <= '～') {
                    var2[var1] -= 'ﻠ';
                } else {
                    var2[var1] = var2[var1];
                }
            }

            return new String(var2);
        }
    }

    /**
     * 半角转全角
     */
    public static String halfWidthToFullWidth(String var0) {
        if(isEmpty(var0)) {
            return var0;
        } else {
            char[] var2 = var0.toCharArray();

            for(int var1 = 0; var1 < var2.length; ++var1) {
                if(var2[var1] == 32) {
                    var2[var1] = 12288;
                } else if(var2[var1] >= 33 && var2[var1] <= 126) {
                    var2[var1] += 'ﻠ';
                } else {
                    var2[var1] = var2[var1];
                }
            }

            return new String(var2);
        }
    }

    /**
     * 是否是数字
     */
    public static boolean isNum(String var0) {
        return var0.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }

    /**
     * 是否是11位手机号
     */
    public static boolean isPhoneNumber(String phoneStr) {
        if(phoneStr == null || phoneStr.length() != 11){
            return false;
        }
        //定义电话格式的正则表达式
        String regex = "(13[0-9]|15[0-9]|17[0-9]|18[0-9])\\d{8}";
        //设定查看模式
        Pattern p = Pattern.compile(regex);
        //判断Str是否匹配，返回匹配结果
        Matcher m = p.matcher(phoneStr);
        return m.find();
    }

    /**
     * 是否包含汉字
     */
    public static boolean hasChinese(String var0) {
        int var1 = 0;
        Matcher var4 = Pattern.compile("[\\u4e00-\\u9fa5]").matcher(var0);

        while(var4.find()) {
            int var2 = var4.groupCount();

            for(int var3 = 0; var3 <= var2; ++var3) {
                ++var1;
            }
        }

        if(var1 > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 移除空格
     */
    public static String removalSpace(String var0) {
        while(var0.startsWith(" ")) {
            var0 = var0.substring(1, var0.length()).trim();
        }

        while(var0.endsWith(" ")) {
            var0 = var0.substring(0, var0.length() - 1).trim();
        }

        return var0;
    }

    /**
     * 是否是IP
     */
    public static boolean isIp(String var0) {
        boolean var1 = false;
        String[] var2;
        if((var0 = removalSpace(var0)).matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}") && Integer.parseInt((var2 = var0.split("\\."))[0]) < 255 && Integer.parseInt(var2[1]) < 255 && Integer.parseInt(var2[2]) < 255 && Integer.parseInt(var2[3]) < 255) {
            var1 = true;
        }

        return var1;
    }

    /**
     * 是否是web链接
     */
    public static boolean isWebUrl(String urlPath){
        return Patterns.WEB_URL.matcher(urlPath).matches() || URLUtil.isValidUrl(urlPath);
    }

    public static String chineseToPinyin(String chinese){
        return Pinyin.toPinyin(chinese,"");
    }

    public static List<String> sortChinese(List<String> chinese){
        List<NameModel> nameModelList = new ArrayList<>();
        for (String name : chinese){
            nameModelList.add(new NameModel(name));
        }

        for (int chineseIndex = 0; chineseIndex < nameModelList.size() - 1; chineseIndex++){
            for (int baseIndex = 0; baseIndex < nameModelList.size() -1- chineseIndex; baseIndex++) {
                String namePinYin1 = nameModelList.get(baseIndex).pinyin;
                String namePinYin2 = nameModelList.get(baseIndex + 1).pinyin;

                int size = namePinYin1.length() >= namePinYin2.length() ? namePinYin2.length() : namePinYin1.length();
                for (int pinyinIndex = 0; pinyinIndex < size ; pinyinIndex++){
                    char current = namePinYin1.charAt(pinyinIndex);
                    char next = namePinYin2.charAt(pinyinIndex);
                    if(current < next){

                        break;
                    }
                    if(current > next){
                        NameModel nameBean = nameModelList.get(baseIndex);
                        nameModelList.set(baseIndex, nameModelList.get(baseIndex +1));
                        nameModelList.set(baseIndex + 1, nameBean);
                        break;
                    }
                }
            }
        }

        List<String> result = new ArrayList<>();
        for (NameModel nameModel : nameModelList){
            result.add(nameModel.name);
        }

        return result;
    }

    public static List<String> sortChinese(String[] chinese){
        return sortChinese(ListUtil.toList(chinese));
    }
}
