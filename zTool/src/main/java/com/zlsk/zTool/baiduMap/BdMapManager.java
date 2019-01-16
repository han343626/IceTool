package com.zlsk.zTool.baiduMap;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.district.DistrictSearch;
import com.baidu.mapapi.search.district.DistrictSearchOption;
import com.zlsk.zTool.R;
import com.zlsk.zTool.baiduMap.model.AddressDetail;
import com.zlsk.zTool.baiduMap.model.OnMapSingleConfirmCallback;
import com.zlsk.zTool.baiduMap.model.OnMarkerClickCallback;
import com.zlsk.zTool.dialog.ZToast;
import com.zlsk.zTool.utils.CommonToolUtil;
import com.zlsk.zTool.utils.other.GsonUtil;
import com.zlsk.zTool.utils.other.TransPixelUtil;

import java.util.List;

public class BdMapManager {
    public static final String BUNDLE_KEY = "bundle_key";

    private static BdMapManager instance;
    private MapView mapView;
    private BaiduMap baiduMap;
    private boolean isFirstLocate = true;

    private OnMarkerClickCallback onMarkerClickCallback;
    private OnMapSingleConfirmCallback onMapSingleConfirmCallback;

    private Overlay locateOverlay;
    private  BitmapDescriptor bitmap;

    public void setOnMarkerClickCallback(OnMarkerClickCallback onMarkerClickCallback) {
        this.onMarkerClickCallback = onMarkerClickCallback;
    }

    public void setOnMapSingleConfirmCallback(OnMapSingleConfirmCallback onMapSingleConfirmCallback) {
        this.onMapSingleConfirmCallback = onMapSingleConfirmCallback;
    }

