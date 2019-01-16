package com.zlsk.zTool.utils.other;

import com.google.gson.Gson;

import org.xutils.common.util.LogUtil;

/**
 * Created by IceWang on 2018/4/23.
 */

public class GsonUtil {
    private static final Gson gson = new Gson();

    public static String toJson(Object obj) {
        try {
            return gson.toJson(obj, obj.getClass());
        } catch (Exception e) {
            LogUtil.e("GsonUtil", e);
            throw new RuntimeException(e);
        }
    }

    public static <T> T toObject(String json, Class<T> cls) {
        try {
            return gson.fromJson(json, cls);
        } catch (Exception e) {
            LogUtil.e("GsonUtil", e);
            throw new RuntimeException(e);
        }
    }
}
