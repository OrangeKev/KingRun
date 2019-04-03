package com.king.run.baidumap;

import android.app.Activity;
import android.util.Base64;

import org.json.JSONObject;

/**
 *
 */
public class CrodinateConvert {

    public interface OnConverted {
        void onConverted(double lat, double lng, String latlng);
    }

    public final static String URL = "http://api.map.baidu.com/ag/coord/convert";

    static String latlng = "";

    /**
     *
     * @param lat
     * @param lng
     * @param onConverted
     */
    public static void convertGoogle2Baidu(double lat, double lng,
                                           final OnConverted onConverted) {
        //from和to对应的值分别是：0真实坐标；2google坐标；4baidu坐标。
        String url = URL + "?from=2&to=4&x=" + lat + "&y=" + lng;
//        convert(url, lat, lng, onConverted);
    }

//    private static void convert(String url, double lat, double lng,
//                                final OnConverted onConverted) {
//
//        latlng = lat + "," + lng;
//        RequestListener listener = new RequestListener<JSONObject>() {
//            @Override
//            public void success(JSONObject data) {
//                ConvertResult result = JSONUtils.fromJson(data.toString(),
//                        ConvertResult.class);
//                if (result.getError() == 0) {
//                    double lat = Double.valueOf(new String(Base64.decode(
//                            result.getX(), Activity.DEFAULT_KEYS_DIALER)));
//                    double lng = Double.valueOf(new String(Base64.decode(
//                            result.getY(), Activity.DEFAULT_KEYS_DIALER)));
//                    if (onConverted != null) {
//                        onConverted.onConverted(lat, lng, latlng);
//                    }
//                } else {
//                    Util.sendToast(R.string.convert_croodinate_faile);
//                }
//            }
//
//            @Override
//            public void onError(String errorCode, String errorMessage) {
//                Util.sendToast(R.string.convert_croodinate_faile);
//            }
//        };
//        ReverseRegisterNetworkHelper helper = new ReverseRegisterNetworkHelper();
//        helper.setRequestListener(listener);
//        helper.sendGETRequestWithoutToken(url, null, BaseApplication.getContext());
//    }
}