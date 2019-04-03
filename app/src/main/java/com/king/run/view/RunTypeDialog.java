package com.king.run.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.king.run.R;
import com.king.run.util.Utils;

/**
 * 作者：shuizi_wade on 2017/9/13 13:51
 * 邮箱：674618016@qq.com
 */
public class RunTypeDialog extends Dialog {

    private Context context;
    private ClickListener clickListener;
    private ImageView iv_in, iv_out, iv_in_in, iv_out_out;

    public RunTypeDialog(Context context, ClickListener listener) {
        //给dialog定制了一个主题（透明背景，无边框，无标题栏，浮在Activity上面，模糊）
        super(context, R.style.run_type_dialog);
        setContentView(R.layout.dialog_run_type);
        this.context = context;
        clickListener = listener;
        initView();
    }

    private void initView() {
        iv_in = (ImageView) findViewById(R.id.iv_in);
        iv_in_in = (ImageView) findViewById(R.id.iv_in_in);
        iv_out_out = (ImageView) findViewById(R.id.iv_out_out);
        iv_out = (ImageView) findViewById(R.id.iv_out);
        findViewById(R.id.iv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.iv_bg_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.inRun();
                dismiss();
            }
        });
        findViewById(R.id.iv_bg_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.outRun();
                dismiss();
            }
        });
        startAnimotionCounterClockWise(iv_in);
        startAnimotionCounterClockWise(iv_out);
        startAnimotionClockWise(iv_out_out);
        startAnimotionClockWise(iv_in_in);
        //点击空白区域可以取消dialog
        this.setCanceledOnTouchOutside(true);
        //点击back键可以取消dialog
        this.setCancelable(true);
        Window window = this.getWindow();
        //让Dialog显示在屏幕的底部
//        window.setGravity(Gravity.BOTTOM);
        //设置窗口出现和窗口隐藏的动画
        window.setWindowAnimations(R.style.ios_bottom_dialog_anim);
        //设置BottomDialog的宽高属性
        WindowManager.LayoutParams lp = window.getAttributes();
        //WindowManager.LayoutParams.MATCH_PARENT;
        lp.width = Utils.getDisplayWidth((Activity) context);
        lp.height = Utils.getDisplayHeight((Activity) context);
        window.setAttributes(lp);
    }

    /**
     * 逆时针
     *
     * @param view
     */
    private void startAnimotionCounterClockWise(ImageView view) {
        //动画
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.img_animation_counterclockwise);
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
        animation.setInterpolator(lin);
        view.startAnimation(animation);
    }

    /**
     * 顺时针
     *
     * @param view
     */
    private void startAnimotionClockWise(ImageView view) {
        //动画
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.img_animation_clockwise);
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
        animation.setInterpolator(lin);
        view.startAnimation(animation);
    }

    public interface ClickListener {
        void inRun();

        void outRun();
    }
}
