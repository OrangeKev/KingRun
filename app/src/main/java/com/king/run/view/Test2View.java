package com.king.run.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.king.run.R;

import org.xutils.common.util.DensityUtil;

/**
 * 作者：shuizi_wade on 2017/9/4 10:38
 * 邮箱：674618016@qq.com
 */

public class Test2View extends View {

    private static final int HANDLER_PERCENT = 100;

    private long interval = 300;//休眠时间
    private long intervalTime = interval;//休眠时间
    private int speed = 5;//由此计算出新的休眠时间，实现画线速度由慢变快的效果
    private Paint mPaintBg;//底色
    private Paint mPaintDone;//进度的底色
    private int mWidth;//控件宽度
    private int mHeight;//控件高度
    private int[] centerPoint = new int[2];//记录中心点左边，x=int[0],y=int[1]
    private int longLength;//长刻度长度
    private int shortLength;//短刻度长度

    private int outCircleRadius;//外圈半径
    private int inCircleRadius;//内圈半径
    private int dialCount = 100;//刻度个数
    private int allAngle = 300;//总共的角度
    private int startAngle = 120;//开始的角度
    private int longDialCount = 4;//长刻度的个数
    private String dialBg = "#cccccc";//刻度原本背景
    private String dialSweepBg = "#ffcc00";//刻度新背景
    private int contentWidth;
    private int percent;
    private boolean isChange ;
    private Context context;

    public Test2View(Context context) {
        super(context);
        this.context = context;
        init(context);
    }


    public Test2View(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context);
    }


    private void init(Context context) {
        mPaintBg = new Paint();
        mPaintBg.setColor(ContextCompat.getColor(context,R.color.sub_class_content_text_color));
        mPaintBg.setAntiAlias(true);
        mPaintBg.setStrokeWidth(5);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initValues();
    }

    private void initValues() {
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
        centerPoint[0] = mWidth / 2;
        centerPoint[1] = mHeight / 2;
        contentWidth = mWidth > mHeight ? mHeight : mWidth;
        outCircleRadius = contentWidth / 2;
        inCircleRadius = outCircleRadius - DensityUtil.dip2px(30);
        longLength = (outCircleRadius - inCircleRadius) / 2;
        shortLength = (int) (longLength / 1.5);
    }


    /**
     * @param percent 1-100
     */
    public void change(int percent) {
        if (isChange) {
            return;
        }
        isChange = true;
        this.percent = percent;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isChange) {
                        mHandler.removeMessages(HANDLER_PERCENT);
                        mHandler.sendEmptyMessage(HANDLER_PERCENT);
                        Thread.sleep(intervalTime);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    private int currentPercent = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_PERCENT :
                    currentPercent += 1;
                    if (interval > 10) {
                        intervalTime = intervalTime - intervalTime/speed;
                    }
                    if (currentPercent >= percent) {
                        isChange = false;
                        currentPercent = percent;
                        intervalTime = interval;
                    }
                    invalidate();
                    break;
            }
        }
    };
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDial(canvas);
    }

    /**
     * 画刻度
     */
    private void drawDial(Canvas canvas) {
        mPaintBg.setColor(ContextCompat.getColor(context,R.color.sub_class_content_text_color));
        drawLine(canvas, 100);
//        canvas.save(Canvas.ALL_SAVE_FLAG);
        mPaintBg.setColor(ContextCompat.getColor(context,R.color.yellow_deep));
        drawLine(canvas, currentPercent);
//        canvas.save();
    }

    private void drawLine(Canvas canvas, int percent) {
        int length;
        int angle;
        int longDialPoint = dialCount / longDialCount;//长线位置

        for (int i = 0; i <= percent; i++) {
            angle = (int) ((allAngle) / (dialCount * 1f) * i) + startAngle;
            if (i != 0 && i % longDialPoint == 0) {
                length = longLength;
            } else {
                length = shortLength;
            }
            int[] startP = getPointFromAngleAndRadius(angle, inCircleRadius);
            int[] endP = getPointFromAngleAndRadius(angle, inCircleRadius + length);
            canvas.drawLine(startP[0], startP[1], endP[0], endP[1], mPaintBg);
        }
//        canvas.save();
//        canvas.restore();
    }


    /**
     * 根据角度和半径，求一个点的坐标
     */
    private int[] getPointFromAngleAndRadius(int angle, int radius) {
        double x = radius * Math.cos(angle * Math.PI / 180) + centerPoint[0];
        double y = radius * Math.sin(angle * Math.PI / 180) + centerPoint[1];
        return new int[]{(int) x, (int) y};
    }

}
