package com.zlsk.zTool.utils;

import com.zlsk.zTool.utils.storage.SharedPreferencesUtil;
import com.zlsk.zTool.utils.string.StringUtil;

public class ServiceUtil {
    private static final String SP_CONFIG_SERVICE_FILE_NAME = "sp_service_config";
    private static final String SP_CONFIG_SERVICE_IP = "sp_service_ip";
    private static final String SP_CONFIG_SERVICE_PORT = "sp_service_PORT";

    private static ServiceUtil instance;

    private String base_url_ip;
    private String base_url_port;

    private ServiceUtil() {
    }

    public static synchronized ServiceUtil getInstance() {
        if (instance == null) {
            instance = new ServiceUtil();
        }
        return instance;
    }

    public void init(String baseUrl){
        if(StringUtil.isNullString(baseUrl)){
            return;
        }

        String local_ip = SharedPreferencesUtil.getInstance().getSharePreference(SP_CONFIG_SERVICE_FILE_NAME).getString(SP_CONFIG_SERVICE_IP,"-1");
        String local_port = SharedPreferencesUtil.getInstance().getSharePreference(SP_CONFIG_SERVICE_FILE_NAME).getString(SP_CONFIG_SERVICE_PORT,"-1");

        if(!local_ip.equals("-1") && !local_port.equals("-1")){
            base_url_ip = local_ip;
            base_url_port = local_port;
        }else {
            baseUrl = baseUrl.split("//")[1].replace("/","").trim();

            setBase_url_ip(baseUrl.split(":")[0].trim());
            setBase_url_port(baseUrl.split(":")[1].trim());
        }
    }

    public String getBase_url_ip() {
        return base_url_ip;
    }

    public void setBase_url_ip(String base_url_ip) {
        this.base_url_ip = base_url_ip;
        SharedPreferencesUtil.getInstance().getSharePreference(SP_CONFIG_SERVICE_FILE_NAME).putString(SP_CONFIG_SERVICE_IP,base_url_ip);
    }

    public String getBase_url_port() {
        return base_url_port;
    }

    public void setBase_url_port(String base_url_port) {
        this.base_url_port = base_url_port;
        SharedPreferencesUtil.getInstance().getSharePreference(SP_CONFIG_SERVICE_FILE_NAME).putString(SP_CONFIG_SERVICE_PORT,base_url_port);
    }

    public String getBaseUrl(){
        return "http://" + base_url_ip + ":" + base_url_port + "/";
    }
}