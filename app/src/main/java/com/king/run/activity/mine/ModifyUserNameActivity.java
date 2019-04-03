package com.king.run.activity.mine;


import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.widget.EditText;

import com.king.run.R;
import com.king.run.base.BaseActivity;
import com.king.run.util.PrefName;
import com.king.run.util.StringUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * 作者：shuizi_wade on 2017/8/23 09:42
 * 邮箱：674618016@qq.com
 */
@ContentView(R.layout.activity_change_user_name)
public class ModifyUserNameActivity extends BaseActivity {
    @ViewInject(R.id.et_user_name)
    EditText et_user_name;
    private String nickName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
    }

    private void initViews() {
        nickName = getIntent().getExtras().getString("nickName");
        et_user_name.setText(nickName);
        Editable etable = et_user_name.getText();
        Selection.setSelection(etable, etable.length());
        title_tv_title.setText(R.string.modify_nickname);
        title_tv_right.setText(R.string.save);
        title_tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nickName = et_user_name.getText().toString().trim();
                if (StringUtil.isBlank(nickName)) {
                    et_user_name.setError("昵称不能为空");
                    return;
                }
                if (nickName.equals(PrefName.getNickName(context))) {
                    et_user_name.setError("不能和原昵称相同");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("nickName", et_user_name.getText().toString().trim());
                setResultAct(bundle);
                finish();
            }
        });
    }
}
