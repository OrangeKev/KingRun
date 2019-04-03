package com.king.run.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.king.run.R;
import com.king.run.base.BaseActivity;
import com.king.run.model.BaseResult;
import com.king.run.util.PrefName;
import com.king.run.util.StringUtil;
import com.king.run.util.Url;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.activity_modify_login_pwd)
public class ModifyLoginPwdActivity extends BaseActivity {
    @ViewInject(R.id.et_old_pwd)
    EditText et_old_pwd;
    @ViewInject(R.id.et_new_pwd)
    EditText et_new_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
    }

    private void initViews() {
        title_tv_title.setText(R.string.modify_pwd);
    }

    @Event(value = {R.id.btn_sure})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_sure:
                String oldPwd = et_old_pwd.getText().toString().trim();
                String newPwd = et_new_pwd.getText().toString().trim();
                if (StringUtil.isBlank(oldPwd)) {
                    et_old_pwd.setError("旧密码不能为空!");
                    return;
                }
                if (StringUtil.isBlank(newPwd)) {
                    et_new_pwd.setError("新密码不能为空!");
                    return;
                }
                if (newPwd.length() < 6) {
                    et_new_pwd.setError("新密码格式不正确!");
                }
                RequestParams params = new RequestParams(Url.MODIFY_PWD_URL);
                Map<String, String> map = new HashMap<>();
                map.put("newPwd", newPwd);
                map.put("oldPwd", oldPwd);
                map.put("token", PrefName.getToken(context));
                httpPostJSON("modify", params, map);
                break;
        }
    }

    @Override
    public void success(String result, String type) {
        super.success(result, type);
        BaseResult baseResult = JSON.parseObject(result, BaseResult.class);
        senToa(baseResult.getMsg());
        finish();
    }
}
