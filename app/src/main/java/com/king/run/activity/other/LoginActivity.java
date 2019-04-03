package com.king.run.activity.other;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.king.run.R;
import com.king.run.activity.other.model.Login;
import com.king.run.base.BaseActivity;
import com.king.run.model.WxLoginInfo;
import com.king.run.util.PrefName;
import com.king.run.util.PreferencesUtils;
import com.king.run.util.QQConstants;
import com.king.run.util.StringUtil;
import com.king.run.util.Url;
import com.king.run.util.Utils;
import com.king.run.util.WeiBoConstants;
import com.king.run.util.WxConstants;
import com.orhanobut.logger.Logger;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：shuizi_wade on 2017/8/17 09:42
 * 邮箱：674618016@qq.com
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {
    private static final int REGISTER_CODE = 11;
    @ViewInject(R.id.et_phone)
    EditText et_phone;
    @ViewInject(R.id.et_pwd)
    EditText et_pwd;
    private boolean isVisible = false;
    @ViewInject(R.id.iv_visible)
    ImageView iv_visible;
    private String phone;
    @ViewInject(R.id.scrollview)
    ScrollView scrollView;
    private static final String WEIXIN_SCOPE = "snsapi_userinfo";
    private static final String WEIXIN_STATE = "login_state";
    private IWXAPI api;
    private SendAuth.Req req;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                String wxToken = (String) msg.obj;
                getAccess_token(wxToken);
            }
        }
    };
    private Tencent mTencent;
    private BaseUiListener listener = new BaseUiListener();
    private AuthInfo mAuthInfo;
    private SsoHandler mSsoHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleBar();
        regToWx();
        regToQQ();
        initViews();
    }

    private void regToWx() {
        api = WXAPIFactory.createWXAPI(context, WxConstants.APP_ID, true);
        api.registerApp(WxConstants.APP_ID);
    }

    private void regToQQ() {
        mTencent = Tencent.createInstance(QQConstants.APP_ID, this.getApplicationContext());
    }


    private void initViews() {
        iv_visible.setImageResource(R.mipmap.login_icon_unvisible);
        et_pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        et_phone.setText(PrefName.getMobie(context));
        Editable etable = et_phone.getText();
        Selection.setSelection(etable, etable.length());
        et_pwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    login();
                }
                return false;
            }
        });
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Utils.hideInput(context, et_pwd);
                Utils.hideInput(context, et_phone);
                return false;
            }
        });
    }

    @Event(value = {R.id.btn_login, R.id.tv_forget, R.id.tv_register, R.id.iv_visible,
            R.id.iv_weixin, R.id.iv_qq, R.id.iv_weibo})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_forget:
                jumpActvity(ForgetActivity.class);
