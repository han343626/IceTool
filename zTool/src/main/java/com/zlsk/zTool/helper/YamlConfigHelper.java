package com.zlsk.zTool.helper;

import android.content.Context;

import com.zlsk.zTool.utils.CommonToolUtil;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class YamlConfigHelper {
    private static Map<String, Map<String, Object>> cfgs = null;

    private static YamlConfigHelper cfgHelper = null;

    private YamlConfigHelper(){
        if(cfgs == null){
            cfgs = new HashMap<>();
        }
    }

    public static YamlConfigHelper getInstance(){
        if(cfgHelper == null){
            cfgHelper = new YamlConfigHelper();
        }
        return cfgHelper;
    }

    public Map<String, Object> getConfig(String filename){
        Context context = CommonToolUtil.getInstance().getContext();
        if(context == null || filename == null || filename.equals("")){
            return null;
        }
        if(cfgs.containsKey(filename)){
            return cfgs.get(filename);
        }
        InputStream s = null;
        try{
            s = context.getAssets().open(filename + ".yml");
            Yaml yaml = new Yaml();
            Map<String, Object> cfg = (Map<String, Object>) yaml.load(s);
            s.close();
            cfgs.put(filename, cfg);
            return cfg;
        }catch (Exception e){
            if(s != null){
                try{
                    s.close();
                }catch (Exception e1){

                }
            }
        }
        return null;
    }
    public Object getConfig(String filename, String key){
        if(key == null || key.isEmpty()){
            return null;
        }
        Map<String, Object> m = getConfig(filename);
        if(m == null){
            return null;
        }
        String[] ka = key.split("\\.");
        int i = 0;
        for(;i < ka.length - 1;i++){
            if(!m.containsKey(ka[i])){
                return null;
            }
            m = (Map<String, Object>) m.get(ka[i]);
            if(m == null){
                return null;
            }
        }
        return m.get(ka[i]);
    }
    public String getConfigString(String filename, String key){
        Object o = getConfig(filename, key);
        if(o == null){
            return null;
        }
        return o.toString();
    }
}
