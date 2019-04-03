package com.king.run.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.base.BaseActivity;
import com.king.run.util.PrefName;
import com.king.run.util.Url;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_safety_confirm)
public class SafetyConfirmActivity extends BaseActivity {
    @ViewInject(R.id.tv_phone)
    TextView tv_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
    }

    private void initViews() {
        title_tv_title.setText(R.string.safetyconfirm);
        String phone = PrefName.getMobie(context);
        String str = "+86" + phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4, phone.length());
        tv_phone.setText(str);
    }

    @Event(value = {R.id.btn_send})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                RequestParams params = new RequestParams(Url.GET_CODE_URL);
                params.addBodyParameter("telNum", PrefName.getMobie(context));
                httpGet(params, "getCode");
                break;
        }
    }

    @Override
    public void success(String result, String type) {
        super.success(result, type);
        jumpActvity(InputVerifyCodeActivity.class);
        finish();
    }
}
