package com.king.run.baidumap;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMapTouchListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.king.run.R;

import java.util.List;

public class MapManage {
    // mail: iwantate@163.com / a123456
    // baidu: iwantate@163.com / a123456
    public void setMyLocationEnabled() {
        mBaiduMap.setMyLocationEnabled(true);
    }

    public interface OnMapClick {
        void onClick(LatLng point);
    }

    private OnMapClick mOnMapClick = null;

    private GetPoiCallback mGetPoiCallback;

    public interface GetPoiCallback {
        void gotPoi(PoiResult result, int type, int error);

        void gotPoiDetail(int type, int error);
    }


    public interface GetAddressCallback {
        void gotAddress(String addr);

        void gotPoiInfo(List<PoiInfo> info);

        void gotResult(ReverseGeoCodeResult reverseGeoCodeResult);
    }

    private final static int SEARCH_SCOPE = 1000;
    public final static String LOG_TAG = "locationMap";
    PoiSearch mSearch;
    private GeoCoder mMkSearch;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    MapStatus ms;
    private LatLng currentPt = null;

    public MapManage(Context context) {
        mMkSearch = GeoCoder.newInstance();
        mSearch = PoiSearch.newInstance();
    }

    public void initMap(Activity activity, MapView mapView) {
        mMapView = mapView;
        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();
        // mMapView.regMapViewListener(mBMapMan, myMapViewListener);
        // mapView.setBuiltInZoomControls(false);
        // 设置启用内置的缩放控件
        // mMapController = mapView.getController();
        // mMapController.setZoom(16);// 设置地图zoom级别
        int childCount = mMapView.getChildCount();

        // 得到mMapView的控制权,可以用它控制和驱动平移和缩放
        // GeoPoint point = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
        // 用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
        // mMapController.setCenter(point);// 设置地图中心点

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                currentPt = point;
                if (mOnMapClick != null) {
                    mOnMapClick.onClick(currentPt);
                }
            }

            @Override
            public boolean onMapPoiClick(MapPoi arg0) {
                // TODO Auto-generated method stub
                return false;
            }

        });
    }

    public void displayLocOnMap(TextureMapView mapView, final double lat,
                                final double lng) {
        Log.i(LOG_TAG, "show local lat=" + lat + " lng=" + lng);
        // myLocationOverlay = new MyLocationOverlay(mMapView);
        // 构建MarkerOption，用于在地图上添加Marker
        LatLng ll = new LatLng(lat, lng);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 16);
        mBaiduMap.animateMapStatus(u, 100);

        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.ico_signed);
        // 构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(ll).icon(bitmap);
        // 在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }

    public void searchPoi(double lat, double lng, String key,
                          GetPoiCallback gotPoiCallback) {
        LatLng geoPoint = new LatLng(lat, lng);
        Log.i(LOG_TAG, "launchApp search poi lat=" + lat + " lng=" + lng);
        searchPoi(geoPoint, key, gotPoiCallback);
    }

    public void searchPoi(LatLng geoPoint, String key,
                          GetPoiCallback gotPoiCallback) {
        mGetPoiCallback = gotPoiCallback;
        // mMkSearch.poiSearchNearBy(key, geoPoint, SEARCH_SCOPE);
        PoiNearbySearchOption option = new PoiNearbySearchOption();
        option.keyword(key);
        option.radius(SEARCH_SCOPE);
        option.location(geoPoint);
        mSearch.searchNearby(option);
    }

    public void searchAddress(LatLng geoPoint, GetAddressCallback getAddressCallback) {
        Log.v(LOG_TAG, "launchApp address ing");
        mGetAddressCallback = getAddressCallback;
        mMkSearch.reverseGeoCode(new ReverseGeoCodeOption().location(geoPoint));
        mMkSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                mGetAddressCallback.gotAddress(reverseGeoCodeResult.getAddress());
                mGetAddressCallback.gotPoiInfo(reverseGeoCodeResult.getPoiList());
                mGetAddressCallback.gotResult(reverseGeoCodeResult);
            }
        });

    }

    /**
     * 初始化地图显示的位置，并使用默认的缩放比例
     *
     * @param lat
     * @param lng
     */
    public void initLocation(double lat, double lng) {
        Log.i(LOG_TAG, "show local lat=" + lat + " lng=" + lng);
        LatLng ll = new LatLng(lat, lng);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 18);
        mBaiduMap.animateMapStatus(u, 100);

        mBaiduMap.setMyLocationData(new MyLocationData.Builder()
                .accuracy(100f)
                .latitude(lat)
                .longitude(lng).build());
    }


    public void setMyLocationData(BDLocation location) {
        setMyLocationData(location.getLatitude(), location.getLongitude(), location.getRadius());
    }

    public void setMyLocationData(double lat, double lng, double acc) {
        Log.i(LOG_TAG, "show local lat=" + lat + " lng=" + lng + " acc=" + acc);
        LatLng ll = new LatLng(lat, lng);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
        mBaiduMap.animateMapStatus(u, 100);

        mBaiduMap.setMyLocationData(new MyLocationData.Builder()
                .accuracy((float) acc)
                .latitude(lat)
                .longitude(lng).build());
    }


    private GetAddressCallback mGetAddressCallback;
