package com.king.run.activity.other;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.king.run.R;
import com.king.run.activity.other.model.Register;
import com.king.run.base.BaseActivity;
import com.king.run.util.MyCountDownTimer;
import com.king.run.util.PrefName;
import com.king.run.util.PreferencesUtils;
import com.king.run.util.StringUtil;
import com.king.run.util.Url;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：shuizi_wade on 2017/8/17 10:24
 * 邮箱：674618016@qq.com
 */
@ContentView(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {
    @ViewInject(R.id.et_code)
    EditText et_code;
    @ViewInject(R.id.et_phone)
    EditText et_phone;
    @ViewInject(R.id.et_pwd)
    EditText et_pwd;
    private MyCountDownTimer countDownTimer;
    @ViewInject(R.id.btn_code)
    Button btn_code;
    @ViewInject(R.id.tv_second)
    TextView tv_second;
    private boolean isVisible = false;
    @ViewInject(R.id.iv_show)
    ImageView iv_visible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
    }

    private void initViews() {
        iv_visible.setImageResource(R.mipmap.login_icon_unvisible);
        countDownTimer = new MyCountDownTimer(context, 60000, 1000, btn_code, tv_second);
        title_tv_back.setText(R.string.cancel);
        title_iv_back.setVisibility(View.GONE);
        title_tv_title.setText(R.string.new_register);
    }

    @Event(value = {R.id.btn_code, R.id.btn_register, R.id.iv_show})
    private void getEvent(View view) {
        String phone = et_phone.getText().toString().trim();
        switch (view.getId()) {
            case R.id.btn_code:
                if (StringUtil.isBlank(phone)) {
                    et_phone.setError("手机号不能为空！");
                    return;
                }
                if (phone.length() != 11) {
                    et_phone.setError("手机号格式不正确！");
                    return;
                }
                tv_second.setVisibility(View.VISIBLE);
                btn_code.setVisibility(View.GONE);
                countDownTimer.start();
                String url = Url.BASE_URL + Url.V1 + Url.API + Url.USER + Url.SMSCODE;
                RequestParams params = new RequestParams(url);
                params.addBodyParameter("telNum", phone);
                httpGet(params, "code");
                break;
            case R.id.btn_register:
                String code = et_code.getText().toString().trim();
                String pwd = et_pwd.getText().toString().trim();
                if (StringUtil.isBlank(phone)) {
                    et_phone.setError("手机号不能为空！");
                    return;
                }
                if (phone.length() != 11) {
                    et_phone.setError("手机号格式不正确！");
                    return;
                }
                if (StringUtil.isBlank(code)) {
                    et_code.setError("验证码不能为空！");
                    return;
                }
                if (code.length() != 6) {
                    et_code.setError("验证码格式不正确！");
                    return;
                }
                if (StringUtil.isBlank(pwd) || pwd.length() < 6) {
                    et_pwd.setError("密码格式不正确！");
                    return;
                }
                String urlLogin = Url.BASE_URL + Url.V1 + Url.API + Url.USER + Url.REGISTER;
                Map<String, String> map = new HashMap<>();
                map.put("telephone", phone);
                map.put("smsCode", code);
                map.put("password", pwd);
                RequestParams paramsLogin = new RequestParams(urlLogin);
                httpPostJSON("register", paramsLogin, map);
                break;
            case R.id.iv_show:
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
        }
    }

    @Override
    public void success(String result, String type) {
        super.success(result, type);
        if (type.equals("register")) {
            Register register = JSONObject.parseObject(result, Register.class);
            if (register.getCode() == 0) {
                PreferencesUtils.putString(context, PrefName.TOKEN, register.getData().getToken());
                PreferencesUtils.putString(context, PrefName.USER_ID, register.getData().getUserId());
                setResult(RESULT_OK);
                finish();
            } else
                senToa(register.getMsg());
        } else if (type.equals("code")){}
//            senToa(result);
    }
}
