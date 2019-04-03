package com.king.run.view;

/**
 * Created by zhangxiao-ms on 2017/5/31.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

/**
 *
 */
public class DragGridView extends GridView {
    private static final String TAG = "DragGridView";
    private View mDragView;
    private View mLastDragView;
    private Bitmap mShadow; // 被选中的view制造的阴影

    Vibrator mVibrator;
    boolean isDragged = false;
    boolean isExchange = false;

    int x, y;  // 当前touch坐标
    float offsetX, offsetY;

    private int mOldPosition;
    int mScrollUp, mScrollDown;

    private OnItemChanageListener mOnChangeListener;

    public DragGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public DragGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragGridView(Context context) {
        super(context);
        init();
    }

    private void init() {
        // 监听长按
        mVibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

        setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // 创建影像
                isDragged = true;
                mVibrator.vibrate(50);

                mShadow = getViewBitmap(view);

                mOldPosition = position;
                // 当前点击位置到该view左上角的偏移
                offsetX = x - view.getX();
                offsetY = y - view.getY();

                mDragView = view;
                mDragView.setVisibility(View.INVISIBLE);
                return false;
            }
        });
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (mShadow != null && !mShadow.isRecycled()) {
            canvas.drawBitmap(mShadow, x - offsetX, y - offsetY, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        x = (int) ev.getX();
        y = (int) ev.getY();
        if (isDragged) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    // 判断是否需要移动
                    post(mScrollRunnable);
                    if (!isExchange) {
                        swapItem();
                    }
                    invalidate();
                    return true;  // 否则会与滚动冲突
                case MotionEvent.ACTION_UP:
                    removeCallbacks(mScrollRunnable);
                    isDragged = false;
                    mDragView.setVisibility(View.VISIBLE);
                    mDragView = null;
                    // 回收shadow
                    if (mShadow != null && !mShadow.isRecycled()) {
                        mShadow.recycle();
                    }
                    invalidate();
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    private void swapItem() {
        final int tempPosition = pointToPosition(x, y);
        if (tempPosition != AbsListView.INVALID_POSITION && tempPosition != mOldPosition) {
            isExchange = true;
            if (tempPosition > mOldPosition) {
                // 其余view全部向前移动
                TranslateAnimation animation = null;
                for (int pos = mOldPosition + 1; pos <= tempPosition; pos++) {
                    if (pos % getNumColumns() == 0) {
                        View view = getChildAt(pos - getFirstVisiblePosition());
                        if (view != null) {
                            animation = new TranslateAnimation(0, (getNumColumns() - 1) * view.getWidth(), 0, -view.getHeight());
                            animation.setDuration(300);
                            view.startAnimation(animation);
                        }
                    } else {
                        View view = getChildAt(pos - getFirstVisiblePosition());
                        if (view != null) {
                            animation = new TranslateAnimation(0, -view.getWidth(), 0, 0);
                            animation.setDuration(300);
                            view.startAnimation(animation);
                        }
                    }
                }
                if (animation != null) {
                    animation.setAnimationListener(animationListener);
                }
            } else {
                // 其余view全部向后移动
                Animation animation = null;
                for (int pos = mOldPosition - 1; pos >= tempPosition; pos--) {
                    if ((pos + 1) % getNumColumns() == 0) {
                        View view = getChildAt(pos - getFirstVisiblePosition());
                        if (view != null) {
                            animation = new TranslateAnimation(0, -(getNumColumns() - 1) * view.getWidth(), 0, view.getHeight());
                            animation.setDuration(300);
                            view.startAnimation(animation);
                        }
                    } else {
                        View view = getChildAt(pos - getFirstVisiblePosition());
                        if (view != null) {
                            animation = new TranslateAnimation(0, view.getWidth(), 0, 0);
                            animation.setDuration(300);
                            view.startAnimation(animation);
                        }
                    }
                }
                if (animation != null) {
                    animation.setAnimationListener(animationListener);
                }
            }
            if (mOnChangeListener != null) {
                mOnChangeListener.onChange(mOldPosition, tempPosition);
            }
            if (mDragView != null) {
                if (mLastDragView != null) {
                    mLastDragView.setVisibility(View.VISIBLE);
                }
                mLastDragView = mDragView;
            }
            mDragView = getChildAt(tempPosition - getFirstVisiblePosition());
            mOldPosition = tempPosition;
            mDragView.setVisibility(View.INVISIBLE);
        }
    }

    /*private void swapItem() {
        final int tempPosition = pointToPosition(x, y);
        if (tempPosition != AbsListView.INVALID_POSITION && tempPosition != mOldPosition) {

            if (tempPosition > mOldPosition) {
                // 其余view全部向前移动
                TranslateAnimation animation = null;
                for (int pos=mOldPosition + 1; pos <= tempPosition; pos++) {
                    if (pos % getNumColumns() == 0) {
                        View view = getChildAt(pos - getFirstVisiblePosition());
                        if (view != null) {
                            animation = new TranslateAnimation(0, (getNumColumns() - 1) * view.getWidth(), 0, -view.getHeight());
                            animation.setDuration(300);
                            view.startAnimation(animation);
                        }
                    } else {
                        View view = getChildAt(pos - getFirstVisiblePosition());
                        if (view != null) {
                            animation = new TranslateAnimation(0, - view.getWidth(), 0, 0);
                            animation.setDuration(300);
                            view.startAnimation(animation);
                        }
                    }
                    if (animation != null) {
                        animation.setAnimationListener(animationListener);
                    }
                }
            } else {
                // 其余view全部向后移动
                Animation animation = null;
                for (int pos = mOldPosition - 1; pos >= tempPosition; pos--) {
                    if ((pos + 1) % getNumColumns() == 0) {
                        View view = getChildAt(pos - getFirstVisiblePosition());
                        if (view != null) {
                            animation = new TranslateAnimation(0, -(getNumColumns() - 1) * view.getWidth(), 0, view.getHeight());
                            animation.setDuration(300);
                            view.startAnimation(animation);
                        }
                    } else {
                        View view = getChildAt(pos - getFirstVisiblePosition());
                        if (view != null) {
                            animation = new TranslateAnimation(0, view.getWidth(), 0, 0);
                            animation.setDuration(300);
                            view.startAnimation(animation);
                        }
                    }
                    if (animation != null) {
                        animation.setAnimationListener(animationListener);
                    }
                }
            }
            if (mOnChangeListener != null) {
                mOnChangeListener.onChange(mOldPosition, tempPosition);
            }
            if (mDragView != null) {
                if (mLastDragView != null) {
                    mLastDragView.setVisibility(View.VISIBLE);
                }
                mLastDragView = mDragView;
            }
            mDragView = getChildAt(tempPosition - getFirstVisiblePosition());
            mOldPosition = tempPosition;
            mDragView.setVisibility(View.INVISIBLE);
        }
    }*/

    Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (mLastDragView != null) {
                mLastDragView.setVisibility(View.VISIBLE);
                mLastDragView = null;
            }
            isExchange = false;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    public void setOnItemChanageListener(OnItemChanageListener listener) {
        mOnChangeListener = listener;
    }

    private Runnable mScrollRunnable = new Runnable() {
        @Override
        public void run() {

            // 当距离超过高度3/4，则向下滚，当距离小于高度1/4，则向上滚
            if (mScrollUp == 0) {
                mScrollUp = getHeight() / 4;
            }
            if (mScrollDown == 0) {
                mScrollDown = getHeight() * 3 / 4;
            }

            int speedY = 0;
            if (y > mScrollDown) {
                speedY = 30;
                postDelayed(mScrollRunnable, 25);
            } else if (y < mScrollUp) {
                speedY = -30;
                postDelayed(mScrollRunnable, 25);
            }

            smoothScrollBy(speedY, 10);
        }
    };


    /**
     * 将一个view装换成bitmap
     *
     * @param v
     * @return
     */
    public Bitmap getViewBitmap(View v) {
        v.clearFocus();
        v.setPressed(false);

        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);

        // Reset the drawing cache background color to fully transparent
        // for the duration of this operation
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);

        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        // Restore the view
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);

        return bitmap;
    }

    public interface OnItemChanageListener {
        /**
         * 回调通知数据发生变化，完成界面的数据交换
         *
         * @param from 开始的position
         * @param to   拖拽到的position
         */
        void onChange(int from, int to);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
