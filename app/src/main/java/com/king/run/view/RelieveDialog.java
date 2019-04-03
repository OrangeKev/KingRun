package com.king.run.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.util.Utils;

/**
 * 作者：shuizi_wade on 2017/9/13 13:51
 * 邮箱：674618016@qq.com
 */
public class RelieveDialog extends Dialog {
    public LinearLayout options_ll;
    public TextView tv_relieve;
    private Context context;
    private ClickListener clickListener;

    public RelieveDialog(Context context, ClickListener listener) {
        //给dialog定制了一个主题（透明背景，无边框，无标题栏，浮在Activity上面，模糊）
        super(context, R.style.ios_bottom_dialog);
        setContentView(R.layout.relieve_dialog);
        this.context = context;
        clickListener = listener;
        initView();
    }

    private void initView() {
        tv_relieve = findViewById(R.id.tv_relieve);
        options_ll = findViewById(R.id.options_ll);

        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelieveDialog.this.dismiss();
            }
        });

        tv_relieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.relieve();
                dismiss();
            }
        });
        //点击空白区域可以取消dialog
        this.setCanceledOnTouchOutside(true);
        //点击back键可以取消dialog
        this.setCancelable(true);
        Window window = this.getWindow();
        //让Dialog显示在屏幕的底部
        window.setGravity(Gravity.BOTTOM);
        //设置窗口出现和窗口隐藏的动画
        window.setWindowAnimations(R.style.ios_bottom_dialog_anim);
        //设置BottomDialog的宽高属性
        WindowManager.LayoutParams lp = window.getAttributes();
        //WindowManager.LayoutParams.MATCH_PARENT;
        lp.width = Utils.getDisplayWidth((Activity) context);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    public interface ClickListener {

        void relieve();
    }
}
