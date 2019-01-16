package com.zlsk.zTool.baiduMap;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.animation.ScaleAnimation;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.Overlay;
import com.zlsk.zTool.R;
import com.zlsk.zTool.baiduMap.model.MarkOverlay;
import com.zlsk.zTool.baiduMap.model.MarkPoint;
import com.zlsk.zTool.baiduMap.model.OnMapSingleConfirmCallback;
import com.zlsk.zTool.baiduMap.model.OnMarkerClickCallback;
import com.zlsk.zTool.utils.animation.AnimationUtil;
import com.zlsk.zTool.utils.list.ListUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IceWang on 2018/11/30.
 */

public class DbMapView {
    public enum MapType{VEC,IMG}

    private Context context;
    private View view;
    private LinearLayout layout_operate;
    private Map<String,MarkOverlay> markerOverlayMap = new HashMap<>();
    private Map<String,Marker> markerMap = new HashMap<>();

    public DbMapView(Context context) {
        this.context = context;
        init();
    }

    public Map<String, MarkOverlay> getMarkerOverlayMap() {
        return markerOverlayMap;
    }

    public View getView() {
        return view;
    }

    private void init(){
        view = LayoutInflater.from(context).inflate(R.layout.custom_bd_map_view,null);
        MapView map = view.findViewById(R.id.mapView);

        BdMapManager.getInstance(map);
        BdLocationManager.getInstance(context);

        layout_operate = view.findViewById(R.id.layout_operate);

        ImageView img_location = view.findViewById(R.id.img_location);
        ImageView img_zoom_in = view.findViewById(R.id.img_zoom_in);
        ImageView imageView = view.findViewById(R.id.img_zoom_out);
        TextView tv_map_type = view.findViewById(R.id.tv_map_type);
        tv_map_type.setText(BdMapManager.getInstance().getMapType() == MapType.IMG ? "卫星" : "矢量");

        MapOperate mapOperate = new MapOperate();
        img_location.setOnClickListener(mapOperate);
        img_zoom_in.setOnClickListener(mapOperate);
        imageView.setOnClickListener(mapOperate);
        tv_map_type.setOnClickListener(mapOperate);
    }

    public void onDestroy(){
        BdMapManager.getInstance().onDestroy();
        BdLocationManager.getInstance().clear();
    }


    public void onResume() {
        BdMapManager.getInstance().onResume();
    }

    public void onPause() {
        BdMapManager.getInstance().onPause();
    }

