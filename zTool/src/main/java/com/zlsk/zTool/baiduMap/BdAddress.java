package com.zlsk.zTool.baiduMap;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.zlsk.zTool.baiduMap.model.AddressDetail;

import java.util.List;

/**
 * Created by IceWang on 2018/11/27.
 */

public class BdAddress {
    public interface OnAddressDetailCallBack{
        void onSuccess(AddressDetail addressDetail);
        void onFailure(String result);
    }

    private GeoCoder geoCoder;
    private OnAddressDetailCallBack onAddressDetailCallBack;
    private LatLng latLng;

    public BdAddress() {
        geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(new GeoCoderResultListener());
    }

    public void getDetailAddress(OnAddressDetailCallBack onAddressDetailCallBack, LatLng latLng){
        this.latLng = latLng;
        this.onAddressDetailCallBack = onAddressDetailCallBack;
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                .newVersion(1)
                .location(latLng));
    }

    class GeoCoderResultListener implements OnGetGeoCoderResultListener {
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

            if (reverseGeoCodeResult == null ||
                    reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR ||
                    reverseGeoCodeResult.getPoiList() == null ||
                    reverseGeoCodeResult.getPoiList().size() == 0) {
                onAddressDetailCallBack.onFailure("地址查询失败");
                return;
            }

            List<PoiInfo> poiInfoList = reverseGeoCodeResult.getPoiList();
            PoiInfo info = poiInfoList.get(0);
            String address = info.address + "(" + info.name + ")";

            ReverseGeoCodeResult.AddressComponent addressComponent = reverseGeoCodeResult.getAddressDetail();
            AddressDetail addressDetail = new AddressDetail();
            addressDetail.setCountry(addressComponent.countryName);
            addressDetail.setProvince(addressComponent.province);
            addressDetail.setCity(addressComponent.city);
            addressDetail.setDistrict(addressComponent.district);
            addressDetail.setTown(addressComponent.town);
            addressDetail.setStreet(addressComponent.street);
            addressDetail.setDetail(address);
            addressDetail.setX(latLng.latitude);
            addressDetail.setY(latLng.longitude);

            onAddressDetailCallBack.onSuccess(addressDetail);
        }
    }


}
