package com.zlsk.zTool.baiduMap;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;

public class BdLocationManager {
    private static BdLocationManager instance;
    private LocationClient locationClient;
    private Context context;
    private float direction = -1;

    public void clear(){
        instance = null;
    }

    private BdLocationManager(Context context) {
        this.context = context;
        locationClient = new LocationClient(context);
        LocationClientOption option = locationClient.getLocOption();
        option.setCoorType("bd09ll");
        option.setIsNeedLocationPoiList(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setScanSpan(1000);//实时定位
        locationClient.setLocOption(option);
        locationClient.registerLocationListener(new LocationListen());
        locationClient.start();

        addDirectionListen();
    }

    public static BdLocationManager getInstance() {
        return instance;
    }

    public static synchronized BdLocationManager getInstance(Context context) {
        if (instance == null) {
            instance = new BdLocationManager(context);
        }
        return instance;
    }

    public BDLocation getLocation(){
        BDLocation bdLocation = locationClient.getLastKnownLocation();
        if(direction != -1 && bdLocation != null){
            bdLocation.setDirection(direction);
        }
        return bdLocation;
    }

    public float getDirection() {
        return direction;
    }

    public LatLng getLocationPoint(){
        return new LatLng(getLocation().getLatitude(),getLocation().getLongitude());
    }

    public void requestLocation(){
        locationClient.requestLocation();
    }

    /**
     * 方向传感器
     */
    public void addDirectionListen(){
        SensorManager manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        manager.registerListener(new SensorEventListener() {
                                     @Override
                                     public void onSensorChanged(SensorEvent event) {
                                         direction = event.values[0];
                                     }

                                     @Override
                                     public void onAccuracyChanged(Sensor sensor, int accuracy) {

                                     }
                                 }, sensor,
                SensorManager.SENSOR_DELAY_GAME);
    }

    private boolean isFirst = true;
    class LocationListen extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            bdLocation.setDirection(direction);
            BdMapManager.getInstance().setLocationData(bdLocation);
            if(isFirst){
                BdMapManager.getInstance().locateTo(getLocationPoint());
                isFirst = false;
            }
        }
    }
}