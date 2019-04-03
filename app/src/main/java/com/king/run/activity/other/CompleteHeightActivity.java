package com.king.run.activity.other;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.base.BaseActivity;
import com.king.run.util.PrefName;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.text.DecimalFormat;

/**
 * 作者：shuizi_wade on 2017/8/18 09:41
 * 邮箱：674618016@qq.com
 */
@ContentView(R.layout.activity_complete_height)
public class CompleteHeightActivity extends BaseActivity {
    @ViewInject(R.id.vruler)
    ScrollView ruler;
    @ViewInject(R.id.user_height_value)
    TextView user_height_value;
    @ViewInject(R.id.vruler_layout)
    LinearLayout rulerlayout;
    private String height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                scroll();
            }
        }, 400);
    }

    private void scroll() {
        ruler.smoothScrollTo(0, 1700);
    }

    protected void initViews() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/ReductoCondSSK.ttf");
        title_tv_title.setText(R.string.height);
        height = getIntent().getExtras().getString("height");
        user_height_value.setTypeface(typeface);
        user_height_value.setText(height);
        ruler.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                user_height_value.setText(String.valueOf((int) Math.ceil((ruler.getScrollY()) / 10)) + "");
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                height = String.valueOf((int) Math.ceil((ruler.getScrollY()) / 10)) + "";
                                user_height_value.setText(height);
                            }
                        }, 1000);
                        break;
                }
                return false;
            }

        });
        new Handler().postDelayed((new Runnable() {
            @Override
            public void run() {
                constructRuler();
            }
        }), 300);
        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("height", height);
                setResultAct(bundle);
                finish();
            }
        });
    }

    private void constructRuler() {
        int rulerHeight = ruler.getHeight();
        View topView = LayoutInflater.from(this).inflate(
                R.layout.blankvrulerunit, null);
        topView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                rulerHeight / 2));
        rulerlayout.addView(topView);
        for (int i = 0; i < 25; i++) {
            View view = LayoutInflater.from(this).inflate(
                    R.layout.vrulerunit, null);
            view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 100));
            TextView tv = (TextView) view.findViewById(R.id.vrulerunit);
            float size = (float) i / 10;
            DecimalFormat df = new DecimalFormat("0.00");//格式化小数，不足的补0
            String fileSize = df.format(size);
            tv.setText(fileSize);
            rulerlayout.addView(view);
        }
        View bottomView = LayoutInflater.from(this).inflate(
                R.layout.blankvrulerunit, null);
        bottomView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                rulerHeight / 2));
        rulerlayout.addView(bottomView);
    }
}
