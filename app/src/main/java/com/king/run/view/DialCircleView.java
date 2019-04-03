package com.king.run.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;


public class DialCircleView extends View {

    private Paint mPaintBg;//底色
    private Paint mPaintText;//文字颜色
    private int[] centerPoint = new int[2];//记录中心点左边，x=int[0],y=int[1]
    private int longLength;//长刻度长度
    private int shortLength;//短刻度长度

    private int inCircleRadius;//内圈半径
    private int allAngle = 300;//总共的角度
    private int startAngle = 120;//开始的角度
    private int longDialCount = 4;//长刻度的个数
    private String dialBg = "#cccccc";//刻度原本背景
    private String dialSweepBg = "#ffcc00";//刻度新背景
    private int dialCount = 100;//刻度个数
    private int lineWidth = 8;//线条宽度
    private int animTime = 2000;//动画效果时间
    private BarAnimation animation;
    private int allPercent = 100;//总共的进度数
    private int cusPer;//需要显示的进度数
    private int curPercent;//当前进度数


    public DialCircleView(Context context) {
        super(context);
        init(context);
    }


    public DialCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private void init(Context context) {
        mPaintBg = new Paint();
        mPaintBg.setColor(Color.parseColor(dialBg));
        mPaintBg.setAntiAlias(true);
        mPaintBg.setStrokeWidth(lineWidth);
        mPaintBg.setStyle(Paint.Style.FILL);
        mPaintText = new Paint();
        mPaintText.setColor(Color.BLACK);
        animation = new BarAnimation();
        animation.setDuration(animTime);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initValues();
    }

    private void initValues() {
        int mHeight = getMeasuredHeight();//控件宽高度
        int mWidth = getMeasuredWidth();//控件宽度
        centerPoint[0] = mWidth / 2;
        centerPoint[1] = mHeight / 2;
        int contentWidth = mWidth > mHeight ? mHeight : mWidth;//画图的区域
        int outCircleRadius = contentWidth / 2;//外圈半径
        inCircleRadius = outCircleRadius - 50;
        longLength = (outCircleRadius - inCircleRadius);
        shortLength = longLength / 2;
    }


    /**
     * @param percent 1-100
     */
    public void change(int percent) {

        if (percent > 100) {
            percent = 100;
        } else if (percent < 0) {
            percent = 0;
        }
        this.cusPer = percent;
        startAnimation(animation);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDial(canvas);
    }

    /**
     * 画刻度
     */
    private void drawDial(Canvas canvas) {
        mPaintBg.setColor(Color.parseColor(dialBg));
        drawRoundLine(canvas, allPercent);
        mPaintBg.setColor(Color.parseColor(dialSweepBg));
        drawRoundLine(canvas, curPercent);
    }

    /**
     * 画出带有圆弧的直线，实际就是在线条两端画圆
     */
    private void drawRoundLine(Canvas canvas, int per) {
        int length;
        int angle;
        int longDialPoint = dialCount / longDialCount;//长线位置
//        int mPer = isAllPer ? cusPercent : curPercent;
        for (int i = 1; i <= per; i++) {
            angle = (int) ((allAngle) / (dialCount * 1f) * i) + startAngle;
            if (i != 0 && i % longDialPoint == 0) {
                length = longLength;
            } else {
                length = shortLength;
            }
            int[] startP = getPointFromAngleAndRadius(angle, inCircleRadius);
            int[] endP = getPointFromAngleAndRadius(angle, inCircleRadius + length);
            canvas.drawLine(startP[0], startP[1], endP[0], endP[1], mPaintBg);
            canvas.drawCircle(startP[0], startP[1], lineWidth / 2, mPaintBg);
            canvas.drawCircle(endP[0], endP[1], lineWidth / 2, mPaintBg);
        }

    }


    /**
     * 根据角度和半径，求一个点的坐标
     */
    private int[] getPointFromAngleAndRadius(int angle, int radius) {
        double x = radius * Math.cos(angle * Math.PI / 180) + centerPoint[0];
        double y = radius * Math.sin(angle * Math.PI / 180) + centerPoint[1];
        return new int[]{(int) x, (int) y};
    }


    public class BarAnimation extends Animation {

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f) {
                curPercent = (int) (interpolatedTime * cusPer);
            } else {
                curPercent = cusPer;
            }
            postInvalidate();
        }
    }
}
