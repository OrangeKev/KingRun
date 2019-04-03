package com.king.run.activity.other;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.king.run.R;
import com.king.run.base.BaseActivity;
import com.king.run.util.PrefName;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * 作者：shuizi_wade on 2017/8/18 09:41
 * 邮箱：674618016@qq.com
 */
@ContentView(R.layout.activity_complete_sex)
public class CompleteSexActivity extends BaseActivity {
    @ViewInject(R.id.ly_female)
    LinearLayout ly_female;
    @ViewInject(R.id.ly_male)
    LinearLayout ly_male;
    private int sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
    }

    private void initViews() {
        sex = getIntent().getExtras().getInt("sex");
        switch (sex) {
            case 0:
                ly_female.setBackgroundResource(R.drawable.sex_male_bg);
                ly_male.setBackgroundResource(R.drawable.sex_female_bg);
                break;
            case 1:
                ly_male.setBackgroundResource(R.drawable.sex_male_bg);
                ly_female.setBackgroundResource(R.drawable.sex_female_bg);
                break;
        }
        title_tv_title.setText(R.string.sex);
    }

    @Event(value = {R.id.ly_female, R.id.ly_male,R.id.btn_save})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.ly_female:
                sex = 0;
                ly_female.setBackgroundResource(R.drawable.sex_male_bg);
                ly_male.setBackgroundResource(R.drawable.sex_female_bg);
                break;
            case R.id.ly_male:
                sex = 1;
                ly_male.setBackgroundResource(R.drawable.sex_male_bg);
                ly_female.setBackgroundResource(R.drawable.sex_female_bg);
                break;
            case R.id.btn_save:
                Bundle bundle = new Bundle();
                bundle.putInt("sex",sex);
                setResultAct(bundle);
                finish();
                break;
        }
    }
}
