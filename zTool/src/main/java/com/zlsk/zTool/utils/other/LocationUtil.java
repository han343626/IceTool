package com.zlsk.zTool.utils.other;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.zlsk.zTool.customControls.base.LocGeoPoint;
import com.zlsk.zTool.utils.list.ListUtil;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class LocationUtil {
    private final static long DEFAULT_MIN_TIME = 1000;
    private final static float DEFAULT_MIN_DISTANCE = 1;

    private static LocationUtil mInstance = null;

    private Context ctx = null;
    private LocationManager locMgr = null;
    private LocationListener locListener = null;
    private long minTime = DEFAULT_MIN_TIME;
    private float minDistance = DEFAULT_MIN_DISTANCE;
    private boolean isUsingGps = false;
    private boolean isUsingNetwork = false;

    private GeoCoder mSearch;
    private onAddressDetailCallBack onAddressDetailCallBack;

    private LocationListener gpsListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                stopNetworkLocation();
                if (locListener != null) {
                    locListener.onLocationChanged(location);
                }
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (status == LocationProvider.OUT_OF_SERVICE) {
                startNetworkLocation();
            }
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private LocationListener networkListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (locListener != null) {
                locListener.onLocationChanged(location);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            System.out.println(provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            System.out.println(provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            System.out.println(provider);
        }
    };

    public synchronized static LocationUtil getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new LocationUtil(context);
        }
        return mInstance;
    }

    private LocationUtil(Context context) {
        this.ctx = context;
        locMgr = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        startLocation();
        SDKInitializer.initialize(context.getApplicationContext());
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                if(onAddressDetailCallBack == null){
                    return;
                }
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                if(onAddressDetailCallBack == null){
                    return;
                }

                if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR || ListUtil.isEmpty(reverseGeoCodeResult.getPoiList())) {
                    onAddressDetailCallBack.onFailure("地址查询失败");
                    return;
                }

                if (!ListUtil.isEmpty(reverseGeoCodeResult.getPoiList())){
                    PoiInfo info = reverseGeoCodeResult.getPoiList().get(0);
                    String address = info.address + "(" + info.name + ")";
                    onAddressDetailCallBack.onSuccess(address);
                }
            }
        });
    }

    public long getMinTime() {
        return minTime;
    }

    public void setMinTime(long minTime) {
        this.minTime = minTime;
    }

    public float getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(float minDistance) {
        this.minDistance = minDistance;
    }

    public void setListener(LocationListener listner) {
        locListener = listner;
    }

    /**
     * 优先获取GPS定位数据
     */
    @SuppressLint("MissingPermission")
    public Location getLastKnownLocation() {
        Location location = null;
        if(locMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            location = locMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }else if(locMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            location = locMgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        return location;
    }

    public List<String> getAvailableProvides() {
        if (locMgr == null) {
            return null;
        }
        return locMgr.getProviders(true);
    }

    public boolean isGpsAvailable() {
        List<String> ap = getAvailableProvides();
        if (ap == null) {
            return false;
        }
        return ap.contains(LocationManager.GPS_PROVIDER);
    }

    public boolean isNetworkAvailable() {
        List<String> ap = getAvailableProvides();
        if (ap == null) {
            return false;
        }
        return ap.contains(LocationManager.NETWORK_PROVIDER);
    }
    public boolean startLocation(){
        if(!startGpsLocation()){
            return startNetworkLocation();
        }
        return true;
    }
    public void stopLocation(){
        stopGpsLocation();
        stopNetworkLocation();
    }

    @SuppressLint("MissingPermission")
    private boolean startGpsLocation() {
        if (isGpsAvailable()) {
            if (!isUsingGps) {
                locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);
            }
            isUsingGps = true;
            return true;
        }
        return false;
    }
    @SuppressLint("MissingPermission")
    private void stopGpsLocation(){
        if(isUsingGps){
            locMgr.removeUpdates(gpsListener);
            isUsingGps = false;
        }
    }

    @SuppressLint("MissingPermission")
    private boolean startNetworkLocation() {
        if (isNetworkAvailable()) {
            if (!isUsingNetwork) {
                locMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, networkListener);
            }
            isUsingNetwork = true;
            return true;
        }
        return false;
    }

    @SuppressLint("MissingPermission")
    private void stopNetworkLocation(){
        if(isUsingNetwork){
            locMgr.removeUpdates(networkListener);
            isUsingNetwork = false;
        }
    }

    /**
     * 获取地址详情
     */
    public void getAddressDetail(final onAddressDetailCallBack onAddressDetailCallBack){
        this.onAddressDetailCallBack = onAddressDetailCallBack;
        Location location = getLastKnownLocation();
        if(location == null){
            onAddressDetailCallBack.onFailure("地址查询失败");
            return;
        }
        LocGeoPoint point = new LocGeoPoint(location.getLongitude(),location.getLatitude(),0);
        onlineTrans2Baidu(point.x, point.y);
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String res = (String) msg.obj;
            if(res == null){
                onAddressDetailCallBack.onFailure("地址查询失败");
            }else {
                try {
                    getBaiduGpsPointByGps(res);
                } catch (Exception e) {
                    onAddressDetailCallBack.onFailure("地址查询失败");
                }
            }
            return true;
        }
    });

    public interface onAddressDetailCallBack{
        void onSuccess(String result);
        void onFailure(String result);
    }

    private void getBaiduGpsPointByGps(String res) throws Exception {
        JSONObject jsonObj = new JSONObject(res);
        if (jsonObj.has("error") && jsonObj.getInt("error") > 0) {
            throw new Exception("转换GPS坐标失败");
        }
        String sx = jsonObj.getString("x");
        String sy = jsonObj.getString("y");
        double gpsX = Double.valueOf(getFromBase64(sx));
        double gpsY = Double.valueOf(getFromBase64(sy));

        LocGeoPoint pRet = new LocGeoPoint();
        pRet.x = gpsX;
        pRet.y = gpsY;

        LatLng ptCenter = new LatLng(pRet.y, pRet.x);
        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                .location(ptCenter));
    }

    private void onlineTrans2Baidu(final double x, final double y) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String baseUrl = "http://api.map.baidu.com/ag/coord/convert?from=0&to=4";
                baseUrl += "&x=" + x;
                baseUrl += "&y=" + y;

                String result = "";
                try {
                    URL url = new URL(baseUrl);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoOutput(true);
                    con.setRequestMethod("GET");
                    con.connect();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String result1;
                    while ((result1 = reader.readLine()) != null) {
                        result += result1;
                    }
                    reader.close();
                    con.disconnect();
                } catch (Exception e) {
                    result = "";
                }

                Message message = new Message();
                message.obj = result;
                handler.sendMessage(message);
            }
        }).start();
    }

    public static String getFromBase64(String s) {
        try {
            byte[] b = Base64.decode(s, Base64.DEFAULT);
            return new String(b);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
