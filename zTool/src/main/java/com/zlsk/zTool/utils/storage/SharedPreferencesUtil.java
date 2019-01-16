package com.zlsk.zTool.utils.storage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.os.Environment;

import com.zlsk.zTool.dialog.ZToast;
import com.zlsk.zTool.utils.file.FileUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by IceWang on 2016/9/8.
 */
public class SharedPreferencesUtil {
    @SuppressLint("StaticFieldLeak")
    private static SharedPreferencesUtil instance;
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static SharedPreferencesUtil getInstance(Context context) {
        if(instance == null){
            instance = new SharedPreferencesUtil(context);
        }
        return instance;
    }

    public static SharedPreferencesUtil getInstance(){
        return instance;
    }

    private SharedPreferencesUtil(Context context) {
        this.context = context;
        modifySharePreferenceDir();
    }

    @SuppressLint("CommitPrefEdits")
    public SharedPreferencesUtil getSharePreference(String dataName){
        this.sharedPreferences = context.getSharedPreferences(dataName, Activity.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
        return instance;
    }

    /**
     * 将存储位置暴露到指定位置 FileUtil.init().getStoragePath()
     */
    private void modifySharePreferenceDir(){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                Field field;
                field = ContextWrapper.class.getDeclaredField("mBase");
                field.setAccessible(true);
                Object obj = field.get(context);
                field = obj.getClass().getDeclaredField("mPreferencesDir");
                field.setAccessible(true);
                File file = new File(FileUtil.getInstance().getStoragePath());
                field.set(obj, file);
            } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        else{
            ZToast.getInstance().show("路径获取失败");
        }
    }

    public void putInt(String key,int value){
        editor.putInt(key,value);
        editor.commit();
    }

    public void putString(String key,String value){
        editor.putString(key,value);
        editor.commit();
    }

    public void putBoolean(String key,boolean value){
        editor.putBoolean(key,value);
        editor.commit();
    }

    public int getInt(String key,int defValue){
        return sharedPreferences.getInt(key,defValue);
    }

    public String getString(String key,String defValue){
        return sharedPreferences.getString(key,defValue);
    }

    public Boolean getBoolean(String key,Boolean defValue){
        return sharedPreferences.getBoolean(key,defValue);
    }

    public void removeData(String key){
        editor.remove(key);
        editor.commit();
    }

    public Map<String, ?> getAllValue(){
        return sharedPreferences.getAll();
    }
}