//	public void searchAddress(LatLng geoPoint,
//							  GetAddressCallback getAddressCallback) {
//		Log.i(LOG_TAG, "launchApp address ing");
//		mGetAddressCallback = getAddressCallback;
//		// mMkSearch.reverseGeocode(geoPoint);
//		mMkSearch.reverseGeoCode(new ReverseGeoCodeOption().location(geoPoint));
//		mMkSearch
//				.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
//					@Override
//					public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
//
//					}
//
//					@Override
//					public void onGetReverseGeoCodeResult(
//							ReverseGeoCodeResult reverseGeoCodeResult) {
//						mGetAddressCallback.gotAddress(reverseGeoCodeResult
//								.getValue());
//						mGetAddressCallback.gotPoiInfo(reverseGeoCodeResult
//								.getPoiList());
//					}
//				});
//
//	}

    public void searchAddress(double lat, double lng,
                              GetAddressCallback getAddressCallback) {
        LatLng geoPoint = new LatLng(lat, lng);
        searchAddress(geoPoint, getAddressCallback);
    }

    public void updateLocationSign(double lat, double lng) {
        // Drawable mark = mContext.getResources().getDrawable(
        // R.drawable.ico_signed);
        LatLng point = new LatLng(lat, lng);
        // addrPopupOverlay = new PopupOverlay(mMapView, null);
        // Bitmap popupBitmap = ImageTool.drawableToBitmapByBD(mark);
        // addrPopupOverlay.showPopup(popupBitmap, p1, 32);
        BitmapDescriptor bdGround = BitmapDescriptorFactory
                .fromResource(R.mipmap.ico_signed);
        OverlayOptions option = new MarkerOptions().position(point).icon(
                bdGround);
        mBaiduMap.addOverlay(option);

    }

    public void updateLocation(LatLng point) {
        mBaiduMap.clear();
        BitmapDescriptor bdGround = BitmapDescriptorFactory
                .fromResource(R.mipmap.ico_signed);

        OverlayOptions option = new MarkerOptions().position(point).icon(
                bdGround);

        mBaiduMap.addOverlay(option);

    }

    private GetCurrentMapCallback getCurrentMapCallback;

    public interface GetCurrentMapCallback {
        void getImagePth(String path, String msgId);
    }

    /**
     * 显示当前位置的蓝点
     */
    public void showMyLocationPoint(BDLocation location) {
        mBaiduMap.setMyLocationEnabled(true);
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(0)
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(location.getDirection())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        mBaiduMap.setMyLocationData(locData);
    }

    public void animToLocation(double lat, double lng) {
        LatLng ll = new LatLng(lat, lng);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 17);
        mBaiduMap.animateMapStatus(u, 100);
    }

    public interface OnMapMoved {
        void onMove(double lat, double lng);
    }

    public void enableMapDrag(final OnMapMoved onMapMoved) {
        mBaiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {

            @Override
            public void onMapStatusChangeStart(MapStatus arg0) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus arg0) {

            }

            @Override
            public void onMapStatusChange(MapStatus arg0) {
                if (onMapMoved != null) {
                    onMapMoved.onMove(arg0.target.latitude,
                            arg0.target.longitude);
                }
            }
        });
    }

    public void setOnTouchListener(final OnMapTouchListener onMapTouchListener) {
        mBaiduMap.setOnMapTouchListener(onMapTouchListener);
    }

    public void showInfo(Activity context, Double lat, Double lng,
                         String text) {
        View view = context.getLayoutInflater().inflate(R.layout.msg_location_info_layout, null);
        ((TextView) view.findViewById(R.id.addr_tv)).setText(text);
//        TextView textView = new TextView(context);
//        textView.setText(text);
        LatLng pt = new LatLng(lat, lng);
        BitmapDescriptor bpDes = BitmapDescriptorFactory.fromView(view);
        OverlayOptions option = new MarkerOptions().position(pt).icon(bpDes);
        mBaiduMap.addOverlay(option);
    }

    public void destroy() {
//		 try {
//		   if (mBMapMan != null) {
//		       mBMapMan.destroy();
//		       mBMapMan = null;
//		   }
//		 } catch (NullPointerException e) {
//		    e.printStackTrace();
//		 }
    }

    public void start() {
        if (mMapView != null) {
            mMapView.onResume();
        }
    }

    public void stop() {
//		 if (mBMapMan != null) {
//		     mBMapMan.stop();
//		 }
    }
}