    private BdMapManager(MapView mapView) {
        this.mapView = mapView;
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        baiduMap.setTrafficEnabled(false);
        baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,true,null));
        baiduMap.setOnMapClickListener(new OnMapClick());
        baiduMap.setOnMarkerClickListener(new OnMarkClick());

        baiduMap.setOnMapLoadedCallback(() -> {
            int x =  TransPixelUtil.dip2px(CommonToolUtil.getInstance().getContext(),15);
            int y = mapView.getHeight() - mapView.getScaleControlViewHeight();
            mapView.setScaleControlPosition(new Point(x, y));

//            int zoomX = mapView.getWidth() - TransPixelUtil.dip2px(CommonToolUtil.getInstance().getContext(),50);
//            int zoomY = mapView.getHeight() - TransPixelUtil.dip2px(CommonToolUtil.getInstance().getContext(),120);
//            mapView.setZoomControlsPosition(new Point(zoomX,zoomY));

//            BdMapManager.getInstance().locateAndZoom(new LatLng(29.0843844893,119.6540443421));
        });

        showPoi(false);
        showLogo(false);
        showScale(true);
        showZoomControl(false);
        showCompass(true);
    }

    public void onDestroy(){
        mapView.destroyDrawingCache();
        mapView.onDestroy();
        instance = null;
    }

    protected void onResume() {
        mapView.onResume();
    }

    protected void onPause() {
        mapView.onPause();
    }

    public static BdMapManager getInstance() {
        return instance;
    }

    public static synchronized BdMapManager getInstance(MapView mapView) {
        if (instance == null) {
            instance = new BdMapManager(mapView);
        }
        return instance;
    }

    /**
     * 标注显隐
     */
    public void showPoi(boolean visible){
        baiduMap.showMapPoi(visible);
    }

    /**
     * logo显隐
     */
    public void showLogo(boolean visible){
        View child = mapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
    }

    /**
     * 比例尺显隐
     */
    public void showScale(boolean visible){
        mapView.showScaleControl(visible);
    }

    /**
     * 缩放控件显隐
     */
    public void showZoomControl(boolean visible){
        mapView.showZoomControls(visible);
    }

    /**
     * 指南针显隐
     */
    public void showCompass(boolean visible){
        baiduMap.getUiSettings().setCompassEnabled(visible);
    }

    /**
     * 定位到指定点
     */
    public void locateTo(double lat,double lon){
        LatLng desLatLng = new LatLng(lat,lon);
        locateTo(desLatLng);
    }

    /**
     * 定位到指定点
     */
    public void locateTo(LatLng desLatLng){
        MapStatus.Builder builder = new MapStatus.Builder();
        if(isFirstLocate){
            builder.zoom(baiduMap.getMaxZoomLevel() - 2);
            isFirstLocate = false;
        }
        builder.target(desLatLng);
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    /**
     * 定位并放大
     */
    public void locateAndZoom(LatLng desLatLng){
        MapStatus.Builder builder = new MapStatus.Builder();
        int currentZoom = (int) Math.floor(baiduMap.getMapStatus().zoom);
        if(currentZoom < baiduMap.getMaxZoomLevel() - 2){
            builder.zoom(baiduMap.getMaxZoomLevel() - 2);
        }
        builder.target(desLatLng);
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    public void zoomIn(){
        int currentZoom = (int) Math.floor(baiduMap.getMapStatus().zoom);
        if(currentZoom < baiduMap.getMaxZoomLevel()){
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.zoom(currentZoom + 1);
            baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }else {
            ZToast.getInstance().show("已达到最大级别");
        }
    }

    public void zoomOut(){
        int currentZoom = (int) Math.floor(baiduMap.getMapStatus().zoom);
        if(currentZoom > baiduMap.getMinZoomLevel()){
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.zoom(currentZoom - 1);
            baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }else {
            ZToast.getInstance().show("已达到最小级别");
        }
    }

    /**
     * 设置定位点数据
     */
    public void setLocationData(BDLocation bdLocation){
        MyLocationData data = new MyLocationData.Builder()
                .accuracy(bdLocation.getRadius())
                .direction(bdLocation.getDirection())
                .latitude(bdLocation.getLatitude())
                .longitude(bdLocation.getLongitude())
                .build();
        baiduMap.setMyLocationData(data);
    }

    public void addLocationIcon(){
        View view = LayoutInflater.from(CommonToolUtil.getInstance().getContext()).inflate(R.layout.view_locate_point,null);
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromView(view);
        MarkerOptions option = new MarkerOptions()
                .position(BdLocationManager.getInstance().getLocationPoint())
                .icon(bitmap)
                .perspective(true)
                .rotate(BdLocationManager.getInstance().getDirection());

        if(locateOverlay != null){
            locateOverlay.remove();
        }
        locateOverlay = baiduMap.addOverlay(option);
    }

    /**
     * 设置底图类型
     */
    public void setMapType(DbMapView.MapType mapType){
        switch (mapType){
            case IMG:
                baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
            case VEC:
                baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;
        }
    }

    public DbMapView.MapType getMapType(){
        return (baiduMap.getMapType() == BaiduMap.MAP_TYPE_SATELLITE) ? DbMapView.MapType.IMG : DbMapView.MapType.VEC;
    }

    public DbMapView.MapType changeMapType(){
        DbMapView.MapType mapType = (baiduMap.getMapType() == BaiduMap.MAP_TYPE_SATELLITE) ? DbMapView.MapType.VEC : DbMapView.MapType.IMG;
        setMapType(mapType);
        return mapType;
    }

    /**
     * 添加带文字的点标注
     */
    public Overlay addMark(boolean visible,LatLng point,View view,Bundle bundle){
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromView(view);

        MarkerOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap)
                .perspective(true)
                .extraInfo(bundle)
                .visible(visible);

        return  baiduMap.addOverlay(option);
    }

    /**
     * 添加带文字的点标注
     */
    public Overlay addMark(boolean visible,double lat,double lon,View view,Bundle bundle){
        LatLng point = new LatLng(lat, lon);
        return addMark(visible,point,view,bundle);
    }

    /**
     * 绘制行政区域边界
     */
    public void addDistrictPolyline(String cityName){
        DistrictSearch districtSearch = DistrictSearch.newInstance();
        districtSearch.setOnDistrictSearchListener(districtResult -> addPolylineMark(districtResult.getPolylines()));
        districtSearch.searchDistrict(new DistrictSearchOption().cityName(cityName));
    }

    public void addPolylineMark(List<List<LatLng>> polylines){
        if(polylines == null || polylines.size() == 0){
            return;
        }

        for (List<LatLng> polyline : polylines){
            PolygonOptions polygonOptions = new PolygonOptions().points(polyline).stroke(new Stroke(5, Color.RED)).fillColor(Color.TRANSPARENT);
            baiduMap.addOverlay(polygonOptions);
        }
    }

    public void showInfoWindow(View view,double x,double y){
        InfoWindow infoWindow = new InfoWindow(view,new LatLng(x,y),-100);
        baiduMap.showInfoWindow(infoWindow);
    }

    public void closeInfoWindow(){
        baiduMap.hideInfoWindow();
    }

    class OnMapClick implements BaiduMap.OnMapClickListener{
        @Override
        public void onMapClick(LatLng latLng) {
            new BdAddress().getDetailAddress(new BdAddress.OnAddressDetailCallBack() {
                @Override
                public void onSuccess(AddressDetail addressDetail) {
                    baiduMap.hideInfoWindow();
                    String json = GsonUtil.toJson(addressDetail);
                    System.out.println("ICE_FLAG \n" + latLng.toString() + "\n" + json);
                    if(onMapSingleConfirmCallback != null){
                        onMapSingleConfirmCallback.callback(addressDetail);
                    }
                }

                @Override
                public void onFailure(String result) {
                    ZToast.getInstance().show(result);
                    if(onMapSingleConfirmCallback != null){
                        onMapSingleConfirmCallback.callback(null);
                    }
                }
            },latLng);
        }

        @Override
        public boolean onMapPoiClick(MapPoi mapPoi) {
            return false;
        }
    }

    class OnMarkClick implements BaiduMap.OnMarkerClickListener{
        @Override
        public boolean onMarkerClick(Marker marker) {
//            marker.startAnimation();
            if(onMarkerClickCallback != null){
                onMarkerClickCallback.callback(marker.getExtraInfo());
            }
            return false;
        }
    }
}