package com.king.run.view.barchart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.king.run.R;

/**
 * Created by Administrator on 2017/11/11.
 */

public class BarStaView extends View {
    /**
     * 第一步：声明画笔
     */
    private Paint mPaint_X;//X坐标轴画笔
    private Paint mPaint_Y;//Y坐标轴画笔
    private Paint mPaint_InsideLine;//内部虚线P
    private Paint mPaint_Text;//字体画笔
    private Paint mPaint_Rec;//矩形画笔

    //数据
    private int[] data;
    //视图的宽高
    private int width;
    private int height;


    //坐标轴数据
    private String[] mText_Y;

    private String[] mText_X = new String[]{};//默认X轴坐标

    private Shader mShader;
    private int mShapeColor = Color.parseColor("#bcbbc0");//阴影的颜色
    private int mShapeColor1 = Color.parseColor("#696671");//阴影的颜色
    private int mShapeColor2 = Color.parseColor("#474351");//阴影的颜色


    public BarStaView(Context context) {
        super(context);
        init(context, null);
    }

    public BarStaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * 更新XY轴坐标
     */
    public void upDateTextForX(String[] text_X) {
        mText_X = text_X;
        this.postInvalidate();  //更新视图
    }

    /**
     * 更新数据
     */
    public void upData(int[] data) {
        this.data = data;
        this.postInvalidate();  //更新视图
        mText_Y = getText_Y(data);
    }

    /**
     * 初始化画笔
     */
    private void init(Context context, AttributeSet attrs) {
        mPaint_X = new Paint();
        mPaint_InsideLine = new Paint();
        mPaint_Text = new Paint();
        mPaint_Rec = new Paint();
        mPaint_Y = new Paint();

        mPaint_X.setColor(Color.parseColor("#32ffffff"));
        mPaint_X.setStrokeWidth(2);

        mPaint_Y.setColor(Color.GRAY);

        mPaint_InsideLine.setColor(Color.LTGRAY);
        mPaint_InsideLine.setAntiAlias(true);

        mPaint_Text.setTextSize(25);
        mPaint_Text.setTextAlign(Paint.Align.CENTER);
        mPaint_Text.setColor(Color.WHITE);

        mPaint_Rec.setColor(Color.GRAY);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();
        int leftHeight_Every = (height - 50) / 4; //Y轴每个数据的间距

//        int barWidth = dipToPx(20);
        int barWidth = width / 16;
        int length = mText_X.length;
        int downWeight_Every = barWidth * 2;//X轴每个数据的间距

        int startX = (width - downWeight_Every * (length - 1)) / 2;
        //画XY坐标轴
        canvas.drawLine(0, height - 50, width, height - 50, mPaint_X);

        //画X轴坐标
        for (int i = 0; i < length; i++) {
            canvas.drawText(mText_X[i], startX + downWeight_Every * (i), height - 20, mPaint_Text);
        }

        if (this.data != null && this.data.length > 0) {
            //画矩形
            for (int i = 0; i < data.length; i++) {
                int data_Y_One = Integer.parseInt(mText_Y[3]); //Y轴首个数值
                double data_Yx = (double) data[i] / data_Y_One;
                RectF rect = new RectF();
                rect.left = startX + downWeight_Every * i - barWidth / 2;
                rect.right = startX + downWeight_Every * i + barWidth / 2;
                rect.top = (height - 50 - (int) (data_Yx * leftHeight_Every));
                rect.bottom = height - 50;


                mShader = new LinearGradient(0, rect.top, 0, rect.bottom, new int[]
                        {mShapeColor, mShapeColor1, mShapeColor2}, null, Shader.TileMode.REPEAT);
                mPaint_Rec.setShader(mShader);
                canvas.drawRoundRect(rect, barWidth / 2, barWidth / 2, mPaint_Rec);
                if ((rect.bottom - rect.top) > barWidth ) {
                    //为了挡住圆角举行下面的圆角
                    canvas.drawRect(rect.left, rect.bottom - barWidth / 2, rect.right, rect.bottom, mPaint_Rec);
                }
                canvas.drawText(data[i] + "", startX + downWeight_Every * i,
                        rect.top - 20, mPaint_Text);

            }
        }
    }


    /**
     * 获取一组数据的最大值
     */
    public static int getMax(int[] arr) {
        int max = arr[0];
        for (int x = 1; x < arr.length; x++) {
            if (arr[x] > max)
                max = arr[x];
        }
        return max;
    }

    /**
     * 功能：根据传入的数据动态的改变Y轴的坐标
     * 返回：取首数字的前两位并除以2，后面变成0。作为Y轴的基坐标
     */
    public static String[] getText_Y(int[] data) {
        String[] text_Y;
        int textY = 0;

        String numMax = getMax(data) + "";
        char[] charArray = numMax.toCharArray();
        int dataLength = charArray.length;//数据长度 eg：5684：4位
        String twoNumString = "";
        if (dataLength >= 2) {
            for (int i = 0; i < 2; i++) {
                twoNumString += charArray[i];
            }
            int twoNum = Integer.parseInt(twoNumString);
            textY = (int) Math.ceil(twoNum / 3);
            //将数据前两位后加上“0” 用于返回前两位的整数
            if (dataLength - 2 == 1) {
                textY *= 10;
            } else if (dataLength - 2 == 2) {
                textY *= 100;
            } else if (dataLength - 2 == 3) {
                textY *= 1000;
            } else if (dataLength - 2 == 4) {
                textY *= 10000;
            } else if (dataLength - 2 == 5) {
                textY *= 100000;
            }
            text_Y = new String[]{"", textY * 3 + "", textY * 2 + "", textY + ""};

        } else {
            text_Y = new String[]{"", 15 + "", 10 + "", 5 + ""};
        }
        return text_Y;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.EXACTLY || widthSpecMode == MeasureSpec.AT_MOST) {
            width = widthSpecSize;
        } else {
            width = 0;
        }
        if (heightSpecMode == MeasureSpec.AT_MOST || heightSpecMode == MeasureSpec.UNSPECIFIED) {
            height = dipToPx(200);
        } else {
            height = heightSpecSize;
        }
        setMeasuredDimension(width, height);
    }

    private int dipToPx(int dip) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }
}
