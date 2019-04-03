package com.king.run.activity.other;

import android.os.Bundle;
import android.os.Handler;

import com.king.run.R;
import com.king.run.base.BaseActivity;
import com.king.run.util.PrefName;
import com.king.run.util.StringUtil;

import org.xutils.view.annotation.ContentView;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleBar();
        if (checkPhonePermission())
            initData();
    }

    private void initData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String token = PrefName.getToken(MainActivity.this);
                if (StringUtil.isBlank(token))
                    jumpActvity(LoginActivity.class);
                else
                    jumpActvity(HomeActivity.class);
                finish();
            }
        }, 2000);
    }

    @Override
    protected void gotMicroPhonePermissionResult(boolean isGrant) {
        super.gotMicroPhonePermissionResult(isGrant);
        if (isGrant)
            initData();
        else
            senToa("请打开当前应用拨打电话的使用权限");
    }
}
