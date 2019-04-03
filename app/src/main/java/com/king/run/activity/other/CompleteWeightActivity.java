package com.king.run.activity.other;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.base.BaseActivity;
import com.king.run.util.PrefName;
import com.zkk.view.rulerview.RulerView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * 作者：shuizi_wade on 2017/8/18 09:41
 * 邮箱：674618016@qq.com
 */
@ContentView(R.layout.activity_complete_weight)
public class CompleteWeightActivity extends BaseActivity {
    @ViewInject(R.id.ruler_weight)
    RulerView ruler_weight;
    @ViewInject(R.id.tv_weight_num)
    TextView tv_weight_num;
    private String weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
    }

    private void initViews() {
        weight = getIntent().getExtras().getString("weight");
        if (Float.parseFloat(weight) == 0) {
            weight = 55 + "";
        }
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/ReductoCondSSK.ttf");
        tv_weight_num.setTypeface(typeface);
        tv_weight_num.setText(weight);
        title_tv_title.setText(R.string.weight);
        ruler_weight.setOnValueChangeListener(new RulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                tv_weight_num.setText((int) value + "");
                weight = (int) value + "";
            }
        });
        ruler_weight.setValue(Float.parseFloat(weight), 20, 200, 1);
        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("weight", weight);
                setResultAct(bundle);
                finish();
            }
        });
    }
}
