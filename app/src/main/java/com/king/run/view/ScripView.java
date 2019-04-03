package com.king.run.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.icu.util.Measure;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.util.PicassoUtil;
import com.king.run.util.Utils;

import org.xutils.common.util.DensityUtil;
import org.xutils.view.annotation.ViewInject;

/**
 * 作者：shuizi_wade on 2017/12/15 14:33
 * 邮箱：674618016@qq.com
 */
public class ScripView extends LinearLayout {
    public interface OnSnapListener {
        void onSuccess(Bitmap bitmap);

        void onError();
    }

    private TextView tv_step;
    private TextView tv_km;
    private TextView tv_kcal;
    private Context context;
    private ImageView iv_big;
    private LinearLayout ly_right;

    public ScripView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ScripView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public ScripView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScripView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_scrip_view, this, false);
        addView(view);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/ReductoCondSSK.ttf");
        iv_big = view.findViewById(R.id.iv_big);
        tv_step = view.findViewById(R.id.tv_step);
        tv_step.setTypeface(typeface);
        tv_km = view.findViewById(R.id.tv_km);
        tv_kcal = view.findViewById(R.id.tv_kcal);
        ly_right = view.findViewById(R.id.ly_right);
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) iv_big.getLayoutParams();
        layoutParams2.width = Utils.getDisplayWidth((Activity) getContext());
        layoutParams2.height = Utils.getDisplayWidth((Activity) getContext()) - DensityUtil.dip2px(16);
        layoutParams2.setMargins(DensityUtil.dip2px(16), 0, DensityUtil.dip2px(16), 0);
        iv_big.setLayoutParams(layoutParams2);
        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) ly_right.getLayoutParams();
        layoutParams1.width = (Utils.getDisplayWidth((Activity) getContext()) - DensityUtil.dip2px(16)) * 2 / 5;
        layoutParams1.height = Utils.getDisplayWidth((Activity) getContext()) - DensityUtil.dip2px(16);
        layoutParams1.setMargins(DensityUtil.dip2px(16), 0, 0, 0);
        ly_right.setLayoutParams(layoutParams1);
    }

    public void getSnap(final OnSnapListener onSnapListener) {
        initViews(onSnapListener);
    }

    private void initViews(final OnSnapListener onSnapListener) {
        snap(onSnapListener);
    }

    public void setImg(int resId) {
        iv_big.setBackgroundResource(resId);
    }

    public void setImg(String path) {
        PicassoUtil.displayImage(iv_big, path, context);
    }

    public void setText(int step, String km, String kcal) {
        tv_step.setText(step + "");
        tv_km.setText(String.format(getResources().getString(R.string.have_walk), km));
        tv_kcal.setText(String.format(getResources().getString(R.string.have_consume), kcal));
    }

    /**
     * 截图
     *
     * @param onSnapListener
     */
    private void snap(OnSnapListener onSnapListener) {
        if (onSnapListener != null) {
            setDrawingCacheEnabled(true);
            measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
            buildDrawingCache();
            Bitmap tBitmap = getDrawingCache();
            // 拷贝图片，否则在setDrawingCacheEnabled(false)以后该图片会被释放掉
            tBitmap = tBitmap.createBitmap(tBitmap);
            setDrawingCacheEnabled(false);
            onSnapListener.onSuccess(tBitmap);
        }
    }
}
