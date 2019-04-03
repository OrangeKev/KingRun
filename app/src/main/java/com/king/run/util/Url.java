package com.king.run.util;

/**
 * 作者：shuizi_wade on 2017/2/11 17:33
 * 邮箱：674618016@qq.com
 */
public class Url {

    //    public static final String BASE_URL = "http://192.168.204.176:8044/";
//    public static final String BASE_URL = "http://182.254.149.193:8080/";
//    public static final String BASE_URL = "http://ok.tool8.cc:8080/speed_run/";
    public static final String BASE_URL = "http://www.paozherongyao.com/speed_run/";
//    public static final String BASE_URL = "http://ok.tool8.cc/";
//            public static final String BASE_URL = "http://192.168.204.63:8080/";
//        public static final String BASE_URL = "http://hourrun.ngrok.cc/";

    public static final String WEATHER = "https://www.sojson.com/open/api/weather/json.shtml?city=";
//    public static final String WEATHER = "http://wthrcdn.etouch.cn/weather_mini?city=";
    public static final String V1 = "v1/";
    public static final String API = "api/";
    public static final String USER = "user/";
    public static final String LOGIN = "login/";
    public static final String SMSCODE = "smsCode/";
    public static final String baseUrl = BASE_URL + "repast/v1";
    public static final String SSZHAO = "/sszhao";
    public static final String MZ = "/mz";
    public static final String USERS = "/users";
    public static final String PASSWORD = "/password";
    public static final String BANK_CARDS = "/bank_cards";
    public static final String NICK_NAME = "/nickname";
    public static final String IDENTITY = "/identity";
    public static final String SMS = "/sms";
    public static final String Send = "/send";
    public static final String MOBILES = "/mobiles/";
    public static final String DEFAULT = "/default";
    public static final String AVATAR = "/avatar";
    public static final String SALE_PASSWORDS = "/sale_passwords";
    public static final String VALIDATE = "/validate";
    public static final String HOT = "hot/";
    public static final String REGISTER = "register/";
    public static final String LOGIN_URL = BASE_URL + USER + "/v1" + USERS + "/login";
    //    public static final String LOGIN_URL = baseUrl + MZ + USERS + "/login";
    public static final String BANK_CARD_INFO_URL = baseUrl + SSZHAO + USERS;
    public static final String FEED_BACK_URL = baseUrl + SSZHAO + "/feedbacks";
    public static final String UPDATE_USER_NAME_URL = baseUrl + MZ + USERS;
    public static final String UPDATE_USER_PASSWORD_URL = baseUrl + MZ + USERS;
    public static final String CHANGE_PHONE_IDENTIFY_URL = baseUrl + MZ + USERS;
    public static final String CHANGE_PHONE_URL = baseUrl + MZ + MOBILES;
    public static final String MODIFY_TRADERS_PWD_URL = baseUrl + MZ + SALE_PASSWORDS;
    public static final String ADD_BANK_CARD_URL = baseUrl + SSZHAO + USERS;
    public static final String SET_AS_DEFAULT_URL = baseUrl + SSZHAO + USERS;
    public static final String DELETE_BANK_CARD_URL = baseUrl + SSZHAO + BANK_CARDS;
    public static final String VERIFY_TRADERS_PWD_URL = baseUrl + MZ;
    public static final String CHANGE_USER_AVATAR_URL = baseUrl + MZ + USERS;
    public static final String BUSINESS_INFO_URL = baseUrl + SSZHAO + "/merchants?user_id=";

    //    public static final String BASE_H5_BASE_URL = "https://static.kuaiyunma.com/";
    public static final String BASE_H5_BASE_URL = "http://static.yiwenyiwen.com/";
    public static final String BASE_H5_URL = BASE_H5_BASE_URL + "repast_app_web/#/";
    public static final String HOME_URL = BASE_H5_URL + "home";
    public static final String MANAGE_URL = BASE_H5_URL;
    public static final String ABOUT_URL = BASE_H5_URL + "about";
    public static final String MY_EARNINGS = BASE_H5_URL + "my/everyday";
    public static final String EDIT_AVATAR_NICKNAME = BASE_URL + "v1/api/user/edit";
    public static final String FORGET_PED_URL = BASE_URL + "v1/api/user/forget";
    public static final String MY_INFO_URL = BASE_URL + "v1/api/user/myInfo";
    public static final String Edit_INFO_URL = BASE_URL + "v1/api/user/info";
    public static final String GET_CODE_URL = BASE_URL + "v1/api/user/smsCode";
    public static final String MODIFY_PWD_URL = BASE_URL + "v1/api/user/password";
    public static final String UPLOAD_DATA_URL = BASE_URL + "v1/api/user/exerciseData";
    public static final String GET_EXERCISE_DATA_URL = BASE_URL + "v1/api/user/exerciseData";
    public static final String GET_HOT_CIRCLE_URL = BASE_URL + "v1/api/circle/hot";
    public static final String ATTENTION_URL = BASE_URL + "v1/api/circle/follow?id=";
    public static final String USER_ATTENTION_URL = BASE_URL + "v1/api/circle/follow";
    public static final String PUBLISH_URL = BASE_URL + "v1/api/circle/moment";
    public static final String HOT_CIRCLE_MAIN_URL = BASE_URL + "v1/api/circle/index";
    public static final String PAGE_CIRCLE_URL = BASE_URL + "v1/api/circle/moment";
    public static final String DELETE_MOMENT_URL = BASE_URL + "v1/api/circle/moment/del?momentId=";
    public static final String PRAISE_URL = BASE_URL + "v1/api/circle/moment/like?momentId=";
    public static final String MY_ATTENTION_USER_URL = BASE_URL + "v1/api/user/myFollow";
    public static final String ATTENTION_USER_URL = BASE_URL + "v1/api/user/follow?userId=";
    public static final String FANS_USER_URL = BASE_URL + "v1/api/user/myFollowers";
    public static final String COMMENT_URL = BASE_URL + "v1/api/circle/moment/comment/";
    public static final String USER_INFO_DETAIL_URL = BASE_URL + "v1/api/user/index/";
    public static final String CIRCLE_DETAIL_URL = BASE_URL + "v1/api/circle";
    public static final String STATE_DETAIL_URL = BASE_URL + "v1/api/circle/moment/detail";
    public static final String DELETE_COMMENT_URL = BASE_URL + "v1/api/circle/moment/comment/del?commentId=";
    public static final String STATISTICAL_LIST_DATA = BASE_URL + "v1/api/user/exerciseDetailData?page=";//获取数据列表统计
}