    class MapOperate implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            AnimationUtil.startScaleAnimation(v);
            int i = v.getId();
            if (i == R.id.img_location) {
                BdMapManager.getInstance().locateAndZoom(BdLocationManager.getInstance().getLocationPoint());
//                BdMapManager.getInstance().locateAndZoom(new LatLng(29.0843844893,119.6540443421));
            }else if(i == R.id.img_zoom_in){
                BdMapManager.getInstance().zoomIn();
            }else if(i == R.id.img_zoom_out){
                BdMapManager.getInstance().zoomOut();
            }else if(i == R.id.tv_map_type){
                DbMapView.MapType mapType = BdMapManager.getInstance().changeMapType();
                ((TextView)v).setText(mapType == MapType.IMG ? "卫星" : "矢量");
            }
        }
    }

    public List<String> getOverlaysNameList(){
        String[] nameArray = markerOverlayMap.keySet().toArray(new String[markerOverlayMap.keySet().size()]);
        return ListUtil.toList(nameArray);
    }

    /**
     * 刷新
     */
    public void refreshAllMarkPoint(String overlayKey,List<MarkPoint> markPointList){
        Map<String,MarkPoint> markPointMap = new HashMap<>();
        for (MarkPoint markPoint : markPointList){
            markPointMap.put(markPoint.getKey(),markPoint);
        }
        for (MarkPoint markPoint : markPointList){
            if(markerMap.containsKey(markPoint.getKey())){
                //更新
                updateMarker(markPoint.getKey(),markPoint);
            }else {
                //新增
                addMarkPoint(true,overlayKey,ListUtil.singleToList(markPoint));
            }
        }
        List<String> deleteList = new ArrayList<>();
        for (String key : markerMap.keySet()){
            if(!markPointMap.containsKey(key)){
                deleteList.add(key);
            }
        }
        for (String key : deleteList){
            //删除
            Marker marker = markerMap.get(key);
            marker.remove();
            markerMap.remove(key);
        }
    }

    /**
     * 增加标记点
     */
    public void addMarkPoint(boolean visible,String key,List<MarkPoint> markPointList){
        List<Overlay> overlayList = new ArrayList<>();

        for (MarkPoint markPoint : markPointList){
            Overlay overlay = BdMapManager.getInstance().addMark(visible,markPoint.getX(),markPoint.getY(),markPoint.getView(),markPoint.getBundle());
            overlayList.add(overlay);

            Marker marker = (Marker) overlay;
            markerMap.put(markPoint.getKey(),marker);
            setScaleAnimation(marker);
        }

        markerOverlayMap.put(key,new MarkOverlay(visible,overlayList));
    }

    public void updateMarker(String key,MarkPoint markPoint){
        Marker marker = markerMap.get(key);
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromView(markPoint.getView());
        marker.setIcon(bitmap);
        marker.setExtraInfo(markPoint.getBundle());
    }

    /**
     * 缩放动画
     */
    private void setScaleAnimation(Marker marker){
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f,0.6f,1f);
        scaleAnimation.setDuration(500);
        marker.setAnimation(scaleAnimation);
    }


    /**
     * 删除图层组中一个图层
     */
    public void deleteOverlay(String key,String singleKey){
        if(markerOverlayMap.containsKey(key)){
            MarkOverlay markOverlay = markerOverlayMap.get(key);
            List<Overlay> markerOptionsList = markOverlay.getOverlayList();
            for (Overlay overlay : markerOptionsList){
                Bundle bundle = overlay.getExtraInfo();
                String singleKeyInBundle = (String) bundle.get(MarkPoint.KEY_IN_BUNDLE);
                if(singleKeyInBundle != null && singleKeyInBundle.equals(singleKey)){
                    overlay.remove();
                }
            }
        }
    }

    /**
     * 设置图层显隐
     */
    public void setMarkPointsVisible(String key,boolean visible){
        if(markerOverlayMap.containsKey(key)){
            MarkOverlay markOverlay = markerOverlayMap.get(key);
            markOverlay.setVisible(visible);
            List<Overlay> markerOptionsList = markOverlay.getOverlayList();
            for (Overlay overlay : markerOptionsList){
                overlay.setVisible(visible);
            }
        }
    }

    /**
     * 获取图层显隐
     */
    public boolean getMarkPointsVisible(String key){
        if(markerOverlayMap.containsKey(key)){
            MarkOverlay markOverlay = markerOverlayMap.get(key);
            return markOverlay.isVisible();
        }
        return false;
    }

    /**
     * 标注点点击事件监听
     */
    public void setMarkerClickCallback(OnMarkerClickCallback onMarkerClickCallback){
        BdMapManager.getInstance().setOnMarkerClickCallback(onMarkerClickCallback);
    }

    public void setMapSingleConfirmCallback(OnMapSingleConfirmCallback onMapSingleConfirmCallback){
        BdMapManager.getInstance().setOnMapSingleConfirmCallback(onMapSingleConfirmCallback);
    }

    public void setMapType(MapType mapType){
        BdMapManager.getInstance().setMapType(mapType);
    }

    /**
     * 标注显隐
     */
    public void showPoi(boolean visible){
        BdMapManager.getInstance().showPoi(visible);
    }

    /**
     * logo显隐
     */
    public void showLogo(boolean visible){
        BdMapManager.getInstance().showLogo(visible);
    }

    /**
     * 比例尺显隐
     */
    public void showScale(boolean visible){
        BdMapManager.getInstance().showScale(visible);
    }

    /**
     * 缩放控件显隐
     */
    public void showZoomControl(boolean visible){
        BdMapManager.getInstance().showZoomControl(visible);
    }

    /**
     * 绘制行政边界
     */
    public void drawDistrict(String cityName){
        BdMapManager.getInstance().addDistrictPolyline(cityName);
    }

    /**
     * 设置地图操作按钮显隐
     */
    public void setMapOperateVisible(boolean visible){
        layout_operate.startAnimation(visible ? AnimationUtil.getRightInAnimation(context) : AnimationUtil.getRightOutAnimation(context));
        layout_operate.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * 定位
     */
    public void locateTo(double x,double y){
        BdMapManager.getInstance().locateTo(x,y);
    }

    public void showInfoWindow(View view,double x,double y){
        BdMapManager.getInstance().showInfoWindow(view,x,y);
    }

    public void closeInfoWindow(){
        BdMapManager.getInstance().closeInfoWindow();
    }
}
