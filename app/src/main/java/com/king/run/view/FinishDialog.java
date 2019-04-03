package com.king.run.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.util.Utils;

/**
 * 作者：shuizi_wade on 2017/9/13 13:51
 * 邮箱：674618016@qq.com
 */
public class FinishDialog extends Dialog {
    private Context context;
    private ClickListener clickListener;
    private boolean isFinish;
    private ImageView iv_finish;
    private TextView tv_finish_type, tv_finish_content;

    public FinishDialog(Context context, ClickListener listener, boolean isFinish) {
        //给dialog定制了一个主题（透明背景，无边框，无标题栏，浮在Activity上面，模糊）
        super(context, R.style.MyDialog);
        setContentView(R.layout.finish_dialog);
        this.context = context;
        clickListener = listener;
        this.isFinish = isFinish;
        initView();
    }

    private void initView() {
        iv_finish = findViewById(R.id.iv_finish);
        tv_finish_type = findViewById(R.id.tv_finish_type);
        tv_finish_content = findViewById(R.id.tv_finish_content);
        if (isFinish) {
            iv_finish.setImageResource(R.mipmap.train_result_pic_reached);
            tv_finish_type.setText(R.string.finish_done);
            tv_finish_content.setText(R.string.continue_todo);
        } else {
            iv_finish.setImageResource(R.mipmap.train_result_pic_unreached);
            tv_finish_type.setText(R.string.finish_not_done);
            tv_finish_content.setText(R.string.keep_trying);
        }
        findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        //点击空白区域可以取消dialog
        this.setCanceledOnTouchOutside(true);
        //点击back键可以取消dialog
        this.setCancelable(true);
        Window window = this.getWindow();
        //让Dialog显示在屏幕的底部
        window.setGravity(Gravity.CENTER);
        //设置窗口出现和窗口隐藏的动画
        window.setWindowAnimations(R.style.ios_bottom_dialog_anim);
        //设置BottomDialog的宽高属性
        WindowManager.LayoutParams lp = window.getAttributes();
        //WindowManager.LayoutParams.MATCH_PARENT;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    public interface ClickListener {
    }
}
