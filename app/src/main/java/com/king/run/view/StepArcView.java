package com.king.run.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.king.run.R;

import org.xutils.common.util.DensityUtil;

/**
 * Email 674618016@qq.com
 * Created by shuiz_wade on 2017/8/31.
 */

public class StepArcView extends View {
    /**
     * 圆弧的宽度
     */
    private float borderWidth = 38f;
    /**
     * 画步数的数值的字体大小
     */
    private float numberTextSize = 0;
    /**
     * 步数
     */
    private String stepNumber = "0";
    /**
     * 开始绘制圆弧的角度
     */
    private float startAngle = 135;
    /**
     * 终点对应的角度和起始点对应的角度的夹角
     */
    private float angleLength = 270;
    /**
     * 所要绘制的当前步数的红色圆弧终点到起点的夹角
     */
    private float currentAngleLength = 0;
    /**
     * 动画时长
     */
    private int animationLength = 3000;
    /**
     * 要绘制的表盘线条长度
     **/
    int lineLength = 12;
    /**
     * 超出刻度盘长度
     */
    int moreLength = 5;
    private float centerX;

    public StepArcView(Context context) {
        super(context);
    }

    public StepArcView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public StepArcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**中心点的x坐标*/
        centerX = (getWidth()) / 2;
        /**【第六步】绘制"步数"进度标尺类似于钟表线隔*/
        drawLine(canvas);
        drawLineYellow(canvas);
    }

    /**
     * 画默认线
     *
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);//抗锯齿功能
        mPaint.setStrokeWidth(4);
        mPaint.setColor(getResources().getColor(R.color.text_color_6));
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        /**要绘制的表盘线的总数**/
        int count = 120;
        /**要绘制的表盘每个间隔线条之间的夹角**/
        int avgAngle = (360 / (count - 1));
        /**要绘制的表盘的最长的半径**/
        //- borderWidth - bitmap.getHeight()
        float radius = centerX;
        /**起始点**/
        PointF point1 = new PointF();
        /**终止点**/
        PointF point2 = new PointF();
        for (int i = 0; i < count; i++) {
            if (i <= 80 || i >= 100) {
                int angle = avgAngle * i;
                if (i == 5 || i == 30 || i == 55 || i == 80 || i == 100) {
                    /**起始点坐标**/
                    point1.x = centerX + (float) Math.cos(angle * (Math.PI / 180))
                            * radius;
                    point1.y = centerX - (float) Math.sin(angle * (Math.PI / 180))
                            * radius;
                } else {
                    /**起始点坐标**/
                    point1.x = centerX + (float) Math.cos(angle * (Math.PI / 180))
                            * (radius - DensityUtil.dip2px(moreLength));
                    point1.y = centerX - (float) Math.sin(angle * (Math.PI / 180))
                            * (radius - DensityUtil.dip2px(moreLength));
                }
                /**终止点坐标**/
                point2.x = centerX + (float) Math.cos(angle * (Math.PI / 180))
                        * (radius - DensityUtil.dip2px(lineLength));
                point2.y = centerX - (float) Math.sin(angle * (Math.PI / 180))
                        * (radius - DensityUtil.dip2px(lineLength));

                /**画线**/
                canvas.drawLine(point1.x, point1.y, point2.x, point2.y, mPaint);
            }
        }
    }

    /**
     * 画黄色线
     *
     * @param canvas
     */
    private void drawLineYellow(Canvas canvas) {
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);//抗锯齿功能
        mPaint.setStrokeWidth(4);
        mPaint.setColor(getResources().getColor(R.color.yellow_deep));
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        /**要绘制的表盘线的总数**/
        int count = 120;
        /**要绘制的表盘每个间隔线条之间的夹角**/
        int avgAngle = (360 / (count - 1));
        /**要绘制的表盘的最长的半径**/
        //- borderWidth - bitmap.getHeight()
        float radius = centerX;
        /**起始点**/
        PointF point1 = new PointF();
        /**终止点**/
        PointF point2 = new PointF();
        int cur = Integer.parseInt(stepNumber) / 200;
        if (cur > 80) {
            for (int i = 0; i < count; i++) {
                int p = 100 - cur;
                if (i <= 80 || i >= (100 + p)) {
                    cavsDrawLi(avgAngle, i, radius, point1,
                            point2, canvas, mPaint, lineLength);
                }
            }
        } else {
            for (int i = 0; i < count; i++) {
                if (i <= 80 && i >= (80 - cur)) {
                    cavsDrawLi(avgAngle, i, radius, point1,
                            point2, canvas, mPaint, lineLength);
                }
            }
        }
    }

    /**
     * 画黄色进度线
     *
     * @param avgAngle
     * @param i
     * @param radius
     * @param point1
     * @param point2
     * @param canvas
     * @param mPaint
     * @param lineLength
     */
    private void cavsDrawLi(int avgAngle, int i, float radius,
                            PointF point1, PointF point2,
                            Canvas canvas, Paint mPaint, int lineLength) {
        int angle = avgAngle * i;
        if (i == 5 || i == 30 || i == 55 || i == 80 || i == 100) {
            /**起始点坐标**/
            point1.x = centerX + (float) Math.cos(angle * (Math.PI / 180)) * radius;
            point1.y = centerX - (float) Math.sin(angle * (Math.PI / 180)) * radius;
        } else {
            /**起始点坐标**/
            point1.x = centerX + (float) Math.cos(angle * (Math.PI / 180))
                    * (radius - DensityUtil.dip2px(moreLength));
            point1.y = centerX - (float) Math.sin(angle * (Math.PI / 180))
                    * (radius - DensityUtil.dip2px(moreLength));
        }
        /**终止点坐标**/
        point2.x = centerX + (float) Math.cos(angle * (Math.PI / 180))
                * (radius - DensityUtil.dip2px(lineLength));
        point2.y = centerX - (float) Math.sin(angle * (Math.PI / 180))
                * (radius - DensityUtil.dip2px(lineLength));
//        RectF rectF = new RectF(point1.x, point1.y, point2.x, point2.y);
//        canvas.drawRoundRect(rectF, 10, 10, mPaint);
        /**画线**/
        canvas.drawLine(point1.x, point1.y, point2.x, point2.y, mPaint);
    }

    public void setCurrentSteps(int steps) {
        stepNumber = steps + "";
    }

    /**
     * dip 转换成px
     *
     * @param dip
     * @return
     */

    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * 所走的步数进度
     * <p>
     * //     * @param totalStepNum  设置的步数
     *
     * @param currentCounts 所走步数
     */
    public void setCurrentCount(int currentCounts) {
//        stepNumber = currentCounts + "";
//        setTextSize(currentCounts);
//        /**如果当前走的步数超过总步数则圆弧还是270度，不能成为园*/
//        if (currentCounts > totalStepNum) {
//            currentCounts = totalStepNum;
//        }
//        /**所走步数占用总共步数的百分比*/
//        float scale = (float) currentCounts / totalStepNum;
//        /**换算成弧度最后要到达的角度的长度-->弧长*/
//        float currentAngleLength = scale * angleLength;
        /**开始执行动画*/
        setAnimation(0, currentCounts, 3 * 1000);
    }

    /**
     * 为进度设置动画
     * ValueAnimator是整个属性动画机制当中最核心的一个类，属性动画的运行机制是通过不断地对值进行操作来实现的，
     * 而初始值和结束值之间的动画过渡就是由ValueAnimator这个类来负责计算的。
     * 它的内部使用一种时间循环的机制来计算值与值之间的动画过渡，
     * 我们只需要将初始值和结束值提供给ValueAnimator，并且告诉它动画所需运行的时长，
     * 那么ValueAnimator就会自动帮我们完成从初始值平滑地过渡到结束值这样的效果。
     *
     * @param last
     * @param current
     */
    private void setAnimation(int last, int current, int length) {
        ValueAnimator progressAnimator = ValueAnimator.ofInt(last, current);
        progressAnimator.setDuration(length);
        progressAnimator.setTarget(currentAngleLength);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                stepNumber = (int) animation.getAnimatedValue() + "";
                invalidate();
            }
        });
        progressAnimator.start();
    }

    /**
     * 设置文本大小,防止步数特别大之后放不下，将字体大小动态设置
     *
     * @param num
     */
    public void setTextSize(int num) {
        String s = String.valueOf(num);
        int length = s.length();
        if (length <= 4) {
            numberTextSize = dipToPx(50);
        } else if (length > 4 && length <= 6) {
            numberTextSize = dipToPx(40);
        } else if (length > 6 && length <= 8) {
            numberTextSize = dipToPx(30);
        } else if (length > 8) {
            numberTextSize = dipToPx(25);
        }
    }

}