//                jumpActvity(CompleteAvatarNickNameActivity.class);
                break;
            case R.id.tv_register:
                jumpBundleActvityforResult(RegisterActivity.class, null, REGISTER_CODE);
                break;
            case R.id.iv_visible:
                if (!isVisible) {
                    isVisible = true;
                    iv_visible.setImageResource(R.mipmap.login_icon_unvisible);
                    et_pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    isVisible = false;
                    iv_visible.setImageResource(R.mipmap.login_icon_visible);
                    et_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                Editable etable = et_pwd.getText();
                Selection.setSelection(etable, etable.length());
                break;
            case R.id.iv_weixin:
                goToWx();
                break;
            case R.id.iv_qq:
                goToQQ();
                break;
            case R.id.iv_weibo:
                goToWeiBo();
                break;
        }
    }

    private void goToWx() {
        req = new SendAuth.Req();
        req.scope = WEIXIN_SCOPE;
        req.state = WEIXIN_STATE;
        api.sendReq(req);
    }

    private void goToQQ() {
        if (!mTencent.isSessionValid()) {
            mTencent.login(this, "Scope", listener);
        }
    }

    private void goToWeiBo() {
        mSsoHandler = new SsoHandler((Activity) context);
        mSsoHandler.authorize(new SelfWbAuthListener());
    }

    private void login() {
        phone = et_phone.getText().toString().trim();
        String pwd = et_pwd.getText().toString().trim();
        if (StringUtil.isBlank(phone)) {
            et_phone.setError("手机号不能为空！");
            return;
        }
        if (phone.length() != 11) {
            et_phone.setError("手机号格式不正确！");
            return;
        }
        if (StringUtil.isBlank(pwd) || pwd.length() < 6) {
            et_pwd.setError("密码格式不正确！");
            return;
        }
        String macAddress = Utils.getDeviceId(context);
        String device = Utils.getSystemModel();
        String version = Utils.getVersion(context);
        String url = Url.BASE_URL + Url.V1 + Url.API + Url.LOGIN;
        RequestParams params = new RequestParams(url);
        Map<String, String> map = new HashMap<>();
        map.put("userName", phone);
        map.put("passWord", pwd);
        map.put("macAddress", macAddress);
        map.put("device", device);
        map.put("version", version);
        map.put("type", "ANDROID");
        map.put("loginType", "TEL");
        httpPostJSON("login", params, map);
    }

    /**
     * 获取openid accessToken值用于后期操作
     *
     * @param code 请求码
     */
    private void getAccess_token(final String code) {
        String path = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + WxConstants.APP_ID
                + "&secret="
                + WxConstants.APP_SECRET
                + "&code="
                + code
                + "&grant_type=authorization_code";
        RequestParams params = new RequestParams(path);
        httpGetWx(params, "access_token");
    }


    @Override
    public void success(String result, String type) {
        super.success(result, type);
        switch (type) {
            case "login":
                Login login = JSON.parseObject(result, Login.class);
                PreferencesUtils.putString(context, PrefName.AVATAR, login.getData().getAvator());
                PreferencesUtils.putString(context, PrefName.NICK_NAME, login.getData().getNickName());
                PreferencesUtils.putString(context, PrefName.TOKEN, login.getData().getToken());
                PreferencesUtils.putString(context, PrefName.USER_ID, login.getData().getUserId());
                PreferencesUtils.putString(context, PrefName.MOBILE, phone);
                jumpActvity(HomeActivity.class);
                finish();
                senToa(login.getMsg());
                break;
            case "access_token":
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String openid = jsonObject.getString("openid").toString().trim();
                    String access_token = jsonObject.getString("access_token").toString().trim();
                    getUserMesg(access_token, openid);
                } catch (JSONException e) {
                    e.printStackTrace();
                    senToa(e.getMessage());
                }
                break;
            case "userMsg":
                WxLoginInfo userInfo = JSON.parseObject(result.toString(), WxLoginInfo.class);
                PreferencesUtils.putString(context, PrefName.UNIONID, userInfo.getUnionid());
                PreferencesUtils.putString(context, PrefName.WX_NAME, userInfo.getNickname());
                PreferencesUtils.putString(context, PrefName.AVATAR, userInfo.getHeadimgurl());
                break;
        }
    }

    /**
     * 获取微信的个人信息
     *
     * @param access_token
     * @param openid
     */
    private void getUserMesg(final String access_token, final String openid) {
        String path = "https://api.weixin.qq.com/sns/userinfo?access_token="
                + access_token
                + "&openid="
                + openid;
        System.out.println("url:" + path);
        RequestParams params = new RequestParams(path);
        httpGet(params, "userMsg");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, listener);
        if (mSsoHandler != null)
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REGISTER_CODE:
                jumpActvity(CompleteAvatarNickNameActivity.class);
                finish();
                break;
        }
    }


    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
            getUserInfo();
        }

        @Override
        public void onError(UiError e) {
//            showResult("onError:", "code:" + e.errorCode + ", msg:"
//                    + e.errorMessage + ", detail:" + e.errorDetail);
            Log.e("onError:", "code:" + e.errorCode + ", msg:" + e.errorMessage + ", detail:" + e.errorDetail);
        }

        @Override
        public void onCancel() {
//            senToa("onCancel");
        }
    }

    public void getUserInfo() {
        UserInfo info = new UserInfo(this, mTencent.getQQToken());
        info.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object o) {
                Logger.d(o);
            }

            @Override
            public void onError(UiError uiError) {
                Logger.d(uiError);
            }

            @Override
            public void onCancel() {
                Logger.d("cancel");
            }
        });
    }


    private class SelfWbAuthListener implements WbAuthListener {
        @Override
        public void onSuccess(final Oauth2AccessToken token) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    mAccessToken = token;
                    if (token.isSessionValid()) {
                        // 显示 Token
//                        updateTokenView(false);
                        // 保存 Token 到 SharedPreferences
                        AccessTokenKeeper.writeAccessToken(LoginActivity.this, token);
                        senToa(R.string.success);
                    }
                }
            });
        }

        @Override
        public void cancel() {
            senToa("cancel");
        }

        @Override
        public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
            senToa(wbConnectErrorMessage.getErrorMessage());
        }
    }
}
