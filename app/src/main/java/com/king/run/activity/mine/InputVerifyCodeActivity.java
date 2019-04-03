package com.king.run.activity.mine;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.base.BaseActivity;
import com.king.run.util.PrefName;
import com.king.run.util.Url;
import com.king.run.view.CodeCountDownTimer;
import com.king.run.view.VerifycationCodeView;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.activity_input_verify_code)
public class InputVerifyCodeActivity extends BaseActivity {
    @ViewInject(R.id.verifycation_code_view)
    VerifycationCodeView verifycationCodeView;
    private String input = "";
    private CodeCountDownTimer timer;
    @ViewInject(R.id.tv_seconds_send_again)
    TextView tv_code_again;
    @ViewInject(R.id.tv_has_send)
    TextView tv_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
    }

    private void initViews() {
        String phone = PrefName.getMobie(context);
        String str = "+86" + phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4, phone.length());
        tv_phone.setText(String.format(context.getResources().getString(R.string.has_send), str));
        timer = new CodeCountDownTimer(60000, 1000, tv_code_again, context);
        timer.start();
        title_tv_title.setText(R.string.safetyconfirm);
        verifycationCodeView.setAutoCommitListener(new VerifycationCodeView.AutoCommitListener() {
            @Override
            public void autoCommit(String inputText) {
                input = inputText;
            }
        });
    }

    @Event(value = {R.id.btn_sure,R.id.tv_seconds_send_again})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_sure:
                RequestParams params = new RequestParams(Url.GET_CODE_URL);
                Map<String, String> map = new HashMap<>();
                map.put("telphone", PrefName.getMobie(context));
                map.put("smsCode", input);
                httpPostJSON("code", params, map);
                break;
            case R.id.tv_seconds_send_again:
                timer.start();
                break;
        }
    }

    @Override
    public void success(String result, String type) {
        super.success(result, type);
        jumpActvity(ModifyLoginPwdActivity.class);
        finish();
    }
}
