package com.zlsk.zTool.utils.system;

import com.zlsk.zTool.utils.CommonToolUtil;

import java.lang.reflect.Field;

/**
 * Created by IceWang on 2018/4/25.
 */

public class BuildConfigUtil {
    public static Object getBuildConfigValue(String fieldName) {
        try {
            Class<?> clazz = Class.forName(CommonToolUtil.getInstance().getMainPackage() + ".BuildConfig");
            Field field = clazz.getField(fieldName);
            return field.get(null);
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }
}
