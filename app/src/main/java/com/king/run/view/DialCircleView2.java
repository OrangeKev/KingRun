package com.king.run.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.king.run.R;

import org.xutils.common.util.DensityUtil;

public class DialCircleView2 extends View {

    private Paint mPaintBg;//底色
    private Paint mPaintText;//文字
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
    private int lineWidth;//线条宽度
    private int animTime = 2000;//动画效果时间
    private BarAnimation animation;
    private int allPercent = 100;//总共的进度数
    private int cusPer;//需要显示的进度数
    private int curPercent;//当前进度数

    private int mTextWidth;//边框文字宽度
    private int mTextHeight;//边框文字高度
    private int mTextWidth1;//边框文字1宽度
    private int mTextHeight1;//边框文字1高度
    private String mTextColorNormal = "#777777";//
    private String mTextColorBlack = "#000000";//
    private float mSize;//文字大小
    private int mTextSpace = 15;//边上文字间距
    private String[] step1 = new String[]{"5000", "新手"};
    private String[] step2 = new String[]{"10000", "能人"};
    private String[] step3 = new String[]{"15000", "健将"};
    private String[] step4 = new String[]{"20000", "达人"};
    private String[] tags = new String[]{"累计健身(分钟)", "公里", "大卡", "健康生活"};
    private int tag0Width, tag0Height;//累计健身(分钟)宽高
    private int tag1Width, tag1Height;//公里/大卡 宽高
    private int tag3Width, tag3Height;//健康生活 宽高

    private int strokeW;//圈内圆形矩形边框宽度
    private int offSet;//圈内圆形矩形居第一个点和最后一个点X轴的偏移量

    private String leftBootomTextValue = "0";
    private String rightBottomTextValue = "0";
    private String centerTopTextValue = "0";

    private boolean hideCirclrText;//是否隐藏边框上的文字
    private boolean lineWidthEquality;//设置线条长度相等
    private int previousCurrent;// 保存上次进度
    private Context context;
    private boolean showCenterText;
    private String centerBottomTextValue;
    private String centerBottomTextTitle = "大卡";
    private float circleRight, circleLeft, circleTop, circleBottom;

    public DialCircleView2(Context context) {
        super(context);
        this.context = context;
        init(context);

    }


