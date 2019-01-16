package com.zlsk.zTool.utils.network;

import com.zlsk.zTool.dialog.LoadingDialog;
import com.zlsk.zTool.dialog.ZToast;
import com.zlsk.zTool.utils.event.EventBusUtil;
import com.zlsk.zTool.utils.event.RequestEvent;
import com.zlsk.zTool.utils.other.GsonUtil;
import com.zlsk.zTool.utils.string.StringUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IceWang on 2018/4/23.
 */

public class RequestUtil {
    private volatile static RequestUtil instance;

    public static RequestUtil getInstance() {
        if (instance == null) {
            synchronized (RequestUtil.class) {
                if (instance == null) {
                    instance = new RequestUtil();
                }
            }
        }
        return instance;
    }

    public void get(Object object,String url,HashMap<String,Object> params,final int eventId){
        final RequestParams requestParams = new RequestParams(url);
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                requestParams.addQueryStringParameter(entry.getKey(), entry.getValue() == null ? null : entry.getValue().toString());
            }
        }
        RequestEvent requestEvent = new RequestEvent();
        requestEvent.setEventId(eventId);
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                requestEvent.setData(result);
                requestEvent.setPassingParameters(object);
                EventBusUtil.post(requestEvent);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                EventBusUtil.post(requestEvent);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void onSuccessResponse(String result, int eventId, Class tClass){
        if(StringUtil.isNullString(result)){
            ZToast.getInstance().show("服务返回异常");
            return;
        }

        if(tClass == Object.class){
            EventBusUtil.post(new RequestEvent(eventId,result));
        }else {
            try{
                Object object = GsonUtil.toObject(result,tClass);
                EventBusUtil.post(new RequestEvent(eventId,object));
            }catch (Exception exc){
                LoadingDialog.getInstance().dismiss();
                ZToast.getInstance().show("服务返回结果解析异常 " + exc.getMessage());
            }
        }
    }

    private void onErrorResponse(String errorMsg){
        LoadingDialog.getInstance().dismiss();
        ZToast.getInstance().show("请求服务异常 " + errorMsg);
    }

    public<T> void get(final Class<T> tClass, String url, HashMap<String,Object> params, final int eventId){
        if(!NetWorkUtil.isNetworkAvailable()){
            ZToast.getInstance().show("网络不可用");
            LoadingDialog.getInstance().dismiss();
            return;
        }

        StringBuilder stringBuilder = new StringBuilder(url);
        stringBuilder.append("?");
        final RequestParams requestParams = new RequestParams(url);
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                requestParams.addQueryStringParameter(entry.getKey(), entry.getValue() == null ? null : entry.getValue().toString());
                stringBuilder.append(entry.getKey()).append("=").append(entry.getValue() == null ? "" : entry.getValue().toString()).append("&");
            }
        }
        System.out.println("ICE_URL " + stringBuilder.toString().substring(0,stringBuilder.length() - 1));

        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                onSuccessResponse(result,eventId,tClass);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                onErrorResponse(ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public<T> void post(final Class<T> tClass, String url, HashMap<String,Object> params, final int eventId){
        if(!NetWorkUtil.isNetworkAvailable()){
            ZToast.getInstance().show("网络不可用");
            LoadingDialog.getInstance().dismiss();
            return;
        }

        StringBuilder stringBuilder = new StringBuilder(url);
        stringBuilder.append("?");
        final RequestParams requestParams = new RequestParams(url);
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                requestParams.addQueryStringParameter(entry.getKey(), entry.getValue() == null?null:entry.getValue().toString());
                stringBuilder.append(entry.getKey()).append("=").append(entry.getValue() == null ? "" : entry.getValue().toString()).append("&");
            }
        }
        System.out.println("ICE_URL " + stringBuilder.toString().substring(0,stringBuilder.length() - 1));

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                onSuccessResponse(result,eventId,tClass);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                onErrorResponse(ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public<T> void upload(final Class<T> tClass,String url, String[] fileName, HashMap<String,Object> params,final int eventId){
        if(!NetWorkUtil.isNetworkAvailable()){
            ZToast.getInstance().show("网络不可用");
            LoadingDialog.getInstance().dismiss();
            return;
        }

        RequestParams requestParams = new RequestParams(url);
        requestParams.setMultipart(true);

        requestParams.addBodyParameter("title", "images");
        for(String file:fileName){
            requestParams.addBodyParameter("file", new File(file), "multipart/form-data");
        }
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                requestParams.addQueryStringParameter(entry.getKey(), entry.getValue().toString());
            }
        }

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                onSuccessResponse(result,eventId,tClass);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                onErrorResponse(ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public void download(String url, String filePath, final IDownloadCallback iDownloadCallback){
        if(!NetWorkUtil.isNetworkAvailable()){
            ZToast.getInstance().show("网络不可用");
            return;
        }

        RequestParams entity = new RequestParams(url);
        entity.setSaveFilePath(filePath);
        x.http().get(entity, new Callback.ProgressCallback<File>() {
            @Override
            public void onSuccess(File result) {
                iDownloadCallback.onSuccess(result);
                ZToast.getInstance().show("onSuccess");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                iDownloadCallback.onError(ex.getMessage());
                ZToast.getInstance().show("网络错误，下载失败 " + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                ZToast.getInstance().show("下载完成");
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                iDownloadCallback.onLoading(total,current,isDownloading);
            }
        });
    }
}
