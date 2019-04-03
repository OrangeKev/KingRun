package com.king.run.activity.other;

import android.os.Bundle;

import com.king.run.R;
import com.king.run.base.BaseActivity;

import org.xutils.view.annotation.ContentView;

/**
 * 作者：shuizi_wade on 2017/8/18 09:41
 * 邮箱：674618016@qq.com
 */
@ContentView(R.layout.activity_complete_birth)
public class CompleteBirthActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
    }

    private void initViews() {
        title_tv_title.setText(R.string.birth);
    }
}
