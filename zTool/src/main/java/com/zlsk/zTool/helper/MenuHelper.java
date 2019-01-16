package com.zlsk.zTool.helper;

import android.content.Context;

import com.zlsk.zTool.menuCmd.base.AMenuCommand;
import com.zlsk.zTool.menuCmd.base.NullMenuCommand;
import com.zlsk.zTool.utils.system.BuildConfigUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MenuHelper {
    private final static String CONFIG_FILENAME_MENU = "menu";
    private final static String CONFIG_NAME_MENUS = "menus";
    private final static String FIELD_NAME_MENU_KEY = "key";
    private final static String FIELD_NAME_MENU_KEY_IN_USER_INFO = "URL";

    private static MenuHelper mHelper = null;

    private int fMenuAuth = -1;
    private List<Map<String, Object>> allMenus = null;
    private JSONArray userMenuInfo = null;
    private List<Map<String, Object>> userMenus = null;

    private MenuHelper(Context context){
        allMenus = (List<Map<String, Object>>)YamlConfigHelper.getInstance().getConfig(CONFIG_FILENAME_MENU, CONFIG_NAME_MENUS);
        fMenuAuth = (int) BuildConfigUtil.getBuildConfigValue("F_MENU_AUTH");
    }

    public static MenuHelper getInstance(Context context){
        if(mHelper == null){
            mHelper = new MenuHelper(context);
        }
        return mHelper;
    }

    public JSONArray getUserMenuInfo() {
        return userMenuInfo;
    }
    public void setUserMenuInfo(JSONArray userMenuInfo) {
        this.userMenuInfo = userMenuInfo;
    }

    public List<Map<String, Object>> getAvailableMenus(){
        if(fMenuAuth != -1 && allMenus == null){
            return null;
        }
        userMenus = new ArrayList<>();
        for(int i = 0;i < allMenus.size();i++){
            Map<String, Object> mi = allMenus.get(i);
            String k = mi.get(FIELD_NAME_MENU_KEY).toString();
            if(checkUserMenu(k)){
                try {
                    Map<String, Object> m = new HashMap<>();
                    for(Map.Entry e : mi.entrySet()){
                        m.put(e.getKey().toString(), e.getValue());
                    }
                    AMenuCommand mc = (AMenuCommand) Class.forName(m.get("command").toString()).newInstance();
                    m.put("command", mc);
                    userMenus.add(m);
                }catch (Exception e){
                    continue;
                }
            }
        }
        return userMenus;
    }

    public boolean checkUserMenu(String key){
        if(key == null){
            return false;
        }
        if(fMenuAuth == -1){
            return true;
        }
        if(userMenuInfo != null) {
            for (int i = 0; i < userMenuInfo.length(); i++) {
                JSONObject mi = userMenuInfo.optJSONObject(i);
                if (key.equals(mi.optString(FIELD_NAME_MENU_KEY_IN_USER_INFO))) {
                    return true;
                }
            }
        }
        return false;
    }

    public Map<String, Object> newNullMenu(){
        Map<String, Object> m = new HashMap<>();
        m.put("name", "");
        m.put("command", new NullMenuCommand());
        return m;
    }
}
