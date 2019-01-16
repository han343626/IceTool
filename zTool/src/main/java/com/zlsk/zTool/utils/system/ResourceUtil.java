package com.zlsk.zTool.utils.system;

import android.content.Context;

import com.zlsk.zTool.R;

import java.lang.reflect.Field;

/**
 * Created by IceWang on 2018/6/4.
 */

public class ResourceUtil {
    /**
     * 获取当前module下资源
     */
    public static int getDrawableResourceId(String name) {
        try {
            Field field = R.drawable.class.getField(name);
            int i = field.getInt(new R.drawable());
            return i;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * lib获取app中的资源
     */
    public static int getResourceId(Context context,String fieldName,String fileName){
        return context.getResources().getIdentifier(fieldName, fileName, context.getPackageName());
    }
}
