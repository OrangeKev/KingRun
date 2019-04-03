package com.king.run.util;

import android.content.Context;

/**
 * 作者：shuizi_wade on 2017/3/2 15:12
 * 邮箱：674618016@qq.com
 */
public class PrefName {
    public final static String MY_PREF = "repast";
    public final static String TOKEN_TYPE = "token_type";
    public final static String ACCESS_TOKEN = "access_token";
    public final static String TOKEN = "token";
    public final static String LOGIN_NAME = "login_name";
    public final static String NICK_NAME = "nickname";
    public final static String REAL_NAME = "realname";
    public final static String AVATAR = "avatar";
    public final static String USER_ID = "id";
    public final static String NAME = "name";
    public final static String SYS_NAME = "sys_name";
    public final static String ROLE_TYPE = "role_type";
    public final static String SCREEN_HEIGHT = "screen_height";
    public final static String SCREEN_WIDTH = "screen_width";
    public final static String MOBILE = "mobile";
    public final static String LOGIN = "login";
    public final static String MERCHANT_ID = "merchant_id";
    public final static String PREF_LAST_LAT = "last_lat";
    public final static String PREF_LAST_LNG = "last_lng";
    public final static String SEX = "sex";
    public final static String HEIGHT = "height";
    public final static String WEIGHT = "weight";
    public final static String UNIONID = "Unionid";
    public final static String WX_NAME = "wx_name";
    public final static String TRAIN_SWITCH = "train_switch";
    public final static String VOICE_SETTING = "voice_setting";
    public final static String BLUETOOTH_CONNECT_ACTION = "com.example.broadcasttest.LOCAL_BROADCAST";
    public static String getUnionid(Context context) {
        return PreferencesUtils.getString(context, UNIONID);
    }

    public static int getVoiceSetting(Context context) {
        return PreferencesUtils.getInt(context, VOICE_SETTING, 0);
    }

    public static String getWxName(Context context) {
        return PreferencesUtils.getString(context, WX_NAME);
    }

    public static boolean getTrainSwitch(Context context) {
        return PreferencesUtils.getBoolean(context, TRAIN_SWITCH, false);
    }

    public static String getPrefLastLat(Context context) {
        return PreferencesUtils.getString(context, PREF_LAST_LAT);
    }

    public static String getPrefLastLng(Context context) {
        return PreferencesUtils.getString(context, PREF_LAST_LNG);
    }

    public static String getToken(Context context) {
        return PreferencesUtils.getString(context, TOKEN);
    }

    public static String getMobie(Context context) {
        return PreferencesUtils.getString(context, MOBILE);
    }

    public static String getAccessToken(Context context) {
        return PreferencesUtils.getString(context, ACCESS_TOKEN);
    }

    public static String getLogin(Context context) {
        return PreferencesUtils.getString(context, LOGIN);
    }

    public static String getNickName(Context context) {
        return PreferencesUtils.getString(context, NICK_NAME);
    }

    public static String getRealName(Context context) {
        return PreferencesUtils.getString(context, REAL_NAME);
    }

    public static String getAVATAR(Context context) {
        return PreferencesUtils.getString(context, AVATAR);
    }

    public static int getMerchantId(Context context) {
        return PreferencesUtils.getInt(context, MERCHANT_ID);
    }

    public static String getUserId(Context context) {
        return PreferencesUtils.getString(context, USER_ID);
    }

    public static int getSex(Context context) {
        return PreferencesUtils.getInt(context, SEX);
    }

    public static String getNAME(Context context) {
        return PreferencesUtils.getString(context, NAME);
    }

    public static String getSysName(Context context) {
        return PreferencesUtils.getString(context, SYS_NAME);
    }

    public static String getHeight(Context context) {
        return PreferencesUtils.getString(context, HEIGHT);
    }

    public static String getWeight(Context context) {
        return PreferencesUtils.getString(context, WEIGHT, 60 + "");
    }

    public static String getRoleType(Context context) {
        return PreferencesUtils.getString(context, ROLE_TYPE);
    }

    public static String getLoginName(Context context) {
        return PreferencesUtils.getString(context, LOGIN_NAME);
    }

    public static void remove(String key, Context context) {
        PreferencesUtils.getPref(context).edit().remove(key).commit();
    }
}