    public DialCircleView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DialCircleView2);
        String km = a.getString(R.styleable.DialCircleView2_text_km);
        if (!TextUtils.isEmpty(km)) {
            tags[1] = km;
        }
        String kcal = a.getString(R.styleable.DialCircleView2_text_kcal);
        if (!TextUtils.isEmpty(kcal)) {
            tags[2] = kcal;
        }
        String time = a.getString(R.styleable.DialCircleView2_text_time);
        if (!TextUtils.isEmpty(time)) {
            tags[0] = time;
        }
        hideCirclrText = a.getBoolean(R.styleable.DialCircleView2_text_hide_circle, false);
        lineWidthEquality = a.getBoolean(R.styleable.DialCircleView2_line_width_equality, false);
        a.recycle();
        init(context);
    }


    private void init(Context context) {

        strokeW = dpToPx(1);
        offSet = dpToPx(20);
        mSize = dpToPx(10);
        lineWidth = dpToPx(2);

        mPaintBg = new Paint();
        mPaintBg.setColor(Color.parseColor(dialBg));
        mPaintBg.setAntiAlias(true);
        mPaintBg.setStrokeWidth(lineWidth);
        mPaintBg.setStyle(Paint.Style.FILL);

        mPaintText = new Paint();
        setTextPaintStyle(1);
        animation = new BarAnimation();
        animation.setDuration(animTime);
        String text = step4[0];
        String text1 = step4[1];
        Rect rect = new Rect();
        //边框长文字（"40000"）
        mPaintText.getTextBounds(text, 0, text.length(), rect);
        mTextHeight = rect.height();
        mTextWidth = rect.width();
        //边框短文字（"大牛"）
        mPaintText.getTextBounds(text1, 0, text1.length(), rect);
        mTextHeight1 = rect.height();
        mTextWidth1 = rect.width();
        //累计健身(分钟)宽高
        mPaintText.getTextBounds(tags[0], 0, tags[0].length(), rect);
        tag0Height = rect.height();
        tag0Width = rect.width();
        //公里/大卡 宽高
        mPaintText.getTextBounds(tags[1], 0, tags[1].length(), rect);
        tag1Height = rect.height();
        tag1Width = rect.width();
        //健康生活 宽高
        mPaintText.getTextBounds(tags[3], 0, tags[3].length(), rect);
        tag3Height = rect.height();
        tag3Width = rect.width();


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
        centerPoint[1] = (mWidth + mTextHeight) / 2;
        int contentWidth = mWidth > mHeight ? mHeight : mWidth;//画图的区域
        int outCircleRadius = (contentWidth - mTextWidth * 2 - mTextSpace * 2) / 2;//外圈半径
        inCircleRadius = outCircleRadius - 50;
        longLength = (outCircleRadius - inCircleRadius);
        shortLength = longLength / 2;
        if (lineWidthEquality) {
            longLength = shortLength;
        }
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
        if (previousCurrent < 1) {
            //首次进度添加动画效果，其余的不给出动画效果
            startAnimation(animation);
        } else {
            this.curPercent = percent;
            postInvalidate();
        }
    }


    /**
     * 中间三个值的
     */
    public void change(int percent, String leftBootomTextValue, String rightBottomTextValue, String centerTopTextValue) {
        if (cusPer > 0) {
            previousCurrent = cusPer;
        }
        this.leftBootomTextValue = leftBootomTextValue;
        this.rightBottomTextValue = rightBottomTextValue;
        this.centerTopTextValue = centerTopTextValue;
        if (percent > 100) {
            percent = 100;
        } else if (percent < 0) {
            percent = 0;
        }
        this.cusPer = percent;
        if (previousCurrent < 1) {
            //首次进度添加动画效果，其余的不给出动画效果
            startAnimation(animation);
        } else {
            this.curPercent = percent;
            postInvalidate();
        }
    }


    /**
     * 中间两个值的
     */
    public void change2(int percent, boolean showCenterText, String centerBottomTextValue, String centerTopTextValue) {
        if (cusPer > 0) {
            previousCurrent = cusPer;
        }
        this.showCenterText = showCenterText;
        this.centerBottomTextValue = centerBottomTextValue;
        this.centerTopTextValue = centerTopTextValue;
        if (percent > 100) {
            percent = 100;
        } else if (percent < 0) {
            percent = 0;
        }
        this.cusPer = percent;
        if (previousCurrent < 1) {
            //首次进度添加动画效果，其余的不给出动画效果
            startAnimation(animation);
        } else {
            this.curPercent = percent;
            postInvalidate();
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawText(canvas);
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
     * 画文字
     */
    private void drawText(Canvas canvas) {
        //画边框上的字
        int angle;
        int avgAngle = allAngle / 4;
        int[] firstP = getPointFromAngleAndRadius(startAngle, inCircleRadius + shortLength);//第一个点的坐标
        int[] lastP = getPointFromAngleAndRadius(startAngle + allAngle, inCircleRadius + shortLength);//最后一个点的坐标
        Paint rfPaint = new Paint();
        rfPaint.setStyle(Paint.Style.STROKE);
        rfPaint.setColor(Color.parseColor(mTextColorNormal));
        rfPaint.setAntiAlias(true);
        rfPaint.setStrokeWidth(strokeW);
        int rfLeft = firstP[0] + offSet;//left
        int rfTop = firstP[1] - 3 * tag3Height;//top
        int rfRight = lastP[0] - offSet;//right
        for (int i = 1; i < 5; i++) {
            setTextPaintStyle(1);
            if (hideCirclrText) {
                mPaintText.setColor(Color.parseColor("#00000000"));
            }
            angle = avgAngle * i + startAngle;
            int[] startP = getPointFromAngleAndRadius(angle, inCircleRadius);
            int[] endP = getPointFromAngleAndRadius(angle, inCircleRadius + longLength);

            if (i == 1) {
                //5000、新手
                canvas.drawText(step1[0], endP[0] - mTextWidth
                        , endP[1] - mTextHeight / 2, mPaintText);
                canvas.drawText(step1[1], endP[0] - mTextWidth + (mTextWidth - mTextWidth1) / 2 - 6
                        , endP[1] + mTextHeight / 2 + mTextSpace, mPaintText);
            } else if (i == 2) {
                //10000、能人
                canvas.drawText(step2[0], endP[0] - mTextWidth / 2
                        , endP[1] - mTextHeight - lineWidth - mTextSpace, mPaintText);
                canvas.drawText(step2[1], endP[0] - mTextWidth1 / 2
                        , endP[1] - lineWidth, mPaintText);

                //圈内字(时间)
                if (hideCirclrText) {
                    mPaintText.setColor(Color.parseColor(mTextColorNormal));
                }
                canvas.drawText(tags[0], startP[0] - tag0Width / 2, startP[1] + tag0Height * 3 + DensityUtil.dip2px(10), mPaintText);
                setTextPaintStyle(3);
                Rect rect = new Rect();
                mPaintText.getTextBounds(centerTopTextValue, 0, centerTopTextValue.length(), rect);
                canvas.drawText(
                        centerTopTextValue,
                        startP[0] - rect.width() / 2,
                        startP[1] + tag0Height * 4 + rect.height() + DensityUtil.dip2px(15),
                        mPaintText);
                if (showCenterText) {
                    //中间下文字
                    setTextPaintStyle(1);
                    mPaintText.getTextBounds("大卡", 0, centerTopTextValue.length(), rect);
                    canvas.drawText("大卡", startP[0] - rect.width() / 2 - DensityUtil.dip2px(5), firstP[1] - 3 * tag3Height - tag1Height * 6, mPaintText);

                    setTextPaintStyle(2);
                    mPaintText.getTextBounds(centerBottomTextValue, 0, centerTopTextValue.length(), rect);
                    canvas.drawText(centerBottomTextValue, startP[0] - rect.width() / 2, firstP[1] - 3 * tag3Height - tag1Height * 2, mPaintText);
                }
            } else if (i == 3) {
                //15000、健将
                canvas.drawText(step3[0], endP[0]
                        , endP[1] - mTextHeight / 2, mPaintText);
                canvas.drawText(step3[1], endP[0] + (mTextWidth - mTextWidth1) / 2
                        , endP[1] + mTextHeight / 2 + mTextSpace, mPaintText);
            } else if (i == 4) {
                //20000、达人
                canvas.drawText(step4[0], endP[0] + mTextSpace
                        , endP[1], mPaintText);
                canvas.drawText(step4[1], endP[0] + mTextSpace + (mTextWidth - mTextWidth1) / 2
                        , endP[1] + mTextHeight + mTextSpace, mPaintText);
            }
        }
        if (hideCirclrText) {
            mPaintText.setColor(Color.parseColor(mTextColorNormal));
        }
        //画圈内的字
        // 画中间圆角矩形
        circleLeft = rfLeft;
        circleBottom = firstP[1] + DensityUtil.dip2px(10);
        circleRight = rfRight;
        circleTop = rfTop + DensityUtil.dip2px(10);
        RectF rf = new RectF(circleLeft, circleTop, circleRight, circleBottom);
        canvas.drawRoundRect(rf, 30, 30, rfPaint);
        //圆角矩形内写字
        canvas.drawText(tags[3], (lastP[0] - firstP[0]) / 2 + firstP[0] - tag3Width / 2,
                firstP[1] - 3 * tag3Height / 2 + tag1Height / 2 - strokeW + DensityUtil.dip2px(10), mPaintText);


        Rect f = new Rect();
        if (!showCenterText) {
            //左下边文字
            setTextPaintStyle(1);
            canvas.drawText(tags[1], rfLeft, rfTop - tag1Height * 6, mPaintText);
            setTextPaintStyle(2);
            mPaintText.getTextBounds(leftBootomTextValue, 0, leftBootomTextValue.length(), f);
            canvas.drawText(leftBootomTextValue, rfLeft + tag1Width / 2 - f.width() / 2, rfTop - tag1Height * 2, mPaintText);
            //右下边文字
            setTextPaintStyle(1);
            canvas.drawText(tags[2], rfRight - tag1Width, rfTop - tag1Height * 6, mPaintText);
            setTextPaintStyle(2);
            mPaintText.getTextBounds(rightBottomTextValue, 0, rightBottomTextValue.length(), f);
            canvas.drawText(rightBottomTextValue, rfRight - tag1Width + tag1Width / 2 - f.width() / 2, rfTop - tag1Height * 2, mPaintText);
        }
    }

    /**
     * 画出带有圆弧的直线，实际就是在线条两端画圆
     */
    private void drawRoundLine(Canvas canvas, int per) {
        int length;
        int angle;
        int longDialPoint = dialCount / longDialCount;//长线位置
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 获取点击屏幕时的点的坐标
        float x = event.getX();
        float y = event.getY();
        whichCircle(x, y);
        return super.onTouchEvent(event);
    }

    private CircleClickInterface circleClickInterface;

    /**
     * 确定点击的点在哪个圆内
     *
     * @param x
     * @param y
     */
    private void whichCircle(float x, float y) {
        if (x < circleRight && x > circleLeft && y > circleTop && y < circleBottom) {
            circleClickInterface.onClick();
        }
    }

    public void setOnCircleClick(CircleClickInterface circleClick) {
        this.circleClickInterface = circleClick;
    }

    public interface CircleClickInterface {
        void onClick();
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

    /**
     * 初始化画笔
     */
    private void initTextPaint() {
        mPaintText.reset();
        mPaintText.setAntiAlias(true);
    }

    private void setTextPaintStyle(int type) {
        initTextPaint();
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/ReductoCondSSK.ttf");
        if (type == 1) {
            mPaintText.setColor(Color.parseColor(mTextColorNormal));
            mPaintText.setTextSize(mSize);
        } else if (type == 2) {
            mPaintText.setColor(Color.parseColor(mTextColorBlack));
            mPaintText.setTextSize(mSize * 3);
        } else if (type == 3) {
            mPaintText.setColor(Color.parseColor(mTextColorBlack));
            mPaintText.setTextSize(mSize * 6);
        }
        mPaintText.setTypeface(typeface);
    }

    private int dpToPx(int size) {
        return DensityUtil.dip2px(size);
    }

    private int spToPx(int size) {
        return (int) (size * DensityUtil.getDensity() / 160);
    }

}
