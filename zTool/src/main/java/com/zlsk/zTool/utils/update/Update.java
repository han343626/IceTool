package com.zlsk.zTool.utils.update;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.zlsk.zTool.dialog.LoadingDialog;
import com.zlsk.zTool.dialog.UpdateDialog;
import com.zlsk.zTool.dialog.ZToast;
import com.zlsk.zTool.model.other.UpdateInformationModel;
import com.zlsk.zTool.model.other.UpdateModel;
import com.zlsk.zTool.utils.event.EventBusUtil;
import com.zlsk.zTool.utils.event.RequestEvent;
import com.zlsk.zTool.utils.network.IDownloadCallback;
import com.zlsk.zTool.utils.network.NetWorkUtil;
import com.zlsk.zTool.utils.network.RequestUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * APP更新
 */
public class Update {
    public static final int EVENT_ID_CONNECT_TEST = 9999;
    public static final int EVENT_ID_CHECK_UPDATE = 10000;

    private static Update instance;
    private Context context;

    private String xmlUrl,appUrl,appFile;
    private int localVersionCode;
    private NoUpdateCallback noUpdateCallback;

    public interface NoUpdateCallback{
        void callback();
    }

    private Update(Context context) {
        EventBusUtil.register(this);
        this.context = context;
    }

    public static synchronized Update getInstance(Context context) {
        if (instance == null) {
            instance = new Update(context);
        }
        return instance;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(RequestEvent event){
        switch (event.getEventId()){
            case EVENT_ID_CONNECT_TEST:
                if(event.getTarget() == Update.class){
                    boolean isOk = (boolean) event.getData();
                    if(isOk){
                        LoadingDialog.getInstance().show("检查更新");
                        RequestUtil.getInstance().get(UpdateModel.class,xmlUrl,new HashMap<>(),EVENT_ID_CHECK_UPDATE);
                    }else {
                        noUpdateCallback.callback();
                    }
                }
                break;
            case EVENT_ID_CHECK_UPDATE:
                LoadingDialog.getInstance().dismiss();
                onXmlCallback((UpdateModel) event.getData());
                break;
        }
    }

    public Update setParams(String xmlUrl, String appUrl, String appFile, int localVersionCode, NoUpdateCallback noUpdateCallback){
        this.xmlUrl = xmlUrl;
        this.appUrl = appUrl;
        this.appFile = appFile;
        this.localVersionCode = localVersionCode;
        this.noUpdateCallback = noUpdateCallback;
        return instance;
    }

    public void checkUpdate(){
        //首先判断服务端是否有更新日志XML
        NetWorkUtil.connectionTest(Update.class, xmlUrl, EVENT_ID_CONNECT_TEST);
    }

    /**
     * 解析服务端更新日志xml
     */
    public void onXmlCallback(UpdateModel updateModel){
        LoadingDialog.getInstance().dismiss();
        int versionCode = Integer.parseInt(updateModel.version_code);
        if(localVersionCode < versionCode){
            update(updateModel.version_name,updateModel.size,updateModel.updateInformation);
        }else {
            noUpdateCallback.callback();
        }
    }

    private void update(String versionName,String size,List<UpdateInformationModel> modelList){
        File file = new File(appFile);
        if(file.exists()){
            file.delete();
        }
        new UpdateDialog(context, versionName + " 更新详情",size, modelList, ok -> {
            if(ok){
                LoadingDialog.getInstance().show("更新包下载");
                RequestUtil.getInstance().download(appUrl, appFile, new IDownloadCallback() {
                    @Override
                    public void onSuccess(File file) {
                        LoadingDialog.getInstance().dismiss();
                        install(file);
                    }

                    @Override
                    public void onError(String msg) {
                        LoadingDialog.getInstance().dismiss();
                        ZToast.getInstance().show("更新包下载失败");
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isDownloading) {
                        int progress = (int)((current * 100)/total) ;
                        LoadingDialog.getInstance().update("更新包下载 " + size + "(" + progress + "%)");
                    }
                });
            }else {
                noUpdateCallback.callback();
            }
        }).show();
    }

    /**
     * 跳到app安装
     */
    private void install(File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }
}